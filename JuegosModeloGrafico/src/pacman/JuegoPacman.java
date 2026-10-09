package pacman;

import juegos.Jugable;
import juegos.Puntuacion;
import matriz.PartidaMatriz;

import javax.swing.JFrame;

/**
 * Pacman en una matriz. Es Jugable: la consola lo lanza con start().
 */
public class JuegoPacman implements Jugable {
    private final JFrame menu;

    public JuegoPacman(JFrame menu) {
        this.menu = menu;
    }

    @Override
    public String getNombre() {
        return "Pacman";
    }

    @Override
    public Puntuacion start() {
        String instrucciones = "MUEVE A PACMAN CON LAS FLECHAS O W A S D.\n"
                + "COME TODOS LOS PUNTOS +.\n"
                + "AL COMER UN PODER * LOS FANTASMAS SE VUELVEN HUECOS,\n"
                + "SE MUEVEN LENTO Y PUEDES COMERTELOS.\n"
                + "TIENES " + PanelPacman.VIDAS + " VIDAS.  Q O ESC = RENDIRSE.";
        return PartidaMatriz.jugar(menu, getNombre(), instrucciones, PanelPacman::new);
    }
}