package ProblemasClasicos;

import java.util.concurrent.locks.*;
import java.util.Scanner;

class Mesa {
    private final int N = 5;
    private static final int pensando = 0;
    private static final int hambriento = 1;
    private static final int comiendo = 2;

    private final int[] estados = new int[N];
    private final Lock lock = new ReentrantLock();
    private final Condition[] puedeComer = new Condition[N];

    public Mesa() {
        for (int i = 0; i < N; i++) {
            estados[i] = pensando;
            puedeComer[i] = lock.newCondition();
        }
    }

    private void revisarVecinos(int i) {
        int izq = (i + N - 1) % N; //Se resta 1 para pasar al de su izquierda, pero es necesario primero sumarle N a i para evitar que, si i = 0, 0 - 1 = -1 y se salga. Entonces con + N y luego % N se obtenga 4 (un resultado dentro de los límites)
        int der = (i + 1) % N; //Solo se suma 1 a i (pasando al filósofo a su derecha) incluyendo "% N" para manejar el caso de i = 4 (normalmente daría 5 y se saldría del rango, pero con % N da cero y se mantiene dentro de los límites)
        
        //Si está hambriento y los vecinos no están comiendo
        if (estados[i] == hambriento && estados[izq] != comiendo && estados[der] != comiendo) {
            estados[i] = comiendo;
            puedeComer[i].signal();
        }
    }

    //Método donde un filósofo "i" intenta tomar los tenedores
    public void tomarTenedores(int i) throws InterruptedException {
        lock.lock();
        try {
            //Primero se debe marcar como hambriento
            estados[i] = hambriento;
            System.out.println("filósofo " + i + " está hambriento");

            revisarVecinos(i); //Revisa a ver si puede comer (pasar a estado COMIENDO)

            if (estados[i] != comiendo) {
                System.out.println("Filósofo " + i + " no puede comer, espera a poder");
                puedeComer[i].await();
            }

            //Al despertar, su estado ya estará modificado a COMIENDO
            System.out.println("Filósofo " + i + " está comiendo");
        } finally {
            lock.unlock();
        }
    }

    public void soltarTenedores(int i) {
        lock.lock();
        try {
            estados[i] = pensando;
            System.out.println("Filósofo " + i + " terminó de comer, se pone a pensar");

            //Reviso si los vecinos (que están hambrientos) ahora pueden comer
            int izq = (i + N - 1) % N;
            int der = (i + 1) % N;

            revisarVecinos(izq);
            revisarVecinos(der);

        } finally {
            lock.unlock();
        }
    }

}

class Filosofo implements Runnable {
    private final Mesa mesa;
    private int id;

    public Filosofo(Mesa unaMesa, int unId) {
        this.mesa = unaMesa;
        this.id = unId;
    } 

    public void run() {
        try {
            while (true) {
                mesa.tomarTenedores(id);
                Thread.sleep((long) (Math.random() * 1000));

                mesa.soltarTenedores(id);
                Thread.sleep((long) (Math.random()) * 2000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class CenaFilosofos {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Mesa mesa = new Mesa();
        Thread[] filosofos = new Thread[5];

        for (int i = 0; i < filosofos.length; i++) {
            filosofos[i] = new Thread(new Filosofo(mesa, i));
            filosofos[i].start();
        }
        
        sc.close();
    }
}
