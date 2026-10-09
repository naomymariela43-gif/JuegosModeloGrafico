package snake;

import matriz.Posicion;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Una parte (cuadrado verde) del cuerpo de la serpiente.
 */
public class Segmento {
    public static final Color COLOR_CUERPO = new Color(40, 190, 70);
    public static final Color COLOR_CABEZA = new Color(110, 255, 120);

    private final Posicion posicion;

    public Segmento(Posicion posicion) {
        this.posicion = posicion;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    /** Dibuja el cuadrado en su casilla de la matriz. */
    public void dibujar(Graphics2D g, int celda, boolean esCabeza) {
        int x = posicion.getColumna() * celda;
        int y = posicion.getFila() * celda;
        g.setColor(esCabeza ? COLOR_CABEZA : COLOR_CUERPO);
        g.fillRect(x + 2, y + 2, celda - 4, celda - 4);
        g.setColor(new Color(20, 110, 40));
        g.drawRect(x + 2, y + 2, celda - 5, celda - 5);
    }
}
