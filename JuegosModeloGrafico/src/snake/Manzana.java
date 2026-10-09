package snake;

import matriz.Posicion;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * La manzana: una bolita roja que come la serpiente.
 */
public class Manzana {
    public static final Color COLOR = new Color(230, 30, 40);

    private Posicion posicion;
    private final Random random = new Random();

    public Posicion getPosicion() {
        return posicion;
    }

    /** Coloca la manzana en una casilla libre al azar. Devuelve false si no hay espacio. */
    public boolean colocar(TableroSnake tablero, Serpiente serpiente) {
        List<Posicion> libres = new ArrayList<>();
        for (int f = 0; f < tablero.getFilas(); f++) {
            for (int c = 0; c < tablero.getColumnas(); c++) {
                Posicion p = new Posicion(f, c);
                if (!tablero.esPared(p) && !serpiente.ocupa(p)) {
                    libres.add(p);
                }
            }
        }
        if (libres.isEmpty()) {
            return false;
        }
        posicion = libres.get(random.nextInt(libres.size()));
        return true;
    }

    /** Dibuja la bolita roja con un brillo. */
    public void dibujar(Graphics2D g, int celda) {
        int x = posicion.getColumna() * celda;
        int y = posicion.getFila() * celda;
        g.setColor(COLOR);
        g.fillOval(x + 4, y + 4, celda - 8, celda - 8);
        g.setColor(new Color(255, 160, 160));
        g.fillOval(x + celda / 3, y + celda / 3 - 2, celda / 6, celda / 6);
    }
}
