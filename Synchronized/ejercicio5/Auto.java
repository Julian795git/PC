package Synchronized.ejercicio5;

public class Auto implements Runnable {

    private String patente;
    private int kmRecorridos;
    private int combustibleActual;
    private int consumoPorKm;
    private Surtidor surtidor;

    public Auto (String unaPatente, int cantKm, int cantCombustible, int unConsumo, Surtidor unSurtidor) {
        this.patente = unaPatente;
        this.kmRecorridos = cantKm;
        this.combustibleActual = cantCombustible;
        this.consumoPorKm = unConsumo;
        this.surtidor = unSurtidor;
    }

    public void run() {
        while (surtidor.getLitros() > 0) {
            int naftaCargada = 0;

            if (this.combustibleActual <= 0) {
                System.out.println(patente + " necesita cargar combustible" + "\n");
                naftaCargada = surtidor.abastecer(10, patente);
                this.combustibleActual += naftaCargada;
            }

            if (naftaCargada < 10 && naftaCargada >= 1) {
                //Recorrer lo que se pueda con el combustible actual
                this.combustibleActual -= this.consumoPorKm * naftaCargada;
                this.kmRecorridos += naftaCargada;
            } else {
                //Recorrer 10km
                this.combustibleActual -= this.consumoPorKm * 10;
                this.kmRecorridos += 10;
            }
            

            try {
                Thread.sleep(1000); //Simula tiempo en recorrer kilómetros
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(this.patente + " recorrió " + this.kmRecorridos + " km. Combustible restante: " + this.combustibleActual + "L." + "\n");
        }

        System.out.println(this.patente + " terminó su recorrido" + "\n");
    }
}
