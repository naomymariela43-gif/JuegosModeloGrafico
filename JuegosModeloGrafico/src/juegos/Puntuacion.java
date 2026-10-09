package juegos;

public class Puntuacion {
    private String juego;
    private String jugador;
    private int puntos;

    public Puntuacion(String juego, String jugador, int puntos) {
        this.juego = juego;
        this.jugador = jugador;
        this.puntos = puntos;
    }

    public String getJuego() { return juego; }
    public String getJugador() { return jugador; }
    public int getPuntos() { return puntos; }

    //METODO DE COMPARACION
    public boolean esMejorQue(Puntuacion otra) {
        return this.puntos > otra.puntos;
    }

    @Override
    public String toString() {
        return jugador + " - " + puntos + " pts";
    }
}
