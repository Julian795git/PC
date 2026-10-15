package SemaforosGenericos.ejercicio1;

public class Persona implements Runnable {
    
    private GestorPiscina piscina;

    public Persona(GestorPiscina unaPisc) {
        this.piscina = unaPisc;
    }

    public void run() {
        try {
            this.piscina.acceder();
            try {
                Thread.sleep(3000);
            } finally {
                this.piscina.abandonar();
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " fue interrumpido antes de entrar.");
        }
    }

}

/*
ANOTACIONES EXTRA:
¿Por qué NO usar un solo try-catch-finally? (La trampa de los permisos fantasma)

A primera vista, poner todo en un solo bloque parece el camino más limpio, así:
// CÓDIGO PELIGROSO (NO HACER)
try {
    this.piscina.acceder(); // Intenta hacer acquire()
    Thread.sleep(3000);
} catch (InterruptedException e) {
    // Manejo de error
} finally {
    this.piscina.abandonar(); // Hace release()
}

El problema gravísimo acá ocurre si el hilo es interrumpido MIENTRAS está esperando afuera de la piscina.

Fijate la secuencia de un hilo "atacado" por un InterruptedException:
1. El hilo llama a acceder() y se queda bloqueado en el acquire() porque la piscina está llena. Aún no tiene el permiso.
2. Alguien interrumpe el hilo (salta la InterruptedException).
3. El hilo abandona el acceder() (sin haber conseguido el permiso) y salta directamente al catch.
4. Después del catch, el código ejecuta obligatoriamente el bloque finally.
5. En el finally, llama a abandonar(), lo que ejecuta un accesos.release().

Resultado: ¡El hilo acaba de liberar un permiso que nunca obtuvo! Acaba de "fabricar" un espacio extra. Si tu piscina tenía capacidad para 10 personas, ahora mágicamente tiene 11.

Por eso se usa el try-finally anidado:
this.piscina.acceder(); // Si esto falla y lanza excepción, el hilo vuela al catch externo y NO pasa por el finally interno.

// Si llegamos acá, es 100% seguro que el hilo TIENE el permiso.
try { 
    Thread.sleep(3000);
} finally {
    this.piscina.abandonar(); // Solo se libera si efectivamente entramos.
}
*/