package Synchronized.ejercicio4;

import java.util.Random;

public class ClientePro implements Runnable {
    
    private String nombre;
    private CarruselPro carrusel;
    private MontaniaRusaPro rusa;
    private RuedaFortunaPro rueda;

    public ClientePro (String nom, CarruselPro carru, MontaniaRusaPro unaRusa, RuedaFortunaPro unaRueda) {
        this.nombre = nom;
        this.carrusel = carru;
        this.rusa = unaRusa;
        this.rueda = unaRueda;
    }

    @Override
    public void run() {
        boolean reservaExitosa = false;
        int intentos = 0;
        int maxIntentos = 3; // Lo dejamos intentar 3 veces antes de rendirse
        Random rand = new Random();

        while (!reservaExitosa && intentos < maxIntentos) {
            int eleccion = rand.nextInt(3);
            
            switch (eleccion) {
                case 0:
                    reservaExitosa = carrusel.usarCarrusel(this.nombre);
                    break;
                case 1:
                    reservaExitosa = rusa.usarRusa(this.nombre);
                    break;
                case 2:
                    reservaExitosa = rueda.usarRueda(this.nombre);
                    break;
            }

            if (!reservaExitosa) {
                intentos++;
                // Le damos un respiro antes de que vuelva a intentar reservar
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
        }

        if (!reservaExitosa) {
            System.out.println(this.nombre + " se fue del parque porque estaba todo lleno.");
        }
    }
}
