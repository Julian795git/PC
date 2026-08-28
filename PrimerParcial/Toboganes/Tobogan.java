package PrimerParcial.Toboganes;

import java.util.concurrent.Semaphore;

public class Tobogan {

    private Semaphore semVisitante = new Semaphore(0);
    private Semaphore semEncargado = new Semaphore(0);
    private Semaphore semTobogan1 = new Semaphore(1);
    private Semaphore semTobogan2 = new Semaphore(1);
    private Semaphore semEscalon = new Semaphore(1);

    private final int escalones;
    private int usoEscalones = 0;
    private Visitante visitanteActual;

    public Tobogan (int escalones) {
        this.escalones = escalones;
    }

    public void accesoEscalera(Visitante v) throws InterruptedException {

        semEscalon.acquire(); //Asegurar que se realice correctamente la actualización de los escalones en uso sin que se superponga con otro hilo
        if (usoEscalones < escalones) {
            usoEscalones++;
            System.out.println("Visitante " + v.getId() + " sube al último escalón libre. Escalones ocupados: " + usoEscalones);
            semEscalon.release(); //ya se hizo la actualización, se libera para que otro visitante pueda o no conseguir su escalón

            //Inicia proceso de subirse a uno de los toboganes
            this.visitanteActual = v;
            semVisitante.release(); //hay un cliente queriendo ingresar a un tobogán: avisa al encargado
            semEncargado.acquire(); //El visitante es alertado por un nuevo visitante que quiere subirse a un tobogán
        } else {
            System.out.println("No había más lugar en la escalera para subir al tobogán");
            semEscalon.release();
        }
    }

    public void autorizarVisitante() throws InterruptedException {
        semVisitante.acquire(); //atiende al visitante

        if (semTobogan1.tryAcquire()) {
            this.visitanteActual.setToboganAsig(1);
            semTobogan1.release();
        } else {
            this.visitanteActual.setToboganAsig(2);
        }

        semEncargado.release(); //cumplió su trabajo, anuncia al visitante efectivamente cuál tobogán usar
    }

    public void bajarTobogan(Visitante v) throws InterruptedException {
        Semaphore elegido;

        if (v.getToboganAsig() == 1) {
            elegido = semTobogan1;
        } else {
            elegido = semTobogan2;
        }
        
        elegido.acquire();
        System.out.println("Visitante " + v.getId() + " baja por tobogán " + v.getToboganAsig());
        Thread.sleep(2000);
        System.out.println("Visitante " + v.getId() + " terminó de bajar por el tobogán " + v.getToboganAsig());
        elegido.release();

        //Liberar escalón
        semEscalon.acquire(); //Para asegurar consistencia
        usoEscalones--;
        semEscalon.release();

    }



}
