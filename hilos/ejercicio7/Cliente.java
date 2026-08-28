package hilos.ejercicio7;

public class Cliente {
    private String nombre;
    private int[] carroCompra;

    Cliente (String unNombre, int[] unCarro) {
        this.nombre = unNombre;
        this.carroCompra = unCarro;
    }


public String getNombre() {
    return this.nombre;
}

public int[] getCarroCompra() {
    return this.carroCompra;
}

}