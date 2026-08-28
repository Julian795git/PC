package ProblemasClasicos;

//Existe ReentrantReadWriteLock para este caso clásico en particular :O

import java.util.concurrent.locks.*;
import java.util.Scanner;

class Libro {
    private final Lock lock = new ReentrantLock();
    private final Condition puedeLeer = lock.newCondition();
    private final Condition puedeEscribir = lock.newCondition();

    private int lectoresActuales = 0;
    private int colaEscritores = 0;
    private boolean escribiendo = false;

    //Método de entrada al RC para un lector
    public void empezarLectura() throws InterruptedException {
        lock.lock();
        try {
            while (escribiendo || lectoresActuales > 0) {
                System.out.println("Lector " + Thread.currentThread().getName() + " esperando...");
                puedeLeer.await();
            }
            lectoresActuales++;
            System.out.println("Lector " + Thread.currentThread().getName() + " consigue acceso. Cantidad Lectores Actual: " + lectoresActuales);
        } finally {
            lock.unlock();
        }
    }

    //Método de salida para los lectores
    public void terminarLectura() {
        lock.lock();
        try {
            lectoresActuales--;
            if (lectoresActuales == 0) {
                puedeEscribir.signal(); //Si ya es el último lector en salir (libera RC de lectores), avisa a escritor para que pueda acceder a escribir
            }
        } finally {
            lock.unlock();
        }
    }

    //Método de entrada para escritores
    public void empezarEscritura() throws InterruptedException {
        lock.lock();
        try {
            colaEscritores++; //entra en la cola de espra
            while (lectoresActuales > 0 || escribiendo ) {
                System.out.println("Escritor " + Thread.currentThread().getName() + " esperando..."); 
                puedeEscribir.await(); //Espera a que el último lector avise que salió
            }
            //Cuando accede:
            colaEscritores--; //Se sale de la cola de espera
            this.escribiendo = true; //empieza a escribir
            System.out.println("Escritor " + Thread.currentThread().getName() + " escribiendo");
        } finally {
            lock.unlock();
        }
    }

    public void terminarEscritura() { //En estos métodos de salida, no es necesario el throws InterruptedException ya que no llaman a ninguna Condition que son las que lanzan InterruptedException
        lock.lock();
        try {
            this.escribiendo = false;
            if (colaEscritores > 0) {
                puedeEscribir.signal(); //Si todavía hay escritores en la cola de espera, le avisa a uno de ellos que entre primero 
            } else {
                puedeLeer.signalAll(); //Avisa a todos los lectores que pueden acceder al RC libremente
            }
        } finally {
            lock.unlock();
        }
    }
}

class Lector implements Runnable {
    private final Libro libro;

    public Lector(Libro unLibro) {
        this.libro = unLibro;
    }

    public void run() {
        try {
            while(true) {

                libro.empezarLectura();
                System.out.println("Lector " + Thread.currentThread().getName() + " está leyendo");
                Thread.sleep((long) (Math.random()) * 500);

                libro.terminarLectura();
                Thread.sleep((long) (Math.random()) * 1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

class Escritor implements Runnable {
    private final Libro libro;

    public Escritor(Libro unLibro) {
        this.libro = unLibro;
    }

    public void run() {
        try {
            while (true) {
                libro.empezarEscritura();
                System.out.println("Escritor " + Thread.currentThread().getName() + " está escribiendo");
                Thread.sleep((long) (Math.random()) * 2000);

                libro.terminarEscritura();
                Thread.sleep((long) (Math.random()) + 1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class LectoresEscritores {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Libro libro = new Libro();
        int cantEscritores = sc.nextInt();
        int cantLectores = sc.nextInt();
        Thread[] escritores = new Thread[cantEscritores];
        Thread[] lectores = new Thread[cantLectores];

        for (int i = 0; i < escritores.length; i++) {
            escritores[i] = new Thread(new Escritor(libro));
            escritores[i].start();
        }

        for (int j = 0; j < lectores.length; j++) {
            lectores[j] = new Thread(new Lector(libro));
            lectores[j].start();
        }

        sc.close();
    }
}
