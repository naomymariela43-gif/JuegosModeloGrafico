package pacman;

import matriz.Posicion;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * La matriz del laberinto de Pacman.
 *   #  pared (roja)
 *   +  punto
 *   *  poder (hace que los fantasmas vayan lentos)
 *   P  inicio de Pacman      G  inicio de los fantasmas
 */
public class TableroPacman {
    public static final char PARED = '#';
    public static final char PUNTO = '+';
    public static final char PODER = '*';
    public static final char VACIO = ' ';

    private static final Color COLOR_PARED = new Color(200, 25, 30);
    private static final Color COLOR_PARED_BORDE = new Color(255, 90, 90);
    private static final Color COLOR_PUNTO = new Color(245, 235, 220);
    private static final Color COLOR_PODER = Color.WHITE;

    private static final String[] MAPA = {
            "###################",
            "#*+++++++#+++++++*#",
            "#+##+###+#+###+##+#",
            "#+++++++++++++++++#",
            "#+##+#+#####+#+##+#",
            "#++++#+++#+++#++++#",
            "####+###+#+###+####",
            "#+++++++GGG+++++++#",
            "####+#+#####+#+####",
            "#++++#+++++++#++++#",
            "#+##+###+#+###+##+#",
            "#*+++++++P+++++++*#",
            "###################"
    };

    private final char[][] matriz;
    private Posicion inicioPacman;
    private final List<Posicion> iniciosFantasmas = new ArrayList<>();

    public TableroPacman() {
        matriz = new char[MAPA.length][];
        for (int f = 0; f < MAPA.length; f++) {
            matriz[f] = MAPA[f].toCharArray();
            for (int c = 0; c < matriz[f].length; c++) {
                if (matriz[f][c] == 'P') {
                    inicioPacman = new Posicion(f, c);
                    matriz[f][c] = VACIO;
                } else if (matriz[f][c] == 'G') {
                    iniciosFantasmas.add(new Posicion(f, c));
                    matriz[f][c] = VACIO;
                }
            }
        }
    }

    public int getFilas() {
        return matriz.length;
    }

    public int getColumnas() {
        return matriz[0].length;
    }

    public Posicion getInicioPacman() {
        return inicioPacman;
    }

    public List<Posicion> getIniciosFantasmas() {
        return iniciosFantasmas;
    }

    public boolean esPared(Posicion p) {
        return matriz[p.getFila()][p.getColumna()] == PARED;
    }

    /** Pacman come lo que haya en la casilla. Devuelve lo que había. */
    public char comer(Posicion p) {
        char antes = matriz[p.getFila()][p.getColumna()];
        if (antes == PUNTO || antes == PODER) {
            matriz[p.getFila()][p.getColumna()] = VACIO;
        }
        return antes;
    }

    public int comidaRestante() {
        int total = 0;
        for (char[] fila : matriz) {
            for (char c : fila) {
                if (c == PUNTO || c == PODER) total++;
            }
        }
        return total;
    }

    /** Dibuja paredes rojas, los puntos + y los poderes *. */
    public void dibujar(Graphics2D g, int celda, int paso) {
        Font fuentePunto = new Font(Font.MONOSPACED, Font.BOLD, celda / 2);
        Font fuentePoder = new Font(Font.MONOSPACED, Font.BOLD, paso % 6 < 3 ? celda : celda - 6); // late
        for (int f = 0; f < matriz.length; f++) {
            for (int c = 0; c < matriz[f].length; c++) {
                int x = c * celda;
                int y = f * celda;
                switch (matriz[f][c]) {
                    case PARED:
                        g.setColor(COLOR_PARED);
                        g.fillRoundRect(x + 1, y + 1, celda - 2, celda - 2, 8, 8);
                        g.setColor(COLOR_PARED_BORDE);
                        g.drawRoundRect(x + 1, y + 1, celda - 3, celda - 3, 8, 8);
                        break;
                    case PUNTO:
                        g.setColor(COLOR_PUNTO);
                        dibujarCentrado(g, "+", fuentePunto, x, y, celda);
                        break;
                    case PODER:
                        g.setColor(COLOR_PODER);
                        dibujarCentrado(g, "*", fuentePoder, x, y, celda);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static void dibujarCentrado(Graphics2D g, String texto, Font fuente, int x, int y, int celda) {
        g.setFont(fuente);
        FontMetrics fm = g.getFontMetrics();
        int tx = x + (celda - fm.stringWidth(texto)) / 2;
        int ty = y + (celda - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(texto, tx, ty);
    }
}
