package Synchronized.ejercicio4;

import java.util.Random;

public class Cliente implements Runnable { //implements (algo = una interfaz) -> implementar a mano todos los métodos (en este caso runnable solo)
    
    //SE PODRÍA HABER FUSIONADO LAS 3 ATRACCIONES EN UNA SOLA CLASE ATRACCIONES Y HABER INSTANCIADO ESA CLASE EN 3 OBJETOS CON SUS NOMBRES

    private String nombre;
    private Carrusel carrusel;
    private MontaniaRusa montania;
    private RuedaFortuna rueda;

    public Cliente(String unNombre, Carrusel unCarru, MontaniaRusa unaMont, RuedaFortuna unaRueda) {
        this.nombre = unNombre;
        this.carrusel = unCarru;
        this.montania = unaMont;
        this.rueda = unaRueda;
    }

    public void run() {
        Random rand = new Random();
        int eleccion = rand.nextInt(3); // Genera 0, 1 o 2 al azar

        System.out.println(this.nombre + " está decidiendo a qué atracción ir...");
        
        // El cliente elige libremente una atracción
        switch (eleccion) {
            case 0:
                carrusel.usarCarrusel(this.nombre);
                break;
            case 1:
                montania.usarRusa(this.nombre);
                break;
            case 2:
                rueda.usarRueda(this.nombre);
                break;
        }
    }
}
