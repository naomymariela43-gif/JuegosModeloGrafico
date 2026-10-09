package juegos;

public class Jugador {
    private String nombre;
    private int movimientos;
    private int puntos;
    private long tiempo; // en milisegundos

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.movimientos = 0;
        this.puntos = 0;
        this.tiempo = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getMovimientos() {
        return movimientos;
    }

    public int getPuntos() {
        return puntos;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void sumarMovimiento() {
        this.movimientos++;
    }

    public void sumarPuntos(int puntos) {
        this.puntos += puntos;
    }

    public void sumarTiempo(long milisegundos) {
        this.tiempo += milisegundos;
    }

    @Override
    public String toString() {
        return nombre + " | movimientos: " + movimientos +
                " | puntos: " + puntos +
                " | tiempo: " + (tiempo / 1000) + " seg";
    }
}
