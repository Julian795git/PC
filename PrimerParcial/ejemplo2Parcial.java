package PrimerParcial;

import java.util.Scanner;

//Enunciado
/*
En una empresa de desarrollo de software hay E empleados trabajando duro. Para el almuerzo y los descansos, la oficina cuenta con un único Microondas en el comedor compartido.
La rutina de cada empleado funciona así:

    1- El empleado está en su escritorio programando (simular con un tiempo de espera aleatorio).

    2- Le da hambre y decide ir al comedor a calentar su comida.

    3- Si el microondas está libre, el empleado toma el control: abre la puerta, mete su tupper, configura el tiempo de calentado (simular con otro tiempo de espera) y finalmente saca su comida, dejando el microondas disponible.

    4- Si el microondas está ocupado cuando llega, el empleado simplemente se queda esperando ahí hasta que se libere.

    5- Una vez que comió, el empleado vuelve a su escritorio a seguir trabajando.

    6- Cada empleado repetirá este ciclo de "trabajar y comer" unas C veces durante su jornada laboral (simulado con un ciclo for). Al terminar sus C comidas, finaliza su jornada y se va a casa.

*/

class Microondas { 

    public synchronized void usarMicroondas() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " encontró el microondas libre.");
        System.out.println("Mete su comida a calentar en el microondas.");
        Thread.sleep(3000);
        System.out.println("Saca su comida del microondas");
        System.out.println("El microondas queda libre para su uso");
    }

}

class Empleado implements Runnable {

    private Microondas micro;
    private int cantComidas;

    public Empleado (Microondas unMicro, int unaCant) {
        this.micro = unMicro;
        this.cantComidas = unaCant;
    }

    public void run() {

        for (int comida = 0; comida < this.cantComidas; comida++) {
            try {
                System.out.println(Thread.currentThread().getName() + " está haciendo sus cosas");
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + " quiere usar el microondas");
                micro.usarMicroondas();
            } catch (InterruptedException e) {

            }
        }

        System.out.println(Thread.currentThread().getName() + " terminó sus actividades");
    }


}
public class ejemplo2Parcial {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingrese la cantidad de comidas en el día:");
        int c = sc.nextInt();

        System.out.println("ingrese la cantidad de empleados:");
        int e = sc.nextInt();

        Microondas micro = new Microondas();

        Thread[] empleados = new Thread[e];

        for (int i = 0; i < e; i++) {
            empleados[i] = new Thread(new Empleado(micro, c), "Empleado " + (i + 1));
            empleados[i].start();
        }

        sc.close();
    }
}
