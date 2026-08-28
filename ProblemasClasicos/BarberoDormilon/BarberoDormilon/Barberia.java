package ProblemasClasicos.BarberoDormilon.BarberoDormilon;

import java.util.concurrent.Semaphore;

public class Barberia {
    
    private Semaphore semCliente = new Semaphore(0);
    private Semaphore semBarbero = new Semaphore(0);
    private Semaphore semSilla = new Semaphore(1);
    private int sillasEsperando;
    private final int capacidad;

    public Barberia (int capacidad) {
        this.capacidad = capacidad;
        this.sillasEsperando = 0;
    }

    public boolean accesoCliente(int id) throws InterruptedException {
        boolean exito = true;
        
        semSilla.acquire();
        if (sillasEsperando < capacidad) {
            sillasEsperando++;
            System.out.println("Cliente " + id + " se sienta a esperar. Sillas ocupadas: " + sillasEsperando);
            semSilla.release();

            semCliente.release(); //Despierta al barbero si está durmiendo
            semBarbero.acquire(); //Espera a que el barbero esté listo
            System.out.println("Cliente " + id + " está siendo atendido");

        } else {
            System.out.println("Cliente " + id + " se va ya que no hay más sillas disponibles");
            semSilla.release();
            exito = false;
        }

        return exito;
    }

    public void cortarPelo() throws InterruptedException {
        semCliente.acquire(); //un cliente es atendido por el barbero
        semSilla.acquire(); //Se desocupa una de las sillas de espera
        this.sillasEsperando--;
        System.out.println("Barbero atendiendo a un cliente. Sillas ocupadas: " + sillasEsperando);
        semSilla.release();

        semBarbero.release();
    }

}
