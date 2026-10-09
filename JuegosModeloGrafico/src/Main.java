import juegos.Consola;
import juegos.estilo.EstiloArcade;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        EstiloArcade.aplicar(); //primero el estilo, para que todas las ventanas salgan arcade

        //las ventanas de Swing se crean dentro de invokeLater
        SwingUtilities.invokeLater(() -> {
            Consola consola = new Consola();
            consola.setVisible(true);
        });
    }
}
