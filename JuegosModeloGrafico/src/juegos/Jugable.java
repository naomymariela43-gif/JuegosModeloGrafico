package juegos;

/**
 * Interfaz Jugable: el "contrato" que deben cumplir TODOS los juegos de la consola.
 * <p>
 * Solo los objetos que implementan Jugable se pueden insertar en la {@link Consola}
 * (la consola lo revisa con {@code instanceof}). Cualquier otro objeto se rechaza.
 * <p>
 * Para que un juego funcione en la consola, su clase principal debe:
 * <ol>
 *     <li>importar {@code juegos.Jugable} y {@code juegos.Puntuacion}</li>
 *     <li>poner {@code implements Jugable}</li>
 *     <li>escribir su metodo {@link #start()}, que al terminar DEVUELVE la {@link Puntuacion} del ganador</li>
 * </ol>
 * IMPORTANTE: todos deben usar ESTE MISMO archivo, en el package {@code juegos}.
 */
public interface Jugable {

    /**
     * Metodo inicial fijo: la consola lo llama desde "Lanzar Juego" para arrancar el juego.
     * <p>
     * Debe ESPERAR a que el juego termine y despues devolver la puntuacion.
     * La consola la registra y la compara con el Top 3 de ese juego.
     *
     * @return la {@link Puntuacion} del ganador, o {@code null} si se cancelo o no se termino
     */
    Puntuacion start();

    /**
     * Nombre del juego: aparece en el boton del menu y en las estadisticas.
     * <p>
     * Es {@code default}: si el juego no lo escribe, se usa el nombre de la clase.
     *
     * @return el nombre del juego
     */
    default String getNombre() {
        return getClass().getSimpleName();
    }
}
