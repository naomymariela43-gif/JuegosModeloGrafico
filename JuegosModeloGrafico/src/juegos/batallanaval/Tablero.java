package juegos.batallanaval;

import java.util.LinkedList;

public class Tablero {
    public static final int TAMANO = 6;

    // '~' = agua, 'X' = tocado, 'o' = fallo
    private char[][] casillas;
    // guarda que barco hay en cada casilla (null si no hay)
    private Barco[][] posicionBarcos;
    private LinkedList<Barco> barcos;

    public Tablero() {
        this.casillas = new char[TAMANO][TAMANO];
        this.posicionBarcos = new Barco[TAMANO][TAMANO];
        this.barcos = new LinkedList<>();

        for (int f = 0; f < TAMANO; f++) {
            for (int c = 0; c < TAMANO; c++) {
                this.casillas[f][c] = '~';
            }
        }
    }

    public char getCasilla(int fila, int columna) {
        return this.casillas[fila][columna];
    }

    public boolean hayBarco(int fila, int columna) {
        return this.posicionBarcos[fila][columna] != null;
    }

    //el jugador esconde su barco. Devuelve false si no cabe o choca con otro barco
    public boolean colocarBarco(Barco barco, int fila, int columna, boolean horizontal) {
        if (!cabe(barco, fila, columna, horizontal)) {
            return false;
        }
        for (int i = 0; i < barco.getTamano(); i++) {
            if (horizontal) {
                this.posicionBarcos[fila][columna + i] = barco;
            } else {
                this.posicionBarcos[fila + i][columna] = barco;
            }
        }
        this.barcos.add(barco);
        return true;
    }

    private boolean cabe(Barco barco, int fila, int columna, boolean horizontal) {
        for (int i = 0; i < barco.getTamano(); i++) {
            int f = horizontal ? fila : fila + i;
            int c = horizontal ? columna + i : columna;
            if (f >= TAMANO || c >= TAMANO) {
                return false;
            }
            if (this.posicionBarcos[f][c] != null) {
                return false;
            }
        }
        return true;
    }

    //devuelve un mensaje con lo que paso: AGUA, TOCADO, HUNDIDO o REPETIDO
    public String disparar(int fila, int columna) {
        if (this.casillas[fila][columna] != '~') {
            return "REPETIDO";
        }

        Barco barco = this.posicionBarcos[fila][columna];
        if (barco == null) {
            this.casillas[fila][columna] = 'o';
            return "AGUA";
        }

        this.casillas[fila][columna] = 'X';
        barco.recibirImpacto();
        if (barco.estaHundido()) {
            return "HUNDIDO";
        }
        return "TOCADO";
    }

    public boolean todosHundidos() {
        for (int i = 0; i < this.barcos.size(); i++) {
            if (!this.barcos.get(i).estaHundido()) {
                return false;
            }
        }
        return true;
    }
}
