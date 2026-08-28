package Semaforos.ejercicio4;

public class Cliente implements Runnable {
    
    private GestorImpresoras gestor;
    private int idCliente;

    public Cliente(GestorImpresoras unGestor, int unId) {
        this.gestor = unGestor;
        this.idCliente = unId;
    }

    public void run() {
        try {
            int idImpresora = gestor.solicitarImpresora();
            System.out.println("Cliente " + idCliente + " usando impresora " + idImpresora);

            //Simulación de impresión
            Thread.sleep(2000);

            System.out.println("Cliente " + idCliente + " terminó de imprimir");
            gestor.liberarImpresora(idImpresora);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
