package Synchronized.ejercicio3;

public class Main {

    public static void main(String[] args) {
        Plato plato = new Plato();
        Rueda rueda = new Rueda();
        Hamaca hamaca = new Hamaca();
    
        //Creación de una x cantidad de hámster (los justos para el ejercicio)
        /* 
        Thread h1 = new Thread(new Hamster("Hamster 1", plato, rueda, hamaca));
        Thread h2 = new Thread(new Hamster("Hamster 2", plato, rueda, hamaca));
        Thread h3 = new Thread(new Hamster("Hamster 3", plato, rueda, hamaca));

        h1.start();
        h2.start();
        h3.start();
        */

        //Creación de varios hámster de más
        
        int cantidad = 5;

        Thread[] arrHamsters = new Thread[cantidad];

        for (int i = 0; i < cantidad; i++) {
            Thread hilo = new Thread(new Hamster("Hamster " + (i + 1), plato, rueda, hamaca));
            arrHamsters[i] = hilo;
            arrHamsters[i].start();
        }
     
        //Formas de forzar un orden de ejecución de hilos (por si me hiciera falta alguna vez)
        
        //1) usar Join()
        /*
            Con Join(), el main espara a que un hilo termine antes de arrancar el siguiente, de esa forma los hilos se ejecutan de a uno y en orden:
            DENTRO DEL FOR:
            try {
                hilos[i].join()
            } catch (InterruptedException e) {
                e.printStackTrace(); 
            }
        */

        //2) usar sleep() para escalonar
        /*
            Con sleep(), se puede dar una pequeña pausa entre start()
            DENTRO DEL FOR:
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
         */

    }
}
