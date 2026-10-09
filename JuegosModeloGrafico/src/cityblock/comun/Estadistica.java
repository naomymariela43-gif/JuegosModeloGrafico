package cityblock.comun;

import java.util.List;

/** Guarda tiempo, movimientos y ganador de una partida. */
public class Estadistica {
    private final String juego;
    private final List<Jugador> jugadores;
    private final long inicio;
    private long fin;
    private Jugador ganador;

    public Estadistica(String juego, List<Jugador> jugadores) {
        this.juego = juego;
        this.jugadores = jugadores;
        this.inicio = System.currentTimeMillis();
    }

    public void terminar(Jugador ganador) {
        this.fin = System.currentTimeMillis();
        this.ganador = ganador;
    }

    public long getSegundos() { return (fin - inicio) / 1000; }

    public int getTotalMovimientos() {
        int total = 0;
        for (Jugador j : jugadores) total += j.getMovimientos();
        return total;
    }

    public void imprimir() {
        System.out.println("\n===== ESTADISTICAS: " + juego + " =====");
        System.out.println("Tiempo total : " + getSegundos() + " segundos");
        System.out.println("Movimientos  : " + getTotalMovimientos());
        for (Jugador j : jugadores) {
            System.out.println("  - " + j.getNombre() + ": " + j.getMovimientos()
                    + " movimientos, " + j.getPuntos() + " puntos");
        }
        System.out.println("GANADOR      : " + (ganador == null ? "Empate (sin ganador)" : ganador.getNombre()));
        System.out.println("==========================================\n");
    }

    @Override
    public String toString() {
        return juego + " | ganador: " + (ganador == null ? "Empate (sin ganador)" : ganador.getNombre())
                + " | " + getSegundos() + "s | " + getTotalMovimientos() + " movimientos";
    }
}
