package SegundoParcial.ProblemasClasicos;

// El Monitor que encapsula el recurso compartido y la sincronización
class Barberia {
    private final int sillasEsperaTotales;
    private int clientesEsperando = 0;
    private boolean sillonOcupado = false;

    public Barberia(int sillas) {
        this.sillasEsperaTotales = sillas;
    }

    // Método que ejecuta el Cliente
    public synchronized boolean llegarParaCorte(int idCliente) throws InterruptedException {
        boolean fueAtendido = false;

        if (clientesEsperando < sillasEsperaTotales) {
            clientesEsperando++;
            System.out.println("Cliente " + idCliente + " entra y se sienta a esperar.");
            
            // Despierta a todos (al barbero si estaba durmiendo)
            notifyAll(); 

            // Espera guardada: mientras el sillón del barbero esté ocupado, el cliente espera
            while (sillonOcupado) {
                wait();
            }
            
            // ¡Es su turno! Pasa de la silla de espera al sillón del barbero
            clientesEsperando--;
            sillonOcupado = true;
            System.out.println("Cliente " + idCliente + " se sienta en el sillón del barbero.");
            
            fueAtendido = true;
        } else {
            System.out.println("Cliente " + idCliente + " se va. Barbería llena.");
        }

        return fueAtendido;
    }

    // Método que ejecuta el Barbero para buscar trabajo
    public synchronized void siguienteCliente() throws InterruptedException {
        // Si no hay nadie esperando y el sillón está vacío, el barbero se duerme
        while (clientesEsperando == 0 && !sillonOcupado) {
            System.out.println("Zzz... El barbero se duerme.");
            this.wait(); 
        }
    }

    // Método que ejecuta el Barbero cuando termina un corte
    public synchronized void terminarCorte() {
        System.out.println("El barbero terminó un corte.");
        sillonOcupado = false;
        
        // Avisa a los clientes que esperan que el sillón se liberó
        notifyAll(); 
    }
}

class HiloBarbero implements Runnable {
    private final Barberia barberia;

    public HiloBarbero(Barberia barberia) {
        this.barberia = barberia;
    }

    @Override
    public void run() {
        try {
            while (true) {
                barberia.siguienteCliente();
                
                // Simula el tiempo que tarda en cortar el pelo (fuera del monitor)
                Thread.sleep((long) (Math.random() * 1500)); 
                
                barberia.terminarCorte();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class HiloCliente implements Runnable {
    private final Barberia barberia;
    private final int id;

    public HiloCliente(Barberia barberia, int id) {
        this.barberia = barberia;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            // El cliente intenta entrar
            boolean pudoEntrar = barberia.llegarParaCorte(id);
            
            if (pudoEntrar) {
                // Si entró, su hilo muere de forma natural cuando el barbero termina, 
                // ya que la espera (wait) sucede dentro del método llegarParaCorte.
                System.out.println("Cliente " + id + " sale de la barbería con un nuevo look.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class ProblemaBarberoDormilon {
    public static void main(String[] args) {
        // Inicializamos la barbería con 3 sillas de espera
        Barberia barberia = new Barberia(3);
        
        Thread barbero = new Thread(new HiloBarbero(barberia), "Barbero");
        barbero.start();

        // Simulamos la llegada aleatoria de 10 clientes a lo largo del tiempo
        for (int i = 1; i <= 10; i++) {
            Thread cliente = new Thread(new HiloCliente(barberia, i), "Cliente-" + i);
            cliente.start();
            try {
                // Llegan en intervalos irregulares
                Thread.sleep((long) (Math.random() * 800));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
