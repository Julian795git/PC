package ProblemasClasicos;

import java.util.concurrent.locks.*;
import java.util.Scanner;

class Buffer {
    private int[] buffer;
    private int capacidad;
    private int cantidadActual;
    private int indexPoner;
    private int indexSacar;
    private final Lock lock = new ReentrantLock();
    private final Condition lleno = lock.newCondition();
    private final Condition vacio = lock.newCondition();

    public Buffer(int capacidad) {
        this.capacidad = capacidad;
        this.buffer = new int[capacidad];
        this.cantidadActual = 0;
        this.indexPoner = 0;
        this.indexSacar = 0;
    }

    //Método para el productor
    public void producir (int item) throws InterruptedException {
        lock.lock();
        try {
            while (cantidadActual == capacidad) {
                lleno.await();
            }
            buffer[indexPoner] = item;
            indexPoner = (indexPoner + 1) % capacidad;
            cantidadActual++;
            vacio.signal(); //Notifico a algún consumidor en espera que se sumó un producto nuevo
        } finally {
            lock.unlock();
        }
    }

    //Método para el consumidor
    public int consumir() throws InterruptedException {
        lock.lock();
        int item = 0;
        try {
            while (cantidadActual == 0) {
                vacio.await();
            }
            item = buffer[indexSacar];
            indexSacar = (indexSacar + 1) % capacidad;
            cantidadActual--;
            lleno.signal();
        } finally {
            lock.unlock();
        }

        return item;
    }

}

class Productor implements Runnable {
    private Buffer buffer;

    public Productor (Buffer unBuffer) {
        this.buffer = unBuffer;
    }

    public void run() {
        int item = 1;
        try {
            while (true) {
                buffer.producir(item);
                Thread.sleep((long) (Math.random() * 1000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumidor implements Runnable {
    private final Buffer buffer;

    public Consumidor (Buffer unBuffer) {
        this.buffer = unBuffer;
    }

    public void run() {
        try {
            while (true) {
                int item = buffer.consumir();
                System.out.println(Thread.currentThread().getName() + " consumió: " + item);
                Thread.sleep((long) (Math.random() * 1000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class ProductorConsumidor {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacidadBuffer = sc.nextInt();
        Buffer buffer = new Buffer(capacidadBuffer);
        int cantConsumidores = sc.nextInt();
        int cantProductores = sc.nextInt();
        Thread[] consumidores = new Thread[cantConsumidores];
        Thread[] productores = new Thread[cantProductores];

        for (int i = 0; i < consumidores.length; i++) {
            consumidores[i] = new Thread(new Consumidor(buffer));
            consumidores[i].start();
        }

        for (int j = 0; j < productores.length; j++) {
            productores[j] = new Thread(new Productor(buffer));
            productores[j].start();
        }

        sc.close();
    }
}
