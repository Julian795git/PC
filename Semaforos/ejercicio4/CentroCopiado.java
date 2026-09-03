package Semaforos.ejercicio4;

public class CentroCopiado {
    public static void main(String[] args) {
        GestorImpresoras gestor = new GestorImpresoras (3);        

        for (int i = 0; i <= 10; i++) {
            new Thread(new Cliente(gestor, (i + 1)), "Cliente-" + i).start();
        }

    }
}
