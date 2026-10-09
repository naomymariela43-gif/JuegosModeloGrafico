package juegos;

import java.util.LinkedList;

//Las ESTADISTICAS de la consola: un Ranking (top 3) por cada juego.
//Es generico: no importa que juego sea, solo necesita su nombre y su Puntuacion.
public class Estadisticas {
    private LinkedList<Ranking> rankings;

    public Estadisticas() {
        this.rankings = new LinkedList<>();
    }

    //se llama cuando la consola acepta un juego: le crea su ranking
    public void agregarJuego(String nombreJuego) {
        if (buscarRanking(nombreJuego) == null) {
            this.rankings.add(new Ranking(nombreJuego));
        }
    }

    //busca el ranking de un juego por su nombre (null si no existe)
    public Ranking buscarRanking(String nombreJuego) {
        for (int i = 0; i < this.rankings.size(); i++) {
            if (this.rankings.get(i).getJuego().equals(nombreJuego)) {
                return this.rankings.get(i);
            }
        }
        return null;
    }

    //REGISTRAR: manda la puntuacion al ranking de su juego para compararla con el top 3.
    //Devuelve el lugar en el que quedo (1, 2 o 3), o 0 si no entro.
    public int registrar(Puntuacion puntuacion) {
        agregarJuego(puntuacion.getJuego()); //por si el juego no tenia ranking todavia
        return buscarRanking(puntuacion.getJuego()).registrar(puntuacion);
    }

    public LinkedList<Ranking> getRankings() {
        return rankings;
    }

    @Override
    public String toString() {
        if (this.rankings.isEmpty()) {
            return "La consola no tiene juegos.";
        }
        String texto = "";
        for (int i = 0; i < this.rankings.size(); i++) {
            texto += this.rankings.get(i).toString() + "\n";
        }
        return texto;
    }
}