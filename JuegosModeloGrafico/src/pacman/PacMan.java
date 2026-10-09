package pacman;

import matriz.Direccion;
import matriz.Posicion;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * El "bicho" que controla el jugador: una bola amarilla que abre y cierra la boca.
 */
public class PacMan {
    public static final Color COLOR = new Color(255, 225, 0);

    private final Posicion inicio;
    private Posicion posicion;
    private Direccion direccion;          // hacia dónde va
    private Direccion direccionDeseada;   // la última tecla presionada
    private boolean bocaAbierta = true;
    private int vidas;

    public PacMan(Posicion inicio, int vidas) {
        this.inicio = inicio;
        this.posicion = inicio;
        this.vidas = vidas;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public int getVidas() {
        return vidas;
    }

    public void setDireccionDeseada(Direccion d) {
        direccionDeseada = d;
    }

    /**
     * Avanza una casilla. Si se puede girar hacia la dirección deseada, gira;
     * si no, sigue derecho. Devuelve false si está frente a una pared.
     */
    public boolean avanzar(TableroPacman tablero) {
        if (direccionDeseada != null && !tablero.esPared(posicion.mover(direccionDeseada))) {
            direccion = direccionDeseada;
        }
        if (direccion == null || tablero.esPared(posicion.mover(direccion))) {
            return false;
        }
        posicion = posicion.mover(direccion);
        bocaAbierta = !bocaAbierta;
        return true;
    }

    public void perderVida() {
        vidas--;
        posicion = inicio;
        direccion = null;
        direccionDeseada = null;
    }

    /** Dibuja la bola amarilla con la boca hacia donde se mueve. */
    public void dibujar(Graphics2D g, int celda) {
        int x = posicion.getColumna() * celda + 2;
        int y = posicion.getFila() * celda + 2;
        int tam = celda - 4;
        g.setColor(COLOR);
        if (!bocaAbierta || direccion == null) {
            g.fillOval(x, y, tam, tam);
            return;
        }
        int inicioAngulo;
        switch (direccion) {
            case ARRIBA:    inicioAngulo = 120; break;
            case IZQUIERDA: inicioAngulo = 210; break;
            case ABAJO:     inicioAngulo = 300; break;
            default:        inicioAngulo = 30;  break;
        }
        g.fillArc(x, y, tam, tam, inicioAngulo, 300);
    }
}
