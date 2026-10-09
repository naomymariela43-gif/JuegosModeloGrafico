package cityblock.comun;

/** Un jugador con nombre y cantidad de movimientos realizados. */
public class Jugador {
    private final String nombre;
    private int movimientos;
    private int puntos;

    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }
    public int getMovimientos() { return movimientos; }
    public int getPuntos() { return puntos; }

    public void registrarMovimiento() { movimientos++; }
    public void sumarPuntos(int p) { puntos += p; }

    @Override
    public String toString() { return nombre; }
}
