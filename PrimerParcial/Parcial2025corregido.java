package PrimerParcial;

//Ejercicio 1 de parcial: el recorrido del colectivo (semáforos - SIN ESPERA ACTIVA - SIN INTERBLOQUEOS)
/*
 * Se desea simular el funcionamiento de un colectivo en su recorrido. El colectivo tiene capacidad para k pasajeros, y hay 1 parada de salida y 1 parada de llegada
 * 
 * El colectivero comienza su recorrido en la parada de salida, cuando el colectivo se llena. El colectivo tiene 1 puerta adelante que se habilita para la subida de los pasajeros
 * en la parada de salida, y una puerta atrás que se habilita para el descenso de los pasajeros cuando llega a destino
 * 
 * Una vez habilitada la puerta de ingreso, las personas esperando en la parada pueden comenzar a subir. Al subir un pasajero, el colectivero le cobra y verifica si hay lugar para un pasajero más,
 * en cuyo caso caso habilita la sbida de otro pasajero. Este proceso se repite hasta que se llena el colectivo o pudieron subir todas las personas que estaban esperando.
 * 
 * Luego el colectivo inicia su recorrido usual, hasta llegar a destino. Cuando llega a destino, el conductor del colectivo habilita la puerta trasera para que los pasajeros comiencen a descender.
 * Cuando un pasajero termina de bajar, le avisa al siguiente pasajero para que pueda bajar. El último pasajero en bajar le avisa al conductor que ya todos han bajado para que pueda comenzar un recorrido nuevo
 * 
 * Para ello, el colectivo se mueve hacia la parada de salida nuevamente y se repite el ciclo. Considere que el colectivo hace cantR recorridos:
 * 1. No interesa simular el cobro del pasaje
 * 2. Las personas que no puedan subir porque no hay espacio disponible deben esperar hasta la próxima habilitación para subir
 * 3. Las personas que lleguen a la parada mientras el colectivo está en su recorrido deben esperar
 * 4. Los pasajeros del colectivo NO pueden bajarse durante el recorrido. 
 */

import java.util.concurrent.Semaphore;
import java.util.Scanner;

//Primer recurso compartido con su debido semáforo (no implementado en el parcial)
class Parada {
    private int pasajerosEsperando = 0;
    //Semáforo para proteger el contador de pasajeros (exlusión mutua)
    private Semaphore mutex = new Semaphore(1);

    //No es necesario un constructor vacío, Java lo agrega por defecto

    //Método que devuelve la cantidad de pasajeros esperando, usado para que el colectivero vea si hay gente o no
    public int verPasajeros() throws InterruptedException {
        mutex.acquire();
        int p = this.pasajerosEsperando;
        mutex.release();
        return p;
    }

    //Método llamado por un hilo Pasajero cuando llega a la parada
    public void agregarPasajero() throws InterruptedException {
        mutex.acquire();
        this.pasajerosEsperando++;
        System.out.println(Thread.currentThread().getName() + " llegó a la parada.");
        mutex.release();
    }

    //Método llamado por un hilo Pasajero cuando se sube al colectivo
    public void quitarPasajero() throws InterruptedException {
        mutex.acquire();
        this.pasajerosEsperando--;
        System.out.println(Thread.currentThread().getName() + " deja la parada para subirse al colectivo");
        mutex.release();
    }

}

//Segundo recurso compartido con sus semáforos (este si lo tenia)
class Colectivo {

    private int capacidad;
    private int ocupacion = 0;
    private Parada parada;

    //Semáforos de sincronización

    //1. Para la subida 1 por 1 
    //El colectivero libera, el pasajero adquiere
    private Semaphore semPuedeSubir = new Semaphore(0);
    //El pasajero libera, el colectivero adquiere
    private Semaphore semPasajeroSubido = new Semaphore(0);

    //2. Para la bajada (en cadena)
    //El colectivero libera 1 vez, luego los pasajeros en cadena
    private Semaphore semPuedeBajar = new Semaphore(0);

    //3. Para el fin del recorrido
    //El último pasajero liberam el colectivero adquiere
    private Semaphore semColectiveroEsperando = new Semaphore(0);

