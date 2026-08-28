package Synchronized.ejercicio4;

public class Main {
    
    public static void main(String[] args) {
        
        /* 
        Carrusel carrusel = new Carrusel();
        MontaniaRusa montania = new MontaniaRusa();
        RuedaFortuna rueda = new RuedaFortuna();
        */
        
        CarruselPro carrusel = new CarruselPro();
        MontaniaRusaPro montania = new MontaniaRusaPro();
        RuedaFortunaPro rueda = new RuedaFortunaPro();        

        int cant = 35;

        //Thread[] arr = new Thread[cant];
        Thread[] arrPro = new Thread[cant];

        /* 
        for (int i = 0; i < cant; i++) {
            Thread cliente = new Thread(new Cliente ("Cliente " + (i + 1), carrusel, montania, rueda));
            arr[i] = cliente;
            arr[i].start();
        }
        */

        
        for (int i = 0; i < cant; i++) {
            Thread cliente = new Thread(new ClientePro("Cliente" + (i + 1), carrusel, montania, rueda));
            arrPro[i] = cliente;
            arrPro[i].start();
        }
        

    }
}
