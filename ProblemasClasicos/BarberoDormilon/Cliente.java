package ProblemasClasicos.BarberoDormilon;

public class Cliente implements Runnable{
    
    private Barberia barberia;
    private int id; 

    public Cliente (Barberia unaBarberia, int unId) {
        this.barberia = unaBarberia;
        this.id = unId;
    }

    public void run() {
        try { 
            if (barberia.accesoCliente(this.id)) {
                System.out.println("Cliente " + id + " se va después de ser atendido");
            }
        } catch (InterruptedException e) {}
    }
    

}
