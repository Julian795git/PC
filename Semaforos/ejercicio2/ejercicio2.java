package Semaforos.ejercicio2;

import java.util.concurrent.Semaphore;

public class ejercicio2 {
    
    private static Semaphore sem1 = new Semaphore(0);
    private static Semaphore sem2 = new Semaphore(1);
    private static Semaphore sem3 = new Semaphore(0);
    private static Semaphore sem4 = new Semaphore(0);

    static class P1 extends Thread {
        public void run() {
            try {
                sem2.acquire();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                sem1.release();
            }
        }
    }

    //Si sem2 se inicializa en cero (acceso cerrado), el programa nunca podrá ingresar a la clase P1 y realizar sus operaciones al no poder obtener el permiso de entrada.
    //Si sem1 se inicializa en uno (acceso abierto), el programa al realizar sem1.release(), estaría dejando a sem1 con 2 accesos (dejando de ser binario).

    static class P2 extends Thread {
        public void run() {
            try {
                sem3.acquire();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                sem2.release();
            }
        }
    }

    static class P3 extends Thread {
        public void run() {
            try {
                sem1.acquire();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                sem3.release();
                sem4.release();
            }
        }
    }

    static class P4 extends Thread {
        public void run() {
            try {
                sem4.acquire();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new P1().start();
        new P2().start();
        new P3().start();
        new P4().start();
    }
}
