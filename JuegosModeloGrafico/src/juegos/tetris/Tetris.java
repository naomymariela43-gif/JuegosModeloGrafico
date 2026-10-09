package juegos.tetris;

import juegos.Juego;
import juegos.Jugable;
import juegos.Jugador;
import juegos.Puntuacion;
import juegos.estilo.BotonArcade;
import juegos.estilo.Colores;
import juegos.estilo.EstiloArcade;
import juegos.estilo.TextoNeon;

import javax.swing.*;
import java.awt.*;

//Tetris por turnos con ventana: todos juegan en el mismo tablero.
//En su turno cada jugador mueve y gira su pieza con botones y luego la suelta.
public class Tetris extends Juego implements Jugable {
    private TableroTetris tablero;
    private int rondas;
    private int rondaActual;
    private int turno;
    private Pieza pieza;
    private int columna;
    private long inicioTurno;

    private JLabel[][] celdas;
    private TextoNeon lblTurno;
    private JLabel lblRonda;
    private JLabel[] lblNombres;
    private JLabel[] lblPuntos;

    public Tetris(JFrame menu) {
        super("Tetris por turnos", menu);
        this.tablero = new TableroTetris();
        this.celdas = new JLabel[TableroTetris.ALTO][TableroTetris.ANCHO];
    }

    //metodo de la interfaz Jugable: aqui arranca el juego y al final DEVUELVE la puntuacion
    @Override
    public Puntuacion start() {
        reiniciar(); //por si ya se habia jugado antes
        this.tablero = new TableroTetris();
        this.celdas = new JLabel[TableroTetris.ALTO][TableroTetris.ANCHO];

        int cantidad = elegirNumero("¿Cuántos jugadores?", 2, 4);
        if (cantidad == -1 || !pedirJugadores(cantidad)) {
            cancelar();
            return null; //no hay puntuacion
        }
        this.rondas = elegirNumero("¿Cuántas piezas pone cada jugador?", 3, 10);
        if (this.rondas == -1) {
            cancelar();
            return null;
        }

        armarVentana();
        comenzarCronometro();
        this.rondaActual = 1;
        this.turno = 0;
        nuevaPieza();

        return esperarResultado(); //espera a que termine el juego y devuelve la puntuacion
    }

