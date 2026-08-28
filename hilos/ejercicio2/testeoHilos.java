package hilos.ejercicio2;

public class testeoHilos {
    public static void main(String[] args) {
        Thread miHilo = new MiEjecucion();
        miHilo.start();
        //Esta parte del try catch con el uso del join() hace que el hilo del main espere a que se ejecute el hilo del método 
        //Y después de eso recién 
        try {
            miHilo.join();
        }   catch (InterruptedException e) {
            System.out.println("El hilo fue interrumpido");
        }
        
        System.out.println("En el main");
    }

}
