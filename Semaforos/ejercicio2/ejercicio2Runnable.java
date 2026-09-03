package Semaforos.ejercicio2;

import java.util.concurrent.Semaphore;

public class ejercicio2Runnable {

    // Tarea para el Proceso 1
    static class TareaP1 implements Runnable {
        private final Semaphore semEntrada;
        private final Semaphore semSalida;

        public TareaP1(Semaphore semEntrada, Semaphore semSalida) {
            this.semEntrada = semEntrada;
            this.semSalida = semSalida;
        }

        @Override
        public void run() {
            try {
                semEntrada.acquire();
                System.out.println("Iniciando operaciones P1...");
                Thread.sleep(1000);
                System.out.println("Finalizado P1.");
                semSalida.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Tarea para el Proceso 2
    static class TareaP2 implements Runnable {
        private final Semaphore semEntrada;
        private final Semaphore semSalida;

        public TareaP2(Semaphore semEntrada, Semaphore semSalida) {
            this.semEntrada = semEntrada;
            this.semSalida = semSalida;
        }

        @Override
        public void run() {
            try {
                semEntrada.acquire();
                System.out.println("Iniciando operaciones P2...");
                Thread.sleep(1000);
                System.out.println("Finalizado P2.");
                semSalida.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Tarea para el Proceso 3 (libera dos semáforos)
    static class TareaP3 implements Runnable {
        private final Semaphore semEntrada;
        private final Semaphore semSalida1;
        private final Semaphore semSalida2;

        public TareaP3(Semaphore semEntrada, Semaphore semSalida1, Semaphore semSalida2) {
            this.semEntrada = semEntrada;
            this.semSalida1 = semSalida1;
            this.semSalida2 = semSalida2;
        }

        @Override
        public void run() {
            try {
                semEntrada.acquire();
                System.out.println("Iniciando operaciones P3...");
                Thread.sleep(1000);
                System.out.println("Finalizado P3.");
                semSalida1.release();
                semSalida2.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Tarea para el Proceso 4 (solo adquiere)
    static class TareaP4 implements Runnable {
        private final Semaphore semEntrada;

        public TareaP4(Semaphore semEntrada) {
            this.semEntrada = semEntrada;
        }

        @Override
        public void run() {
            try {
                semEntrada.acquire();
                System.out.println("Iniciando operaciones P4...");
                Thread.sleep(1000);
                System.out.println("Finalizado P4.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Inicialización según el enunciado
        Semaphore sem1 = new Semaphore(0);
        Semaphore sem2 = new Semaphore(1);
        Semaphore sem3 = new Semaphore(0);
        Semaphore sem4 = new Semaphore(0);

        // Instanciamos las tareas pasando los semáforos correspondientes
        Runnable tarea1 = new TareaP1(sem2, sem1);
        Runnable tarea2 = new TareaP2(sem3, sem2);
        Runnable tarea3 = new TareaP3(sem1, sem3, sem4);
        Runnable tarea4 = new TareaP4(sem4);

        // Creamos los hilos a partir de las tareas
        Thread h1 = new Thread(tarea1, "Hilo-P1");
        Thread h2 = new Thread(tarea2, "Hilo-P2");
        Thread h3 = new Thread(tarea3, "Hilo-P3");
        Thread h4 = new Thread(tarea4, "Hilo-P4");

        // Iniciamos todos los hilos
        h1.start();
        h2.start();
        h3.start();
        h4.start();
    }
}
