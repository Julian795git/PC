package SegundoParcial.ProblemasClasicos;

import java.util.concurrent.Semaphore;

class MesaFilosofos {
    private final int NUM_FILOSOFOS = 5;
    
    // Arreglo de semáforos binarios: cada tenedor es un recurso de exclusión mutua
    private final Semaphore[] tenedores = new Semaphore[NUM_FILOSOFOS];
    
    // Semáforo General: limita a 4 la cantidad de filósofos que intentan comer a la vez
    // Esto rompe la condición de espera circular y evita el deadlock al 100%
    private final Semaphore capacidadMesa = new Semaphore(NUM_FILOSOFOS - 1);

    public MesaFilosofos() {
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            tenedores[i] = new Semaphore(1); // 1 permiso = tenedor apoyado en la mesa
        }
    }

    public void tomarTenedores(int id) throws InterruptedException {
        // 1. El filósofo pide permiso para "sentarse a intentar comer"
        capacidadMesa.acquire();
        
        int tenedorIzquierdo = id;
        int tenedorDerecho = (id + 1) % NUM_FILOSOFOS;

        // 2. Toma el tenedor izquierdo
        tenedores[tenedorIzquierdo].acquire();
        System.out.println("Filósofo " + id + " tomó el tenedor IZQUIERDO (" + tenedorIzquierdo + ")");

        // 3. Toma el tenedor derecho
        tenedores[tenedorDerecho].acquire();
        System.out.println("Filósofo " + id + " tomó el tenedor DERECHO (" + tenedorDerecho + ")");
    }

    public void soltarTenedores(int id) {
        int tenedorIzquierdo = id;
        int tenedorDerecho = (id + 1) % NUM_FILOSOFOS;

        // 1. Libera los tenedores para que sus vecinos puedan usarlos
        tenedores[tenedorIzquierdo].release();
        tenedores[tenedorDerecho].release();
        System.out.println("Filósofo " + id + " soltó ambos tenedores (" + tenedorIzquierdo + " y " + tenedorDerecho + ")");

        // 2. Avisa al portero que terminó, cediendo su lugar de intento a otro
        capacidadMesa.release();
    }
}

class Filosofo implements Runnable {
    private final MesaFilosofos mesa;
    private final int id;

    public Filosofo(MesaFilosofos mesa, int id) {
        this.mesa = mesa;
        this.id = id;
    }

    private void pensar() throws InterruptedException {
        System.out.println("Filósofo " + id + " está pensando...");
        Thread.sleep((long) (Math.random() * 2000));
    }

    private void comer() throws InterruptedException {
        System.out.println("Filósofo " + id + " está COMIENDO fideos.");
        Thread.sleep((long) (Math.random() * 1500));
    }

    @Override
    public void run() {
        try {
            while (true) {
                pensar();
                
                mesa.tomarTenedores(id);
                comer();
                mesa.soltarTenedores(id);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class ProblemaFilosofos {
    public static void main(String[] args) {
        MesaFilosofos mesa = new MesaFilosofos();
        Thread[] filosofos = new Thread[5];

        // Se sientan los 5 filósofos a la mesa
        for (int i = 0; i < 5; i++) {
            filosofos[i] = new Thread(new Filosofo(mesa, i), "HiloFilosofo-" + i);
            filosofos[i].start();
        }
    }
}