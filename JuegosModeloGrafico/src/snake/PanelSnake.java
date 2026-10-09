package snake;

import matriz.Direccion;
import juegos.Jugador;
import matriz.PanelJuego;
import matriz.Posicion;

import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * La ventana del Snake: la serpiente avanza sola y el jugador solo cambia la dirección.
 */
public class PanelSnake extends PanelJuego {
    static final int FILAS = 16;
    static final int COLUMNAS = 24;
    static final int PUNTOS_POR_MANZANA = 10;
    static final int META_MANZANAS = 15;
    private static final int VELOCIDAD_MS = 250;   // milisegundos por paso: menos = más rápido, más = más lento

    private final TableroSnake tablero = new TableroSnake(FILAS, COLUMNAS);
    private final Serpiente serpiente = new Serpiente(new Posicion(FILAS / 2, 6), 3);
    private final Manzana manzana = new Manzana();
    private final Deque<Direccion> teclasPendientes = new ArrayDeque<>();
    private int manzanas;

    public PanelSnake(Jugador jugador) {
        super(jugador, FILAS, COLUMNAS, VELOCIDAD_MS);
        manzana.colocar(tablero, serpiente);
    }

    @Override
    protected void cambiarDireccion(Direccion d) {
        // Se guardan hasta 2 teclas para que los giros rápidos no se pierdan
        if (teclasPendientes.size() < 2) {
            teclasPendientes.add(d);
        }
    }

    @Override
    protected void paso() {
        if (!teclasPendientes.isEmpty()) {
            serpiente.cambiarDireccion(teclasPendientes.poll());
        }

        Posicion siguiente = serpiente.siguientePosicion();
        boolean come = siguiente.equals(manzana.getPosicion());

        if (tablero.esPared(siguiente) || serpiente.choca(siguiente, come)) {
            terminar("Perdió", "¡Chocaste!");
            return;
        }

        serpiente.avanzar(siguiente, come);
        movimientos++;

        if (come) {
            manzanas++;
            puntos += PUNTOS_POR_MANZANA;
            if (manzanas >= META_MANZANAS || !manzana.colocar(tablero, serpiente)) {
                terminar("Completó", "¡Llegaste a la meta!");
            }
        }
    }

    @Override
    protected void dibujarJuego(Graphics2D g) {
        tablero.dibujar(g, CELDA);
        manzana.dibujar(g, CELDA);
        serpiente.dibujar(g, CELDA);
    }

    @Override
    protected String infoExtra() {
        return "Manzanas: " + manzanas + "/" + META_MANZANAS + "    Largo: " + serpiente.getLargo()
                + "    Flechas o WASD para girar";
    }
}