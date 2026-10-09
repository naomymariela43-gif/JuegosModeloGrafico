package cityblock.comun;

/** Codigos ANSI para colorear mensajes en consola. */
public final class Colores {
    public static final String RESET    = "\u001B[0m";
    public static final String VERDE    = "\u001B[32m";  // Juego 1
    public static final String AZUL     = "\u001B[34m";  // Juego 2
    public static final String AMARILLO = "\u001B[33m";  // Juego 3

    // En Windows (cmd/PowerShell) hay que activar el modo ANSI de la consola.
    // Ejecutar un comando vacio de cmd con la consola heredada lo activa.
    static {
        if (System.getProperty("os.name", "").toLowerCase().contains("win")) {
            try {
                new ProcessBuilder("cmd", "/c", "").inheritIO().start().waitFor();
            } catch (Exception e) {
                // si falla, el juego sigue funcionando sin color
            }
        }
    }

    private Colores() { } // clase de utilidad: no se instancia
}
