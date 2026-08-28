package Synchronized.ejercicio3;

public class Plato {
    
    public synchronized void comer(String hamster) {
        try {
            System.out.println(hamster + " está comiendo en el plato");
            Thread.sleep(2000);
            System.out.println(hamster + " terminó de comer");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
