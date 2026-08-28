package Synchronized.ejercicio4;

public class MontaniaRusa {
    
    private int espaciosDisp = 10;
    private int lugarActual = 1;

    public MontaniaRusa() {

    }

    public synchronized void usarRusa(String usuario) {
        if (espaciosDisp > 0) {
            try {
                System.out.println(usuario + " está reservando lugar para MONTAÑA RUSA");
                Thread.sleep(1000);
                System.out.println(usuario + " reservó el lugar " + lugarActual + " para la MONTAÑA RUSA");
                espaciosDisp--;
                lugarActual++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        } else {
            System.out.println(usuario + " no pudo reservar lugar para MONTAÑA RUSA");
        }
    }

}
