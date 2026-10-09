package matriz;

import juegos.Jugador;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Panel base donde se dibuja la matriz de un juego.
 * El juego avanza solo con un Timer (no hay que imprimir la matriz en cada movimiento).
 * Se encarga del teclado, del tiempo, de la barra de información y del mensaje final.
 */
public abstract class PanelJuego extends JPanel {
    protected static final int CELDA = 30;   // tamaño en píxeles de cada casilla de la matriz
    private static final int BARRA = 44;     // alto de la barra de información

    private final Jugador jugador;
    private final Timer timer;

    protected int puntos;
    protected int movimientos;

    private boolean empezado;
    private long inicio;
    private double segundosFinales;
    private String resultado;     // null mientras se está jugando
    private String mensajeFinal;

    protected PanelJuego(Jugador jugador, int filas, int columnas, int milisegundosPorPaso) {
        this.jugador = jugador;
        setPreferredSize(new java.awt.Dimension(columnas * CELDA, filas * CELDA + BARRA));
        setBackground(Color.BLACK);
        setFocusable(true);

        timer = new Timer(milisegundosPorPaso, e -> {
            if (resultado == null) {
                paso();
            }
            repaint();
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                manejarTecla(e.getKeyCode());
            }
        });
    }

    // ---------- Lo que cada juego debe implementar ----------

    /** Un paso del juego (lo llama el Timer). */
    protected abstract void paso();

    /** El jugador presionó una dirección. */
    protected abstract void cambiarDireccion(Direccion d);

    /** Dibuja la matriz y los objetos del juego. */
    protected abstract void dibujarJuego(Graphics2D g);

    /** Texto extra para la barra (por ejemplo vidas o manzanas). */
    protected abstract String infoExtra();

    // ---------- Teclado ----------

    private void manejarTecla(int tecla) {
        if (resultado != null) {   // el juego terminó: ENTER cierra la ventana
            if (tecla == KeyEvent.VK_ENTER || tecla == KeyEvent.VK_ESCAPE || tecla == KeyEvent.VK_SPACE) {
                cerrarVentana();
            }
            return;
        }
        if (tecla == KeyEvent.VK_Q || tecla == KeyEvent.VK_ESCAPE) {
            terminar("Abandonó", "Te rendiste");
            return;
        }
        Direccion d = direccionDeTecla(tecla);
        if (d != null) {
            if (!empezado) {
                empezado = true;
                inicio = System.currentTimeMillis();
                timer.start();
            }
            cambiarDireccion(d);
        }
    }

    private static Direccion direccionDeTecla(int tecla) {
        switch (tecla) {
            case KeyEvent.VK_UP:    case KeyEvent.VK_W: return Direccion.ARRIBA;
            case KeyEvent.VK_DOWN:  case KeyEvent.VK_S: return Direccion.ABAJO;
            case KeyEvent.VK_LEFT:  case KeyEvent.VK_A: return Direccion.IZQUIERDA;
            case KeyEvent.VK_RIGHT: case KeyEvent.VK_D: return Direccion.DERECHA;
            default: return null;
        }
    }

    // ---------- Control del juego ----------

    /** Termina el turno con un resultado ("Completó", "Perdió" o "Abandonó"). */
    protected void terminar(String resultado, String mensaje) {
        if (this.resultado != null) return;
        this.segundosFinales = getSegundos();
        this.resultado = resultado;
        this.mensajeFinal = mensaje;
        timer.stop();
        repaint();
    }

    /** Detiene el Timer (se llama cuando se cierra la ventana). */
    public void detener() {
        timer.stop();
    }

    private void cerrarVentana() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) w.dispose();
    }

    public double getSegundos() {
        if (!empezado) return 0;
        if (resultado != null) return segundosFinales;
        return (System.currentTimeMillis() - inicio) / 1000.0;
    }

    /** "Completó", "Perdió" o "Abandonó". Si se cerró la ventana sin terminar, cuenta como abandono. */
    public String getResultado() {
        return resultado != null ? resultado : "Abandonó";
    }

    public int getPuntos() {
        return puntos;
    }

    public int getMovimientos() {
        return movimientos;
    }

    // ---------- Dibujo ----------

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Barra de información
        g.setColor(new Color(25, 25, 35));
        g.fillRect(0, 0, getWidth(), BARRA);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        g.drawString(jugador.getNombre() + "   Puntos: " + puntos + "   Mov: " + movimientos
                + "   Tiempo: " + (int) getSegundos() + "s", 10, 19);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        g.setColor(new Color(200, 200, 200));
        g.drawString(infoExtra(), 10, 37);

        // Matriz del juego
        Graphics2D gj = (Graphics2D) g.create();
        gj.translate(0, BARRA);
        dibujarJuego(gj);
        gj.dispose();

        if (!empezado) {
            mensajeCentral(g, "Presione una flecha o WASD para empezar", "Q o ESC = rendirse");
        } else if (resultado != null) {
            mensajeCentral(g, mensajeFinal + "  —  " + puntos + " puntos", "Presione ENTER para continuar");
        }
    }

    private void mensajeCentral(Graphics2D g, String linea1, String linea2) {
        int alto = 70;
        int y = BARRA + (getHeight() - BARRA - alto) / 2;
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, y, getWidth(), alto);

        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(linea1, (getWidth() - fm.stringWidth(linea1)) / 2, y + 30);

        g.setColor(new Color(190, 190, 190));
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        fm = g.getFontMetrics();
        g.drawString(linea2, (getWidth() - fm.stringWidth(linea2)) / 2, y + 55);
    }
}
