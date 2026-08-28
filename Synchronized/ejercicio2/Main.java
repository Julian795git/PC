package Synchronized.ejercicio2;

public class Main {

    public static void main(String[] args) {
     
        Energia energia = new Energia();

        Thread hiloCriatura = new Thread(new CriaturaOscura(energia));
        Thread hiloSanador = new Thread(new Sanador(energia));

        hiloCriatura.start();
        hiloSanador.start();

        try {
            hiloCriatura.join();
            hiloSanador.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        //Estos Joins serían necesarios si se quiere que el main espere a que ambos hilos terminen antes de, por ejemplo
        //Mostrar "Programa Finalizado" o imprimir la energía final. Ahí si se usaría join()
        //En cambio, si se quiere mostrar sólo cómo interactúan 2 hilos sobre el RC mientras se ejecuta el programa, no se necesita join()

        System.out.println("Programa finalizado. Energía final: " + energia.getEnergia());
    }
}
