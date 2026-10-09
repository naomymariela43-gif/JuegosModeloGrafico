package cityblock;

import java.util.ArrayList;
import java.util.List;
import cityblock.comun.Jugador;

/** La torre de un jugador: pisos, vidas y balanceo. */
public class Torre {
    public enum Resultado { PERFECTO, BIEN, REGULAR, FALLO, INESTABLE }

    public static final int VIDAS = 3;
    public static final int LIMITE_BALANCEO = 10;
    public static final int PISOS_META = 10;

    private final Jugador jugador;
    private final List<Integer> centros = new ArrayList<>();
    private int vidas = VIDAS;
    private int balanceo = 0;

    public Torre(Jugador jugador) {
        this.jugador = jugador;
        centros.add(Grua.CENTRO); // base
    }

    public Jugador getJugador() { return jugador; }
    public int getPisos() { return centros.size() - 1; }
    public int getVidas() { return vidas; }
    public int getBalanceo() { return balanceo; }
    public boolean activa() { return vidas > 0 && getPisos() < PISOS_META; }
    private int ultimoCentro() { return centros.get(centros.size() - 1); }

    /** Intenta apilar un piso soltado en 'centro'. */
    public Resultado soltar(int centro) {
        int desvio = Math.abs(centro - ultimoCentro());
        if (desvio >= Bloque.ANCHO) {      // cae al vacio
            vidas--;
            return Resultado.FALLO;
        }
        centros.add(centro);
        if (desvio <= 1) {                 // casi alineado: la torre se estabiliza
            balanceo = Math.max(0, balanceo - 1);
            return Resultado.PERFECTO;
        }
        balanceo += desvio;                // mal alineado: la torre se balancea mas
        if (balanceo > LIMITE_BALANCEO) {  // se tambalea y pierde el ultimo piso
            centros.remove(centros.size() - 1);
            balanceo = 0;
            vidas--;
            return Resultado.INESTABLE;
        }
        return desvio <= 3 ? Resultado.BIEN : Resultado.REGULAR;
    }

    /** Dibuja los ultimos pisos de la torre. */
    public String dibujar() {
        StringBuilder sb = new StringBuilder();
        int desde = Math.max(0, centros.size() - 5);
        for (int i = centros.size() - 1; i >= desde; i--)
            sb.append(Bloque.linea(centros.get(i), Grua.CAMPO, i == 0 ? '#' : '=')).append("\n");
        return sb.toString();
    }
}
