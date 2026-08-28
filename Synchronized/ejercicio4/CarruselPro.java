package Synchronized.ejercicio4;

public class CarruselPro {
    private int espaciosDisp = 5;
    private int lugarActual = 1;

    public CarruselPro() {

    }

    public synchronized boolean usarCarrusel(String usuario) {
        boolean exito = false;
        if (espaciosDisp > 0) {
            try {
                System.out.println(usuario + " está reservando lugar para CARRUSEL");
                Thread.sleep(1000);
                System.out.println(usuario + " reservó el lugar " + lugarActual + " para el CARRUSEL");
                espaciosDisp--;
                lugarActual++;
                exito = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        } else {
            System.out.println(usuario + " no pudo reservar lugar para CARRUSEL");
        }
        
        return exito;
    }
}
