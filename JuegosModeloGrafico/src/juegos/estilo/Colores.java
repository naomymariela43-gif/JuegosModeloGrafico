package juegos.estilo;

import java.awt.Color;

//Paleta de colores neon estilo arcade
public class Colores {
    public static final Color FONDO = new Color(8, 8, 16);
    public static final Color FONDO_CELDA = new Color(14, 14, 26);
    public static final Color BORDE_CELDA = new Color(30, 30, 50);

    public static final Color VERDE = new Color(57, 255, 20);
    public static final Color ROSA = new Color(255, 45, 170);
    public static final Color CIAN = new Color(0, 230, 255);
    public static final Color AMARILLO = new Color(255, 230, 0);
    public static final Color ROJO = new Color(255, 40, 70);
    public static final Color AZUL = new Color(30, 120, 255);
    public static final Color GRIS = new Color(140, 140, 160);

    //colores de la Batalla Naval
    public static final Color AGUA = new Color(0, 25, 55);
    public static final Color BORDE_AGUA = new Color(0, 120, 160);
    public static final Color BARCO = new Color(90, 90, 120);
    public static final Color TAPADO = new Color(22, 22, 32);

    //color de cada jugador segun su posicion (jugador 1, 2, 3 o 4)
    public static Color colorJugador(int indice) {
        if (indice == 0) return ROJO;
        if (indice == 1) return CIAN;
        if (indice == 2) return VERDE;
        return AMARILLO;
    }

    //convierte un color a texto tipo "#FF2846" para usarlo dentro de etiquetas HTML
    public static String hex(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }
}
