package Synchronized.ejercicio5;

import java.util.Scanner;

public class Ciudad {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Thread[] arrAutos = new Thread[5];
        System.out.println("Ingrese la cantidad de litros que tendrá el surtidor");
        Surtidor surtidor = new Surtidor(sc.nextInt()); //Capacidad de 50L

        for (int i = 0; i < 5; i++) {
            Thread hilo = new Thread (new Auto ("A" + (i + 1), 0, 50, 1, surtidor));
            arrAutos[i] = hilo;
            hilo.start();
        }
        
        sc.close();
    }

}
