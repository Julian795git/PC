package Semaforos.ejercicio3;

import java.util.concurrent.Semaphore;

public class SecuenciaPasos {
    
    private static Semaphore s1 = new Semaphore (1); //Semáforo con un permiso libre para que acceda el P1 al inicio
    private static Semaphore s3 = new Semaphore (0); //Semáforo sin permisos, P3 no podrá acceder al recurso compartido
    private static Semaphore s2 = new Semaphore (0); //Semáforo sin permisos, P2 no podrá acceder al recurso compartido
    //Semáforos estáticos debido a que los procesos P1, P2 y P3 se representan como clases estáticas dentro de la clase principal y no como clases públicas aparte
    //Entonces, para que estas 3 clases estáticas dentro de SecuenciaPasos puedan acceder a los semáforos directamente sin necesidad de crear instancias de SecuenciaPasos
    //Si los semáforos no se declaran como estáticos, los procesos no podrían acceder directamente: habría que pasarle los semáforos por parámetro a cada proceso P o directamente crear cada clase pública para cada proceso y en el constructor agregarle el semáforo

    static class P1 extends Thread {
        public void run() {
            try {
                while (true) {
                    s1.acquire(); //P1 adquiere el permiso del semáforo
                    System.out.println("Ejecutando P1");
                    Thread.sleep(500); //Simulación de trabajo
                    s3.release(); //Se libera el semáforo correspondiente a P3 para que, durante su ejecución, pueda tomar el permiso
                }
            } catch (InterruptedException e) {}
        }
    }

    static class P3 extends Thread {
        public void run() {
            try {
                while (true) {
                    s3.acquire(); //P3 adquiere ese permiso que se había liberado en la ejecución de P1
                    System.out.println("Ejecutando P3");
                    Thread.sleep(500);
                    s2.release(); //Se libera el semáforo de P2 para que sea el siguiente en poder tomar el permiso y ejecutar
                }
            } catch (InterruptedException e) {}
        }
    }

    static class P2 extends Thread {
        public void run() {
            try {
                while (true) {
                    s2.acquire(); //P2 adquiere el permiso que se había liberado en la ejecución de P3
                    System.out.println("Ejecutando P2");
                    Thread.sleep(500);
                    s1.release(); //Se libera el permiso de P1 tomado al principio para que P1 pueda volver a acceder y se vuelva a iniciar el ciclo de ejecución
                }
            } catch (InterruptedException e) {}
        }
    }

    //Static class dentro de otra clase significa una clase anidada estática que no depende de uns instancia de una clase externa.
    //Es como si fuera una clase "normal", solo que queda definida adentro de otra clase para agruparla lógicamente. A éstas se las puede instanciar sin crear antes un objeto de la clase externa
    //Conviene usar "static class" si el ejercicio es chico y todas las clases están fuertemente relacionadas con la lógica de sincronización, quedando más prolijo agrupando las clases en un único archivo con static class.

    public static void main(String[] args) {
        new P1().start();
        new P3().start();
        new P2().start();
    }
}
