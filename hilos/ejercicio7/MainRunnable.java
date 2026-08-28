package hilos.ejercicio7;

public class MainRunnable {
 
    public static void main(String[] args) {
     
        Cliente cliente1 = new Cliente("Cliente 1", new int[] {2, 2, 1, 5, 2, 3});
        Cliente cliente2 = new Cliente("Cliente 2", new int[] {1, 3, 5, 1, 1});

        long initialTime = System.currentTimeMillis();

        //Como la clase CajeroRunnable implementa Runnable, no tiene acceso "directo" a la superclase Thread
        //Por lo tanto, debe usar el constructor de Thread para crear las instancias de CajeroRunnable
        Thread cajero1 = new Thread(new CajeroRunnable("Cajero 1", cliente1, initialTime));
        Thread cajero2 = new Thread(new CajeroRunnable("Cajero 2", cliente2, initialTime));
        //Diferencia clave con respecto a "extends Thread": Se crean los nuevos hilos usando el constructor de Thread y 
        //dentro del argumento se usa el constructor del objeto representado en el hilo

        cajero1.start();
        cajero2.start();

        //Si quisiera que el cajero 2 se ejecute luego de que el cajero 1 termine de procesar al cliente 1:
            /*
            * try {
            *      cajero1.start();
            *      cajero1.join();
            * 
            *      cajero2.start();
            *      cajero2.join();
            *  
            * } catch (InterruptedException e) {
            *      sout(...);
            * }
            * 
            */
        //Esto asegura que, además de que el hilo 1 se ejecute antes que el hilo 2, el main deba esperar a la ejecución de ambos hilos secundarios antes de terminar
        //Esto se logra gracias al join() de ambos hilos y no solo del hilo 2, ya que si solo se pusiera el join() al hilo 2 se ejecutarían ambos en paralelo

    }
}