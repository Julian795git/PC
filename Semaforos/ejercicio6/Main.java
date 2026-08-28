package Semaforos.ejercicio6;

public class Main {
    
    public static void main(String[] args) {
        Taxi taxi = new Taxi();
        
        Taxista taxista = new Taxista(taxi);
        new Thread (taxista).start();

        for (int i = 0; i <= 5; i++) {
            try { Thread.sleep(2000); } catch (Exception e) {}
            new Thread (new Pasajero(taxi, (i + 1))).start();
        }

    }
}
