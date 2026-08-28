package excepciones;

public class ejercicio6 {
 
    public static double acceso_por_indice (double[] v, int j) throws RuntimeException {

        try {
            return v[j];
        } catch (RuntimeException exc) {
            throw exc;
        }

    }

    public static void main(String[] args) {
        double[] v = new double[15];
        acceso_por_indice(v, 16);
    }

}
