package Semaforos.ejercicio3;

import java.util.concurrent.Semaphore;

public class SecuenciaPasosPro {

    static class ProcesoCiclico implements Runnable {
        private final String nombre;
        private final Semaphore semEntrada;
        private final Semaphore semSalida;

        //ünica clase que repite funcionamiento entre los distintos Pi, con los semáforos por parámetro para recibir los semáforos correspondientes al Pi adecuado.
        public ProcesoCiclico(String nombre, Semaphore semEntrada, Semaphore semSalida) {
            this.nombre = nombre;
            this.semEntrada = semEntrada;
            this.semSalida = semSalida;
        }

        @Override
        public void run() {
            try {
                // Ciclo controlado: corre mientras el hilo no reciba una orden de parada - en lugar de hacer while(true)
                while (!Thread.currentThread().isInterrupted()) {
                    semEntrada.acquire();

                    System.out.println("Ejecutando " + nombre);
                    Thread.sleep(500); // Simulación de trabajo

                    semSalida.release();
                }
            } catch (InterruptedException e) {
                // Restablece el estado de interrupción al salir limpiamente - mejor tener esto a que no tener nada definido para el contenido del catch
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Inicialización para secuencia cíclica P1 -> P3 -> P2 -> P1 ...
        Semaphore s1 = new Semaphore(1); // Arranca habilitado P1
        Semaphore s3 = new Semaphore(0);
        Semaphore s2 = new Semaphore(0);

        // Instanciamos la misma clase configurando los turnos
        Thread p1 = new Thread(new ProcesoCiclico("P1", s1, s3));
        Thread p3 = new Thread(new ProcesoCiclico("P3", s3, s2));
        Thread p2 = new Thread(new ProcesoCiclico("P2", s2, s1));

        p1.start();
        p3.start();
        p2.start();
    }
}