package snake;

import juegos.Jugable;
import juegos.Puntuacion;
import matriz.PartidaMatriz;

import javax.swing.JFrame;

/**
 * Snake en una matriz. Es Jugable: la consola lo lanza con start().
 * La serpiente son cuadrados verdes y las manzanas bolitas rojas.
 */
public class JuegoSnake implements Jugable {
    private final JFrame menu;

    public JuegoSnake(JFrame menu) {
        this.menu = menu;
    }

    @Override
    public String getNombre() {
        return "Snake";
    }

    @Override
    public Puntuacion start() {
        String instrucciones = "LA SERPIENTE AVANZA SOLA.\n"
                + "USA LAS FLECHAS O W A S D PARA GIRAR.\n"
                + "COME MANZANAS (BOLITAS ROJAS). META: " + PanelSnake.META_MANZANAS + " MANZANAS.\n"
                + "NO CHOQUES CON LAS PAREDES NI CONTIGO MISMA.\n"
                + "Q O ESC = RENDIRSE.";
        return PartidaMatriz.jugar(menu, getNombre(), instrucciones, PanelSnake::new);
    }
}