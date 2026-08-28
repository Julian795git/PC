package Semaforos.ejercicio6;

public class Pasajero implements Runnable {
    
    private Taxi taxi;
    private int id;
    
    public Pasajero(Taxi unTaxi, int unId) {
        this.taxi = unTaxi;
        this.id = unId;
    }

    public void run() {
        try {
            taxi.tomarTaxi(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
