package Synchronized.ejercicio6;

import java.util.Random;
import java.util.Arrays;

public class Main {
    
    /**
     * Método delegado que rellena el arreglo con números aleatorios entre 1 y 10.
     */
    public static void llenarArreglo(int[] arreglo) {
        Random random = new Random();
        // Usamos la API de Streams/Arrays para llenarlo eficientemente en paralelo
        // random.nextInt(10) genera de 0 a 9, le sumamos 1 para que sea de 1 a 10
        Arrays.parallelSetAll(arreglo, i -> random.nextInt(10) + 1);
        System.out.println("Arreglo llenado exitosamente.");
    }
    
    /*
    Método alternativo:
    public static void llenarArreglo(int[] arreglo) {
        Random random = new Random();
        // Genera 50.000 enteros entre 1 (inclusivo) y 11 (exclusivo)
        int[] arreglo = random.ints(50000, 1, 11).toArray();
    }
    
    */

    public static void main(String[] args) {
        int capacidad = 50000;
        int[] enteros = new int[capacidad];

        // 1. Delegamos el llenado del arreglo al método auxiliar
        llenarArreglo(enteros);

        // 2. Determinamos la cantidad óptima de hilos (k)
        int k = Runtime.getRuntime().availableProcessors();
        System.out.println("Se utilizarán " + k + " hilos para procesar el arreglo.");
        
        Thread[] hilos = new Thread[k];
        Sumador[] tareas = new Sumador[k]; // Referencias para obtener las sumas parciales luego. 

        int tamañoBloque = enteros.length / k; // Tamaño de bloque que cada hilo va a procesar para la suma

        // 3. Crear, asignar segmentos y lanzar los hilos
        for (int i = 0; i < k; i++) {
            int inicio = i * tamañoBloque;
            int fin = (i == k - 1) ? enteros.length : inicio + tamañoBloque;

            //Es necesario ir guardando los objetos Sumador en su propio arreglo para tener la referencia a ellos y así no perder el método getSumaParcial para ir sumando los resultados parciales
            tareas[i] = new Sumador(enteros, inicio, fin);
            hilos[i] = new Thread(tareas[i], "Hilo-" + (i + 1));
            hilos[i].start();
        }

        // 4. Sincronización: Esperar a que terminen y recolectar resultados
        long sumaTotal = 0;
        for (int i = 0; i < k; i++) {
            try {
                //Se usa el join para garantizar que haya una barrera de sincronización: el main solo suma los subtotales una vez que todos los hilos finalizaron su cómputo
                hilos[i].join(); // El main se pausa hasta que el hilo "i" finaliza
                sumaTotal += tareas[i].getSumaParcial(); // Obtenemos la suma calculada por la serie de hilos independientes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 5. Escribir el valor final resultante
        System.out.println("--------------------------------------------------");
        System.out.println("La suma total de los " + capacidad + " elementos es: " + sumaTotal);
    }
}