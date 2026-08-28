package Synchronized.ejercicio2;

public class Energia {
    
    private int energia = 10;

    public synchronized void drenar(int cantidad) {
        energia -= cantidad;
        System.out.println("La Criatura Oscura drena " + cantidad + " unidades de energía");
        System.out.println("Energía total actual: " + energia  + " unidades.");
    }

    public synchronized void revitalizar (int cantidad) {
        energia += cantidad;
        System.out.println("El Sanador revitaliza " + cantidad + " unidades de energía");
        System.out.println("Energía total actual: " + energia  + " unidades.");
    }

    public synchronized int getEnergia() {
        return energia;
    }

}
