package matriz;

/**
 * Las cuatro direcciones de movimiento. Se controlan con W A S D.
 */
public enum Direccion {
    ARRIBA(-1, 0),
    ABAJO(1, 0),
    IZQUIERDA(0, -1),
    DERECHA(0, 1);

    private final int df; // cambio en fila
    private final int dc; // cambio en columna

    Direccion(int df, int dc) {
        this.df = df;
        this.dc = dc;
    }

    public int getDf() {
        return df;
    }

    public int getDc() {
        return dc;
    }

    public Direccion opuesta() {
        switch (this) {
            case ARRIBA:    return ABAJO;
            case ABAJO:     return ARRIBA;
            case IZQUIERDA: return DERECHA;
            default:        return IZQUIERDA;
        }
    }

    /** Convierte una tecla (w, a, s, d) en dirección. Devuelve null si no es válida. */
    public static Direccion desdeTecla(char tecla) {
        switch (Character.toLowerCase(tecla)) {
            case 'w': return ARRIBA;
            case 's': return ABAJO;
            case 'a': return IZQUIERDA;
            case 'd': return DERECHA;
            default:  return null;
        }
    }
}
