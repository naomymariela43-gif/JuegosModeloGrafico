package cityblock.comun;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Clase base: pide los jugadores y maneja los turnos. */
public abstract class Juego {
    protected final Scanner entrada;
    protected final List<Jugador> jugadores = new ArrayList<>();
    private final String nombre;

    protected Juego(String nombre, Scanner entrada) {
        this.nombre = nombre;
        this.entrada = entrada;
    }

    public String getNombre() { return nombre; }

    // ---- Estructura para Jugable.start() (no altera la logica de los juegos) ----
    private Estadistica ultimaEstadistica;

    /** Estadistica de la ultima partida terminada (null si aun no se ha jugado). */
    public Estadistica getUltimaEstadistica() { return ultimaEstadistica; }

    /** Corre una partida completa, muestra sus estadisticas y las guarda. La usa start() de cada juego. */
    protected void correrPartida() {
        ultimaEstadistica = jugar();
        ultimaEstadistica.imprimir();
    }

    /** Cada juego implementa su propia partida y devuelve sus estadisticas. */
    protected abstract Estadistica jugarPartida();

    /** Muestra las reglas del juego. */
    protected abstract void mostrarReglas();

    public Estadistica jugar() {
        System.out.println("\n##### " + nombre.toUpperCase() + " #####");
        mostrarReglas();
        pedirJugadores();
        return jugarPartida();
    }

    private void pedirJugadores() {
        jugadores.clear();
        int cantidad = leerEntero("Cuantos jugadores (2-6)? ", 2, 6);
        for (int i = 1; i <= cantidad; i++) {
            System.out.print("Nombre del jugador " + i + ": ");
            String n = entrada.nextLine().trim();
            if (n.isEmpty()) n = "Jugador" + i;
            jugadores.add(new Jugador(n));
        }
    }

    protected int leerEntero(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            try {
                int v = Integer.parseInt(entrada.nextLine().trim());
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException e) {
                // se vuelve a preguntar
            }
            System.out.println("Ingresa un numero entre " + min + " y " + max + ".");
        }
    }

    /** Jugador con mas puntos, o null si hay empate en el primer lugar. */
    protected Jugador mejorPuntaje() {
        Jugador mejor = null;
        boolean empate = false;
        for (Jugador j : jugadores) {
            if (mejor == null || j.getPuntos() > mejor.getPuntos()) {
                mejor = j;
                empate = false;
            } else if (j.getPuntos() == mejor.getPuntos()) {
                empate = true;
            }
        }
        return empate ? null : mejor;
    }
}
