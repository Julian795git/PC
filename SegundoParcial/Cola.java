/************* Autores ***********
    Jeremías Martínez, Legajo FAI-4695
    Juan Ignacio Muñoz, Legajo FAI-4484
    Julián Forquera, Legajo FAI-5187
    Xavier Mora, Legajo FAI-5338
*/

package SegundoParcial;

public class Cola {
    
    private Nodo frente;
    private Nodo fin;

    public Cola() {
        this.frente = null;
        this.fin = null;
    }

    public boolean poner (Object nuevoElem) {
        Nodo nuevoNodo = new Nodo (nuevoElem, null);
        
        if(esVacia()) {
            this.frente = nuevoNodo;
        } else {
            this.fin.setEnlace(nuevoNodo);
        }
        this.fin = nuevoNodo;

        return true;
    }

    public boolean sacar() {
        boolean exito = true;
        if (esVacia()) {
            exito = false;
        } else {
            this.frente = this.frente.getEnlace();
            if (this.frente == null) {
                this.fin = null;
            }
        }

        return exito;
    }

    public Object obtenerFrente() {
        Object elem = null;
        if (!esVacia()) {
            elem = this.frente.getElem();
        }
        return elem;
    }

    public boolean esVacia() {
        return (this.frente == null);
    }

    public void vaciar() {
        this.frente = null;
        this.fin = null;
    }
    
    public Cola clone() {
        Cola clon = new Cola();
        /* clon.frente = this.frente;
        clon.fin = this.fin;*/
        //Esto está mal porque no se clona la cola, sino que se asigna la referencia
        //Entonces, se produce un falso clonamiento

        if (!this.esVacia()) {
            Nodo aux = this.frente;
            while (aux != null) {
                clon.poner(aux.getElem());
                aux = aux.getEnlace();
            }
        }
        return clon;
    }

    public String toString() {
        String colaTxt = "";
        Nodo aux = this.frente;
        
        if (esVacia()) {
            colaTxt = "[]";
        } else {
            colaTxt = "[";
            while (aux != null) {
                colaTxt += aux.getElem().toString();
                aux = aux.getEnlace();
                if (aux != null) {
                    colaTxt += ",";
                }
            }
            colaTxt += "]";
        }        

        return colaTxt;
    }

}