package Semaforos.ejercicio5;

public class Cliente implements Runnable {
    private final GestorImpresorasMixto gestor;
    private final int idCliente;
    private final char preferencia; //'A', 'B' o 'C'

    public Cliente(GestorImpresorasMixto gestor, int idCliente, char preferencia) {
        this.gestor = gestor;
        this.idCliente = idCliente;
        this.preferencia = preferencia;
    }
    

    @Override
    public void run() {
        GestorImpresorasMixto.ImpresoraAsignada miImpresora = null;
        try {
            miImpresora = gestor.solicitarImpresora(this.preferencia);

            System.out.println("Cliente " + idCliente + " (pidió " + preferencia + ") " + "usando impresora Tipo " + miImpresora.tipo + " - ID: " + miImpresora.id);

            //Simulación de trabajo de impresión
            Thread.sleep((long) (Math.random() * 2000 + 1000));

            System.out.println("Cliente " + idCliente + " terminó.");

        } catch (InterruptedException e) {
            System.err.println("Cliente " + idCliente + " interrumpido.");
            Thread.currentThread().interrupt();
        } finally {
            // El bloque finally garantiza que la impresora se devuelva siempre
            if (miImpresora != null) {
                gestor.liberarImpresora(miImpresora.tipo, miImpresora.id);
            }
        }
    }
}
