package Semaforos.ejercicio7;

import java.util.concurrent.Semaphore;

public class Confiteria {
    
    private Semaphore silla = new Semaphore(1); //Para garantizar la exclusión (sólo una silla en la confiteria, si está ocupada nadie más puede sentarse y pedir)
    private Semaphore semMozo = new Semaphore(0);
    private Semaphore semEmpleado = new Semaphore(0);

    public void ordenar(int id) throws InterruptedException {
        silla.acquire();
        System.out.println("Empleado " + id + " se sienta y pide ordenar comida");
        //Avisa al mozo
        semEmpleado.release();
        //Espera a que el mozo entregue la comida
        semMozo.acquire();
        Thread.sleep(2000); //tiempo comiendo
        System.out.println("Empleado " +  id + " ha terminado de comer y agradece por la comida");
        silla.release(); //Se levanta y se va, liberando la silla
    }

    public void prepararComida() throws InterruptedException {
        while (true) {
            semEmpleado.acquire(); //Llega un pedido 
            System.out.println("Mozo recibe pedido y se dispone a llevarlo a cocina");
            Thread.sleep(2000);
            semMozo.release(); //Avisa al Empleado que puede comer
            System.out.println("Pedido listo y entregado al empleado, avisándole que ya puede comer");
        }
    }

}
