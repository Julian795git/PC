package PrimerParcial.Toboganes;

public class Visitante implements Runnable {
    
    private int id;
    private Tobogan tobogan;
    private int toboganAsignado;

    public Visitante (int unId, Tobogan unTobogan) {
        this.id = unId;
        this.tobogan = unTobogan;
    }

    public int getId() {
        return this.id;
    }

    public int getToboganAsig() {
        return this.toboganAsignado;
    }

    public void setToboganAsig(int t) {
        this.toboganAsignado = t;
    }

    public void run() {
        try {
            tobogan.accesoEscalera(this);
            tobogan.bajarTobogan(this);
        } catch (InterruptedException e) {}
    }

}
