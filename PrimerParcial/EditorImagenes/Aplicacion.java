package PrimerParcial.EditorImagenes;

import java.util.concurrent.Semaphore;

public class Aplicacion {
    private Semaphore semImagen = new Semaphore(0);
    private Semaphore semAplicacion = new Semaphore(0);

    public void solicitarEdicion() throws InterruptedException {
        System.out.println("Imagen nueva solicitando edición");
        semImagen.release(); //Avisa a aplicación que está para editarse
        semAplicacion.acquire(); //Aplicación reacciona y se pone a editar (se bloquea para que ninguna otra imagen pueda acceder mientras)
        System.out.println("Imagen editada correctamente");
    }

    public void edicionImagen() throws InterruptedException{
        semImagen.acquire(); //Aplicacion se dispone a editar imagen (la bloquea)
        System.out.println("Aplicación empezó a editar imagen nueva");
        Thread.sleep(2000);
        System.out.println("Aplicación terminó de editar imagen nueva");
        semAplicacion.release(); //Le avisa a imagen que ya fue editada, y queda aplicación disponible para más imagenes
    }

}
