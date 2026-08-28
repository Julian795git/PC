package Semaforos.ejercicio6;

public class Taxista implements Runnable {
    private Taxi taxi;

    public Taxista (Taxi unTaxi) {
        this.taxi = unTaxi;
    }

    public void run() {
        try {
            while (true) {
                taxi.conducir();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
