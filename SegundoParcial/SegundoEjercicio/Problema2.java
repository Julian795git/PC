package SegundoParcial.SegundoEjercicio;

import java.util.Random;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

// Implementación simple de Cola para poder ejecutar y probar el ejercicio
class Cola {
    private Queue<Integer> elementos = new LinkedList<>();

    public void poner(int dato) {
        elementos.add(dato);
    }

    public int sacar() {
        return elementos.poll();
    }

    public boolean esVacia() {
        return elementos.isEmpty();
    }

    public int longitud() {
        return elementos.size();
    }
}

class Buffer {
    private final Cola cola1 = new Cola();
    private final Cola cola2 = new Cola();
    
    // Etiquetas iniciales: Cola 1 para insertar, Cola 2 para extraer
    private int colaPoner = 1;
    private int colaExtraer = 2;

    // Semáforo general que cuenta cuántos elementos totales hay en el buffer
    private final Semaphore extraer = new Semaphore(0);
    // Semáforo binario (mutex) para los insertores y el cambio de etiquetas
    private final Semaphore insertar = new Semaphore(1, true);
    // Semáforo binario (mutex) para los extractores
    private final Semaphore mutexExtraer = new Semaphore(1, true);

    private void oscilar() {
        if (colaPoner == 1) {
            colaPoner = 2;
            colaExtraer = 1;
        } else {
            colaPoner = 1;
            colaExtraer = 2;
        }
        System.out.println("   <---> [OSCILACIÓN] Ahora se inserta en Cola " + colaPoner + " y se extrae de Cola " + colaExtraer);
    }

    public void nuevo(int dato) throws InterruptedException {
        // Tomamos mutexExtraer primero si y solo si necesitamos sincronizar el estado vacío,
        // o bien mantenemos el orden estricto para evitar interbloqueos.
        // Pero mirá qué simple: si la cola de extracción está vacía y nadie está extrayendo:
        insertar.acquire();
        if (colaPoner == 1) {
            cola1.poner(dato);
            System.out.println(Thread.currentThread().getName() + " insertó " + dato + " en Cola 1");
            if (cola2.esVacia()) {
                oscilar();
            }
        } else {
            cola2.poner(dato);
            System.out.println(Thread.currentThread().getName() + " insertó " + dato + " en Cola 2");
            if (cola1.esVacia()) {
                oscilar();
            }
        }
        insertar.release();
        // Avisamos que hay un nuevo elemento disponible recién DESPUÉS de haber oscilado (si estaba vacía)
        extraer.release(); 
    }

    public int sacar() throws InterruptedException {
        // 1. Espera a que haya al menos un elemento en el sistema
        extraer.acquire();
        // 2. Garantiza exclusión mutua entre extractores
        mutexExtraer.acquire();
        
        int dato;
        if (colaExtraer == 1) {
            if (cola1.longitud() == 1) {
                // Es el último elemento de la cola de extracción: bloqueamos inserción para oscilar
                insertar.acquire();
                dato = cola1.sacar();
                System.out.println(Thread.currentThread().getName() + " extrajo " + dato + " de Cola 1 (quedó vacía)");
                oscilar();
                insertar.release();
            } else {
                // Hay más de 1 elemento: extracción 100% simultánea con inserción
                dato = cola1.sacar();
                System.out.println(Thread.currentThread().getName() + " extrajo " + dato + " de Cola 1");
            }
        } else { // colaExtraer == 2
            if (cola2.longitud() == 1) {
                // Es el último elemento de la cola de extracción: bloqueamos inserción para oscilar
                insertar.acquire();
                dato = cola2.sacar();
                System.out.println(Thread.currentThread().getName() + " extrajo " + dato + " de Cola 2 (quedó vacía)");
                oscilar();
                insertar.release();
            } else {
                // Hay más de 1 elemento: extracción 100% simultánea con inserción
                dato = cola2.sacar();
                System.out.println(Thread.currentThread().getName() + " extrajo " + dato + " de Cola 2");
            }
        }
        
        mutexExtraer.release();
        return dato;
    }
}

class HiloInsertor implements Runnable {

    private Buffer buffer;

    public HiloInsertor(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            Random r = new Random();
            while (true) {
                int dato = r.nextInt(0, 101);
                buffer.nuevo(dato);
                Thread.sleep((long)(Math.random()) * 1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class HiloExtractor implements Runnable {
    private Buffer buffer;

    public HiloExtractor (Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            while (true) {
                int dato = buffer.sacar();
                System.out.println(Thread.currentThread().getName() + " sacó " + dato);
                Thread.sleep((long)(Math.random()) * 1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Problema2 {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        // Creamos 3 hilos insertores y 2 hilos extractores para probar la concurrencia
        for (int i = 1; i <= 3; i++) {
            Thread insertor = new Thread(new HiloInsertor(buffer), "Insertor-" + i);
            insertor.start();
        }

        for (int i = 1; i <= 2; i++) {
            Thread extractor = new Thread(new HiloExtractor(buffer), "Extractor-" + i);
            extractor.start();
        }
    }
}
