package hilos.ejercicio6;



public class Corredor {
    private String nombre;
    private double distanciaRecorrida;

    public Corredor(String nombre) {
        this.nombre = nombre;
        this.distanciaRecorrida = 0;
    }
    
    public synchronized void run(ContadorDistancia elContador) {
        distanciaRecorrida += (Math.random() * 10 + 1) ;
        elContador.registrar(this.nombre, this.distanciaRecorrida);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        }
    }
    
    public void imprimir() {
        System.out.println(nombre + " " + distanciaRecorrida);
    }
}