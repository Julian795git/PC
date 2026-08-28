package hilos.ejercicio3;

public class ThreadEjemploRunnable implements Runnable {
    //Mejor método, más recomendado para lo que viene más adelante

    private String nombre; //Con atributo nombre se identifica al hilo

    public ThreadEjemploRunnable(String nombre) {
        this.nombre = nombre;
    }

    //Contiene la lógica del hilo
    public void run() {
        for (int i = 0; i < 10; i ++) {
            System.out.println(i + " " + nombre);
        }
        System.out.println("Termina Thread " + nombre);
    }

    public static void main(String[] args) {
        //Se crean instancias de ThreadEjemploRunnable con los nombres correspondientes
        //Estas instancias se pasan al constructor del Thread
        Thread hilo1 = new Thread (new ThreadEjemploRunnable("Maria Jose"));
        Thread hilo2 = new Thread (new ThreadEjemploRunnable("Jose Maria"));

        //Se llama al método start() en las instancias de Thread para iniciar los hilos
        hilo1.start();
        hilo2.start();

        System.out.println("Termina Thread main");
        //La salida será similar a la del ejemplo que extiende a la clase Thread
    }

}
