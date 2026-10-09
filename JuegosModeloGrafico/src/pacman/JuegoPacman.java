package pacman;

import juegos.Estadisticas;
import juegos.Jugable;
import matriz.PartidaMatriz;

import javax.swing.JFrame;

/**
 * Pacman en una matriz. Es Jugable: la consola lo lanza con start().
 */
public class JuegoPacman implements Jugable {
    private final JFrame menu;
    private final Estadisticas estadisticas;

    public JuegoPacman(JFrame menu, Estadisticas estadisticas) {
        this.menu = menu;
        this.estadisticas = estadisticas;
    }

    @Override
    public String getNombre() {
        return "Pacman";
    }

    @Override
    public void start() {
        String instrucciones = "MUEVE A PACMAN CON LAS FLECHAS O W A S D.\n"
                + "COME TODOS LOS PUNTOS +.\n"
                + "AL COMER UN PODER * LOS FANTASMAS SE VUELVEN HUECOS,\n"
                + "SE MUEVEN LENTO Y PUEDES COMERTELOS.\n"
                + "TIENES " + PanelPacman.VIDAS + " VIDAS.  Q O ESC = RENDIRSE.";
        PartidaMatriz.jugar(menu, estadisticas, getNombre(), instrucciones, PanelPacman::new);
    }
}
