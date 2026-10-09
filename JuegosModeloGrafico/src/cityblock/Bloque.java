package cityblock;

/** Un piso de la torre. Solo sabe como dibujarse en texto. */
public class Bloque {
    public static final int ANCHO = 9;

    /** Linea de texto de largo 'campo' con el bloque centrado en 'centro'. */
    public static String linea(int centro, int campo, char relleno) {
        int ini = centro - ANCHO / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < campo; i++) {
            if (i < ini || i >= ini + ANCHO) sb.append(' ');
            else if (i == ini) sb.append('[');
            else if (i == ini + ANCHO - 1) sb.append(']');
            else sb.append(relleno);
        }
        return sb.toString();
    }
}
