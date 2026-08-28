package ProblemasClasicos;

import java.util.concurrent.Semaphore;
import java.util.Scanner;

class Buffer {
    private int capacidadBuffer;
    private int[] almacenamiento;

    //Punteros para la lógica del buffer circular
    private int poner = 0; //Donde escribe el Productor
    private int sacar = 0; //Donde lee el consumidor
    
    private Semaphore mutex = new Semaphore(1); //Hace posible que se mantenga la consistencia al momento de actualizar tanto los permisos de los espacios llenos o vacios
    private Semaphore semVacio; //Simula un contador de los espacios vacios actualmente (inicia en la capacidad total del buffer)
    private Semaphore semLleno = new Semaphore(0); //Simula un contador de espacios llenos actualmente en los que los consumidores puedan consumir algo
    
    public Buffer (int unaCap) {
        this.capacidadBuffer = unaCap;
        this.almacenamiento = new int[this.capacidadBuffer];
        this.semVacio = new Semaphore (this.capacidadBuffer);
    }    
    
    //Método llamado por el PRODUCTOR

    public void producir(int producto) throws InterruptedException {
        //1. Sincronización (espera condicional): ¿Hay espacio para poner algo? Si semVacio es 0, se bloquea
        semVacio.acquire();

        try {
            //Exclusión mutua (entrada): Se quiere modificar el array, necesito el mutex para asegurar la consistencia de los datos al modificar
            mutex.acquire();

            //Sección Crítica
            this.almacenamiento[poner] = producto;
            System.out.println(Thread.currentThread().getName() + " Produjo: " + producto + " en posición [" + poner + "]");

            //Lógica circular: si se llega al final, vuelvo al índice 0
            poner = (poner + 1) % capacidadBuffer;

        } finally {
            //3. Exclusión mutua (salida)
            mutex.release();
        }

        //Sincronización (aviso)
        //Hay un nuevo item listo! Despierta a un Consumidor si estaba durmiendo
        semLleno.release();
    }

    //Método llamado por el CONSUMIDOR
    public int consumir() throws InterruptedException {
        //1. Sincronización (espera condicional)
        //Si semLleno es 0, se duerme
        semLleno.acquire();

        int producto = -999; //Valor temporal

        try {
            //2. Exclusión mutua (entrada)
            mutex.acquire();

            //Sección Crítica
            producto = this.almacenamiento[sacar];
            System.out.println(Thread.currentThread().getName() + " Consumió: " + producto + " de posición [" + sacar + "]"); 

            //Lógica circular
            sacar = (sacar + 1) % capacidadBuffer;
        } finally {
            //3. Exclusión mutua (salida)
            mutex.release();
        }

        //4. Sincronización (aviso)
        //Se liberó un espacio! Despierta a un Productor si estaba esperando
        semVacio.release();

        return producto;
    }

}

class Productor implements Runnable {
    private Buffer buffer;

    public Productor(Buffer unBuffer) {
        this.buffer = unBuffer;
    }

    public void run() {
        try {
            for (int i = 0; i <= 10; i++) { //Produce 10 cosas
                buffer.producir(i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {}
    }
}

class Consumidor implements Runnable {
    private Buffer buffer;

    public Consumidor (Buffer unBuffer) {
        this.buffer = unBuffer;
    }

    public void run() {
        try {
            while (true) {
                buffer.consumir();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {}
    }

}

public class ProdConsSem {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        Buffer buffer = new Buffer(10);
        
        System.out.println("Ingrese la cantidad de Productores existentes:");
        int cantProd = sc.nextInt();

        System.out.println("Ingrese la cantidad de consumidores existentes:");
        int cantCons = sc.nextInt();

        Thread[] productores = new Thread[cantProd];
        Thread[] consumidores = new Thread[cantCons];

        for (int i = 0; i < productores.length; i++) {
            productores[i] = new Thread (new Productor (buffer), "Productor - " + (i + 1));
            productores[i].start();
            try {
                Thread.sleep(500); //Mini sleep para dejar espacio entre hilo e hilo creado
            } catch (InterruptedException e) {}
        }

        for (int j = 0; j < consumidores.length; j++) {
            consumidores[j] = new Thread (new Consumidor(buffer), "Consumidor - " + (j + 1));
            consumidores[j].start();
            try {
                Thread.sleep(500); //Lo mismo que en productores
            } catch (InterruptedException e) {}
        }

        sc.close();
    }
}
