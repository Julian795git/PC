package Synchronized.ejercicio3;

public class Rueda {
    
    public synchronized void rodar(String hamster) {
        try {
            System.out.println(hamster + " está rodando en la rueda");
            Thread.sleep(2000);
            System.out.println(hamster + " terminó de rodar");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
