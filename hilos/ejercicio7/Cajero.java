package hilos.ejercicio7;

public class Cajero {
 
    private String nombre;

    Cajero(String unNombre) {
        this.nombre = unNombre;
    }

    public void procesarCompra(Cliente cliente, long timeStamp) {
        System.out.println("El " +  this.nombre + " Comienza a procesar la compra del " + cliente.getNombre() + " en " + (System.currentTimeMillis() - timeStamp) / 1000 + " seg");
        for (int i = 0; i < cliente.getCarroCompra().length; i++) {
            this.esperarXsegundos(cliente.getCarroCompra()[i]);
            System.out.println("Procesando el producto " + (i + 1) + " --- Tiempo: " + (System.currentTimeMillis() - timeStamp) / 1000 + " seg");
        }

        System.out.println("El " + this.nombre + " ha terminado de procesar los productos del " + cliente.getNombre() +  " en: " + (System.currentTimeMillis() - timeStamp) / 1000 +  " seg");

    }

    private void esperarXsegundos(int segundos) {
        try {
            Thread.sleep(segundos * 1000L); //Convierte segundos a milisegundos
        } catch (InterruptedException e) {
            System.out.println("El cajero fue interrumpido mientras esperaba");
        }
    }


}