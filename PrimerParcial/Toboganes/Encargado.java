package PrimerParcial.Toboganes;

public class Encargado implements Runnable {
    
    private Tobogan tobogan;

    public Encargado (Tobogan unTobogan) {
        this.tobogan = unTobogan;
    }

    public void run() {
        while (true) {
            try {
                tobogan.autorizarVisitante();
                System.out.println("Encargado autorizando a Visitante");
            } catch (InterruptedException e) {}
        }
    }

}