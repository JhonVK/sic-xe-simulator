package Carregador;

import Mem.Memoria;
import Mem.Palavramem;
import Regs.Registrador;
import Regs.Registradores;
import java.io.*;

public class AbsoluteLoader {
    private Memoria memoria;
    private Registradores registradores;

    public AbsoluteLoader(Memoria memoria, Registradores registradores) {
        this.memoria = memoria;
        this.registradores = registradores;
    }

    public void execute() {
        System.out.println("Memória antes da execução:");
        memoria.printMemory(10);
        loadModule("teste.txt");
        System.out.println("\nMemória após a execução:");
        memoria.printMemory(200);
    }

    public void executeAtAddress(int address) {
        System.out.println("Executando programa no endereço: " + address);
        
        // Set program counter to starting address
        // Check if we're using a valid register index
        int pcRegIndex = 7; // Using register 7 (index 7) as PC instead of 8
        registradores.getRegistradores(pcRegIndex).setReg((byte)0, (byte)0, (byte)address);
        
        // Execute instructions until a HALT instruction is encountered
        boolean running = true;
        while (running) {
            // Read instruction from memory at current PC
            int pc = address; // Or get from registradores if you're updating PC
            if (pc >= memoria.memoria.size()) {
                System.err.println("Erro: PC aponta para endereço fora da memória: " + pc);
                break;
            }
            
            Palavramem instrucao = memoria.memoria.get(pc);
            
            // Decode and execute the instruction
            byte opcode = instrucao.getBytes()[0];
            System.out.println("Executando instrução: opcode=" + String.format("%02X", opcode) + " no endereço " + pc);
            
            // Example: Update registers based on instruction
            switch (opcode) {
                case 0x18: // ADD instruction
                    // Get target register and value
                    int regIdx = instrucao.getBytes()[1] & 0x0F;
                    if (regIdx >= 8) {
                        System.err.println("Erro: Índice de registrador inválido: " + regIdx);
                        break;
                    }
                    
                    int value = instrucao.getBytes()[2] & 0xFF;
                    
                    // Update register
                    Registrador reg = registradores.getRegistradores(regIdx);
                    byte[] currentValue = reg.getReg();
                    int newValue = ((currentValue[1] & 0xFF) << 8 | (currentValue[2] & 0xFF)) + value;
                    reg.setReg(currentValue[0], (byte)((newValue >> 8) & 0xFF), (byte)(newValue & 0xFF));
                    System.out.println("Registrador " + regIdx + " atualizado para " + newValue);
                    break;
                    
                case 0x4C: // HALT instruction
                    running = false;
                    System.out.println("Instrução HALT encontrada. Terminando execução.");
                    break;
                    
                default:
                    System.out.println("Opcode não implementado: " + String.format("%02X", opcode));
                    // Optionally, you might want to break the loop if an unimplemented opcode is encountered
                    // running = false;
                    break;
            }
            
            // Update PC
            address++; // Simplified - actual PC update would depend on instruction
            if (address >= memoria.memoria.size()) {
                System.out.println("PC alcançou o final da memória. Terminando execução.");
                running = false;
            }
        }
    }

    public void loadModule(String module) {
        try (BufferedReader file = new BufferedReader(new FileReader(module))) {
            String register;
            while ((register = file.readLine()) != null) {
                String[] parts = register.split("\\^");
                char type = parts[0].charAt(0);

                if (type == 'H') {
                    System.out.println("Header encontrado: " + register);
                } else if (type == 'T') {
                    processTextRecord(parts);
                } else if (type == 'E') {
                    executeAtAddress(findFirstUsedAddress());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao abrir ou ler o arquivo: " + e.getMessage());
        }
    }

    private void processTextRecord(String[] parts) {
        int address = Integer.parseInt(parts[1], 16);
        String code = parts[3]; 
        moveToMemory(memoria, address, code);
    }
    
    public int findNextFreeMemoryIndex() {
        for (int i = 0; i < memoria.memoria.size(); i++) {
            Palavramem palavra = memoria.memoria.get(i);
            if (palavra.getBytes()[0] == 0 && palavra.getBytes()[1] == 0 && palavra.getBytes()[2] == 0) {
                return i;
            }
        }
        return memoria.memoria.size();
    }

    public void moveToMemory(Memoria memoria, int address, String code) {
        for (int i = 0; i < code.length(); i += 6) {
            int memIndex = findNextFreeMemoryIndex(); // Encontrando o próximo índice livre
            if (memIndex >= memoria.memoria.size()) {
                System.err.println("Erro: Memória insuficiente!");
                return;
            }
            if (i + 6 <= code.length()) {
                byte b1 = (byte) Integer.parseInt(code.substring(i, i + 2), 16);
                byte b2 = (byte) Integer.parseInt(code.substring(i + 2, i + 4), 16);
                byte b3 = (byte) Integer.parseInt(code.substring(i + 4, i + 6), 16);
                memoria.memoria.get(memIndex).setValor(b1, b2, b3);  // Atualizando a memória
            }
        }
    }

    private int findFirstUsedAddress() {
        return 0;
    }
}
