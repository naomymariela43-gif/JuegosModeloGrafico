package pacman;

import matriz.Direccion;
import matriz.Posicion;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Un fantasma cuadrado de color (azul, verde o rosa). Persigue a Pacman.
 * Con el poder (*) se ve hueco y se mueve lento.
 */
public class Fantasma {
    private static final Random RANDOM = new Random();

    private final Posicion inicio;
    private final Color color;
    private Posicion posicion;
    private Direccion direccion = Direccion.ARRIBA;

    public Fantasma(Posicion inicio, Color color) {
        this.inicio = inicio;
        this.color = color;
        this.posicion = inicio;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void volverAlInicio() {
        posicion = inicio;
        direccion = Direccion.ARRIBA;
    }

    /**
     * Da un paso. Normalmente persigue a Pacman; a veces se mueve al azar
     * para que el juego no sea imposible. Evita devolverse si puede.
     */
    public void mover(TableroPacman tablero, Posicion objetivo) {
        List<Direccion> opciones = new ArrayList<>();
        for (Direccion d : Direccion.values()) {
            if (!tablero.esPared(posicion.mover(d)) && d != direccion.opuesta()) {
                opciones.add(d);
            }
        }
        if (opciones.isEmpty()) {           // callejón sin salida: se devuelve
            opciones.add(direccion.opuesta());
        }

        Direccion elegida;
        if (RANDOM.nextInt(100) < 60) {     // 60%: persigue
            elegida = opciones.get(0);
            for (Direccion d : opciones) {
                if (posicion.mover(d).distancia(objetivo) < posicion.mover(elegida).distancia(objetivo)) {
                    elegida = d;
                }
            }
        } else {                            // 40%: al azar
            elegida = opciones.get(RANDOM.nextInt(opciones.size()));
        }

        direccion = elegida;
        posicion = posicion.mover(elegida);
    }

    /**
     * Dibuja el cuadrado. Normal: relleno de su color con ojos.
     * Asustado (poder activo): cuadrado hueco que parpadea al final del poder.
     */
    public void dibujar(Graphics2D g, int celda, boolean asustado, boolean parpadeo) {
        int x = posicion.getColumna() * celda + 3;
        int y = posicion.getFila() * celda + 3;
        int tam = celda - 6;

        if (asustado) {
            Color borde = parpadeo ? Color.WHITE : color;
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 70));
            g.fillRect(x, y, tam, tam);
            g.setColor(borde);
            g.setStroke(new java.awt.BasicStroke(2.5f));
            g.drawRect(x, y, tam, tam);
            g.setStroke(new java.awt.BasicStroke(1f));
            return;
        }

        g.setColor(color);
        g.fillRect(x, y, tam, tam);
        // ojos que miran hacia donde va
        int ojo = tam / 4;
        int mx = direccion.getDc() * 2;
        int my = direccion.getDf() * 2;
        g.setColor(Color.WHITE);
        g.fillRect(x + tam / 5, y + tam / 4, ojo, ojo);
        g.fillRect(x + tam - tam / 5 - ojo, y + tam / 4, ojo, ojo);
        g.setColor(Color.BLACK);
        g.fillRect(x + tam / 5 + ojo / 4 + mx, y + tam / 4 + ojo / 4 + my, ojo / 2, ojo / 2);
        g.fillRect(x + tam - tam / 5 - ojo + ojo / 4 + mx, y + tam / 4 + ojo / 4 + my, ojo / 2, ojo / 2);
    }
}
