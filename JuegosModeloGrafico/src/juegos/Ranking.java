package juegos;

import java.util.LinkedList;

public class Ranking {
    public static final int TAMANO = 3;
    private String juego;
    private LinkedList<Puntuacion> top;   //top.get(0) es el 1er lugar

    public Ranking(String juego) {
        this.juego = juego;
        this.top = new LinkedList<>();
        cargarPuntuacionesQuemadas();
    }

    private void cargarPuntuacionesQuemadas() {
        this.top.add(new Puntuacion(juego, "CPU", 30));
        this.top.add(new Puntuacion(juego, "CPU", 20));
        this.top.add(new Puntuacion(juego, "CPU", 10));
    }

    public String getJuego() { return juego; }

    public int registrar(Puntuacion nueva) {
        for (int i = 0; i < this.top.size(); i++) {
            if (nueva.esMejorQue(this.top.get(i))) {
                this.top.add(i, nueva);              //entra y empuja a los demas
                if (this.top.size() > TAMANO) {
                    this.top.removeLast();           //el 4to se sale
                }
                return i + 1;
            }
        }
        if (this.top.size() < TAMANO) {
            this.top.add(nueva);
            return this.top.size();
        }
        return 0;
    }

    @Override
    public String toString() {
        String texto = "===== " + juego + " =====\n";
        for (int i = 0; i < this.top.size(); i++) {
            texto += "   " + (i + 1) + ". " + this.top.get(i) + "\n";
        }
        return texto;
    }
}