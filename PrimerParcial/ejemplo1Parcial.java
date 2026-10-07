package PrimerParcial;

//Enunciado
/*
Se desea simular el control de acceso a una sala histórica de un museo. Por cuestiones de preservación, a la sala solo pueden entrar grupos de hasta K turistas por vez. Hay 1 Guía encargado de la sala y N turistas que irán llegando.
El ciclo de la visita funciona de la siguiente manera:

    1. Ingreso controlado: Los turistas llegan al museo y esperan en la puerta de la sala. El Guía abre la puerta y habilita el ingreso de los turistas estrictamente de a uno por vez. Cuando un turista ingresa a la sala, le avisa al guía. El guía verifica si la sala ya alcanzó su capacidad máxima o si no hay más gente esperando; si todavía hay lugar y gente, habilita el ingreso de otro turista.

    2. La charla: Una vez que el grupo está completo (o entraron todos los que estaban esperando), el Guía cierra la puerta y da su charla explicativa (simular con un tiempo de espera). Los turistas que están adentro escuchan la charla.

    3. Salida ordenada (en cadena): Al terminar la charla, el Guía abre la puerta de salida, pero como es muy estrecha, los turistas deben salir en fila india. El Guía le da la orden de salir al primer turista. Cuando ese turista sale de la sala, le avisa al siguiente turista que puede salir, y así sucesivamente.

    4. Cierre: El último turista en salir de la sala le avisa al Guía que la sala quedó completamente vacía.

    5. El Guía vuelve a la puerta de entrada para iniciar un nuevo recorrido. El Guía realizará un total de R recorridos en su jornada.
*/

import java.util.Scanner;
import java.util.concurrent.Semaphore;

class Puerta {

    private int turistasEsperando = 0;
    private Semaphore mutexEspera = new Semaphore(1);

    public int getTuristas() throws InterruptedException {
        mutexEspera.acquire();
        int t = this.turistasEsperando;
        mutexEspera.release();
        return t;
    }

    public void agregarTurista() throws InterruptedException {
        mutexEspera.acquire();
        this.turistasEsperando++;
        System.out.println(Thread.currentThread().getName() + " está esperando en la puerta.");
        mutexEspera.release();
    }

    public void bajarTurista() throws InterruptedException {
        mutexEspera.acquire();
        this.turistasEsperando--;
        System.out.println(Thread.currentThread().getName() + " deja de esperar en la puerta.");
        mutexEspera.release();
    }
}

class Museo {

    private int capacidad;
    private int ocupacion = 0;
    private Puerta puerta;

    private Semaphore semPuedeEntrar = new Semaphore(0);
    private Semaphore semTuristaAdentro = new Semaphore(0);
    private Semaphore semPuedeSalir = new Semaphore(0);
    private Semaphore semTodosFuera = new Semaphore(0);
    private Semaphore mutexMuseo = new Semaphore(1); //Para cambios de valores de variables (ocupacion)

    public Museo(int k, Puerta unaPuerta) {
        this.capacidad = k;
        this.puerta = unaPuerta;
    }

    //----Métodos Guia----
    public void permitirEntrar() throws InterruptedException {

        while (this.ocupacion < this.capacidad && puerta.getTuristas() > 0) {
            System.out.println("Guia: Siguiente!");
            semPuedeEntrar.release();
            semTuristaAdentro.acquire();
        }

        System.out.println("Puerta cerrada. Ocupación: " + this.ocupacion);

    }

    public void iniciarTour() {
        System.out.println("Guia: Iniciando el tour por el museo.");
    }

    public void permitirSalir() throws InterruptedException {

        if (this.ocupacion != 0) {
            System.out.println("Guia: Terminamos el tour! Abriendo la puerta");
            semPuedeSalir.release();

            semTodosFuera.acquire();
            System.out.println("Guia: Ultimo turista salió.");
        } else {
            System.out.println("Guia: No hay nadie por salir");
        }

    }

    //----Métodos Turistas----
    public void hacerFila() throws InterruptedException {
        puerta.agregarTurista();
    }

    public void entrar() throws InterruptedException {
        semPuedeEntrar.acquire();
        puerta.bajarTurista();
        mutexMuseo.acquire();
        this.ocupacion++;
        mutexMuseo.release();
        semTuristaAdentro.release();
    }

    public void salir() throws InterruptedException {
        semPuedeSalir.acquire();
        mutexMuseo.acquire();
        this.ocupacion--;
        
        if (this.ocupacion > 0) {
            System.out.println(Thread.currentThread().getName() + ": Que salga el siguiente");
            semPuedeSalir.release();
        } else {
            System.out.println(Thread.currentThread().getName() + ": Soy el último turista.");
            semTodosFuera.release();
        }

        mutexMuseo.release();
    }
}

class Guia implements Runnable {

    private Museo museo;
    private int cantRecorridos;

    public Guia(Museo unMuseo, int r) {
        this.museo = unMuseo;
        this.cantRecorridos = r;
    }

    public void run() {
        int recorridoActual = 0;
        while (recorridoActual < this.cantRecorridos) {
            try {
                System.out.println("Recorrido " + (recorridoActual + 1) + "/" + this.cantRecorridos);
                museo.permitirEntrar();
                museo.iniciarTour();
                Thread.sleep(3000);
                museo.permitirSalir();

                Thread.sleep(1000);
                recorridoActual++;
            } catch (InterruptedException e) {

            }
        }

        System.out.println("Turno terminado.");
    }
}

class Turista implements Runnable {
    private Museo museo;

    public Turista(Museo unMuseo) {
        this.museo = unMuseo;
    }

    public void run() {
        try {
            museo.hacerFila();
            museo.entrar();
            museo.salir();
            System.out.println(Thread.currentThread().getName() + " terminó su tour");
        } catch (InterruptedException e) {

        }
    }
}

public class ejemplo1Parcial {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingrese la cantidad de tours por el museo:");
        int r = sc.nextInt();

        System.out.println("Ingrese la cantidad de turistas existentes:");
        int n = sc.nextInt();

        System.out.println("Ingrese la capacidad del museo:");
        int k = sc.nextInt();

        Puerta puerta = new Puerta();
        Museo museo = new Museo(k, puerta);
        Thread guia = new Thread(new Guia(museo, r), "Guia");
        guia.start();

        Thread[] turistas = new Thread[n];
        for (int i = 0; i < n; i++) {
            turistas[i] = new Thread(new Turista(museo), "Turista " + (i + 1));
            turistas[i].start();

            try {
                Thread.sleep(500); //Cooldown entre creación de hilos
            } catch (InterruptedException e) {

            }
        }

        sc.close();
    }
}
