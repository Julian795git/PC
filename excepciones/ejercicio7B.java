package excepciones;

public class ejercicio7B {
    
    private static int metodo() {
        int valor = 0;
        try {
            valor = valor + 1; //Esto se suma antes del error, se mantiene
            valor = valor + Integer.parseInt("W"); //Ocurre el error y se entra al catch
            valor = valor + 1; //No se alcanza a ejecutar por el error ocurrido en la línea anterior
            System.out.println("Valor al final del try: " + valor);
        } catch (NumberFormatException e) {
            valor = valor + Integer.parseInt("42"); //Entra al catch
            System.out.println("Valor al final del catch: " + valor);
        } finally {
            valor = valor + 1; //Sigue con la ejecución en el finally
            System.out.println("Valor al final de finally: " + valor);
        }
        valor = valor + 1;
        System.out.println("Valor antes del return: " + valor);
        return valor;
    } //Se entra en el catch pero como se respeta el formato de parseInt dentro del catch, se sigue la ejecución normalmente sumando el resto a "valor"
    //Luego, retorno exitoso de valor = 45

    public static void main(String[] args) {
        try { 
            System.out.println(metodo());
        } catch (Exception e) {
            System.err.println("Excepcion en metodo()");
            e.printStackTrace();
        }
    }


}
