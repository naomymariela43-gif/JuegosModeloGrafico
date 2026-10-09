package pacman;

import matriz.Direccion;
import juegos.Jugador;
import matriz.PanelJuego;
import matriz.Posicion;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * La ventana de Pacman: Pacman avanza solo en la dirección elegida.
 * Los fantasmas lo persiguen; con el poder (*) se vuelven lentos y se pueden comer.
 */
public class PanelPacman extends PanelJuego {
    static final int VIDAS = 3;
    private static final int PUNTOS_PUNTO = 10;
    private static final int PUNTOS_PODER = 50;
    private static final int PUNTOS_FANTASMA = 100;
    private static final int VELOCIDAD_MS = 160;    // un paso de Pacman cada 160 ms
    private static final int DURACION_PODER = 45;   // pasos que dura el poder (~7 segundos)
    private static final int PAUSA_AL_MORIR = 8;    // pasos de pausa después de perder una vida

    /** Colores de los fantasmas: azul, verde y rosa. */
    private static final Color[] COLORES_FANTASMAS = {
            new Color(30, 110, 255),
            new Color(0, 200, 90),
            new Color(255, 105, 180)
    };

    private final TableroPacman tablero;
    private final PacMan pacman;
    private final List<Fantasma> fantasmas = new ArrayList<>();

    private int paso;          // contador de pasos del Timer
    private int pasosPoder;    // cuánto le queda al poder
    private int pausa;         // pausa después de perder una vida

    public PanelPacman(Jugador jugador) {
        this(jugador, new TableroPacman());
    }

    private PanelPacman(Jugador jugador, TableroPacman tablero) {
        super(jugador, tablero.getFilas(), tablero.getColumnas(), VELOCIDAD_MS);
        this.tablero = tablero;
        this.pacman = new PacMan(tablero.getInicioPacman(), VIDAS);
        List<Posicion> inicios = tablero.getIniciosFantasmas();
        for (int i = 0; i < inicios.size(); i++) {
            fantasmas.add(new Fantasma(inicios.get(i), COLORES_FANTASMAS[i % COLORES_FANTASMAS.length]));
        }
    }

    @Override
    protected void cambiarDireccion(Direccion d) {
        pacman.setDireccionDeseada(d);
    }

    @Override
    protected void paso() {
        paso++;
        if (pausa > 0) {
            pausa--;
            return;
        }

        // 1) Pacman avanza y come
        if (pacman.avanzar(tablero)) {
            movimientos++;
            char comida = tablero.comer(pacman.getPosicion());
            if (comida == TableroPacman.PUNTO) {
                puntos += PUNTOS_PUNTO;
            } else if (comida == TableroPacman.PODER) {
                puntos += PUNTOS_PODER;
                pasosPoder = DURACION_PODER;
            }
        }
        if (revisarChoques()) return;

        // 2) Los fantasmas se mueven: normal 3 de cada 4 pasos, con poder solo 1 de cada 3 (lentos)
        boolean lentos = pasosPoder > 0;
        boolean seMueven = lentos ? paso % 3 == 0 : paso % 4 != 0;
        if (seMueven) {
            for (Fantasma g : fantasmas) {
                g.mover(tablero, pacman.getPosicion());
            }
            if (revisarChoques()) return;
        }

        if (pasosPoder > 0) pasosPoder--;

        // 3) ¿Se comió todo?
        if (tablero.comidaRestante() == 0) {
            terminar("Completó", "¡Te comiste todo el laberinto!");
        }
    }

    /** Revisa si Pacman toca un fantasma. Devuelve true si Pacman perdió una vida. */
    private boolean revisarChoques() {
        for (Fantasma g : fantasmas) {
            if (g.getPosicion().equals(pacman.getPosicion())) {
                if (pasosPoder > 0) {
                    puntos += PUNTOS_FANTASMA;   // se lo comió
                    g.volverAlInicio();
                } else {
                    perderVida();
                    return true;
                }
            }
        }
        return false;
    }

    private void perderVida() {
        pacman.perderVida();
        for (Fantasma g : fantasmas) {
            g.volverAlInicio();
        }
        pasosPoder = 0;
        if (pacman.getVidas() <= 0) {
            terminar("Perdió", "¡Te atraparon los fantasmas!");
        } else {
            pausa = PAUSA_AL_MORIR;
        }
    }

    @Override
    protected void dibujarJuego(Graphics2D g) {
        tablero.dibujar(g, CELDA, paso);
        boolean asustados = pasosPoder > 0;
        boolean parpadeo = asustados && pasosPoder < 12 && paso % 2 == 0;  // avisa que se acaba
        for (Fantasma f : fantasmas) {
            f.dibujar(g, CELDA, asustados, parpadeo);
        }
        if (pausa == 0 || paso % 2 == 0) {   // parpadea después de perder una vida
            pacman.dibujar(g, CELDA);
        }
    }

    @Override
    protected String infoExtra() {
        String vidas = "Vidas: " + "● ".repeat(Math.max(0, pacman.getVidas()));
        String poder = pasosPoder > 0 ? "    ¡PODER! fantasmas lentos" : "";
        return vidas + "   Comida: " + tablero.comidaRestante() + poder;
    }
}
