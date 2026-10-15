package SemaforosGenericos.ejercicio1;

import java.util.concurrent.Semaphore;

public class GestorPiscina {

    // private Semaphore mutex = new Semaphore(1);
    // NO ES NECESARIO UN SEMÁFORO MUTEX; LOS MÉTODOS ACQUIRE Y RELEASE DE SEMAPHORE YA SON ATÓMICOS Y "THREAD-SAFE". NO ES NECESARIO ENVOLVERLOS EN UN MUTEX ADICIONAL

    private Semaphore accesos; // = new Semaphore(capacidad); 
    /*
    En Java, las variables de clase se inicializan antes de que se ejecute el constructor. Como capacidad es un int y no le diste valor inicial, 
    arranca valiendo 0. Entonces, tu semáforo nace con 0 permisos. Cuando llamás al constructor GestorPiscina(int k) y le asignás this.capacidad = k, el semáforo ya se creó con 0. Nadie va a poder entrar a la piscina nunca.
    La instanciación del semáforo tiene que hacerse adentro del constructor.
    */

    public GestorPiscina(int k) {
        this.accesos = new Semaphore(k);
    }

    public void acceder() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " desea entrar a la piscina");
        //mutex.acquire();
        accesos.acquire();
        System.out.println(Thread.currentThread().getName() + " consiguió acceder a la piscina");
        //mutex.release();
    }

    public void abandonar() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " desea salir de la piscina");
        //mutex.acquire();
        accesos.release();
        System.out.println(Thread.currentThread().getName() + " logró salir de la piscina");
        //mutex.release();
    }

}

/*
ANOTACIONES EXTRA:
¿El mutex era solo para variables numéricas manuales?
¡Exactamente! Lo entendiste a la perfección.

La clase Semaphore que viene en java.util.concurrent (al igual que AtomicInteger, ReentrantLock, etc.) ya está programada a bajo nivel para que sus operaciones sean atómicas. Cuando hacés accesos.acquire() o accesos.release(), Java se asegura mediante el sistema operativo y el procesador de que ningún otro hilo pise esa actualización del contador interno.

Vos necesitás usar un mutex (ya sea otro Semaphore(1), un bloque synchronized o un Lock) cuando el recurso compartido es una variable o estructura que no es "Thread-safe".

Por ejemplo, si además de controlar la capacidad, el gestor tuviera que llevar un registro histórico de cuánta gente entró en todo el día:
private int contadorHistorico = 0; // Variable común y corriente

public void acceder() throws InterruptedException {
    accesos.acquire(); // Thread-safe, no necesita mutex
    
    // ZONA CRÍTICA PARA LA VARIABLE COMÚN
    mutex.acquire();
    contadorHistorico++; // <-- Esto NO es atómico (son 3 pasos: leer, sumar, guardar)
    mutex.release();
}

Ahí sí el mutex es obligatorio, porque si dos hilos hacen contadorHistorico++ al mismo tiempo sin protección, se pueden pisar y perder sumas. 
Pero para el control de los permisos del semáforo en sí, delegá toda la confianza en los métodos del propio semáforo.

*/