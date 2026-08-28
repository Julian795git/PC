package Semaforos.ejercicio4;

import java.util.concurrent.Semaphore;

public class GestorImpresoras {
    private Semaphore[] impresoras;

    public GestorImpresoras (int numImpresoras) {
        impresoras = new Semaphore[numImpresoras];
        for (int i = 0; i < numImpresoras; i++) {
            impresoras[i] = new Semaphore(1);
        }
    }

    public int solicitarImpresora() {
       int idAsignada = -1;
       
        while (idAsignada == -1) {
            boolean hallado = false;
            for (int i = 0; i < impresoras.length && !hallado; i++) {
                try {
                    impresoras[i].acquire();
                    idAsignada = i;
                    hallado = true;
                } catch (InterruptedException e) {}
            }
        }

        return idAsignada;
    }

    public void liberarImpresora(int id) {
        impresoras[id].release();
    }

}
