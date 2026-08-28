package hilos.ejercicio6;



public class ContadorDistancia {
    
    private String nombreCorredorMaximo;
    private double distanciaMaxima;

    public synchronized void registrar(String nombreCorredor, double distanciaCorredor) {
        if (distanciaCorredor > distanciaMaxima) {
        this.distanciaMaxima = distanciaCorredor;
        this.nombreCorredorMaximo = nombreCorredor;
        }
    }

    public String obtenerMayor() {
        return nombreCorredorMaximo + ", Distancia: " + distanciaMaxima;
    }

}