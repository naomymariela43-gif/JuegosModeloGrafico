package cityblock;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.Scanner;
import cityblock.comun.Colores;
import cityblock.comun.Estadistica;
import cityblock.comun.Juego;
import juegos.Estadisticas;
import juegos.Jugable;
import cityblock.comun.Jugador;

/** Juego 2: City Block (estilo Tower Bloxx) por turnos. */
public class JuegoCityBlock extends Juego implements Jugable {

    private final Estadisticas estadisticas;
    private boolean enCurso = false; //evita abrir dos partidas a la vez

    public JuegoCityBlock(Estadisticas estadisticas) {
        super("City Block", new Scanner(System.in));
        this.estadisticas = estadisticas;
    }

    // =================== JUEGO: CITY BLOCK ===================
    //Este juego se juega por TEXTO (Scanner). Como el menu es grafico, start() corre la partida
    //en un hilo aparte para no congelar la ventana, y se juega en la consola de IntelliJ (pestana Run).
    @Override
    public void start() {
        if (enCurso) {
            JOptionPane.showMessageDialog(null, "CITY BLOCK YA ESTA EN CURSO.\nREVISA LA CONSOLA (PESTANA RUN).",
                    "City Block", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(null, "CITY BLOCK SE JUEGA EN LA CONSOLA DE TEXTO.\n"
                + "ABRE LA PESTANA RUN DE INTELLIJ Y SIGUE LAS INSTRUCCIONES.",
                "City Block", JOptionPane.PLAIN_MESSAGE);
        enCurso = true;

        Thread hilo = new Thread(() -> {
            try {
                System.out.println(Colores.AZUL + ">>> CITY BLOCK <<<" + Colores.RESET);
                correrPartida();
                if (getUltimaEstadistica() != null) {
                    String resumen = "Juego: City Block\n" + getUltimaEstadistica().toString() + "\n";
                    SwingUtilities.invokeLater(() -> estadisticas.agregarPartida(resumen));
                }
            } finally {
                enCurso = false;
            }
        });
        hilo.start();
    }
    // =========================================================

    @Override
    protected void mostrarReglas() {
        System.out.println("La grua balancea un piso. Presiona ENTER para soltarlo sobre la torre.");
        System.out.println("Mejor alineado = mas puntos y torre mas estable. Mal alineado = la torre");
        System.out.println("se balancea; si pasa del limite pierdes el piso y una vida.");
        System.out.println("Si el piso cae al vacio tambien pierdes una vida. Tienes " + Torre.VIDAS + " vidas.");
        System.out.println("Meta: " + Torre.PISOS_META + " pisos. Gana quien tenga mas puntos.");
        System.out.println("(Usa una terminal real para ver la animacion.)");
    }

    @Override
    protected Estadistica jugarPartida() {
        Estadistica est = new Estadistica(getNombre(), jugadores);
        List<Torre> torres = new ArrayList<>();
        for (Jugador j : jugadores) torres.add(new Torre(j));

        boolean quedan = true;
        while (quedan) {
            quedan = false;
            for (Torre torre : torres) {
                if (!torre.activa()) continue;
                quedan = true;
                turno(torre);
            }
        }

        System.out.println("\n=== Resultado final ===");
        for (Torre t : torres)
            System.out.println(t.getJugador() + ": " + t.getPisos() + " pisos, "
                    + t.getJugador().getPuntos() + " pts, vidas " + t.getVidas());
        est.terminar(mejorPuntaje());
        return est;
    }

    private void turno(Torre torre) {
        Jugador actual = torre.getJugador();
        System.out.println("\n--- Turno de " + actual + " | Pisos: " + torre.getPisos()
                + " | Vidas: " + torre.getVidas()
                + " | Balanceo: " + torre.getBalanceo() + "/" + Torre.LIMITE_BALANCEO
                + " | Puntos: " + actual.getPuntos() + " ---");
        System.out.println(torre.dibujar());
        System.out.print("Listo? Presiona ENTER para que la grua empiece...");
        entrada.nextLine();

        // la grua se hace mas rapida conforme crece la torre
        long periodo = Math.max(2200, 4200 - 200L * torre.getPisos());
        Grua grua = new Grua(periodo);
        int posicion = grua.soltar(entrada);

        actual.registrarMovimiento();
        switch (torre.soltar(posicion)) {
            case PERFECTO:
                actual.sumarPuntos(15);
                System.out.println("PERFECTO! +15 pts");
                break;
            case BIEN:
                actual.sumarPuntos(13);
                System.out.println("Bien alineado. +13 pts");
                break;
            case REGULAR:
                actual.sumarPuntos(10);
                System.out.println("Mal alineado, la torre se balancea. +10 pts");
                break;
            case INESTABLE:
                System.out.println("La torre se tambalea y pierdes el piso! -1 vida");
                break;
            default:
                System.out.println("El piso cayo al vacio! -1 vida");
        }
    }
}
