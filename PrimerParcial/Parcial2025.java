package PrimerParcial;

//Ejercicio 1 de parcial: el recorrido del colectivo (semáforos - SIN ESPERA ACTIVA - SIN INTERBLOQUEOS)
/*
 * Se desea simular el funcionamiento de un colectivo en su recorrido. El colectivo tiene capacidad para k pasajeros, y hay 1 parada de salida y 1 parada de llegada
 * 
 * El colectivero comienza su recorrido en la parada de salida, cuando el colectivo se llena. El colectivo tiene 1 puerta adelante que se habilita para la subida de los pasajeros
 * en la parada de salida, y una puerta atrás que se habilita para el descenso de los pasajeros cuando llega a destino
 * 
 * Una vez habilitada la puerta de ingreso, las personas esperando en la parada pueden comenzar a subir. Al subir un pasajero, el colectivero le cobra y verifica si hay lugar para un pasajero más,
 * en cuyo caso caso habilita la sbida de otro pasajero. Este proceso se repite hasta que se llena el colectivo o pudieron subir todas las personas que estaban esperando.
 * 
 * Luego el colectivo inicia su recorrido usual, hasta llegar a destino. Cuando llega a destino, el conductor del colectivo habilita la puerta trasera para que los pasajeros comiencen a descender.
 * Cuando un pasajero termina de bajar, le avisa al siguiente pasajero para que pueda bajar. El último pasajero en bajar le avisa al conductor que ya todos han bajado para que pueda comenzar un recorrido nuevo
 * 
 * Para ello, el colectivo se mueve hacia la parada de salida nuevamente y se repite el ciclo. Considere que el colectivo hace cantR recorridos:
 * 1. No interesa simular el cobro del pasaje
 * 2. Las personas que no puedan subir porque no hay espacio disponible deben esperar hasta la próxima habilitación para subir
 * 3. Las personas que lleguen a la parada mientras el colectivo está en su recorrido deben esperar
 * 4. Los pasajeros del colectivo NO pueden bajarse durante el recorrido. 
 */

import java.util.concurrent.Semaphore;
import java.util.Scanner;

class Colectivo {
    private Semaphore semColectivero = new Semaphore(0);
    private Semaphore semPasajero = new Semaphore(0);
    private Semaphore semPuertaAd = new Semaphore(0); //Puerta de adelante
    private Semaphore semPuertaAt = new Semaphore(0);
    private Semaphore semAsiento = new Semaphore(1);
    private Semaphore semParada = new Semaphore(0);
    private int ocupacion = 0;
    private int capacidad;
    private Parada salida; 

    public Colectivo (int unaCapacidad, Parada unaParada) {
        this.capacidad = unaCapacidad;
        this.salida = unaParada;
    }

    public void accesoCole() throws InterruptedException {
        //Corrección del profe: Si no entras en este if, el permiso de "semPasajero" no se libera nunca y el próximo método de hacerRecorrido, el colectiveroo queda en deadlock
        if (salida.getPasajeros() > 0) { 
            semPasajero.release();
            System.out.println("Hay pasajeros queriendo entrar");
            semColectivero.acquire();
            while (this.ocupacion < this.capacidad) {
                semPuertaAd.release();
                System.out.println("Adelante pasajero");
                Thread.sleep(500); //Mal uso de Thread en la clase del recurso compartido, sólo usar en runs() de hilos
                semAsiento.acquire();
                this.ocupacion++;
                semAsiento.release();
                System.out.println("Subió un pasajero");
                semPuertaAd.acquire();
                semParada.acquire();
                salida.setPasajeros(salida.getPasajeros() - 1);
                semParada.release();
            }
        }
    }

    public void hacerRecorrido() throws InterruptedException {
        semPasajero.acquire();
        System.out.println("Comienza el recorrido");
        Thread.sleep(5000); //De nuevo, mal uso de Thread acá
        System.out.println("Se llegó a destino");
        while (this.ocupacion > 1) {
            semPuertaAt.release();
            Thread.sleep(500); //De nuevo
            System.out.println("Bajó un pasajero. Que baje el siguiente");
            semPuertaAt.acquire();
            semAsiento.acquire();
            this.ocupacion--;
            semAsiento.release();
        }
        //Nota del Profe: Este mensaje lo debe decir el chofer, no un pasajero
        semPuertaAt.release();
        System.out.println("Soy el último pasajero, chofer"); 
        Thread.sleep(500); //De nuevo el sleep
        System.out.println("Último pasajero bajó exitosamente");
        semPuertaAt.acquire();
        semAsiento.acquire();
        this.ocupacion--;
        semAsiento.release();
        semColectivero.release();
    }

    public void hacerFila() throws InterruptedException {
        semParada.acquire();
        System.out.println("Un nuevo pasajero llegó a esperar el cole");
        salida.setPasajeros(salida.getPasajeros() + 1);
        semParada.release();
    }

}

//Nota del profe: Los sleeps deberían estar en los run de los hilos, no en el recurso compartido
//No hay comunicación entre pasajeros y chofer/colectivero, todo lo hace el chofer. Todos los semáforos los libera y agarra el mismo hilo, pero la idea era poder usar los semáforos para comunicarse con los pasajeros.

class Pasajero implements Runnable {
    private Colectivo cole;

    public Pasajero (Colectivo unCole) {
        this.cole = unCole;
    }

    public void run() {
        try {
            //Nota del profe: Una vez que se sube al colectivo, termina su vida. No simula la bajada del colectivo en este hilo y el rendevouz que eso conlleva
            cole.hacerFila();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Parada {
    private int pasajeros = 0;

    public Parada () {
        //Haría falta este método "constructor"?
    }

    public int getPasajeros() {
        return this.pasajeros;
    }

    public void setPasajeros(int unaCant) {
        this.pasajeros = unaCant;
    }

    //Nota del profe: la variable this.pasajero no está protegido. Entre pasajeros sí, por el semáforo "semParada" pero no el colectivero al preguntar cuanta gente hay en la parada
}

class Colectivero implements Runnable {
    private Colectivo cole;
    private final int cantRecorridos;
    private int recorridoActual = 0;

    public Colectivero (Colectivo unCole, int unaCant) {
        this.cole = unCole;
        this.cantRecorridos = unaCant;
    }

    public void run() {
        while (this.recorridoActual < this.cantRecorridos) {
            try {
                cole.accesoCole();
                cole.hacerRecorrido();
                this.recorridoActual++;
            } catch (InterruptedException e) {

            }
        }
    }
}

public class Parcial2025 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //Nota del profe: Faltarían mensajes (sout) para saber qué se está pidiendo ingresar por el scanner
        int capacidadCole = sc.nextInt();
        int cantidadTotalPasajeros = sc.nextInt();
        int cantRecorridos = sc.nextInt();
        Parada salida = new Parada();
        Colectivo colectivo = new Colectivo(capacidadCole, salida);
        Thread colectivero = new Thread (new Colectivero (colectivo, cantRecorridos));
        colectivero.start();

        Thread[] pasajeros = new Thread[cantidadTotalPasajeros];
        for (int i = 0; i < pasajeros.length; i++) {
            pasajeros[i] = new Thread (new Pasajero (colectivo));
            pasajeros[i].start(); //Nota del profe: Debería ser run en vez de start
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
            }
        }

        sc.close();
    }
}
