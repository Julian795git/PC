package hilos.ejercicio6;



public class Main {
    public static void main(String[] args) {
        Corredor[] arregloCorredores;
        Thread[] arregloHilos;
        ContadorDistancia contador = new ContadorDistancia();
        arregloCorredores = new Corredor[20];
        arregloHilos = new Thread[20];
        for (int i = 0; i < 20; i++) {
            arregloCorredores[i] = new Corredor("Corredor " + i);
            arregloHilos[i] = new Thread(new Hilo(arregloCorredores[i], contador));
            arregloHilos[i].start();
        }
        
        for(int i = 0; i < 20; i++) {
            try {
                arregloHilos[i].join();
                arregloCorredores[i].imprimir();
            } catch (InterruptedException e) {}
        }
        
        System.out.println( "Corredor maximo: " + contador.obtenerMayor());
    }

} 
