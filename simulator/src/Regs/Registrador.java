package Regs;

public class Registrador {
    private byte[] reg;

    public Registrador() {
        reg = new byte[3];
    }

    public byte[] getReg() {
        return reg;
    }

    public void setReg(byte b1, byte b2, byte b3) {
        reg[0] = b1;
        reg[1] = b2;
        reg[2] = b3;
    }

    public int getValue() {
        // Combina os 3 bytes em um valor inteiro de 24 bits
        return ((reg[0] & 0xFF) << 16) | ((reg[1] & 0xFF) << 8) | (reg[2] & 0xFF);
    }
    public String getValueAsString() {
        StringBuilder hexValue = new StringBuilder();
        for (byte b : reg) {
            hexValue.append(String.format("%02X", b));
        }
        return hexValue.toString();
    }
}
