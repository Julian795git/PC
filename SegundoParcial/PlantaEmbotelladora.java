package SegundoParcial;

import java.util.concurrent.locks.*;
import java.util.Scanner;

class Almacen {
    private int maxCaja = 10;
    private int botellasEnCajaVino = 0;
    private int botellasEnCajaAgua = 0;

    //Estado almacén
    private int litrosEnAlmacen = 0;
    private final int maxAlmacen = 100;

    private boolean reponiendoCajaVino = false;
    private boolean reponiendoCajaAgua = false;

    private int cajasVinoEnAlmacen = 0;
    private int cajasAguaEnAlmacen = 0;
    private boolean repartoEnCurso = false;
    private final long tiempoMaduracion = 3000;

    private final Lock lock = new ReentrantLock();
    private final Condition embotellarVino = lock.newCondition();
    private final Condition embotellarAgua = lock.newCondition();
    private final Condition cajaLlena = lock.newCondition();
    private final Condition almacenLleno = lock.newCondition();
    private final Condition almacenNoLleno = lock.newCondition();


    public void embotellar(char tipoItem) throws InterruptedException {
        lock.lock();
        try {
            if (tipoItem == 'V') {
                while (botellasEnCajaVino == maxCaja || reponiendoCajaVino) {
                    embotellarVino.await();
                }
                botellasEnCajaVino++;
                System.out.println("Embotellador de vino");

                if (botellasEnCajaVino == maxCaja) {
                    cajaLlena.signal(); //Avisa al empaquetador
                }
            } else { //tipoItem = 'A'
                while (botellasEnCajaAgua == maxCaja || reponiendoCajaAgua) {
                    embotellarAgua.await();
                }
                botellasEnCajaAgua++;
                System.out.println("Embotellador agua");
                if (botellasEnCajaAgua == maxCaja) {
                    cajaLlena.signal();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void empaquetarCaja() throws InterruptedException {
        lock.lock();
        try {
            while (botellasEnCajaAgua < maxCaja && botellasEnCajaVino < maxCaja) {
                cajaLlena.await();
            }

            while (litrosEnAlmacen == maxAlmacen || repartoEnCurso) {
                almacenNoLleno.await();
            }

            if (botellasEnCajaVino == maxCaja) {
                reponiendoCajaVino = true;
                litrosEnAlmacen += botellasEnCajaVino;
                cajasVinoEnAlmacen++;
                botellasEnCajaVino = 0;
                reponiendoCajaVino = false;
                embotellarVino.signalAll();
            } else if (botellasEnCajaAgua == maxCaja) {
                reponiendoCajaAgua = true;
                litrosEnAlmacen += botellasEnCajaAgua;
                cajasAguaEnAlmacen++;
                botellasEnCajaAgua = 0;
                reponiendoCajaAgua = false;
                embotellarAgua.signalAll();
            }

            if (litrosEnAlmacen == maxAlmacen) {
                almacenLleno.signal();
            }

        } finally {
            lock.unlock();
        }
    }

    public long prepararReparto() throws InterruptedException {
    long tiempoEspera = 0;
    lock.lock();
    try {
        // 1. Espera MIENTRAS el almacén NO esté lleno
        while (litrosEnAlmacen < maxAlmacen) {
            System.out.println("Camión (F1): Esperando almacén lleno...");
            almacenLleno.await();
        }

        // 2. Almacén lleno. Lo "reserva" para que el empaquetador no meta más
        System.out.println("Camión (F1): Almacén lleno. Reservando carga.");
        repartoEnCurso = true; 

        // 3. Revisa si debe esperar por maduración
        if (cajasVinoEnAlmacen > 0) {
            System.out.println("Camión (F1): Lote tiene vino. Se requiere maduración.");
            tiempoEspera = this.tiempoMaduracion;
        } else {
            System.out.println("Camión (F1): Lote solo tiene agua. Salida inmediata.");
        }
        
        // Devuelve el tiempo de sleep al hilo Camion
        return tiempoEspera; 
    } finally {
        lock.unlock(); // <-- Libera el lock ANTES de que el hilo Camion haga sleep
    }
}

}

/*
class Embotellador implements Runnable {

} */

/* class Empaquetador implements Runnable {

} */

public class PlantaEmbotelladora {
    public static void main(String[] args) {
        



    }    
}
