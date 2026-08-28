package hilos.ejercicio1;

public class Cliente extends Thread {
    public void run(){
        System.out.println("soy " + Thread.currentThread().getName()); //Método heredado de la clase Thread
        Recurso.uso();
        try {
                Thread.sleep(2000); //Método de la clase Thread
                //Siempre los Sleep se hacen en un try catch
        } catch (InterruptedException e) {

        }
    }    
}