    //4. Mutex para la variable ocupación
    private Semaphore mutexOcupacion = new Semaphore(1);

    //Aclaración sobre la cantidad de permisos iniciales para cada semáforo:
    
    // A. Semáforo inicializado con 1 permiso:
    // Un semáforo se inicializa con 1 permiso inicial cuando el semáforo corresponde a la protección de alguna variable o bloque de código que solamente puede ser accedido de a una vez por hilo
    // Es decir, cuando el recurso compartido al que se quiere acceder es algo que se accede para modificar/actualizar su valor y salir, se habilita un acceso previo para que sea accesible de forma controlada asegurando la consistencia de los datos
    
    // B. Semáforo inicializado con 0 permisos:
    // Un semáforo se inicializa con 0 permisos iniciales cuando el acceso al recurso compartido protegido depende de la habilitación previa de parte de otro hilo que ejecuta esa liberación del acceso a dicho recurso compartido
    // Cambia con respecto al semáforo con 1 permiso porque no se puede tomar 1 permiso directamente, sino que el hilo que espera debe esperar al accionar de otra operación para tener la chance de acceder


    public Colectivo (int unaCapacidad, Parada unaParada) {
        this.capacidad = unaCapacidad;
        this.parada = unaParada;
    }

    //--- MÉTODOS PARA EL COLECTIVERO ---

    //Método donde el colectivero gestiona la subida 1 por 1
    public void gestionarSubida() throws InterruptedException {
        
        //el while simula al colectivero chequeando si hay lugar en el cole y si hay gente esperando en la parada antes de habiltar la subida
        while (this.ocupacion < this.capacidad && parada.verPasajeros() > 0) {
            System.out.println("Colectivero: ¡Que suba el siguiente pasajero!");
            semPuedeSubir.release(); //Habilita a UN pasajero
            semPasajeroSubido.acquire(); //Espera a que ESE pasajero suba
        }

        //Si sale del while es porque se llenó el cole o no hay más gente esperando
        System.out.println("Colectivero: Cierro la puerta delantera. Ocupación: " + this.ocupacion);
    }

    //Este método se implementa más por un tema de diseño y de respeto a las responsabilidades de cada clase
    //Si se saltea iniciarRecorrido y se junta ambas cosas en gestionarSubida(), se rompe el principio de responsabilidad única para cada método
    public void iniciarRecorrido() {
        System.out.println("Colectivero: Comienza el recorrido!");
    }

    public void gestionarBajada() throws InterruptedException {
        if (this.ocupacion != 0) {
            System.out.println("Colectivero: llegamos a destino, pasajeros! Abro la puerta trasera");
            //Da la señal al PRIMER pasajero para que inicie la cadena de bajada
            semPuedeBajar.release();

            //Espera la señal del ÚlTIMO pasajero
            semColectiveroEsperando.acquire();
            System.out.println("Colectivero: último pasajero bajó. Colectivo vacío");
        } else {
            System.out.println("Colectivero: No hay nadie a bordo para bajar.");
        }
    }

    //--- MÉTODOS PARA EL PASAJERO ---

    //Método llamado por el pasajero al llegar a la parada
    public void hacerFila() throws InterruptedException {
        parada.agregarPasajero();
    }

    //Método llamado por el pasajero al momento de subir al cole
    public void subir() throws InterruptedException {
        //1. Espera permiso del colectivero
        semPuedeSubir.acquire();

        //2. Abandona la parada (ya no está esperando)
        parada.quitarPasajero();

        //3. Subir y ocupar asiento (sección crítica)
        mutexOcupacion.acquire();
        this.ocupacion++;
        System.out.println(Thread.currentThread().getName() + " subió. Ocupación actual: " + this.ocupacion);
        mutexOcupacion.release();

        //4. Avisar al colectivero que ya subió
        semPasajeroSubido.release();
    }

