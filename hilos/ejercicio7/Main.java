package hilos.ejercicio7;

/*
 * Consigna ejercicio 7:
 * Supongamos que debemos simular el proceso de cobro de un supermercado; es decir, unos clientes van 
 * con un carro lleno de productos y una cajera les cobra los productos, pasando uno a uno por el escáner
 * de la caja registradora. En este caso, el cajero debe procesar la compra cliente a cliente, es decir, 
 * que primero le cobra al cliente 1, luego al cliente 2 y así sucesivamente. Para ello, se debe definir una clase
 * "Cajero" y una clase "Cliente", el cual tendrá un "array de enteros" que representarán los productos que ha comprado
 * y el tiempo que el cajero tardará en pasar el producto por el escáner; es decir, que si tenemos un array con [1, 3, 5]
 * significa que el cliente ha comprado 3 productos y que el cajero tardará en procesar 1 segundo el producto 1, 3 segundos 
 * el producto 2 y 5 segundos el producto 3, con lo cual el tiempo total empleado por el cajero será de 9 segundos
 * 
 * Con los códigos dados de la clase Cajero, Cliente y Main, completar y ubicar en la clase que corresponda la implementación
 * del método: esperarXSegundos
 */

public class Main {
 
    public static void main(String[] args) {
        Cliente cliente1 = new Cliente("Cliente 1", new int[] {2, 2, 1, 5, 2, 3});
        Cliente cliente2 = new Cliente("Cliente 2", new int[] {1, 3, 5, 1, 1});

        Cajero cajero1 = new Cajero("Cajero 1");

        long initialTime = System.currentTimeMillis();
        cajero1.procesarCompra(cliente1, initialTime);
        cajero1.procesarCompra(cliente2, initialTime);

    }
}