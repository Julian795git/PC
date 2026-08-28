package excepciones;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

//EJERCICIO 8

public class PruebaExcep {
    
    //EJERCICIO 8A
    public static class MenorEdadException extends ArithmeticException {
        public MenorEdadException(String message) {
            super(message);
        }
    }
    //Si una clase extiende a RuntimeException o inferiores, la excepción será "No Comprobada" (Unchecked),
    //Sin necesitar declarar throws ni capturarla obligatoriamente

    //En cambio si se extiende a IOException, la excepción será comprobada (checked), debiendo si o si declarar throws
    //en el método que la lanza y a capturarla o propagarla


    public static boolean mayorEdad (int edadIngresada) throws MenorEdadException { //El "throws MenorEdadException" no es necesario en esta situación
    //No es necesario ya que MenorEdadException extiende a una excepción del tipo Runtime, no a una IOException (en ese caso si debería declararse "throws ...")
        if (edadIngresada < 18) {
            throw new MenorEdadException("La edad ingresada corresponde a un menor de edad");
        }
        return true;
    }

    //EJERCICIO 8B
    public static class NumeroIncorrectoException extends ArithmeticException {
        public NumeroIncorrectoException(String message) {
            super(message);
        }
    }

    public static boolean numeroRuleta(int numeroJugador) { //throws NumeroIncorrectoException 
        Random ruleta = new Random();
        int numeroAzar = ruleta.nextInt(37); //37 es la "longitud" de posibles casos para el random (0 a 36)
        
        //También se puede hacer usando Math Random
        //int numeroAzar = (int) (Math.random() * 37);

        if (numeroAzar != numeroJugador) {
            throw new NumeroIncorrectoException("El número seleccionado es distinto al que salió en la ruleta :(");
        }

        return true;
    }

    //EJERCICIO 8C
    public static void metodoColeccion() {
        Scanner sc = new Scanner(System.in);
        ArrayList<Integer> numeros = new ArrayList<>();

        System.out.println("Ingrese 5 números");
        for (int i = 0; i < 5; i++) {
            numeros.add(sc.nextInt());
        }

        try {
            for (int i = 0; i < 7; i++) {
                System.out.println("Número en posición " + i + ": " + numeros.get(i));
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Se excedió el índice máximo de la colección");
        }

        sc.close();
    }
    //No hizo falta crear una excepción nueva, ya que el problema plantea el salirse de los límites de una colección (Excepción ya existente)
    

        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);

            System.out.println("Ingrese su edad");
            int edad = sc.nextInt();
            boolean verificacionEdad = mayorEdad(edad);

            System.out.println("Mayor de edad: " + verificacionEdad);

            //Lo mismo con método ruleta y método colección

            sc.close();
        }
}
