package Synchronized.ejercicio5;

public class Surtidor {
    
    private int capacidadLitros;
    private int litrosActual;

    public Surtidor (int unaCapacidad) {
        this.capacidadLitros = unaCapacidad;
        this.litrosActual = this.capacidadLitros;
    }

    public int getLitros() {
        return this.litrosActual;
    }

    public synchronized int abastecer(int cantLitros, String patente) {
        if (litrosActual > 0) {

            if (cantLitros > this.litrosActual) {
                System.out.println(patente + " quiere cargar " + cantLitros + "L. pero el surtidor solo tiene " + this.litrosActual + "\n");
                cantLitros = this.litrosActual;
            }

            System.out.println(patente + " cargando " + cantLitros + "L." + "\n");
            this.litrosActual -= cantLitros;

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(patente + " terminó de cargar.");
            System.out.println("Combustible restante en el surtidor: " + this.litrosActual + "L." + "\n");

        } else {
            System.out.println(patente + " intentó cargar combustible pero el surtidor ya no tiene más." + "\n");
        }  
        
        return cantLitros;
    }

    //Se podría hacer un método para recargar el surtidor

}
