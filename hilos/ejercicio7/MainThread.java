package hilos.ejercicio7;

/* Y si en vez de procesar primero un cliente y después otro, procesamos los 2 a la vez?
 * Cuánto tardaría el programa en ejecutarse? Si en vez de haber solo una Cajera (es decir, un solo hilo),
 * hubiese 2 cajeras (2 hilos o threads), podríamos procesar los 2 clientes a la vez y tardar menos tiempo 
 * en ejecutar el programa.
 */

public class MainThread {

    public static void main(String[] args) {
    
        Cliente cliente1 = new Cliente("Cliente 1", new int[] {2, 2, 1, 5, 2, 3});
        Cliente cliente2 = new Cliente("Cliente 2", new int[] {1, 3, 5, 1, 1});

        long initialTime = System.currentTimeMillis();

        //Se crean las instancias de CajeroThread directamente, ya que la clase CajeroThread extiende a la superclase Thread
        CajeroThread cajero1 = new CajeroThread("Cajero 1", cliente1, initialTime);
        CajeroThread cajero2 = new CajeroThread("Cajero 2", cliente2, initialTime);

        cajero1.start();
        cajero2.start();
        //Como ahora tengo 2 hilos de Cajero que van a trabajar concurrentemente, debo inicializar los hilos de Cajero

    }
}