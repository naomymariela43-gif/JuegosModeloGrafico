package juegos.batallanaval;

public class Barco {
    private String nombre;
    private int tamano;
    private int impactos;

    public Barco(String nombre, int tamano) {
        this.nombre = nombre;
        this.tamano = tamano;
        this.impactos = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTamano() {
        return tamano;
    }

    public void recibirImpacto() {
        this.impactos++;
    }

    public boolean estaHundido() {
        return this.impactos >= this.tamano;
    }

    @Override
    public String toString() {
        return nombre + " (tamano " + tamano + ")";
    }
}
