package snake;

import matriz.Direccion;
import matriz.Posicion;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

/**
 * La serpiente: una lista de segmentos (cuadrados). El primero es la cabeza.
 */
public class Serpiente {
    private final LinkedList<Segmento> cuerpo = new LinkedList<>();
    private Direccion direccion = Direccion.DERECHA;

    /** Crea la serpiente con la cabeza en 'inicio' y el cuerpo hacia la izquierda. */
    public Serpiente(Posicion inicio, int largoInicial) {
        for (int i = 0; i < largoInicial; i++) {
            cuerpo.add(new Segmento(new Posicion(inicio.getFila(), inicio.getColumna() - i)));
        }
    }

    public Posicion getCabeza() {
        return cuerpo.getFirst().getPosicion();
    }

    public int getLargo() {
        return cuerpo.size();
    }

    public Direccion getDireccion() {
        return direccion;
    }

    /** Cambia la dirección, pero no permite darse vuelta sobre sí misma. */
    public void cambiarDireccion(Direccion nueva) {
        if (nueva != direccion.opuesta()) {
            direccion = nueva;
        }
    }

    public Posicion siguientePosicion() {
        return getCabeza().mover(direccion);
    }

    /**
     * ¿La posición choca con el cuerpo? Si la serpiente no va a crecer,
     * la cola se mueve, así que esa casilla no cuenta.
     */
    public boolean choca(Posicion p, boolean vaACrecer) {
        int limite = vaACrecer ? cuerpo.size() : cuerpo.size() - 1;
        for (int i = 0; i < limite; i++) {
            if (cuerpo.get(i).getPosicion().equals(p)) {
                return true;
            }
        }
        return false;
    }

    public boolean ocupa(Posicion p) {
        return choca(p, true);
    }

    /** Mueve la serpiente un paso. Si crece, no se quita la cola. */
    public void avanzar(Posicion nuevaCabeza, boolean crecer) {
        cuerpo.addFirst(new Segmento(nuevaCabeza));
        if (!crecer) {
            cuerpo.removeLast();
        }
    }

    /** Dibuja todos los cuadrados verdes; la cabeza lleva ojos. */
    public void dibujar(Graphics2D g, int celda) {
        for (int i = cuerpo.size() - 1; i >= 0; i--) {
            cuerpo.get(i).dibujar(g, celda, i == 0);
        }
        // ojos de la cabeza
        Posicion c = getCabeza();
        int cx = c.getColumna() * celda + celda / 2;
        int cy = c.getFila() * celda + celda / 2;
        int ox = direccion.getDc() * celda / 5;
        int oy = direccion.getDf() * celda / 5;
        int sx = direccion.getDf() != 0 ? celda / 5 : 0;  // separación entre ojos
        int sy = direccion.getDc() != 0 ? celda / 5 : 0;
        g.setColor(Color.BLACK);
        g.fillOval(cx + ox - sx - 3, cy + oy - sy - 3, 6, 6);
        g.fillOval(cx + ox + sx - 3, cy + oy + sy - 3, 6, 6);
    }
}
