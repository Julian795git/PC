package Synchronized.ejercicio4;

public class MontaniaRusaPro {
    private int espaciosDisp = 10;
    private int lugarActual = 1;

    public MontaniaRusaPro() {

    }

    public synchronized boolean usarRusa(String usuario) {
        boolean exito = false;
        if (espaciosDisp > 0) {
            try {
                System.out.println(usuario + " está reservando lugar para MONTAÑA RUSA");
                Thread.sleep(1000);
                System.out.println(usuario + " reservó el lugar " + lugarActual + " para la MONTAÑA RUSA");
                espaciosDisp--;
                lugarActual++;
                exito = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        } else {
            System.out.println(usuario + " no pudo reservar lugar para MONTAÑA RUSA");
        }

        return exito;
    }
}
