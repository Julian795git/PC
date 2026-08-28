package Semaforos.ejercicio7;

public class Main {
    
    public static void main(String[] args) {
        
        Confiteria confiteria = new Confiteria();
        Thread mozo = new Thread (new Mozo (confiteria));
        mozo.start();

        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread (new Empleado(confiteria, (i + 1))).start();
        }

    }
}
