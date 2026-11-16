package SegundoParcial.ProblemasClasicos;

import java.util.concurrent.locks.*;

class BaseDeDatos {
    // 1. El cerrojo principal para proteger el estado
    private final Lock lock = new ReentrantLock();
    
    // 2. Colas de condición separadas para un control fino
    private final Condition puedeLeer = lock.newCondition();
    private final Condition puedeEscribir = lock.newCondition();

    // 3. Variables de estado
    private int lectoresActivos = 0;
    private boolean escribiendo = false;
    private int escritoresEsperando = 0;

    // --- MÉTODOS PARA LECTORES ---

    public void empezarLectura(int idLector) throws InterruptedException {
        lock.lock();
        try {
            // Espera guardada: no lee si hay alguien escribiendo o si hay escritores en fila
            while (escribiendo || escritoresEsperando > 0) {
                puedeLeer.await();
            }
            lectoresActivos++;
            System.out.println("Lector " + idLector + " entra a leer. Lectores actuales: " + lectoresActivos);
        } finally {
            lock.unlock();
        }
    }

    public void terminarLectura(int idLector) {
        lock.lock();
        try {
            lectoresActivos--;
            System.out.println("Lector " + idLector + " termina. Lectores restantes: " + lectoresActivos);
            
            // Si soy el último lector en salir, le aviso a UN escritor que ya puede pasar
            if (lectoresActivos == 0) {
                puedeEscribir.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    // --- MÉTODOS PARA ESCRITORES ---

    public void empezarEscritura(int idEscritor) throws InterruptedException {
        lock.lock();
        try {
            escritoresEsperando++; // Me anoto en la fila
            
            // Espera guardada: exclusión mutua total (ni lectores ni otro escritor)
            while (lectoresActivos > 0 || escribiendo) {
                puedeEscribir.await();
            }
            
            escritoresEsperando--; // Salgo de la fila
            escribiendo = true;    // Tomo el control exclusivo
            System.out.println("Escritor " + idEscritor + " empieza a escribir.");
        } finally {
            lock.unlock();
        }
    }

    public void terminarEscritura(int idEscritor) {
        lock.lock();
        try {
            escribiendo = false;
            System.out.println("Escritor " + idEscritor + " termina de escribir.");
            
            // Política de despertar: si hay más escritores esperando, les doy prioridad
            if (escritoresEsperando > 0) {
                puedeEscribir.signal(); // Despierta a un solo escritor
            } else {
                puedeLeer.signalAll();  // Despierta a todos los lectores esperando
            }
        } finally {
            lock.unlock();
        }
    }
}

// Hilo Lector
class Lector implements Runnable {
    private final BaseDeDatos bd;
    private final int id;

    public Lector(BaseDeDatos bd, int id) {
        this.bd = bd;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                bd.empezarLectura(id);
                // Simula el tiempo de lectura
                Thread.sleep((long) (Math.random() * 1000));
                bd.terminarLectura(id);
                
                // Tiempo fuera de la BD
                Thread.sleep((long) (Math.random() * 2000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Hilo Escritor
class Escritor implements Runnable {
    private final BaseDeDatos bd;
    private final int id;

    public Escritor(BaseDeDatos bd, int id) {
        this.bd = bd;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                bd.empezarEscritura(id);
                // Simula el tiempo de escritura
                Thread.sleep((long) (Math.random() * 1500));
                bd.terminarEscritura(id);
                
                // Los escritores operan con menos frecuencia
                Thread.sleep((long) (Math.random() * 4000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class ProblemaLectoresEscritores {
    public static void main(String[] args) {
        BaseDeDatos bd = new BaseDeDatos();

        // Creamos 5 Lectores y 2 Escritores
        for (int i = 1; i <= 5; i++) {
            new Thread(new Lector(bd, i), "Lector-" + i).start();
        }
        for (int i = 1; i <= 2; i++) {
            new Thread(new Escritor(bd, i), "Escritor-" + i).start();
        }
    }
}
