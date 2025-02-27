package Montador.testemacros;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DividirArquivoASM {

    public static void main(String[] args) {
        String arquivoEntrada = "Macro/MASMAPRG.ASM";
        String arquivoSaida1 = "Montador/entradaMontador1.asm";
        String arquivoSaida2 = "Montador/entradaMontador2.asm";
        String linhaDivisao = "SUB C";

        try (
            BufferedReader leitor = new BufferedReader(new FileReader(arquivoEntrada));
            BufferedWriter escritor1 = new BufferedWriter(new FileWriter(arquivoSaida1));
            BufferedWriter escritor2 = new BufferedWriter(new FileWriter(arquivoSaida2))
        ) {
            String linha;
            boolean encontrouDivisao = false;

            while ((linha = leitor.readLine()) != null) {
                if (linha.contains(linhaDivisao)) {
                    encontrouDivisao = true;
                }

                if (!encontrouDivisao) {
                    escritor1.write(linha);
                    escritor1.newLine();
                } else {
                    escritor2.write(linha);
                    escritor2.newLine();
                }
            }

            System.out.println("Divisão concluída com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao processar o arquivo: " + e.getMessage());
        }
    }
}

