package juegos;

/**
 * Interfaz para comparar puntuaciones.
 * <p>
 * La usa el {@link Ranking} para decidir si una puntuacion nueva entra al Top 3.
 * Funciona igual que comparar dos numeros:
 * <ul>
 *     <li>devuelve un numero <b>mayor que 0</b> si esta puntuacion es MEJOR que la otra</li>
 *     <li>devuelve un numero <b>menor que 0</b> si esta puntuacion es PEOR que la otra</li>
 *     <li>devuelve <b>0</b> si las dos son IGUALES (empate)</li>
 * </ul>
 */
public interface PuntComparable {

    /**
     * Compara esta puntuacion con otra.
     *
     * @param otra la puntuacion contra la que se compara
     * @return mayor que 0 si esta es mejor, menor que 0 si es peor, 0 si son iguales
     */
    int comparar(Puntuacion otra);
}