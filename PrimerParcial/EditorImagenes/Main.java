package PrimerParcial.EditorImagenes;

public class Main {
    
    public static void main(String[] args) {
        Aplicacion aplicacion = new Aplicacion();

        Thread telefono = new Thread (new Telefono(aplicacion));
        telefono.start();

        int cantidadImagenes = 10;
        Thread[] imagenes = new Thread[cantidadImagenes];

        for (int i = 0; i < cantidadImagenes; i++) {
            imagenes[i] = new Thread(new Imagen(aplicacion));
            imagenes[i].start();
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {}
        }
    }
}