    //Método llamado por el pasajero para bajar. El hilo se bloquea hasta que le toque su turno
    public void bajar() throws InterruptedException {
        //1. Esperar su turno para bajar (sea del colectivero o de otro pasajero)
        semPuedeBajar.acquire();

        //2. Bajar y liberar asiento (sección critica)
        mutexOcupacion.acquire();
        this.ocupacion--;
        System.out.println(Thread.currentThread().getName() + " bajó. Quedan: " + this.ocupacion + " pasajeros.");

        if (this.ocupacion > 0) {
            //No es el último, avisa al siguiente
            System.out.println(Thread.currentThread().getName() + ": Adelante el siguiente");
            semPuedeBajar.release(); //Da paso al siguiente pasajero (cadena)
        } else {
            //Es el último pasajero
            System.out.println(Thread.currentThread().getName() + "¡Soy el último en bajar, chofer!");
            semColectiveroEsperando.release(); //Aviso al colectivero que puede volver
        }

        mutexOcupacion.release();
    }
}

class Colectivero implements Runnable {
    private Colectivo cole;
    private final int cantRecorridos;

    public Colectivero (Colectivo unCole, int unaCant) {
        this.cole = unCole;
        this.cantRecorridos = unaCant;
    }

    public void run() {
        int recorridoActual = 0;
        while (recorridoActual < this.cantRecorridos) {
            try {
                System.out.println("Colectivero: Iniciando recorrido " + (recorridoActual + 1) + "/" + cantRecorridos);
                System.out.println("Colectivero: En parada de salida.");

                //1. Gestiona la subida de pasajeros
                cole.gestionarSubida();

                //2. Inicia recorrido (simulación del viaje)
                cole.iniciarRecorrido();
                Thread.sleep((long) (Math.random()) * 3000);
                
                //3. LLega a destino y gestiona la bajada
                cole.gestionarBajada();

                //4. Regresa a la parada de salida (Simulacion)
                System.out.println("Colectivero: Volviendo a la parada de salida... ");
                Thread.sleep((long) (Math.random()) * 2000);

                recorridoActual++;
            } catch (InterruptedException e) {

            }
        }
        System.out.println("Fin de la jornada del colectivero");
    }
}

class Pasajero implements Runnable {
    private Colectivo cole;

    public Pasajero(Colectivo unCole) {
        this.cole = unCole;
    }

    public void run() {
        try {
            //1. llega a la parada y hace fila
            cole.hacerFila();

            //2. Sube al colectivo (el hilo se bloquea acá hasta que el colectivero lo deja)
            cole.subir();

            //3. Viaja (simulación de viaje)

            //4. Baja del colectivo (el hilo se bloquea hasta que llega y le toca)
            cole.bajar();

            System.out.println(Thread.currentThread().getName() + " terminó su viaje");
        } catch (InterruptedException e) {

        }
    }
}

public class Parcial2025corregido {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingresa la capacidad del colectivo");
        int capacidadCole = sc.nextInt();

        System.out.println("Ingresa la cantidad total de pasajeros a simular");
        int cantidadTotalPasajeros = sc.nextInt();

        System.out.println("Ingrese la cantidad de recorridos del colectivo");
        int cantRecorridos = sc.nextInt();

        Parada salida = new Parada();
        Colectivo colectivo = new Colectivo(capacidadCole, salida);

        //el segundo argumento del constructor del Thread corresponde en este caso al argumento destinado al nombre del hilo 
        //la clase Thread tiene varios constructores sobrecargados, pero las más comunes son las usadas en PC (solo la creación del hilo específico, o con el agregado del nombre)
        Thread colectivero = new Thread (new Colectivero (colectivo, cantRecorridos), "Colectivero");
        colectivero.start();

        Thread[] pasajeros = new Thread[cantidadTotalPasajeros];
        for (int i = 0; i < pasajeros.length; i++) {
            pasajeros[i] = new Thread (new Pasajero (colectivo), "Pasajero-" + (i + 1));
            pasajeros[i].start(); //La nota del profe de cambiar start() por run() en teoría es errónea
            //usar run() acá haría que todos los pasajeros se ejecuten uno después del otro en el hilo main, matando toda la concurrencia

            try {
                Thread.sleep(500); //Dejo un tiempo entre creación y creación de hilos Pasajeros
            } catch (InterruptedException e) {

            }
        }

        System.out.println("Ejecución Finalizada");
        sc.close();
    }
}