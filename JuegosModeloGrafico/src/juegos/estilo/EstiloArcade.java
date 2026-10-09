package juegos.estilo;

import javax.swing.*;
import java.awt.*;

//Configuracion general del estilo retro arcade
public class EstiloArcade {

    //letra tipo maquina de escribir/videojuego viejo
    public static Font fuente(int tamano) {
        return new Font(Font.MONOSPACED, Font.BOLD, tamano);
    }

    //cambia los colores de TODAS las ventanitas (JOptionPane) del programa.
    //Se llama una sola vez al inicio, antes de crear cualquier ventana.
    public static void aplicar() {
        UIManager.put("OptionPane.background", Colores.FONDO);
        UIManager.put("Panel.background", Colores.FONDO);
        UIManager.put("OptionPane.messageForeground", Colores.VERDE);
        UIManager.put("OptionPane.messageFont", fuente(16));
        UIManager.put("OptionPane.buttonFont", fuente(14));
        UIManager.put("OptionPane.okButtonText", "OK");
        UIManager.put("OptionPane.cancelButtonText", "CANCELAR");

        UIManager.put("Button.background", Colores.FONDO);
        UIManager.put("Button.foreground", Colores.AMARILLO);
        UIManager.put("Button.select", new Color(70, 60, 0));
        UIManager.put("Button.focus", Colores.FONDO);
        UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colores.AMARILLO, 2),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));

        UIManager.put("TextField.background", Color.BLACK);
        UIManager.put("TextField.foreground", Colores.VERDE);
        UIManager.put("TextField.caretForeground", Colores.VERDE);
        UIManager.put("TextField.font", fuente(16));
        UIManager.put("TextField.border", BorderFactory.createLineBorder(Colores.VERDE, 2));
    }

    //pantalla negra con letras verdes, como una tabla de puntajes de arcade
    public static JScrollPane crearPantallaTexto(String texto, int ancho, int alto) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setBackground(Color.BLACK);
        area.setForeground(Colores.VERDE);
        area.setFont(fuente(14));
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(Colores.VERDE, 2));
        scroll.setPreferredSize(new Dimension(ancho, alto));
        return scroll;
    }

    //etiqueta normal con letra arcade
    public static JLabel crearTexto(String texto, Color color, int tamano) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setForeground(color);
        etiqueta.setFont(fuente(tamano));
        return etiqueta;
    }
}
