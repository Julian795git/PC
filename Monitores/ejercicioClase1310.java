package Monitores;

class Persona implements Runnable {
    protected GestorSala sala;

    public Persona(GestorSala unaSala) {
        this.sala = unaSala;
    }

    public void accionar() {
        this.sala.entrarSala();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e ){
            e.printStackTrace();
        }
        this.sala.salirSala();
    }

    public void run() {
        while (true) {
            accionar();
        }
        
    }
}

class Jubilado extends Persona {
    public Jubilado (GestorSala unaSala) {
        super(unaSala);
    }

    public void accionar() {
        this.sala.entrarSalaJubilado();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.sala.salirSala();
    }

}

class Temperatura implements Runnable {
    private int tempActual;
    private GestorSala sala;

    public Temperatura(GestorSala unaSala) {
        this.sala = unaSala;
        this.tempActual = 0;
    }

    public void setTemp(int unaTemp) {
        this.tempActual = unaTemp;
    }

    public void run() {
        while (true) {
            sala.notificarTemperatura(tempActual);
        }
        
    }
}


//GestorSala recurso compartido
class GestorSala {

    private int capacidadMaxima = 50;
    private int cantPersonas;
    private int cantJubilados;
    private int umbralTemp = 30;

    public GestorSala() {
        this.cantPersonas = 0;
        this.cantJubilados = 0;
    }

    //LOS MÉTODOS PRIVADOS NO NECESITAN SER SINCRONIZADOS, SOLO LOS PÚBLICOS PARA MONITORES
    
    public synchronized void entrarSala() { //Se invoca cuando una persona quiere entrar en la sala

    }

    public synchronized void entrarSalaJubilado() { //Se invoca cuando una persona jubilada quiere entrar en la sala

    }

    public synchronized void salirSala() { //Se invoca cuando una persona, jubilada o no, quiere salir de la sala

    }

    public synchronized void notificarTemperatura(int temperatura) { //La invoca la hebra que mide la temperatura de la sala para indicar el último valor medido

    }

    private boolean puedeEntrar() {
        boolean puede = false;

        return puede;
    }

}

public class ejercicioClase1310 {
    public static void main(String[] args) {
        
    }
}
