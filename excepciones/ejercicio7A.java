package excepciones;

public class ejercicio7A {
    
    private static int metodo() {
        int valor = 0;
        try {
            valor = valor + 1;
            valor = valor + Integer.parseInt("42");
            valor = valor + 1;
            System.out.println("Valor al final del try: " + valor);
        } catch (NumberFormatException e) {
            valor = valor + Integer.parseInt("42"); //Convierte al texto "42" en un int con dicho valor numérico 42
            System.out.println("Valor al final del catch " + valor);
        } finally {
            valor = valor + 1;
            System.out.println("Valor al final de finally: " +  valor);
        }
        valor = valor + 1;
        System.out.println("Valor antes del return: " + valor);
        return valor;
    } //No retorna ninguna excepción porque el formato del texto dentro del parseInt es válido y se convierte exitosamente en int
    //Luego, retorno exitoso con int valor = 46 sin entrar al catch

    public static void main(String[] args) {
        try {
            System.out.println(metodo());
        } catch (Exception e) {
            System.err.println("Excepcion en metodo()");
            e.printStackTrace();
        }
    }

}
