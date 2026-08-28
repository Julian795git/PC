package PrimerParcial.Toboganes;

public class Main {
    public static void main(String[] args) {
        Tobogan tobogan = new Tobogan(3);

        Thread encargado = new Thread(new Encargado(tobogan));
        encargado.start();

        int numVisitantes = 5; //Se puede modificar, 5 como ejemplo
        Thread[] visitantes = new Thread[numVisitantes];

        for (int i = 0; i < numVisitantes; i++) {
            visitantes[i] = new Thread(new Visitante(i + 1, tobogan));
            visitantes[i].start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
