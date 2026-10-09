package juegos;

import java.util.LinkedList;

//El TOP 3 de UN juego. La consola tiene un Ranking por cada juego que se inserta.
//Para comparar usa el metodo comparar() de la interfaz PuntComparable.
public class Ranking {
    public static final int TAMANO = 3; //cuantas puntuaciones se guardan (top 3)

    private String juego;
    private LinkedList<Puntuacion> top; //ordenado de mayor a menor: top.get(0) es el 1er lugar

    public Ranking(String juego) {
        this.juego = juego;
        this.top = new LinkedList<>();
        cargarPuntuacionesQuemadas();
    }

    //ESCENARIO FICTICIO (puntuaciones quemadas): el ranking no empieza vacio, como en las maquinitas.
    //Asi "Ver Estadisticas" ya muestra un Top 3 desde el inicio. Para superarlas hay que hacer mas puntos.
    private void cargarPuntuacionesQuemadas() {
        this.top.add(new Puntuacion(juego, "CPU", 30));
        this.top.add(new Puntuacion(juego, "CPU", 20));
        this.top.add(new Puntuacion(juego, "CPU", 10));
    }

    public String getJuego() {
        return juego;
    }

    public LinkedList<Puntuacion> getTop() {
        return top;
    }

    //REGISTRAR: compara la nueva puntuacion con el top 3.
    //Si es mejor que alguna, entra en ese lugar y la ultima se sale.
    //Devuelve el lugar en el que quedo (1, 2 o 3), o 0 si no entro al top.
    public int registrar(Puntuacion nueva) {
        for (int i = 0; i < this.top.size(); i++) {
            if (nueva.comparar(this.top.get(i)) > 0) { //> 0 quiere decir que la nueva es mejor
                this.top.add(i, nueva);          //entra en el lugar i y empuja a los demas hacia abajo
                if (this.top.size() > TAMANO) {
                    this.top.removeLast();       //el que quedo de 4to se sale del top
                }
                return i + 1;
            }
        }
        //si no le gano a nadie pero todavia hay espacio, entra al final
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