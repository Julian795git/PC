package Semaforos.ejercicio5;

import java.util.concurrent.Semaphore;

public class GestorImpresorasMixto {
    
    //Objeto para devolver el tipo y el ID asignado
    public static class ImpresoraAsignada {
        public final char tipo;
        public final int id;

        public ImpresoraAsignada(char tipo, int id) {
            this.tipo = tipo;
            this.id = id;
        }
    }

    private final Semaphore mutex = new Semaphore(1); //Exclusión mutua

    //Semáforos binarios para que los clientes esperen
    private final Semaphore esperaA = new Semaphore(0);
    private final Semaphore esperaB = new Semaphore(0);
    private final Semaphore esperaCualquiera = new Semaphore(0);

    //Estado de las impresoras
    private final boolean[] ocupadasA;
    private final boolean[] ocupadasB;

    //Contadores de hilos bloqueados
    private int esperandoA = 0;
    private int esperandoB = 0;
    private int esperandoCualquiera = 0;

    public GestorImpresorasMixto(int numA, int numB) {
        this.ocupadasA = new boolean[numA];
        this.ocupadasB = new boolean[numB];
    }

    public ImpresoraAsignada solicitarImpresora(char tipoRequerido) throws InterruptedException {
        mutex.acquire();
        ImpresoraAsignada asignada = null;

        if (tipoRequerido == 'A') {
            while (noHayLibres(ocupadasA)) {
                esperandoA++;
                mutex.release();
                esperaA.acquire();
                mutex.acquire();
            }
            asignada = new ImpresoraAsignada('A', ocuparPrimeraLibre(ocupadasA));
        } else if (tipoRequerido == 'B') {
            while (noHayLibres(ocupadasB)) {
                esperandoB++;
                mutex.release();
                esperaB.acquire();
                mutex.acquire();
            }
            asignada = new ImpresoraAsignada('B', ocuparPrimeraLibre(ocupadasB));
        } else if (tipoRequerido == 'X') { //'X' representa a los que aceptan A o B
            while (noHayLibres(ocupadasA) && noHayLibres(ocupadasB)) {
                esperandoCualquiera++;
                mutex.release();
                esperaCualquiera.acquire();
                mutex.acquire();
            }
            //Se prioriza darle la A si hay libre, sino se le da la B
            if(!noHayLibres(ocupadasA)) {
                asignada = new ImpresoraAsignada('A', ocuparPrimeraLibre(ocupadasA));
            } else {
                asignada = new ImpresoraAsignada('B', ocuparPrimeraLibre(ocupadasB));
            }
        }

        mutex.release();
        return asignada;
    }

    public void liberarImpresora(char tipo, int id) {
        try {
            mutex.acquire();

            if (tipo == 'A') {
                ocupadasA[id] = false;
                //Si alguien espera estrictamente por A, se lo despierta primero
                if (esperandoA > 0) {
                    esperandoA--;
                    esperaA.release();
                } else if (esperandoCualquiera > 0) {
                    //Si no hay nadie exlusivo de A, pero sí alguien que acepta cualquiera
                    esperandoCualquiera--;
                    esperaCualquiera.release();
                }
            } else if (tipo == 'B') {
                ocupadasB[id] = false;
                if (esperandoB > 0) {
                    esperandoB--;
                    esperaB.release();
                } else if (esperandoCualquiera > 0) {
                    esperandoCualquiera--;
                    esperaCualquiera.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    private boolean noHayLibres(boolean[] impresoras) {
        boolean todasOcupadas = true;
        int i = 0;
        while (i < impresoras.length && todasOcupadas) {
            if (!impresoras[i]) {
                todasOcupadas = false; //Se encontró una libre
            }
            i++;
        }

        return todasOcupadas;
    }

    private int ocuparPrimeraLibre(boolean[] impresoras) {
        int idEncontrado = -1;
        int i = 0;
        while (i < impresoras.length && idEncontrado == -1) {
            if (!impresoras[i]) {
                impresoras[i] = true;
                idEncontrado = i;
            }
            i++;
        }

        return idEncontrado;
    }

}
