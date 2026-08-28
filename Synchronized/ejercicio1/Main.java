package Synchronized.ejercicio1;

public class Main {
    public static void main(String[] args) {
        
        //VerificarCuenta vc = new VerificarCuenta(); //Cuenta compartida entre Luis y Manuel

        //Si se quisiera hacer una cuenta separada para cada uno, habría que crear otro VerificarCuenta nuevo para tener 2 a la vez
        VerificarCuenta vc = new VerificarCuenta();

        Thread Luis = new Thread(vc, "Luis");
        Thread Manuel = new Thread(vc, "Manuel");
        
        Luis.start();
        Manuel.start();

    }

    //CORRECCIÓN DEL EJERCICIO PARA EVITAR INCONSISTENCIAS
    /* 
    
    El problema del ejercicio tal como está, es que ambos hilos Luis y Manuel están accediendo simultáneamente
    al método retiroBancario y modificando el balance sin coordinación. Esto puede provocar condiciones de carrera
    (Race conditions) y resultados inconsistentes.

    Para solucionar esto, se pueden usar los métodos sincronizados, sincronizando el método retiroBancario 
    en la clase CuentaBanco (Zona Crítica donde existe la posibilidad de inconsistencia al momento de la concurrencia)
    
    En realidad, se sincroniza el método que engloba todos los métodos que generen o puedan generar inconsistencias, siendo el método clave HacerRetiro
    Luego, sincronizando el método HacerRetiro de VerificarCuenta hace que el método retiroBancario quede "protegido"
    */ 

}