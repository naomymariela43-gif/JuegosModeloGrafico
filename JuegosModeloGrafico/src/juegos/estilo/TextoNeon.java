package juegos.estilo;

import javax.swing.*;
import java.awt.*;

//Etiqueta con efecto de "brillo neon": el texto se dibuja varias veces
//alrededor con un color transparente y encima se dibuja el texto normal.
public class TextoNeon extends JLabel {

    public TextoNeon(String texto, Color color, int tamano) {
        super(texto);
        setForeground(color);
        setFont(EstiloArcade.fuente(tamano));
    }

    @Override
    protected void paintComponent(Graphics g) {
        String texto = getText();
        if (texto == null || texto.isEmpty()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(getFont());

        //centrar el texto
        FontMetrics medidas = g2.getFontMetrics();
        int x = (getWidth() - medidas.stringWidth(texto)) / 2;
        int y = (getHeight() - medidas.getHeight()) / 2 + medidas.getAscent();

        //1) el brillo: copias transparentes alrededor (letras mas grandes = brillo mas grande)
        Color color = getForeground();
        int radio = Math.max(1, getFont().getSize() / 14);
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
        for (int dx = -radio; dx <= radio; dx++) {
            for (int dy = -radio; dy <= radio; dy++) {
                g2.drawString(texto, x + dx, y + dy);
            }
        }

        //2) el texto encima
        g2.setColor(color);
        g2.drawString(texto, x, y);
        g2.dispose();
    }
}
