package juegos;

import java.util.LinkedList;

public class Estadisticas {
    private LinkedList<Ranking> rankings;

    public Estadisticas() {
        this.rankings = new LinkedList<>();
    }

    public void agregarJuego(String nombreJuego) {
        if (buscarRanking(nombreJuego) == null) {
            this.rankings.add(new Ranking(nombreJuego));
        }
    }

    public Ranking buscarRanking(String nombreJuego) {
        for (int i = 0; i < this.rankings.size(); i++) {
            if (this.rankings.get(i).getJuego().equals(nombreJuego)) {
                return this.rankings.get(i);
            }
        }
        return null;
    }

    public int registrar(Puntuacion puntuacion) {
        agregarJuego(puntuacion.getJuego());
        return buscarRanking(puntuacion.getJuego()).registrar(puntuacion);
    }

    @Override
    public String toString() {
        if (this.rankings.isEmpty()) return "La consola no tiene juegos.";
        String texto = "";
        for (int i = 0; i < this.rankings.size(); i++) {
            texto += this.rankings.get(i).toString() + "\n";
        }
        return texto;
    }
}