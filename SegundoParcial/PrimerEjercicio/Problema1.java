package SegundoParcial.PrimerEjercicio;

import java.util.concurrent.locks.*;
import java.util.Scanner;

class Mesa {

    private final Lock mesaLock = new ReentrantLock();
    private final Condition mesaVacia = mesaLock.newCondition();
    private final Condition mesaLibre = mesaLock.newCondition();
    private int[] cantCanciones = {0,0,0,0};
    //indices para acceder al género deseado
    //Rock = 0
    //Folk = 1
    //Blues = 2
    //Country = 3

    private boolean estaVacia() { //Me quedó un subrayado como corrección en 'private', será que tenía que ser público?
    boolean vacio = true;
    for (int i = 0; i < cantCanciones.length && vacio; i++) {
        if (cantCanciones[i] != 0) {
            vacio = false;
        }
    }
    return vacio;
    }

    public void tomarCancion (int genero) {
        mesaLock.lock();
        try {
            while(estaVacia()) {
                mesaVacia.await();
            }
        cantCanciones[genero]--; //Corrección: "Ojo! Puede ser que no esté vacía pero no sea de ese género", algo así dice
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mesaLock.unlock();
        }
    }

    public void ponerCancion (int genero) {
        mesaLock.lock();
        try{
            cantCanciones[genero]++;
            mesaVacia.signalAll(); //Corrección: Podría tener distintos Conditions para los distintos generos
        } finally {
            mesaLock.unlock();
        }
    }
}

class Album {
    private final Lock albumLock = new ReentrantLock();
    private final Condition terminado = albumLock.newCondition();
    private final Condition pendiente = albumLock.newCondition();
    private int duracionAlbum;
    private int duracionActual = 0;
    private int[] duraciones = {duracionR, duracionF, duracionB, duracionC};
    //indices iguales a arreglo cantCanciones de Mesa
    // Rock = 0, Folk = 1, Blues = 2, Country = 3
    private boolean nombre = false;

    public Album (int unaDuracion, int[] valores) {
    this.duracionAlbum = unaDuracion;
    for (int i = 0; i < duraciones.length; i++) {
        duraciones[i] = valores[i];
    }
    }

    public void agregarCancion(int genero) {
    albumLock.lock();
    try {
        while (!nombre || duraciones[genero] + duracionActual > duracionAlbum) {
            terminado.signal();
            pendiente.await();
        }
        duracionActual += duraciones[genero];
        if (duracionActual == duracionAlbum) {
            terminado.signal();
        }
    } finally {
        albumLock.unlock();
    }
    }

    public void promocionar() {
    albumLock.lock();
    try {
        while (duracionActual < duracionAlbum) {
            terminado.await();
        }
        this.nombre = false;
    } finally {
        albumLock.unlock();
    }
    }

    public void pensarNombre() {
    albumLock.lock();
    try {
        this.nombre = true;
        pendiente.signalAll();
    } finally {
        albumLock.unlock();
    }
    }
}

class BobDylan implements Runnable {

    private Mesa mesa;
    private generadorCanciones gen;        private int generoCancion;

     public BobDylan (Mesa unaMesa, GeneradorCanciones unGen) { //... , GeneradorCanciones unGen)
        this.mesa = unaMesa;
        this.gen = unGen;
        this.generoCancion = -1;
     }

    public void run() {
        try {
            while (true) {
                this.generoCancion = gen.generarCancion(); 
                mesa.ponerCancion(this.generoCancion);
                Thread.sleep((long)(Math.random()) * 500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Productor implements Runnable {

    private Mesa mesa;
    private Album album;
    private int generoOcupado;

    public Productor(Mesa unaMesa, Album unAlbum, int genero) {
        this.mesa = unaMesa;
        this.album = unAlbum;
        this.generoOcupado = genero;
    }

    public void run() {
        try {
            while (true) {
                mesa.tomarCancion(this.generoOcupado);
                Thread.sleep((long)(Math.random()) * 500);
                album.agregarCancion(this.generoOcupado);
                Thread.sleep((long)(Math.random()) * 1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Manager implements Runnable {

    private Album album;

    public Manager(Album unAlbum) {
        this.album = unAlbum;
    }

    public void run() {
        try {
            while (true) {
                album.promocionar();
                Thread.sleep((long)(Math.random()) * 4000);
                album.pensarNombre(); //Corrección: Mover al inicio del while (poner al principio del while)
                Thread.sleep((long)(Math.random()) * 1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Problema1 {
    
    public static void main(String[] args) {
    
        Scanner sc = new Scanner(System.in);    
        Mesa mesa = new Mesa();
        int[] valores = new int[4];
        for (int i = 0; i < 4; i++) {
            System.out.println("Ingrese la duraciónd el género " + (i + 1));
            valores[i] = sc.nextInt();
        }
        //Rock = 1, Folk = 2, Blues = 3, Country = 4
        System.out.println("Ingrese la duración máxima de los álbumes:");
        int duracionMax = sc.nextInt();
        Album album = new Album(duracionMax, valores);
        Thread generador = new Thread(new GeneradorCanciones());
        Thread bob = new Thread(new BobDylan(mesa, generador));
        Thread manager = new Thread(new Manager(album));
        System.out.println("Ingrese la cantidad total de productores:");
        int cantProd = sc.nextInt();
        Thread[] productores = new Thread[cantProd];
        for (int j = 0; j < productores.length; j++) {
            productores[j] = new Thread(new Productor(mesa, album, (Math.random() * 3))); //Random de cero a tres [genera conflicto al devolver double - revisar]
            productores[j].start();
        }

        manager.start();
        generador.start();
        bob.start();

    }
}
