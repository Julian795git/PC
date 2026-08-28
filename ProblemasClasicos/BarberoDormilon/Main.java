package ProblemasClasicos.BarberoDormilon;

public class Main {
    
    public static void main(String[] args) {
        Barberia barberia = new Barberia(3);
        Thread barbero = new Thread (new Barbero(barberia));
        barbero.start();

        for (int i = 0; i < 10; i++) {
            Cliente cliente = new Cliente(barberia, (i + 1));
            new Thread(cliente).start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}