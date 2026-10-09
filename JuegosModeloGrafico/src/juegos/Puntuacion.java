package juegos;

/**
 * Lo que DEVUELVE cada juego al terminar, en su metodo {@link Jugable#start()}.
 * <p>
 * Es generica: sirve para cualquier juego. Solo guarda de que juego es,
 * quien la hizo y cuantos puntos saco. Implementa {@link PuntComparable}
 * para poder compararse con otras puntuaciones en el Top 3.
 */
public class Puntuacion implements PuntComparable {
    private String juego;
    private String jugador;
    private int puntos;

    public Puntuacion(String juego, String jugador, int puntos) {
        this.juego = juego;
        this.jugador = jugador;
        this.puntos = puntos;
    }

    public String getJuego() {
        return juego;
    }

    public String getJugador() {
        return jugador;
    }

    public int getPuntos() {
        return puntos;
    }

    //METODO DE COMPARACION (viene de la interfaz PuntComparable):
    //   > 0  esta puntuacion es mejor
    //   < 0  esta puntuacion es peor
    //   = 0  son iguales
    @Override
    public int comparar(Puntuacion otra) {
        if (this.puntos > otra.puntos) {
            return 1;
        } else if (this.puntos < otra.puntos) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return jugador + " - " + puntos + " pts";
    }
}