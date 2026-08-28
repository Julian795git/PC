package hilos.ejercicio6;



public class Hilo implements Runnable {
    private Corredor corredor; 
    private ContadorDistancia contador;
    
    Hilo(Corredor elCorredor, ContadorDistancia elContador) {
        this.corredor = elCorredor;
        this.contador = elContador;
    }
    
    public void run() {
        for (int i = 0; i < 10; i++) {
            this.corredor.run(this.contador);
        }
    }
}