package matriz;

import java.util.Objects;

/**
 * Una posición (fila, columna) dentro de una matriz.
 * Es inmutable: moverse crea una posición nueva.
 */
public class Posicion {
    private final int fila;
    private final int columna;

    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    /** Devuelve la posición vecina en la dirección indicada. */
    public Posicion mover(Direccion d) {
        return new Posicion(fila + d.getDf(), columna + d.getDc());
    }

    /** Distancia "de cuadras" entre dos posiciones. */
    public int distancia(Posicion otra) {
        return Math.abs(fila - otra.fila) + Math.abs(columna - otra.columna);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posicion)) return false;
        Posicion p = (Posicion) o;
        return fila == p.fila && columna == p.columna;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fila, columna);
    }
}
