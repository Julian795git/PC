package excepciones;

public class ejercicio7C {
    
    private static int metodo() {
        int valor = 0;
        try {
            valor = valor + 1; //Se ejecuta normalmente
            valor = valor + Integer.parseInt("W"); //Ocurre el error y se pasa al catch
            valor = valor + 1; //No se llega a ejecutar
            System.out.println("Valor al final del try: " +  valor); //Tampoco
        } catch (NumberFormatException e) {
            valor = valor + Integer.parseInt("W"); //Se provoca el mismo error que en el try, entonces se lanza excepción
            System.out.println("Valor al final del catch: " +  valor); //No se ejecuta
            //como no hay otro catch para que atrape a esta excepción, ocurre lo del finally y se termina la ejecución sin la última parte
        } finally {
            valor = valor + 1; //Se suma a valor
            System.out.println("Valor al final del finally: " + valor); //Se muestra
        }
        //No se logra llegar a esta parte por la excepción del catch 
        valor = valor + 1;
        System.out.println("Valor antes del return: " +  valor);
        return valor; //No hay return porque el método no pudo ejecutarse en su totalidad debido a la excepción NumberFormatExcepcion
    }

    public static void main(String[] args) {
        try {
            System.out.println(metodo());
        } catch (Exception e) {
            System.err.println("Excepcion en metodo()");
            e.printStackTrace(); //Imprime por pantalla la traza de la pila (stack trace) de la excepción lanzada
            //Significa que muestra el tipo de excepción, el mensaje de error y la lista de métodos que se estaban ejecutando cuando ocurrió el error
            //Desde el punto donde se lanzó la excepción hasta el punto donde fue capturada
        }
    }

}
