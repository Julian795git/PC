package Semaforos.ejercicio6;

import java.util.concurrent.Semaphore;

public class Taxi {
    private Semaphore semPasajero = new Semaphore(0); //Semáforo que representa el pedido de un taxi por parte del pasajero (inicia en cero porque nadie ha pedido taxi todavía)
    private Semaphore semTaxista = new Semaphore(0); //Semáforo que representa cuando un taxista llegó a destino (Inicia en cero porque el pasajero debe esperar)

    public void tomarTaxi(int unId) throws InterruptedException {
        System.out.println("Pasajero " + unId + ": Quiero un taxi");
        semPasajero.release(); //Significa que hay un pasajero esperando a ser llevado, entonces se despierta al taxista para que maneje
        semTaxista.acquire(); //El pasajero adquiere el semáforo, simulando la espera a que el taxista lo lleve a su destino (Taxista ocupado)
        System.out.println("Pasajero " + unId + ": Llegué a mi destino");
    }

    public void conducir() throws InterruptedException {
        semPasajero.acquire(); //Taxista hace acquire sobre este semáforo: aplica cuando un pasajero se subió al taxi (no pueden subir más pasajeros por mientras)
        System.out.println("Taxista: Pasajero subió, iniciando viaje...");
        Thread.sleep(2000);
        System.out.println("Taxista: Llegamos a destino");
        semTaxista.release(); //Se libera al taxista: Se despierta al pasajero y se le avisa que llegó al destino. 
    }
}

//RAZONAMIENTO DE LOS SEMÁFOROS:
/*
 * En este modelo los semáforos no representan el estado “ocupado/libre” del pasajero o del taxista, sino una señal/acción que un hilo le comunica al otro:
 * 
 * Semáforo de pasajero:
 * -Lo libera el pasajero
 * -lo adquiere el taxista
 * -Significa que un pasajero está esperando al taxista, lo despierta y hace que conduzca
 * -NO significa que el pasajero esté ocupado o librem indica que el pasajero está solicitando acción del taxista
 * 
 * Semáforo de taxista:
 * -Lo liberea el taxista
 * -Lo adquiere el pasajero
 * -Significa que el taxi llegó a destino, le avisa al pasajero que ya se puede bajar y continuar sus actividades
 * -NO indica que el taxista está disponible, indica que el taxista terminó su acción y notifica al pasajero
 * 
 */

