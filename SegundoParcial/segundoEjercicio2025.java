package SegundoParcial;

import java.util.concurrent.Semaphore;

class Buffer {
    private Cola cola1 = new Cola();
    private Cola cola2 = new Cola();
    private int colaExtraer = 2;
    private int colaPoner = 1;
    private Semaphore extraer = new Semaphore(0);
    private Semaphore insertar = new Semaphore(1, true);
    private Semaphore mutexExtraer = new Semaphore(1);

    public int sacar() throws InterruptedException {
        extraer.acquire();
        mutexExtraer.acquire();
        int dato = -1;
        /* 
        if (colaExtraer == 1 && cola1.longitud() == 1) {
            insertar.acquire();
            colaPoner = 1;
            colaExtraer = 2;
            dato = cola1.sacar();
            insertar.release();
        } else if (!cola1.esVacia()) {
            dato = cola1.sacar();
        } else if (colaExtraer == 2 && cola2.longitud() == 1){
            insertar.acquire();
            colaPoner = 2;
            colaExtraer = 1;
            dato = cola2.sacar();
            insertar.release();
        } else {
            dato = cola2.sacar();
        }
        */
        mutexExtraer.release();

        return dato;
    }

    public void nuevo(int dato) throws InterruptedException {
        insertar.acquire();
        if (colaPoner == 2) {
            cola2.poner(dato);
            if (cola1.esVacia()) {
                colaPoner = 1;
                colaExtraer = 2;
            }
        } else {
            cola1.poner(dato);
            if (cola2.esVacia()) {
                colaPoner = 2;
                colaExtraer = 1;
            }
        }

        extraer.release();
        insertar.release();
    }
}

class HiloInsertor implements Runnable {
    private Buffer buffer;

    public HiloInsertor (Buffer unBuffer) {
        this.buffer = unBuffer;
    }

    public void run() {
        try {
            while (true) {
                int dato = (int) (Math.random() * 101);
                buffer.nuevo(dato);
                Thread.sleep((long) (dato));
            }
        } catch (InterruptedException e) {}
    }

}

class HiloExtractor implements Runnable {
    private Buffer buffer;

    public HiloExtractor (Buffer unBuffer) {
        this.buffer = unBuffer;
    }

    public void run() {
        try {
            while (true) {
                int dato = buffer.sacar();
                System.out.println(Thread.currentThread().getName() +  " saca " + dato);
                Thread.sleep((long) (Math.random()) * 1000);
            }
        } catch (InterruptedException e) {}
    }
}

public class segundoEjercicio2025 {
    //Main...
}
