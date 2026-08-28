package hilos.ejercicio5;

public class MiHiloThread extends Thread {
    
    MiHiloThread (String str) {
        super(str); // Usa el constructor de la clase Thread para asignar el nombre
    }

    public void run() {
        System.out.println("Comenzando " + getName());
        try {
            for (int contar = 0; contar < 10; contar++) {
                Thread.sleep(400);
                System.out.println("En " + getName() + ", el recuento " + contar);
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + " interrumpido.");
        }
        System.out.println("Terminando " + getName());
    }

}

//REPASO TEÓRICO DE PORQUÉ SE SEPARAN EL RUN Y EL MAIN EN 2 CLASES:
//Separar el método run() y el método main() en 2 clases distintas es una buena práctica en PC por distintas razones:
//
//1) RESPONSABILIDAD ÚNICA (Single Responsibility Principle):
//  Clase del Hilo(MiHiloThread): Se encarga exclusivamente de definir el comportamiento del hilo, es decir, lo que se debe hacer cuando se ejecuta run()
//  Clase principal(UsoHilosThread): Se encarga de la lógica de control del programa, como crear e iniciar los hilos, y manejar la ejecución general
//
//2) REUTILIZACIÓN DE CÓDIGO
//  Al definir ls lógica del hilo en una clase separada, se puede reutilizar en diferentes programas o contextos sin necesidad de modificar la clase principal
//
//3) FLEXIBILIDAD Y ESCALABILIDAD
//  Si se decide cambiar el comportamiento del hilo, solo se necesita modificar la clase que implementa run(), sin afectar la lógica del programa principal