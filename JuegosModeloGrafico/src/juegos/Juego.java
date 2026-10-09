package juegos;

import juegos.estilo.Colores;
import juegos.estilo.EstiloArcade;

import javax.swing.*;
import java.util.LinkedList;

//Clase abstracta: cada juego es una ventana con nombre, jugadores, ganador y tiempo.
//Aqui esta todo lo que comparten los juegos. Lo que los hace JUGABLES es la interfaz Jugable.
//
//Es un JDialog MODAL (antes era JFrame): cuando se hace setVisible(true) el codigo se queda
//esperando ahi hasta que la ventana se cierre. Asi start() puede esperar a que termine el juego
//y despues DEVOLVER la Puntuacion.
public abstract class Juego extends JDialog {
    private String nombre;
    protected LinkedList<Jugador> jugadores;
    protected String ganador;
    private long tiempoTotal;
    private long inicioPartida;
    private JFrame menu;
    private Puntuacion puntuacion; //lo que va a devolver start() (null mientras no termine)

    public Juego(String nombre, JFrame menu) {
        super(menu, nombre, true); //true = modal: espera hasta que se cierre
        this.nombre = nombre;
        this.jugadores = new LinkedList<>();
        this.ganador = "Nadie";
        this.tiempoTotal = 0;
        this.menu = menu;
        this.puntuacion = null;

        getContentPane().setBackground(Colores.FONDO);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); //al cerrar solo se cierra el juego, no el menu
    }

    //deja todo como nuevo para poder jugar otra vez con el mismo objeto
    public void reiniciar() {
        this.jugadores = new LinkedList<>();
        this.ganador = "Nadie";
        this.tiempoTotal = 0;
        this.puntuacion = null;
        getContentPane().removeAll(); //borra lo que tenia la ventana de la partida anterior
    }

    public String getNombre() {
        return nombre;
    }

    public LinkedList<Jugador> getJugadores() {
        return jugadores;
    }

    public String getGanador() {
        return ganador;
    }

    //pide los nombres con ventanitas. Devuelve false si el usuario cancela
    public boolean pedirJugadores(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            String nombreJugador = JOptionPane.showInputDialog(menu,
                    "NOMBRE DEL JUGADOR " + (i + 1) + ":", nombre, JOptionPane.PLAIN_MESSAGE);
            if (nombreJugador == null) {
                return false;
            }
            nombreJugador = nombreJugador.trim();
            if (nombreJugador.isEmpty()) {
                nombreJugador = "Jugador " + (i + 1);
            }
            this.jugadores.add(new Jugador(nombreJugador));
        }
        return true;
    }

    //muestra botones con los numeros de min a max. Devuelve -1 si cierra la ventanita
    public int elegirNumero(String mensaje, int min, int max) {
        String[] opciones = new String[max - min + 1];
        for (int i = 0; i < opciones.length; i++) {
            opciones[i] = String.valueOf(min + i);
        }
        int elegido = JOptionPane.showOptionDialog(menu, mensaje, nombre,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        if (elegido == -1) {
            return -1;
        }
        return min + elegido;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje.toUpperCase(), nombre, JOptionPane.PLAIN_MESSAGE);
    }

    //si el usuario cancela antes de empezar, se cierra el juego
    public void cancelar() {
        dispose();
    }

    public void comenzarCronometro() {
        this.inicioPartida = System.currentTimeMillis();
    }

    //muestra la ventana del juego y ESPERA a que se cierre. Despues devuelve la puntuacion.
    //Cada juego lo usa al final de su start():   return esperarResultado();
    public Puntuacion esperarResultado() {
        setVisible(true);        //como es modal, aqui se queda esperando mientras se juega
        return this.puntuacion;  //si cerraron la ventana sin terminar, sigue en null
    }

    //crea la Puntuacion del ganador. Si hubo empate, usa al primero con mas puntos.
    public Puntuacion crearPuntuacion() {
        Jugador mejor = this.jugadores.get(0);
        for (int i = 0; i < this.jugadores.size(); i++) {
            if (this.jugadores.get(i).getNombre().equals(this.ganador)) {
                return new Puntuacion(nombre, this.jugadores.get(i).getNombre(), this.jugadores.get(i).getPuntos());
            }
            if (this.jugadores.get(i).getPuntos() > mejor.getPuntos()) {
                mejor = this.jugadores.get(i);
            }
        }
        return new Puntuacion(nombre, mejor.getNombre(), mejor.getPuntos());
    }

    //arma la puntuacion, muestra el resumen y cierra la ventana del juego
    public void terminarPartida() {
        this.tiempoTotal = System.currentTimeMillis() - inicioPartida;
        this.puntuacion = crearPuntuacion();

        String texto = "========= GAME OVER =========\n\n" + this.toString();
        JOptionPane.showMessageDialog(this, EstiloArcade.crearPantallaTexto(texto.toUpperCase(), 560, 220),
                "Fin de la partida", JOptionPane.PLAIN_MESSAGE);

        dispose(); //cierra la ventana del juego, el menu sigue abierto
    }

    //busca al jugador con mas puntos. Si hay varios con el mismo puntaje maximo, es empate
    public String jugadorConMasPuntos() {
        Jugador mejor = this.jugadores.get(0);
        for (int i = 1; i < this.jugadores.size(); i++) {
            if (this.jugadores.get(i).getPuntos() > mejor.getPuntos()) {
                mejor = this.jugadores.get(i);
            }
        }

        int cuantosConMaximo = 0;
        for (int i = 0; i < this.jugadores.size(); i++) {
            if (this.jugadores.get(i).getPuntos() == mejor.getPuntos()) {
                cuantosConMaximo++;
            }
        }
        if (cuantosConMaximo > 1) {
            return "Empate";
        }
        return mejor.getNombre();
    }

    @Override
    public String toString() {
        String contenido = "";
        for (int i = 0; i < this.jugadores.size(); i++) {
            contenido += "   " + this.jugadores.get(i).toString() + "\n";
        }
        return "Juego: " + nombre + "\n" +
                "Ganador: " + ganador + "\n" +
                "Tiempo total: " + (tiempoTotal / 1000) + " seg\n" +
                "Jugadores:\n" + contenido;
    }
}