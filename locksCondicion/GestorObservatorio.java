package locksCondicion;

import java.util.concurrent.locks.*;

class Visitante implements Runnable {
    private boolean sillaRuedas;
    private Observatorio observatorio;

    public Visitante (boolean silla, Observatorio unObser) {
        this.sillaRuedas = silla;
        this.observatorio = unObser;
    }

    public void run(){

    }

}

class PersonaMant implements Runnable {
    private Observatorio observatorio;

    public PersonaMant(Observatorio unObser){
        this.observatorio = unObser;
    }

    public void run() {

    }

}

class Investigador implements Runnable {
    private Observatorio observatorio;

    public Investigador(Observatorio unObser) {
        this.observatorio = unObser;
    }

    public void run() {

    }

}

class Observatorio {
    private final Lock cerrojo = new ReentrantLock(true);
    private final Condition esperaVisitante = cerrojo.newCondition();
    private final Condition esperaMantenimiento = cerrojo.newCondition();
    private final Condition esperaInvestigador = cerrojo.newCondition();
    private int capacidadTotal = 50;
    private int capacidadActual;
    private int discapacitados;

    public Observatorio() {
        this.capacidadActual = 0;
    }

    public void incrementarCapacidad() {
        this.capacidadActual++;
    }

    public void decrementarCapacidad(){
        this.capacidadActual--;
    }

    public void entrarVisitante(boolean silla) {
        cerrojo.lock();
         
        try {

            while (this.capacidadActual > this.capacidadTotal) {
                esperaVisitante.await();
            }

            if (silla) {
                this.capacidadTotal = 30;
                this.discapacitados++;
            }

        }  catch (InterruptedException e) {

        } finally {
            cerrojo.unlock();
        }
            
    }

    public void salirVisitante(boolean silla) {
        cerrojo.lock();

        

    }


}



public class GestorObservatorio {
    
    public static void main(String[] args) {
        
    }

}
