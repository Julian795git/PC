package Synchronized.ejercicio4;

public class RuedaFortuna {
    
    private int espaciosDisp = 20;
    private int lugarActual = 1;

    public RuedaFortuna() {

    }

    public synchronized void usarRueda(String usuario) {
        if (espaciosDisp > 0) {
            try {
                System.out.println(usuario + " está reservando lugar para RUEDA FORTUNA");
                Thread.sleep(1000);
                System.out.println(usuario + " reservó el lugar " + lugarActual + " para la RUEDA FORTUNA");
                espaciosDisp--;
                lugarActual++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(usuario + " no pudo reservar lugar en RUEDA FORTUNA");
        }
    }

}
