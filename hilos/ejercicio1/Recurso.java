package hilos.ejercicio1;

public class Recurso {
    
    static void uso() {
        Thread t = Thread.currentThread();
        System.out.println("En recurso: Soy " +  t.getName());
    }

}