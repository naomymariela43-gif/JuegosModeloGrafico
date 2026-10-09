package juegos;

import java.util.LinkedList;

//Guarda el resumen (texto) de todas las partidas que se han jugado
public class Estadisticas {
    private LinkedList<String> partidas;

    public Estadisticas() {
        this.partidas = new LinkedList<>();
    }

    //se guarda el texto del resultado en el momento en que termina la partida
    public void agregarPartida(String resumen) {
        this.partidas.add(resumen);
    }

    public LinkedList<String> getPartidas() {
        return partidas;
    }

    @Override
    public String toString() {
        if (this.partidas.isEmpty()) {
            return "Todavia no se ha jugado ninguna partida.";
        }
        String contenido = "";
        for (int i = 0; i < this.partidas.size(); i++) {
            contenido += "----- Partida " + (i + 1) + " -----\n" + this.partidas.get(i) + "\n";
        }
        return contenido;
    }
}
