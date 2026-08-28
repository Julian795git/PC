package PrimerParcial.EditorImagenes;

public class Imagen implements Runnable {
    
    private Aplicacion aplicacion;

    public Imagen (Aplicacion unaApp) {
        this.aplicacion = unaApp;
    }

    public void run() {
        try {
            aplicacion.solicitarEdicion();
        } catch (InterruptedException e) {}
    }
}
