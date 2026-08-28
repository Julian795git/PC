package hilos.ejercicio5;

public class MiHilo implements Runnable {
    String nombreHilo;

    MiHilo(String nombre) {
        nombreHilo = nombre;
    }

    //Punto de entrada del hilo
    //Los hilos comienzan a ejecutarse acá
    public void run() {
        System.out.println("Comenzando " + nombreHilo);
            for (int contar = 0; contar < 10; contar++) {
                //Con try catch, se ejecuta el main y luego comienza el hilo #1
                //Sin try catch, se ejecuta todo el hilo #1 y luego finaliza hilo main
                try {
                    Thread.sleep(400);
                    System.out.println("En " + nombreHilo + ", el recuento " + contar);
                } catch (InterruptedException e) {
                    System.out.println(nombreHilo + " Interrumpido.");
                }
                
            }
        
        System.out.println("Terminando " + nombreHilo);

    }
}

//DIFERENCIA ENTRE "EXTENDS THREAD" E "IMPLEMENTS RUNNABLE"
//
//1) HERENCIA VS COMPOSICIÓN
//  Extends Thread:
//      Cuando una clase extiende Thread, hereda directamente de la clase Thread, esto significa que no se puede extender otra clase, ya que Java no permite herencia múltiple
//  Implements Runnable:
//      Cuando una clase implementa Runnable, no hereda de Threaad, sino que implementa la interfaz Runnable, lo cual permite que la clase pueda extender otra clase si es necesario, ya que no está limitada por la herencia
//
//2) FLEXIBILIDAD Y REUTILIZACIÓN
//  Extends Thread:
//      La lógica del hilo está directamente acoplada a la clase Thread, si se necesita reutilizar la lógica del hilo en otro contexto, se tendría que crear una nueva clase que extienda Thread
//  Implements Runnable:
//      La lógica del hilo está desacoplada de la clase Thread, entonces se puede reutilizar la clase que implementa Runnable en diferentes contextos, simplemente pasándola como argumento a diferentes instancias de Thread
//
// 3) SEPARACIÓN DE RESPONSABILIDADES
//  Extends Thread:
//      La clase que extiende Thread combina la lógica del hilo (run()) con el control del hilo (start(), join(), etc.), esto puede hacer que la clase sea menos modular
//  Implements Runnable:
//      La clase que implementa Runnable se enfoca únicamente en la logica del hilo (run()), mientras que el control del hilo se delega a la clase Thread. Esto sigue el principio de responsabilidad única

//ANALOGÍA ENTRE IMPLEMENTS RUNNABLE Y EL USO DE UNA CLASE CON SU POSTERIOR TESTCLASE:
/*
 * Usar implements Runnable es similar a cómo defines una clase como Perro con sus atributos y métodos, y luego tienes una clase separada como TestPerro que contiene el main para crear instancias y ejecutar la lógica.
 * 
 * Clase MiHilo (como Perro):
 *      Define la estructura y el comportamiento del hilo (atributos como nombreHilo y el método run() que contiene la lógica del hilo)
 *      Es como definir la clase Perro con atributos como nombre y métodos como ladrar()
 * 
 * Clase UsoHilos (como TestPerro):
 *      Es el punto de entrada del programa (main), donde se crean las instancias de MiHilo y se ejecutan
 *      Es como la clase TestPerrom, donde se crean instnacias de Perro y se llaman a sus métodos
 * 
 */