    private void armarVentana() {
        setSize(640, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        //----- arriba: turno y ronda -----
        JPanel arriba = new JPanel(new GridLayout(2, 1));
        arriba.setBorder(BorderFactory.createEmptyBorder(12, 10, 0, 10));
        lblTurno = new TextoNeon("", Colores.ROJO, 28);
        lblRonda = EstiloArcade.crearTexto("", Colores.AMARILLO, 14);
        arriba.add(lblTurno);
        arriba.add(lblRonda);
        add(arriba, BorderLayout.NORTH);

        //----- centro: el tablero, cada casilla es un JLabel pintado -----
        JPanel panelTablero = new JPanel(new GridLayout(TableroTetris.ALTO, TableroTetris.ANCHO, 0, 0));
        panelTablero.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colores.ROSA, 3),
                BorderFactory.createLineBorder(Colores.FONDO, 3)));
        for (int f = 0; f < TableroTetris.ALTO; f++) {
            for (int c = 0; c < TableroTetris.ANCHO; c++) {
                JLabel celda = new JLabel();
                celda.setOpaque(true); //para que se vea el color de fondo
                celdas[f][c] = celda;
                panelTablero.add(celda);
            }
        }
        panelTablero.setPreferredSize(new Dimension(TableroTetris.ANCHO * 36 + 12, TableroTetris.ALTO * 36 + 12));
        JPanel contenedor = new JPanel(); //para que el tablero no se estire
        contenedor.add(panelTablero);
        add(contenedor, BorderLayout.CENTER);

        //----- derecha: puntos de cada jugador con su color, tipo "1P", "2P"... -----
        JPanel derecha = new JPanel(new GridLayout(10, 1, 0, 2));
        derecha.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));
        derecha.add(EstiloArcade.crearTexto("PUNTOS", Colores.AMARILLO, 18));
        derecha.add(new JLabel(""));
        lblNombres = new JLabel[this.jugadores.size()];
        lblPuntos = new JLabel[this.jugadores.size()];
        for (int i = 0; i < this.jugadores.size(); i++) {
            Color color = Colores.colorJugador(i);
            lblNombres[i] = EstiloArcade.crearTexto((i + 1) + "P " + this.jugadores.get(i).getNombre().toUpperCase(), color, 15);
            lblPuntos[i] = EstiloArcade.crearTexto("00000", color, 20);
            derecha.add(lblNombres[i]);
            derecha.add(lblPuntos[i]);
        }
        add(derecha, BorderLayout.EAST);

        //----- abajo: botones de control -----
        JPanel abajo = new JPanel(new GridLayout(1, 4, 10, 10));
        abajo.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        BotonArcade btnIzquierda = new BotonArcade("< IZQ", Colores.CIAN, 16);
        BotonArcade btnGirar = new BotonArcade("GIRAR", Colores.AMARILLO, 16);
        BotonArcade btnDerecha = new BotonArcade("DER >", Colores.CIAN, 16);
        BotonArcade btnSoltar = new BotonArcade("SOLTAR", Colores.ROSA, 16);
        btnIzquierda.addActionListener(e -> mover(-1));
        btnDerecha.addActionListener(e -> mover(1));
        btnGirar.addActionListener(e -> girar());
        btnSoltar.addActionListener(e -> soltar());
        abajo.add(btnIzquierda);
        abajo.add(btnGirar);
        abajo.add(btnDerecha);
        abajo.add(btnSoltar);
        add(abajo, BorderLayout.SOUTH);

    }

    //----------------- logica de los turnos -----------------

    private void nuevaPieza() {
        this.pieza = Pieza.crearAleatoria();
        this.columna = TableroTetris.ANCHO / 2 - this.pieza.getAncho() / 2;

        if (!this.tablero.cabe(this.pieza, 0, this.columna)) {
            actualizarPantalla();
            this.ganador = jugadorConMasPuntos();
            mostrarMensaje("¡El tablero se llenó! Se termina el juego.\nGanador: " + this.ganador);
            terminarPartida();
            return;
        }
        this.inicioTurno = System.currentTimeMillis();
        actualizarPantalla();
    }

    private void mover(int direccion) {
        int nueva = this.columna + direccion;
        if (this.tablero.cabe(this.pieza, 0, nueva)) {
            this.columna = nueva;
            this.jugadores.get(turno).sumarMovimiento();
            actualizarPantalla();
        }
    }

    private void girar() {
        int columnaAntes = this.columna;
        this.pieza.rotar();
        //si al girar se sale por la derecha, se corre a la izquierda
        if (this.columna + this.pieza.getAncho() > TableroTetris.ANCHO) {
            this.columna = TableroTetris.ANCHO - this.pieza.getAncho();
        }
        if (this.tablero.cabe(this.pieza, 0, this.columna)) {
            this.jugadores.get(turno).sumarMovimiento();
        } else {
            //no cabe girada: se devuelve como estaba (3 giros mas = vuelta completa)
            this.pieza.rotar();
            this.pieza.rotar();
            this.pieza.rotar();
            this.columna = columnaAntes;
        }
        actualizarPantalla();
    }

    private void soltar() {
        Jugador jugador = this.jugadores.get(turno);
        char numeroJugador = (char) ('1' + turno);

        jugador.sumarMovimiento();
        this.tablero.soltarPieza(this.pieza, this.columna, numeroJugador);
        jugador.sumarPuntos(10);
        jugador.sumarTiempo(System.currentTimeMillis() - inicioTurno);

        int lineas = this.tablero.borrarLineasCompletas();
        this.pieza = null; //para que no se dibuje mientras sale el mensaje
        actualizarPantalla();
        if (lineas > 0) {
            jugador.sumarPuntos(lineas * 100);
            actualizarPantalla();
            mostrarMensaje("¡" + jugador.getNombre() + " completó " + lineas + " línea(s)! +" + (lineas * 100) + " puntos");
        }
        siguienteTurno();
    }

    private void siguienteTurno() {
        this.turno++;
        if (this.turno == this.jugadores.size()) {
            this.turno = 0;
            this.rondaActual++;
        }
        if (this.rondaActual > this.rondas) {
            this.ganador = jugadorConMasPuntos();
            mostrarMensaje("¡Se acabaron las rondas!\nGanador: " + this.ganador);
            terminarPartida();
            return;
        }
        nuevaPieza();
    }

    //----------------- dibujo -----------------

    private void actualizarPantalla() {
        //1) las piezas que ya estan en el tablero
        for (int f = 0; f < TableroTetris.ALTO; f++) {
            for (int c = 0; c < TableroTetris.ANCHO; c++) {
                char casilla = this.tablero.getCasilla(f, c);
                if (casilla == '.') {
                    pintarVacia(celdas[f][c]);
                } else {
                    int indice = casilla - '1'; //'1' -> 0, '2' -> 1 ...
                    pintarBloque(celdas[f][c], Colores.colorJugador(indice));
                }
            }
        }

        if (this.pieza != null) {
            Color color = Colores.colorJugador(turno);

            //2) la "sombra": solo el contorno de donde va a caer la pieza
            int filaCaida = this.tablero.filaDeCaida(this.pieza, this.columna);
            for (int f = 0; f < this.pieza.getAlto(); f++) {
                for (int c = 0; c < this.pieza.getAncho(); c++) {
                    if (this.pieza.tieneBloque(f, c)) {
                        pintarSombra(celdas[filaCaida + f][this.columna + c], color);
                    }
                }
            }

            //3) la pieza arriba, con el color del jugador
            for (int f = 0; f < this.pieza.getAlto(); f++) {
                for (int c = 0; c < this.pieza.getAncho(); c++) {
                    if (this.pieza.tieneBloque(f, c)) {
                        pintarBloque(celdas[f][this.columna + c], color);
                    }
                }
            }
        }

        //textos
        Jugador jugador = this.jugadores.get(turno);
        lblTurno.setText("TURNO DE " + jugador.getNombre().toUpperCase());
        lblTurno.setForeground(Colores.colorJugador(turno));
        lblRonda.setText("RONDA " + Math.min(rondaActual, rondas) + "/" + rondas
                + "   PIEZA 10 PTS   LINEA 100 PTS");
        for (int i = 0; i < this.jugadores.size(); i++) {
            lblPuntos[i].setText(String.format("%05d", this.jugadores.get(i).getPuntos())); //ej: 00030
            //el jugador que tiene el turno se marca con >
            String marca = (i == turno) ? "> " : "";
            lblNombres[i].setText(marca + (i + 1) + "P " + this.jugadores.get(i).getNombre().toUpperCase());
        }
    }

    //bloque con relieve: borde claro arriba-izquierda y oscuro abajo-derecha (como en los arcades)
    private void pintarBloque(JLabel celda, Color color) {
        celda.setBackground(color);
        celda.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
                color.brighter(), color.darker().darker()));
    }

    private void pintarVacia(JLabel celda) {
        celda.setBackground(Colores.FONDO_CELDA);
        celda.setBorder(BorderFactory.createLineBorder(Colores.BORDE_CELDA, 1));
    }

    private void pintarSombra(JLabel celda, Color color) {
        celda.setBackground(Colores.FONDO_CELDA);
        celda.setBorder(BorderFactory.createLineBorder(color, 2));
    }
}