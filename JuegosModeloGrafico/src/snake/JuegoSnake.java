package snake;

import juegos.Estadisticas;
import juegos.Jugable;
import matriz.PartidaMatriz;

import javax.swing.JFrame;

/**
 * Snake en una matriz. Es Jugable: la consola lo lanza con start().
 * La serpiente son cuadrados verdes y las manzanas bolitas rojas.
 */
public class JuegoSnake implements Jugable {
    private final JFrame menu;
    private final Estadisticas estadisticas;

    public JuegoSnake(JFrame menu, Estadisticas estadisticas) {
        this.menu = menu;
        this.estadisticas = estadisticas;
    }

    @Override
    public String getNombre() {
        return "Snake";
    }

    @Override
    public void start() {
        String instrucciones = "LA SERPIENTE AVANZA SOLA.\n"
                + "USA LAS FLECHAS O W A S D PARA GIRAR.\n"
                + "COME MANZANAS (BOLITAS ROJAS). META: " + PanelSnake.META_MANZANAS + " MANZANAS.\n"
                + "NO CHOQUES CON LAS PAREDES NI CONTIGO MISMA.\n"
                + "Q O ESC = RENDIRSE.";
        PartidaMatriz.jugar(menu, estadisticas, getNombre(), instrucciones, PanelSnake::new);
    }
}
