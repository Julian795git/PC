package Synchronized.ejercicio3;

public class Hamster implements Runnable {
    private String nombre;
    private Plato plato;
    private Rueda rueda;
    private Hamaca hamaca;

    public Hamster (String nombre, Plato unPlato, Rueda unaRueda, Hamaca unaHamaca) {
        this.nombre = nombre;
        this.plato = unPlato;
        this.rueda = unaRueda;
        this.hamaca = unaHamaca;
    }
    
    public void run() {
        plato.comer(this.nombre);
        rueda.rodar(this.nombre);
        hamaca.hamacarse(this.nombre);
        System.out.println(this.nombre + " terminó de realizar todas las actividades.");
    }

    //Con el uso de métodos synchronized, si o si se debe seguir un orden fijo en la ejecución de actividades.
    //ChatGPT:
    //Con synchronized, no podés hacer que “salte” a otra actividad si está ocupado, porque queda esperando el lock. 
    //Tendrías que meter lógica de flags + wait/notify, pero se complica bastante y termina siendo reinventar lo que ya te da ReentrantLock.
    //Luego, con synchronized: orden fijo y bloqueante. 
    //Con trylock: Más realista, cada hámster intenta "a ver qué hay libre" y cambia de actividad si no consigue un recurso

}
