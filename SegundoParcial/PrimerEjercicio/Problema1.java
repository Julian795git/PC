package SegundoParcial.PrimerEjercicio;

import java.util.concurrent.locks.*;
import java.util.Scanner;
class Mesa {
    private final Lock mesaLock = new ReentrantLock();
    // Una condición de espera específica para cada género (0: Rock, 1: Folk, 2: Blues, 3: Country)
    private final Condition[] esperaGenero = new Condition[4];
    private final int[] cantCanciones = {0, 0, 0, 0};

    public Mesa() {
        for (int i = 0; i < 4; i++) {
            esperaGenero[i] = mesaLock.newCondition();
        }
    }

    public void ponerCancion(int genero) {
        mesaLock.lock();
        try {
            cantCanciones[genero]++;
            System.out.println("Bob puso una canción del género " + genero + ". Total en bandeja: " + cantCanciones[genero]);
            // Despertamos solo a un productor que esté esperando este género en particular
            esperaGenero[genero].signal();
        } finally {
            mesaLock.unlock();
        }
    }

    public void tomarCancion(int genero) throws InterruptedException {
        mesaLock.lock();
        try {
            // Verifica únicamente la bandeja de su género
            while (cantCanciones[genero] == 0) {
                esperaGenero[genero].await();
            }
            cantCanciones[genero]--;
            System.out.println("Productor de género " + genero + " tomó una canción de la mesa.");
        } finally {
            mesaLock.unlock();
        }
    }
}

class Album {
    private final Lock albumLock = new ReentrantLock();
    private final Condition albumListoParaPromocionar = albumLock.newCondition();
    private final Condition puedeAgregar = albumLock.newCondition();

    private final int duracionMax;
    private int duracionActual = 0;
    private final int[] duraciones; // {duracionR, duracionF, duracionB, duracionC}
    
    private boolean tieneNombre = false;
    private boolean albumTerminado = false;

    public Album(int duracionMax, int[] duraciones) {
        this.duracionMax = duracionMax;
        this.duraciones = duraciones;
    }

    // 1. Primera acción del Manager en el ciclo
    public void ponerNombre() {
        albumLock.lock();
        try {
            this.duracionActual = 0;
            this.albumTerminado = false;
            this.tieneNombre = true;
            System.out.println("Manager asignó nombre al nuevo álbum. ¡Productores pueden agregar canciones!");
            puedeAgregar.signalAll(); // Despierta a todos los productores
        } finally {
            albumLock.unlock();
        }
    }

    // 2. Acción de los Productores
    public void agregarCancion(int genero) throws InterruptedException {
        albumLock.lock();
        try {
            // Intenta agregar la canción en un bucle hasta que logre insertarla en algún álbum
            boolean agregada = false;
            while (!agregada) {
                // Si el álbum no tiene nombre o ya fue marcado como terminado por otro productor, espera
                while (!tieneNombre || albumTerminado) {
                    puedeAgregar.await();
                }

                // Verifica si la canción entra en el álbum actual
                if (duracionActual + duraciones[genero] <= duracionMax) {
                    duracionActual += duraciones[genero];
                    agregada = true;
                    System.out.println("Productor (género " + genero + ") agregó canción. Duración actual: " + duracionActual + "/" + duracionMax);
                } else {
                    // No entra: soy el PRIMER productor que no puede colocar su canción
                    albumTerminado = true;
                    tieneNombre = false; // Bloquea a otros productores hasta el próximo álbum
                    System.out.println("Productor (género " + genero + ") no pudo agregar canción (supera límite). Avisa al Manager.");
                    albumListoParaPromocionar.signal();
                    // Como 'agregada' sigue siendo false, da la vuelta al while y se duerme en puedeAgregar.await()
                    // conservando la canción que ya tomó de la mesa para meterla en el próximo álbum.
                }
            }
        } finally {
            albumLock.unlock();
        }
    }

    // 3. Segunda acción del Manager: esperar a que se llene y promocionarlo
    public void esperarYPromocionar() throws InterruptedException {
        albumLock.lock();
        try {
            while (!albumTerminado) {
                albumListoParaPromocionar.await();
            }
            System.out.println("Manager promocionando el álbum terminado con duración: " + duracionActual);
        } finally {
            albumLock.unlock();
        }
    }
}

// Hilo que representa a Bob Dylan (GeneradorCanciones)
class GeneradorCanciones implements Runnable {
    private final Mesa mesa;

    public GeneradorCanciones(Mesa mesa) {
        this.mesa = mesa;
    }

    private int generarCancion() {
        // No era necesario implementarlo, pero devuelve 0, 1, 2 o 3
        return (int) (Math.random() * 4);
    }

    @Override
    public void run() {
        try {
            while (true) {
                int genero = generarCancion();
                mesa.ponerCancion(genero);
                Thread.sleep((long) (Math.random() * 500));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Productor implements Runnable {
    private final Mesa mesa;
    private final Album album;
    private final int genero;

    public Productor(Mesa mesa, Album album, int genero) {
        this.mesa = mesa;
        this.album = album;
        this.genero = genero;
    }

    @Override
    public void run() {
        try {
            while (true) {
                mesa.tomarCancion(this.genero);
                album.agregarCancion(this.genero);
                Thread.sleep((long) (Math.random() * 1000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Manager implements Runnable {
    private final Album album;

    public Manager(Album album) {
        this.album = album;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Piensa el nombre y habilita el álbum
                Thread.sleep((long) (Math.random() * 1000));
                album.ponerNombre();
                
                // Espera a que un productor le avise que no entra otra canción y lo promociona
                album.esperarYPromocionar();
                Thread.sleep((long) (Math.random() * 2000)); // Tiempo de promoción
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
            System.out.println("Ingrese la duración del género " + (i + 1));
            valores[i] = sc.nextInt();
        }
        //Rock = 1, Folk = 2, Blues = 3, Country = 4
        System.out.println("Ingrese la duración máxima de los álbumes:");
        int duracionMax = sc.nextInt();
        Album album = new Album(duracionMax, valores);
        Thread generador = new Thread(new GeneradorCanciones(mesa));
        Thread manager = new Thread(new Manager(album));
        System.out.println("Ingrese la cantidad total de productores:");
        int cantProd = sc.nextInt();
        Thread[] productores = new Thread[cantProd];
        for (int j = 0; j < productores.length; j++) {
            productores[j] = new Thread(new Productor(mesa, album, (int) (Math.random() * 3))); //Random de cero a tres [genera conflicto al devolver double - revisar]
            productores[j].start();
        }

        manager.start();
        generador.start();

        sc.close();
    }
}
