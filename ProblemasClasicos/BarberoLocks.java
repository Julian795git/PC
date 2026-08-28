package ProblemasClasicos;

import java.util.concurrent.locks.*;
class Barberia {
    private int cantSillas;
    private int sillasOcupadas = 0;
    private boolean barberoOcupado = false;
    private final Lock lock = new ReentrantLock(true);
    private Condition condCliente = lock.newCondition();
    private Condition condBarbero = lock.newCondition();

    public Barberia (int numSillas) {
        this.cantSillas = numSillas;
    }

    //Método que llama al cliente
    public boolean solicitudCliente() throws InterruptedException {
        lock.lock();
        boolean corte = false;
        
        try {
            if (sillasOcupadas < cantSillas) {
                sillasOcupadas++;
                System.out.println(Thread.currentThread().getName() + ": Hay lugar, se sienta a esperar");
                
                while (barberoOcupado) {
                    System.out.println(Thread.currentThread().getName() + " espera su turno en la sala");
                    condCliente.await();
                }
                //Accede a su turno
                sillasOcupadas--;
                barberoOcupado = true;
                System.out.println(Thread.currentThread().getName() + " despierta al barbero y pasa a la silla");
                condBarbero.signal();
                corte = true;
            }
        } finally {
            lock.unlock();
        }

        return corte;
    }

    public void esperarCliente() throws InterruptedException {
        lock.lock();
        try {
            while (!barberoOcupado && sillasOcupadas == 0) {
                System.out.println("Zzzz... barbero duerme");
                condBarbero.await();
            }
            System.out.println("Barbero comienza a cortar el pelo");
        } finally {
            lock.unlock();
        }
    }

    public void terminarCorte() {
        lock.lock();
        try {
            barberoOcupado = false;
            System.out.println("Barbero terminó. Que pase el siguiente");
            condCliente.signal();
        } finally {
            lock.unlock();
        }
    }

}

class Barbero implements Runnable {
    private Barberia barberia;
    
    public Barbero(Barberia unaBarberia) {
        this.barberia = unaBarberia;
    }

    public void run() {
        try {
            while (true) {
                barberia.esperarCliente();
                Thread.sleep((long) (Math.random()) * 1000);
                barberia.terminarCorte();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Cliente implements Runnable {
    private Barberia barberia;

    public Cliente (Barberia unaBarberia) {
        this.barberia = unaBarberia;
    }

    public void run() {
        try {
            barberia.solicitudCliente();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

public class BarberoLocks {
    public static void main(String[] args) {
        // lo de siempre         
    }
}
