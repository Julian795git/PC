package Recuperatorio;

import java.util.concurrent.Semaphore;
import java.util.Scanner;

//Se desea simular un supermercado con las siguientes restricciones:
/*
1. Capacidad del Local: Solo pueden entrar N clientes al mismo tiempo al supermercado por cuestiones de aforo. Si está lleno, los clientes esperan afuera. 

2. La Góndola (stock): Hay una góndola con capacidad para K productos:
    - Hay hilos Repositores que reponen productos (Productores)
    - Hay hilos Clientes que toman productos (Consumidores)
    (Aplican reglas de stock: No se puede tomar si está vacía, no se puede reponer si está llena)

3. Las Cajas: Hay M cajas registradores funcionando
    - Una vez que el cliente tomó su producto, debe ir a pagar
    - Si todas las cajas están ocupadas, debe hacer fila y esperar a que una se libere
    - Solo después de pagar, el cliente abandona el supermercado (liberando lugar en el aforo)

*/

class Local {
    private final int capacidadTotal;
    private int capacidadActual = 0;
    private Semaphore aforoLocal;
    private Semaphore mutexLocal = new Semaphore(1);

    public Local (int unaCapacidad) {
        this.capacidadTotal = unaCapacidad;
        this.aforoLocal = new Semaphore(capacidadTotal, true);
    }

    public void entrarLocal() throws InterruptedException {
        aforoLocal.acquire();
        mutexLocal.acquire();
        capacidadActual++;
        System.out.println("Entró un nuevo cliente. Capacidad actual: " + capacidadActual);
        mutexLocal.release();
    }

    public void salirLocal() throws InterruptedException {
        mutexLocal.acquire();
        capacidadActual--;
        System.out.println("Salió un cliente. Capacidad actual: " + capacidadActual);
        mutexLocal.release();
        aforoLocal.release();
    }

}

class Gondola {
    private final int capacidadTotal;
    private int[] espacios;

    private int indexPoner = 0;
    private int indexSacar = 0;
    
    private Semaphore semLibres;
    private Semaphore semOcupados = new Semaphore(0);
    private Semaphore mutexGondola = new Semaphore (1); 

    public Gondola (int unaCapacidad) {
        this.capacidadTotal = unaCapacidad;
        this.semLibres = new Semaphore(capacidadTotal);
        this.espacios = new int[capacidadTotal];
    }

    public void reponer(int producto) throws InterruptedException {
        semLibres.acquire();
        mutexGondola.acquire();
        espacios[indexPoner] = producto;
        indexPoner = (indexPoner + 1) % capacidadTotal;
        System.out.println("Repositor " + Thread.currentThread().getName() + " repuso producto.");
        mutexGondola.release();
        semOcupados.release();
    }

    public int tomar() throws InterruptedException {
        semOcupados.acquire();
        mutexGondola.acquire();
        int prod = espacios[indexSacar];
        indexSacar = (indexSacar + 1) % capacidadTotal;
        System.out.println("Cliente " + Thread.currentThread().getName() + " toma producto");
        mutexGondola.release();
        semLibres.release();
        return prod;
    }

}

class LineaDeCajas {
    private Semaphore semCajas; //Semáforo contador
    private Semaphore mutexCajas = new Semaphore(1);
    private int cajasDisponibles;

    public LineaDeCajas(int cantidadCajas) {
        this.cajasDisponibles = cantidadCajas;
        this.semCajas = new Semaphore(cantidadCajas, true);
    }

    public void empezarPago() throws InterruptedException {
        semCajas.acquire();
        mutexCajas.acquire();
        this.cajasDisponibles--;
        System.out.println("Nueva caja ocupada. Cajas disponibles actuales: " + cajasDisponibles);
        mutexCajas.release();
    }   

    public void terminarPago() throws InterruptedException {
        mutexCajas.acquire();
        this.cajasDisponibles++;
        System.out.println("Nueva caja desocupada. Cajas disponibles actuales: " + cajasDisponibles);
        mutexCajas.release();
        semCajas.release();
    }

}

class Cliente implements Runnable {
    private Local local;
    private Gondola gondola;
    private LineaDeCajas cajas;

    public Cliente (Local unLocal, Gondola unaGondola, LineaDeCajas unaLinea) {
        this.local = unLocal;
        this.gondola = unaGondola;
        this.cajas = unaLinea;
    }

    public void run() {
        try {
            local.entrarLocal();
            gondola.tomar();
            cajas.empezarPago();
            Thread.sleep((long) (Math.random() * 500));
            cajas.terminarPago();
            local.salirLocal();
        } catch (InterruptedException e) {}
    }

}

class Repositor implements Runnable {
    private Gondola gondola;

    public Repositor (Gondola unaGondola) {
        this.gondola = unaGondola;
    }

    public void run() {
        try {
            int i = 0;
            while (true) {
                gondola.reponer(i++);
                Thread.sleep((long) (Math.random() * 800));
            }
        } catch (InterruptedException e) {}
    }

}

public class Supermercado {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingrese la cantidad de aforo total del supermercado:");
        int aforoSuper = sc.nextInt();

        Local supermercado = new Local(aforoSuper);

        System.out.println("Ingrese la cantidad de productos totales en la góndola");
        int capacidadGondola = sc.nextInt();

        Gondola gondola = new Gondola(capacidadGondola);

        System.out.println("Ingrese la cantidad de cajas totales en el super:");
        int cantidadCajas = sc.nextInt();

        LineaDeCajas lineaCajas = new LineaDeCajas(cantidadCajas);

        System.out.println("Ingrese la cantidad de Clientes en la simulación:");
        int cantClientes = sc.nextInt();
        Thread[] clientes = new Thread[cantClientes];

        System.out.println("Ingrese la cantidad de Repositores en la simulación:");
        int cantRepositores = sc.nextInt();
        Thread[] repositores = new Thread[cantRepositores];

        for (int i = 0; i < clientes.length; i++) {
            clientes[i] = new Thread (new Cliente (supermercado, gondola, lineaCajas), "Cliente " + (i + 1));
            clientes[i].start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
        }

        for (int j = 0; j < repositores.length; j++) {
            repositores[j] = new Thread (new Repositor (gondola), "Repositor " + (j + 1));
            repositores[j].start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
        }

        sc.close();
    }
}
