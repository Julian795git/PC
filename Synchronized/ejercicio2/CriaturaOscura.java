package Synchronized.ejercicio2;

public class CriaturaOscura implements Runnable {
    
    private Energia energia;

    public CriaturaOscura (Energia energia) {
        this.energia = energia;
    }

    public void run() {
        for (int i = 0; i < 5; i++) { //La cantidad de iteraciones hará que varíe el total de energía final
            energia.drenar(3);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
