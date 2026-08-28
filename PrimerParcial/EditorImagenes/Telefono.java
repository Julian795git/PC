package PrimerParcial.EditorImagenes;

public class Telefono implements Runnable {
    
    private Aplicacion aplicacion;

    public Telefono(Aplicacion unaApp) {
        this.aplicacion = unaApp;
    }

    public void run() {
        while (true) { //Acá si va el while (true) porque la aplicación debe estar siempre disponible
            try {
                aplicacion.edicionImagen();
            } catch (InterruptedException e) {}
        }
    }

}
