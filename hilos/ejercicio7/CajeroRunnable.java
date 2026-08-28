package hilos.ejercicio7;

public class CajeroRunnable implements Runnable {
    
    private String nombre;
    private Cliente cliente;
    private long initialTime;

    public CajeroRunnable(String unNombre, Cliente unCliente, long unTime) {
        this.nombre = unNombre;
        this.cliente = unCliente;
        this.initialTime = unTime;
    }

    public void run() {
        System.out.println("El " + this.nombre + " comienza a procesar la compra del " + this.cliente.getNombre() + " en: " + (System.currentTimeMillis() - this.initialTime) / 1000 + " seg");

        for (int i = 0; i < this.cliente.getCarroCompra().length; i++) {
            this.esperarXsegundos(cliente.getCarroCompra()[i]);
            System.out.println("Procesando el producto " + (i + 1) + " del " + this.cliente.getNombre() + " --- Tiempo: " + (System.currentTimeMillis() - this.initialTime) / 1000 + " seg");
        }

        System.out.println("El " +  this.nombre + " ha terminado de procesar al " + this.cliente.getNombre() + " en " + (System.currentTimeMillis() - this.initialTime) / 1000 + " seg");
    }

    private void esperarXsegundos(int segundos) {
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException e) {
            System.out.println("El cajero fue interrumpido mientras esperaba");
        }
    }

}
