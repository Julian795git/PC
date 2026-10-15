package PrimerParcial;

import java.util.concurrent.Semaphore;
import java.util.Scanner;

class EntradaHall {

    private int[] lugaresParaMesa;

    private Semaphore semPuedeEntrar = new Semaphore(0);
    private Semaphore semSentado = new Semaphore(0);
    private Semaphore semPuedeSalir = new Semaphore(0);
    private Semaphore semLiberado = new Semaphore(0);
    private Semaphore mutexMesa = new Semaphore(1);

    public EntradaHall(int[] mesas) {
        this.lugaresParaMesa = mesas;
    }

    public int permisoEntrar() throws InterruptedException {

        int longitud = this.lugaresParaMesa.length;
        boolean hallado = false;
        int mesa = -1;
        mutexMesa.acquire();        
        for (int i = 0; i < longitud && !hallado; i++) {
            if (lugaresParaMesa[i] > 0) {
                hallado = true;
                lugaresParaMesa[i]--;
                mesa = i;
                semPuedeEntrar.release();
                semSentado.acquire();
            } else {
                System.out.println("No hay lugares disponibles en la mesa " + (i + 1));
            }
        }
        mutexMesa.release();

        return mesa;
    }

    public void permisoSalir(int mesa) throws InterruptedException {
        mutexMesa.acquire();
        this.lugaresParaMesa[mesa]++;
        mutexMesa.release();
        semPuedeSalir.release();
        semLiberado.acquire();
    }

    public void entrar() throws InterruptedException {
        semPuedeEntrar.acquire();
        System.out.println(Thread.currentThread().getName() + " busca mesa");
        semSentado.release();
    }

    public void salir() throws InterruptedException {
        semPuedeSalir.acquire();
        System.out.println(Thread.currentThread().getName() + " sale de la FAI");
        semLiberado.release();
    }
}

class MesaConsulta {
    public synchronized void hacerConsulta() throws InterruptedException {

        System.out.println(Thread.currentThread().getName() + ": 'Tengo una consulta...");
        Thread.sleep(2000);
        System.out.println("Tutor: 'La respuesta a tu consulta es...");
        Thread.sleep(5000);
        System.out.println(Thread.currentThread().getName() + ": 'Gracias tutor. Adelante el siguiente!");
    }
}

class Estudiante implements Runnable {

    private int cantConsultas;
    private EntradaHall entrada;
    private MesaConsulta mesa;

    public Estudiante(int cant, EntradaHall ent, MesaConsulta m) {
        this.cantConsultas = cant;
        this.entrada = ent;
        this.mesa = m;
    }

    public void run() {
        try {
            int mesita = entrada.permisoEntrar();
            entrada.entrar();
            int consulta = 0;
            while (consulta < this.cantConsultas) {
                mesa.hacerConsulta();
                consulta++;
            }
            entrada.permisoSalir(mesita);
            entrada.salir();
        } catch (InterruptedException e) {

        }
    }
}

public class Parcial2026 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese la cantidad de tutores:");
        int tutores = sc.nextInt();
        System.out.println("Ingrese la cantidad de estudiantes concurridos:");
        int cantEst = sc.nextInt();

        int[] mesas = new int[tutores];
        for (int i = 0; i < tutores; i++) {
            System.out.println("Ingrese la capacidad de la mesa-" + (i + 1));
            mesas[i] = sc.nextInt();
        }
        MesaConsulta mesa = new MesaConsulta();
        EntradaHall entrada = new EntradaHall(mesas);

        Thread[] estudiantes = new Thread[cantEst];

        for (int j = 0; j < cantEst; j++) {
            estudiantes[j] = new Thread(new Estudiante(3, entrada, mesa), "Estudiante-" + (j +1));
        }

        sc.close();
    }
}
