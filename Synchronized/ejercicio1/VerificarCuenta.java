package Synchronized.ejercicio1;

import java.util.logging.Level; 
//Enumeración que define los niveles de severidad de los mensajes que se registran.
//Algunos niveles comunes:
//Level.SEVERE: Mensajes de error graves
//Level.WARNING: Advertencias que podrían indicar un problema
//Level.INFO: Información general sobre el estado del programa
//Level.FINE, Level.FINER y Level.FINEST: Mensajes de depuración más detallados

import java.util.logging.Logger;
//Registrar mensajes en diferentes niveles de importancia, usándose en este caso para registrar errores si ocurre una excepción
//En este caso, se obtiene una instancia del Logger asociada a una clase o nombre específico mediante el método estático Logger.getLogger(String name), luego se pueden usar métodos como .log() para registrar mensajes

public class VerificarCuenta implements Runnable {
    private CuentaBanco cb = new CuentaBanco();
    
    VerificarCuenta() {

    }

    //Este método también genera problemas porque ambos hilos acceden simultáneamente y, dentro de este método, acceden al método retiroBancario de CuentaBanco
    //Aunque se sincronice el método retiroBancario, el problema persiste porque la verificación del saldo if (cb.getBalance() mayor igual a cantidad) y la operación de retiro
    //no están protegidas como una UNIDAD ATÓMICA. Esto significa que un hilo puede verificar el saldo antes que otro hilo lo modifique, generando inconsistencias
    
    //SOLUCIÓN: Sincronizar el bloque completo en HacerRetiro
    //Que el método sea Synchronized asegura que solo UN hilo pueda ejecutar este método a la vez
    //Esto evita que 2 hilos modifiquen el balance simultáneamente
    private synchronized void HacerRetiro(int cantidad) throws InterruptedException {

        if (cb.getBalance() >= cantidad) {
            System.out.println(Thread.currentThread().getName() + " esta realizando un retiro de: " + cantidad + ".");
            Thread.sleep(1000);
            cb.retiroBancario(cantidad);
            System.out.println(Thread.currentThread().getName() + ": Retiro realizado.");
            System.out.println(Thread.currentThread().getName() + ": Los fondos son de: " + cb.getBalance());
        } else {
            System.out.println("No hay suficiente dinero en la cuenta para realizar el retiro usuario " + Thread.currentThread().getName());
            System.out.println("Su saldo actual es de " + cb.getBalance());
            Thread.sleep(1000);
        }
    }

    public void run() {
        for (int i = 0; i <= 3; i++) {
            try {
                this.HacerRetiro(10);
                if (cb.getBalance() < 0) {
                    System.out.println("La cuenta está sobregirada.");
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(VerificarCuenta.class.getName()).log(Level.SEVERE, null, ex); //El log() es un método de la clase Logger que se utiliza para registrar mensajes. 
                //1ero se obtiene Logger asociado a VerificarCuenta, ayudando a identificar de dónde provienen los mensajes registrados
                //2do se registra un mensaje con el nivel SEVERE, que indica error grave. Este nivel es útil para captuar excepciones críticas como la que ocurre en el bloque catch
            }
        }
    }

    //No hace falta sincronizar el método retiroBancario ya que HacerRetiro llama desde dentro al método retiroBancario
    //Entonces el método retiroBancario quedaría "protegido" por la llamada desde el método sincronizado HacerRetiro
    //Se podría dejar sincronizado retiroBancario pero no es estrictamente sincronizado si ya se sincroniza HacerRetiro, evitando redundancia.

}