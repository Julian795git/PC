package Semaforos.ejercicio4;

import java.util.concurrent.Semaphore;

public class GestorImpresoras {

    private final Semaphore mutex;        // Binario (1): exclusión mutua para modificar estado
    private final Semaphore semEspera;    // Binario (0): señalización donde duermen los clientes sin impresora
    private final boolean[] ocupadas;     // Estado de las impresoras (true = ocupada)
    private int clientesEsperando;        // Contador de cuántos hilos están dormidos

    public GestorImpresoras(int numImpresoras) {
        this.mutex = new Semaphore(1);
        this.semEspera = new Semaphore(0);
        this.ocupadas = new boolean[numImpresoras];
        this.clientesEsperando = 0;
    }

    public int solicitarImpresora() throws InterruptedException {
        mutex.acquire();

        // Mientras no haya impresoras libres, el hilo espera
        while (todasOcupadas()) {
            clientesEsperando++;
            mutex.release();        // Libera la puerta para que otros puedan trabajar o liberar
            semEspera.acquire();    // Se duerme aquí esperando que alguien le haga release()
            mutex.acquire();        // Vuelve a entrar con exclusión mutua
        }

        // Busca y marca la primera impresora que encuentre disponible
        int idAsignada = -1;
        int i = 0;

        while (i < ocupadas.length && idAsignada == -1) {
            if (!ocupadas[i]) {
                ocupadas[i] = true;
                idAsignada = i;
            }
            i++;
        }

        mutex.release();
        return idAsignada;
    }

    public void liberarImpresora(int id) {
        try {
            mutex.acquire();

            ocupadas[id] = false;

            // Si hay alguien esperando, despertamos a exactamente uno
            if (clientesEsperando > 0) {
                clientesEsperando--;
                semEspera.release(); // Pasa el permiso para despertar a un hilo
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    private boolean todasOcupadas() {
        boolean exito = true;
        for (boolean ocupada : ocupadas) {
            if (!ocupada) {
                exito = false;
            }
        }
        return exito;
    }
}