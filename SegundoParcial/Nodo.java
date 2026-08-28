/************* Autores ***********
    Jeremías Martínez, Legajo FAI-4695
    Juan Ignacio Muñoz, Legajo FAI-4484
    Julián Forquera, Legajo FAI-5187
    Xavier Mora, Legajo FAI-5338
*/

package SegundoParcial;

public class Nodo {

    private Object elem;
    private Nodo enlace;

    //Constructor
    public Nodo (Object elemento, Nodo link) {
        this.elem = elemento;
        this.enlace = link;
    }

    public Object getElem() {
        return this.elem;
    }

    public void setElem (Object elemento) {
        this.elem = elemento;
    }
    
    public Nodo getEnlace() {
        return enlace;
    }

    public void setEnlace (Nodo link) {
        this.enlace = link;
    }

}
