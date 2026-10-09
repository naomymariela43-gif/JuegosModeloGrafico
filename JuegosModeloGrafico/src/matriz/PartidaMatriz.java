package matriz;

import juegos.Jugador;
import juegos.Puntuacion;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.function.Function;

//Ayudante para los juegos de matriz (Snake y Pacman) dentro de la consola grafica.
//Hace lo que antes hacian Juego.start() + VentanaJuego pero con ventanitas Swing en vez de Scanner:
//  1) pregunta cuantos jugadores y sus nombres
//  2) cada jugador juega su turno en una ventana (modal: espera a que se cierre)
//  3) elige al ganador, muestra el resumen y DEVUELVE la Puntuacion del ganador
//IMPORTANTE: se llama desde el boton del menu, o sea YA estamos en el hilo de Swing (EDT),
//por eso aqui NO se usa invokeAndWait (daria error estando en el EDT).
public final class PartidaMatriz {

    private PartidaMatriz() {
    }

    public static Puntuacion jugar(JFrame menu, String nombreJuego,
                                   String instrucciones, Function<Jugador, PanelJuego> crearPanel) {
        JOptionPane.showMessageDialog(menu, instrucciones, nombreJuego, JOptionPane.PLAIN_MESSAGE);

        String[] cantidades = {"1", "2", "3", "4"};
        int eleccion = JOptionPane.showOptionDialog(menu, "CUANTOS JUGADORES?", nombreJuego,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, cantidades, cantidades[0]);
        if (eleccion == -1) {
            return null; //cerro la ventanita: se cancela, no hay puntuacion
        }
        int cantidad = eleccion + 1;

        LinkedList<Jugador> jugadores = new LinkedList<>();
        for (int i = 1; i <= cantidad; i++) {
            String nombre = JOptionPane.showInputDialog(menu, "NOMBRE DEL JUGADOR " + i + ":",
                    nombreJuego, JOptionPane.PLAIN_MESSAGE);
            if (nombre == null) {
                return null;
            }
            nombre = nombre.trim();
            if (nombre.isEmpty()) {
                nombre = "Jugador " + i;
            }
            jugadores.add(new Jugador(nombre));
        }

        long inicio = System.currentTimeMillis();
        StringBuilder detalle = new StringBuilder();
        Jugador mejor = null;
        int mejorPuntos = -1;
        boolean empate = false;

        for (Jugador j : jugadores) {
            JOptionPane.showMessageDialog(menu, "TURNO DE " + j.getNombre().toUpperCase()
                    + "\n\nSE ABRIRA LA VENTANA DEL JUEGO.", nombreJuego, JOptionPane.PLAIN_MESSAGE);

            PanelJuego panel = mostrar(menu, nombreJuego + " - " + j.getNombre(), crearPanel.apply(j));

            for (int m = 0; m < panel.getMovimientos(); m++) {
                j.sumarMovimiento();
            }
            j.sumarPuntos(panel.getPuntos());
            j.sumarTiempo((long) (panel.getSegundos() * 1000));

            detalle.append("   ").append(j.toString()).append(" | ").append(panel.getResultado()).append("\n");

            if (panel.getPuntos() > mejorPuntos) {
                mejorPuntos = panel.getPuntos();
                mejor = j;
                empate = false;
            } else if (panel.getPuntos() == mejorPuntos) {
                empate = true;
            }
        }

        String ganador = empate ? "Empate" : mejor.getNombre();
        String resumen = "Juego: " + nombreJuego + "\n"
                + "Ganador: " + ganador + "\n"
                + "Tiempo total: " + ((System.currentTimeMillis() - inicio) / 1000) + " seg\n"
                + "Jugadores:\n" + detalle;

        JOptionPane.showMessageDialog(menu, ("========= GAME OVER =========\n\n" + resumen).toUpperCase(),
                "Fin de la partida", JOptionPane.PLAIN_MESSAGE);

        //se devuelve la puntuacion del mejor jugador (si empataron, la del primero)
        return new Puntuacion(nombreJuego, mejor.getNombre(), mejorPuntos);
    }

    //abre el panel en una ventana modal: setVisible(true) no regresa hasta que se cierra
    private static PanelJuego mostrar(JFrame menu, String titulo, PanelJuego panel) {
        JDialog ventana = new JDialog(menu, titulo, true);
        ventana.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ventana.add(panel);
        ventana.pack();
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                panel.requestFocusInWindow(); //para que el panel reciba las flechas
            }

            @Override
            public void windowClosed(WindowEvent e) {
                panel.detener();
            }
        });
        ventana.setVisible(true);
        return panel;
    }
}