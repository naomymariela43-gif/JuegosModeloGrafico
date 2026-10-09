package juegos.estilo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//Boton negro con borde y letras neon. Al pasar el mouse se "enciende" (se rellena del color).
public class BotonArcade extends JButton {
    private Color color;

    public BotonArcade(String texto, Color color, int tamanoLetra) {
        super(texto);
        this.color = color;

        setFont(EstiloArcade.fuente(tamanoLetra));
        setForeground(color);
        setBackground(Colores.FONDO);
        setContentAreaFilled(false); //quita el degradado gris de Java
        setOpaque(true);             //pero si pinta el fondo negro
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 3),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                encender(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                encender(false);
            }
        });
    }

    private void encender(boolean encendido) {
        if (encendido) {
            setBackground(this.color);
            setForeground(Colores.FONDO);
        } else {
            setBackground(Colores.FONDO);
            setForeground(this.color);
        }
    }
}
