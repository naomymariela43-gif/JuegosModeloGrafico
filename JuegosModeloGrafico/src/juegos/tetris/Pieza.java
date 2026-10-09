package juegos.tetris;

import java.util.Random;

public class Pieza {
    private String nombre;
    private int[][] forma; // 1 = bloque, 0 = vacio

    private static final Random random = new Random();

    public Pieza(String nombre, int[][] forma) {
        this.nombre = nombre;
        this.forma = forma;
    }

    //devuelve una de las 7 piezas clasicas al azar
    public static Pieza crearAleatoria() {
        int opcion = random.nextInt(7);
        if (opcion == 0) return new Pieza("I", new int[][]{{1, 1, 1, 1}});
        if (opcion == 1) return new Pieza("O", new int[][]{{1, 1}, {1, 1}});
        if (opcion == 2) return new Pieza("T", new int[][]{{1, 1, 1}, {0, 1, 0}});
        if (opcion == 3) return new Pieza("S", new int[][]{{0, 1, 1}, {1, 1, 0}});
        if (opcion == 4) return new Pieza("Z", new int[][]{{1, 1, 0}, {0, 1, 1}});
        if (opcion == 5) return new Pieza("J", new int[][]{{1, 0, 0}, {1, 1, 1}});
        return new Pieza("L", new int[][]{{0, 0, 1}, {1, 1, 1}});
    }

    //gira la pieza 90 grados a la derecha
    public void rotar() {
        int filas = this.forma.length;
        int columnas = this.forma[0].length;
        int[][] nueva = new int[columnas][filas];
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                nueva[c][filas - 1 - f] = this.forma[f][c];
            }
        }
        this.forma = nueva;
    }

    public String getNombre() {
        return nombre;
    }

    public int[][] getForma() {
        return forma;
    }

    public int getAlto() {
        return forma.length;
    }

    public int getAncho() {
        return forma[0].length;
    }

    //dice si la pieza tiene un bloque en esa posicion (sirve para dibujarla)
    public boolean tieneBloque(int fila, int columna) {
        if (fila < 0 || fila >= getAlto() || columna < 0 || columna >= getAncho()) {
            return false;
        }
        return this.forma[fila][columna] == 1;
    }
}
