package excepciones;

import java.io.*;

public class ejercicio7D {
    
    private static int metodo() {
        int valor = 0;
        try {
            valor = valor + 1; //Se ejecuta
            valor = valor + Integer.parseInt("W"); //NumberFormatException
            valor = valor + 1; //No se ejecuta
            System.out.println("Valor al final del try: " + valor); //No se ejecuta
            throw new IOException(); //Se tira un IOException (Creo que no se ejecuta)
        } catch (IOException e) { //Se busca catchear un IOException pero el error fue un NumberFormatException, entonces el error no es gestionado
            valor = valor + Integer.parseInt("42");
            System.out.println("Valor al final del catch " +  valor);
        } finally { 
            valor = valor + 1; //Se termina sumando esto a valor
            System.out.println("Valor al final del finally: " + valor); //Sale por pantalla
            //Termina la ejecución por falta de gestión del NumberFormatException
        }
        //NO SE EJECUTA
        valor = valor + 1;
        System.out.println("Valor antes del return: " +  valor);
        return valor;
        //No hay retorno por el error
    }

    public static void main(String[] args) {
        try {
            System.out.println(metodo());
        } catch (Exception e) {
            System.err.println("Excepción en metodo()");
            e.printStackTrace(); //Se atrapa el error del método desde el main
        }
    }

}
