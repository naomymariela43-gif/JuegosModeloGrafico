package cityblock;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import cityblock.comun.Colores;
import cityblock.comun.Estadistica;
import cityblock.comun.Juego;
import cityblock.comun.Jugador;
import juegos.Jugable;
import juegos.Puntuacion;
import juegos.estilo.EstiloArcade;

/** Juego 2: City Block (estilo Tower Bloxx) por turnos. */
public class JuegoCityBlock extends Juego implements Jugable {

    private boolean termino; //true solo si la partida llego hasta el final

    public JuegoCityBlock() {
        super("City Block", new Scanner(System.in));
    }

    // =================== JUEGO: CITY BLOCK ===================
    //Este juego se juega por TEXTO (Scanner) en la consola de IntelliJ (pestana Run).
    //La partida corre en un hilo aparte y mientras tanto se muestra una ventanita MODAL:
    //start() se queda esperando ahi (sin congelar las ventanas) hasta que la partida termina,
    //y despues DEVUELVE la puntuacion.
    @Override
    public Puntuacion start() {
        JOptionPane.showMessageDialog(null, "CITY BLOCK SE JUEGA EN LA CONSOLA DE TEXTO.\n"
                        + "ABRE LA PESTANA RUN DE INTELLIJ Y SIGUE LAS INSTRUCCIONES.",
                "City Block", JOptionPane.PLAIN_MESSAGE);

        //ventanita de espera (no se puede cerrar con la X: se cierra sola al terminar la partida)
        JDialog espera = new JDialog((Frame) null, "City Block", true);
        espera.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        JLabel texto = new JLabel("CITY BLOCK EN CURSO... JUEGA EN LA PESTANA RUN", SwingConstants.CENTER);
        texto.setFont(EstiloArcade.fuente(16));
        texto.setForeground(juegos.estilo.Colores.VERDE);
        texto.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        espera.add(texto);
        espera.pack();
        espera.setLocationRelativeTo(null);

        termino = false;
        Thread hilo = new Thread(() -> {
            try {
                System.out.println(Colores.AZUL + ">>> CITY BLOCK <<<" + Colores.RESET);
                correrPartida();
                termino = true;
            } finally {
                SwingUtilities.invokeLater(espera::dispose); //cierra la ventanita y start() sigue
            }
        });
        hilo.start();

        espera.setVisible(true); //como es modal, aqui se espera hasta que termine la partida

        if (!termino) {
            return null; //no se termino la partida: no hay puntuacion
        }
        return crearPuntuacion();
    }

    //la puntuacion del jugador con mas puntos (si empatan, la del primero)
    private Puntuacion crearPuntuacion() {
        Jugador mejor = jugadores.get(0);
        for (Jugador j : jugadores) {
            if (j.getPuntos() > mejor.getPuntos()) {
                mejor = j;
            }
        }
        return new Puntuacion(getNombre(), mejor.getNombre(), mejor.getPuntos());
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