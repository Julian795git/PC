package Synchronized.ejercicio3;

public class Hamaca {
    public synchronized void hamacarse(String hamster) {
        try {
            System.out.println(hamster + " está hamacándose");
            Thread.sleep(2000);
            System.out.println(hamster + " termina de hamacarse");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
