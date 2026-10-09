package snake;

import matriz.Posicion;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * La matriz del Snake: bordes de pared (#) y espacio libre adentro.
 */
public class TableroSnake {
    public static final char PARED = '#';
    public static final char VACIO = ' ';

    private static final Color COLOR_PARED = new Color(90, 90, 110);
    private static final Color COLOR_CUADRICULA = new Color(22, 22, 30);

    private final char[][] matriz;
    private final int filas;
    private final int columnas;

    public TableroSnake(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        matriz = new char[filas][columnas];
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                boolean borde = f == 0 || c == 0 || f == filas - 1 || c == columnas - 1;
                matriz[f][c] = borde ? PARED : VACIO;
            }
        }
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public boolean esPared(Posicion p) {
        return matriz[p.getFila()][p.getColumna()] == PARED;
    }

    /** Dibuja la matriz: paredes grises y una cuadrícula suave. */
    public void dibujar(Graphics2D g, int celda) {
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                int x = c * celda;
                int y = f * celda;
                if (matriz[f][c] == PARED) {
                    g.setColor(COLOR_PARED);
                    g.fillRect(x + 1, y + 1, celda - 2, celda - 2);
                } else {
                    g.setColor(COLOR_CUADRICULA);
                    g.drawRect(x, y, celda, celda);
                }
            }
        }
    }
}
