package Semaforos.ejercicio1;

public class SynchronizedObjectCounter {
    
    private int c = 0;
    private final Object lock = new Object();

    public void increment() {
        synchronized (lock) {c++;} //Forma corregida a la del tp
    }

    public void decrement() {
        synchronized (lock) {c--;}
    }

    public int value() {
        synchronized (lock) {return c;}
    }

}
