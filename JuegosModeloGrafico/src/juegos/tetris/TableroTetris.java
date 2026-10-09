package juegos.tetris;

public class TableroTetris {
    public static final int ALTO = 14;
    public static final int ANCHO = 8;

    //cada casilla guarda el numero del jugador que la lleno ('1' a '4'), o '.' si esta vacia
    private char[][] casillas;

    public TableroTetris() {
        this.casillas = new char[ALTO][ANCHO];
        for (int f = 0; f < ALTO; f++) {
            for (int c = 0; c < ANCHO; c++) {
                this.casillas[f][c] = '.';
            }
        }
    }

    public char getCasilla(int fila, int columna) {
        return this.casillas[fila][columna];
    }

    //revisa si la pieza cabe con su esquina de arriba-izquierda en (fila, columna)
    public boolean cabe(Pieza pieza, int fila, int columna) {
        int[][] forma = pieza.getForma();
        for (int f = 0; f < pieza.getAlto(); f++) {
            for (int c = 0; c < pieza.getAncho(); c++) {
                if (forma[f][c] == 1) {
                    int ft = fila + f;
                    int ct = columna + c;
                    if (ft >= ALTO || ct < 0 || ct >= ANCHO) {
                        return false;
                    }
                    if (this.casillas[ft][ct] != '.') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //devuelve la fila donde quedaria la pieza si se suelta en esa columna
    public int filaDeCaida(Pieza pieza, int columna) {
        int fila = 0;
        while (cabe(pieza, fila + 1, columna)) {
            fila++;
        }
        return fila;
    }

    //deja caer la pieza en la columna. Devuelve false si ya no cabe (tablero lleno)
    public boolean soltarPieza(Pieza pieza, int columna, char numeroJugador) {
        if (!cabe(pieza, 0, columna)) {
            return false;
        }
        int fila = filaDeCaida(pieza, columna);
        int[][] forma = pieza.getForma();
        for (int f = 0; f < pieza.getAlto(); f++) {
            for (int c = 0; c < pieza.getAncho(); c++) {
                if (forma[f][c] == 1) {
                    this.casillas[fila + f][columna + c] = numeroJugador;
                }
            }
        }
        return true;
    }

    //borra las filas llenas, baja todo lo de arriba y devuelve cuantas borro
    public int borrarLineasCompletas() {
        int borradas = 0;
        for (int f = ALTO - 1; f >= 0; f--) {
            boolean llena = true;
            for (int c = 0; c < ANCHO; c++) {
                if (this.casillas[f][c] == '.') {
                    llena = false;
                }
            }
            if (llena) {
                //cada fila de arriba baja una posicion
                for (int k = f; k > 0; k--) {
                    for (int c = 0; c < ANCHO; c++) {
                        this.casillas[k][c] = this.casillas[k - 1][c];
                    }
                }
                for (int c = 0; c < ANCHO; c++) {
                    this.casillas[0][c] = '.';
                }
                borradas++;
                f++; //se revisa otra vez la misma fila porque bajo una nueva
            }
        }
        return borradas;
    }
}
