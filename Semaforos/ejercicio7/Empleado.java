package Semaforos.ejercicio7;

public class Empleado implements Runnable {
    private Confiteria confiteria;
    private int id;

    public Empleado (Confiteria unaConfiteria, int unId) {
        this.confiteria = unaConfiteria;
        this.id = unId;
    }

    public void run() {
        try {
            confiteria.ordenar(this.id);
        } catch (InterruptedException e) {e.printStackTrace();}
    }    

}
