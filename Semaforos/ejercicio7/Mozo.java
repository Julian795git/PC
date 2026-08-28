package Semaforos.ejercicio7;

public class Mozo implements Runnable {
    private Confiteria confiteria;

    public Mozo (Confiteria unaConfi) {
        this.confiteria = unaConfi;
    }

    public void run() {
        try { 
            confiteria.prepararComida();
        } catch (InterruptedException e) {e.printStackTrace();}
    }

}
