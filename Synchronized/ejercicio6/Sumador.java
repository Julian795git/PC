package Synchronized.ejercicio6;

public class Sumador implements Runnable {
    
    private int[] arreglo;
    private int inicio;
    private int fin;
    private long sumaParcial; // Guardará el subtotal de este hilo

    public Sumador(int[] arreglo, int inicio, int fin) {
        this.arreglo = arreglo;
        this.inicio = inicio;
        this.fin = fin;
        this.sumaParcial = 0;
    }

    @Override
    public void run() {
        // Cada hilo efectúa la suma de su segmento del arreglo
        for (int i = inicio; i < fin; i++) {
            sumaParcial += arreglo[i];
        }
        System.out.println(Thread.currentThread().getName() + " terminó su parte. Suma parcial: " + sumaParcial);
    }

    // Método clave para que el Main recupere el valor calculado
    public long getSumaParcial() {
        return sumaParcial;
    }
}
