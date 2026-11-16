package SegundoParcial.ProblemasClasicos;

import java.util.concurrent.Semaphore;

class BufferLimitado {
    private final int MAX_SIZE = 10;
    private final int[] buffer = new int[MAX_SIZE];
    private int punteroInsercion = 0;
    private int punteroExtraccion = 0;

    // Semáforo general para contar cuántos espacios libres quedan en el buffer
    // Se inicializa en MAX_SIZE porque al principio el buffer está totalmente vacío
    private final Semaphore espaciosVacios = new Semaphore(MAX_SIZE);

    // Semáforo general para contar cuántos elementos hay listos para consumir
    // Se inicializa en 0 porque al principio no hay nada producido
    private final Semaphore elementosDisponibles = new Semaphore(0);

    // Semáforo binario (Mutex) para proteger el acceso a la estructura de datos
    // Se inicializa en 1 para garantizar exclusión mutua
    private final Semaphore mutex = new Semaphore(1);

    public void producir(int dato) throws InterruptedException {
        // 1. Consume un "espacio vacío". Si es 0 (buffer lleno), el hilo se bloquea acá.
        espaciosVacios.acquire();
        
        // 2. Adquiere exclusión mutua para modificar el arreglo sin interferencias
        mutex.acquire();
        try {
            buffer[punteroInsercion] = dato;
            punteroInsercion = (punteroInsercion + 1) % MAX_SIZE; // Movimiento circular
            System.out.println(Thread.currentThread().getName() + " produjo: " + dato);
        } finally {
            // 3. Libera la exclusión mutua
            mutex.release();
        }
        
        // 4. Señaliza que hay un nuevo "elemento disponible" para que un consumidor lo tome
        elementosDisponibles.release();
    }

    public int consumir() throws InterruptedException {
        // 1. Consume un "elemento disponible". Si es 0 (buffer vacío), el hilo se bloquea acá.
        elementosDisponibles.acquire();
        
        int datoExtraido;
        
        // 2. Adquiere exclusión mutua para leer y modificar los punteros del arreglo
        mutex.acquire();
        try {
            datoExtraido = buffer[punteroExtraccion];
            punteroExtraccion = (punteroExtraccion + 1) % MAX_SIZE;
            System.out.println(Thread.currentThread().getName() + " consumió: " + datoExtraido);
        } finally {
            // 3. Libera la exclusión mutua
            mutex.release();
        }
        
        // 4. Señaliza que se liberó un "espacio vacío" para que un productor pueda trabajar
        espaciosVacios.release();
        
        return datoExtraido;
    }
}

class HiloProductor implements Runnable {
    private final BufferLimitado buffer;

    public HiloProductor(BufferLimitado buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            int dato = 1;
            while (true) {
                buffer.producir(dato++);
                Thread.sleep((long) (Math.random() * 500));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class HiloConsumidor implements Runnable {
    private final BufferLimitado buffer;

    public HiloConsumidor(BufferLimitado buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                buffer.consumir();
                Thread.sleep((long) (Math.random() * 1000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class ProblemaProductorConsumidor {
    public static void main(String[] args) {
        BufferLimitado buffer = new BufferLimitado();

        // Lanzamos múltiples productores y consumidores
        for (int i = 1; i <= 3; i++) {
            new Thread(new HiloProductor(buffer), "Productor-" + i).start();
            new Thread(new HiloConsumidor(buffer), "Consumidor-" + i).start();
        }
    }
}