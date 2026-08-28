package ProblemasClasicos.BarberoDormilon;

public class Barbero implements Runnable {
 
    private Barberia barberia;

    public Barbero(Barberia barberia) {
        this.barberia = barberia;
    }

    public void run() {
        try {
            while (true) {
                barberia.cortarPelo();
                Thread.sleep(1000); //Tiempo simulado en cortar el pelo
                System.out.println("Barbero terminó de cortar el pelo");
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
