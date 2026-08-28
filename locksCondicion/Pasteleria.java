package locksCondicion;

import java.util.concurrent.locks.*;
import java.util.Queue;
import java.util.LinkedList;
//import EDAT.lineales.dinamicas.Cola;
//import EDAT.lineales.dinamicas.Nodo;

//En parcial, solamente usar la cola implementando las operaciones básicas
//Repasar este mismo ejercicio pero implementando e importando la cola realizada en EDAT

class Caja {
    private final Lock lockCaja = new ReentrantLock();
    private final Condition tieneLugar = lockCaja.newCondition();
    private final Condition cajaLlena = lockCaja.newCondition();
    private int pesoActual;
    private int pesoMaximo;

    public Caja (int pesoMax) {
        this.pesoActual = 0;
        this.pesoMaximo = pesoMax;
    }

    public void retirarCaja() throws InterruptedException {
        lockCaja.lock();
        try {
            while (this.pesoActual == 0) {
                cajaLlena.await();
            }
            this.pesoActual = 0;
        } finally {
            lockCaja.unlock();
        }
    }

    public void reponerCaja() throws InterruptedException {
        lockCaja.lock();
        try {
            tieneLugar.signalAll();
        } finally {
            lockCaja.unlock();
        }
    }

    public void soltarPastel(int pesoPastel) throws InterruptedException {
        lockCaja.lock();
        try {

            while (this.pesoActual + pesoPastel > this.pesoMaximo) {
                cajaLlena.signal(); //Orden importa: Si se pusiera al revés, solo se ejecutaría cuando el hilo despierte y la condición del while ya no se cumpla
                tieneLugar.await(); //Primero se avisa al brazo y luego se espera
            }
            this.pesoActual += pesoPastel;
            if (pesoActual == pesoMaximo) {
                cajaLlena.signal();
            } else {
                tieneLugar.signal(); //Permite que los empaquetadores se avisen entre sí cuando hay cambios en la caja, evitando que alguno de los empaquetadores se quede bloqueado indefinidamente cuando podría poner su pastel mientras. 
            }

        } finally {
            lockCaja.unlock();
        }
    }
}

class Mostrador {
    private final Lock lockMostrador = new ReentrantLock();
    private final Queue<Integer> colaPesos = new LinkedList<>();
    private final Condition hayPasteles = lockMostrador.newCondition();


    public Mostrador() {

    }

    public void ponerPastelMostrador(int peso) throws InterruptedException {
        lockMostrador.lock();
        try {
            colaPesos.offer(peso); //offer pone un elemento nuevo en la cola
            hayPasteles.signal(); //avisa a los hilos que están esperando a que la cola tenga elementos para agarrar
        } finally {
            lockMostrador.unlock();
        }
    }

    public int tomarPastel() throws InterruptedException {
        lockMostrador.lock();
        int pesoTomado = 0;
        try {
            while(colaPesos.isEmpty()) { //Método para verificar si la cola está vacía
                hayPasteles.await();
            }
            pesoTomado = colaPesos.poll(); //Saca el primer elemento de la cola y lo elimina
        } finally {
            lockMostrador.unlock();
        }

        return pesoTomado;
    }

}

class Horno {
    private Mostrador mostrador;

    public Horno(Mostrador unMostrador) {
        this.mostrador = unMostrador;
    }

}

class Pastel {
    private Mostrador mostrador;
    private int peso;


}

class Empaquetador {
    private Mostrador mostrador;
    private Caja caja;

    public Empaquetador(Mostrador unMostrador, Caja unaCaja) {
        this.mostrador = unMostrador;
        this.caja = unaCaja;
    }


}

class Brazo {

}


public class Pasteleria {


}
