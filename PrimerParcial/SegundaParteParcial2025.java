package PrimerParcial;

import java.util.Scanner;

class Pizarra {

    //Método sincronizado que representa la sección crítica
    //Sólo un hilo puede estar ejecutando este método a la vez sobre la misma instancia de Pizarra

    public synchronized void usar() throws InterruptedException {

        // 1. Encience la señal de Pizarra ocupada
        System.out.println(Thread.currentThread().getName() + " tomó la pizarra. --- PIZARRA OCUPADA ---");

        // 2. Simula la actividad (escribir en la pizarra)
        System.out.println(Thread.currentThread().getName() + " está escribiendo...");

        //Simulación del tiempo de uso
        Thread.sleep(1000 + (long) (Math.random() * 1500));

        // 3. Borra la pizarra y apaga la señal
        System.out.println(Thread.currentThread().getName() + " terminó y borró la pizarra");

        // 4. Apaga la señal de ocupada (esto es implícito, ocurre automáticamente al salir del método y liberar el lock)
        System.out.println(Thread.currentThread().getName() + " liberó la pizarra. --- PIZARRA LIBRE ---");

    }

}

class usuarioPizarra implements Runnable {
    private Pizarra pizarraCompartida;
    private int intentosDeUso = 3; //Cada hilo intentará usarla 3 veces

    public usuarioPizarra(Pizarra p) {
        this.pizarraCompartida = p;
    }

    public void run() {

        for (int i = 1; i <= intentosDeUso; i++) {
            try {
                //Simula que el hilo hace otras cosas (anda por ahí)
                System.out.println(Thread.currentThread().getName() + " está haciendo otras cosas...");
                Thread.sleep((long) (Math.random() * 2000));

                //Intenta tomar el control de la pizarra. Si la pizarra está ocupadam la JVM bloqueará el hilo hasta que esté libre
                System.out.println(Thread.currentThread().getName() + " quiere usar la pizarra");
                pizarraCompartida.usar();

            } catch (InterruptedException e) {}
        }

        System.out.println(Thread.currentThread().getName() + " terminó todas sus tareas");
    }
}

public class SegundaParteParcial2025 {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese la cantidad de hilos (k) que usarán la pizarra");
        int k = sc.nextInt();

        // 1. Se crea un solo recurso compartido
        Pizarra pizarra = new Pizarra();

        // 2. Se crea un arreglo para guardar los k hilos
        Thread[] hilos = new Thread[k];

        // 3. Se crean los hilos, se les da nombre y se les pasa la misma pizarra
        // a su vez, también se los inicializan
        for (int i = 0; i < k; i++) {
            hilos[i] = new Thread (new usuarioPizarra(pizarra), "Hilo " + (i + 1));
            hilos[i].start();
        }

        /*
         * OPCIONAL por las dudas
         * 5. El hilo main espera a que todos los hilos terminen (buena práctica)
         * try { 
         *      for (int i = 0; i < k; i++)
         *          hilos[i].join();
         * } catch (InterruptedException e) {}
         * 
         */

         System.out.println("Simulación Finalizada");
         sc.close();
    }
}
