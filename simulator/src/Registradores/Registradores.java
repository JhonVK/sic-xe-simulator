package Registradores;

public class Registradores {
    
    private Registrador[] registradores;

    public Registradores(int numero){
        registradores=new Registrador[8]; //temos 8 registradores de 3 bytes cada
    }

    public Registrador[] getRegistradores() {
        return registradores;
    }
    
}
