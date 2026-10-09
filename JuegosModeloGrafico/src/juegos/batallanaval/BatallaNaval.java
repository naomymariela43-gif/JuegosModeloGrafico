package juegos.batallanaval;

import juegos.Estadisticas;
import juegos.Juego;
import juegos.Jugable;
import juegos.Jugador;
import juegos.estilo.BotonArcade;
import juegos.estilo.Colores;
import juegos.estilo.EstiloArcade;
import juegos.estilo.TextoNeon;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

//Batalla naval para 2 jugadores con ventana.
//1) Cada jugador esconde sus barcos haciendo clic en el tablero (sin que el otro mire).
//2) Por turnos cada uno hace clic en el tablero del rival para disparar.
public class BatallaNaval extends Juego implements Jugable {
    //tableros.get(0) es del jugador 1 y tableros.get(1) del jugador 2
    private LinkedList<Tablero> tableros;
    private JButton[][] botones;
    private TextoNeon lblTitulo;
    private JLabel lblInfo;
    private BotonArcade btnDireccion;

    private boolean colocando;      //true mientras se esconden barcos, false cuando se dispara
    private int turno;              //0 = jugador 1, 1 = jugador 2
    private LinkedList<Barco> flota; //barcos que le faltan por esconder al jugador
    private int barcoActual;
    private boolean horizontal;
    private long inicioTurno;

    public BatallaNaval(JFrame menu, Estadisticas estadisticas) {
        super("Batalla Naval", menu, estadisticas);
        this.tableros = new LinkedList<>();
        this.botones = new JButton[Tablero.TAMANO][Tablero.TAMANO];
    }

    //metodo de la interfaz Jugable: aqui arranca el juego
    @Override
    public void start() {
        reiniciar(); //por si ya se habia jugado antes
        this.tableros = new LinkedList<>();
        this.botones = new JButton[Tablero.TAMANO][Tablero.TAMANO];

        if (!pedirJugadores(2)) {
            cancelar(); //cancelo, se vuelve al menu
            return;
        }
        this.tableros.add(new Tablero());
        this.tableros.add(new Tablero());

        armarVentana();
        comenzarCronometro();
        empezarColocacion(0);
    }

    private void armarVentana() {
        setSize(580, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        //----- arriba: textos -----
        JPanel arriba = new JPanel(new GridLayout(2, 1));
        arriba.setBorder(BorderFactory.createEmptyBorder(12, 10, 0, 10));
        lblTitulo = new TextoNeon("", Colores.CIAN, 22);
        lblInfo = EstiloArcade.crearTexto("", Colores.AMARILLO, 14);
        arriba.add(lblTitulo);
        arriba.add(lblInfo);
        add(arriba, BorderLayout.NORTH);

        //----- centro: el tablero con numeros de fila y columna -----
        JPanel centro = new JPanel(new GridLayout(Tablero.TAMANO + 1, Tablero.TAMANO + 1, 4, 4));
        centro.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 30));
        centro.add(new JLabel(""));
        for (int c = 0; c < Tablero.TAMANO; c++) {
            centro.add(EstiloArcade.crearTexto(String.valueOf(c + 1), Colores.ROSA, 18));
        }
        for (int f = 0; f < Tablero.TAMANO; f++) {
            centro.add(EstiloArcade.crearTexto(String.valueOf(f + 1), Colores.ROSA, 18));
            for (int c = 0; c < Tablero.TAMANO; c++) {
                JButton boton = new JButton();
                boton.setFont(EstiloArcade.fuente(28));
                boton.setContentAreaFilled(false); //quita el estilo gris de Java
                boton.setOpaque(true);
                boton.setFocusPainted(false);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                int fila = f;       //se copian porque el ActionListener necesita variables fijas
                int columna = c;
                boton.addActionListener(e -> clicCasilla(fila, columna));
                botones[f][c] = boton;
                centro.add(boton);
            }
        }
        add(centro, BorderLayout.CENTER);

        //----- abajo: boton de direccion y leyenda -----
        JPanel abajo = new JPanel(new GridLayout(2, 1, 5, 8));
        abajo.setBorder(BorderFactory.createEmptyBorder(0, 15, 12, 15));
        btnDireccion = new BotonArcade("", Colores.ROSA, 15);
        btnDireccion.addActionListener(e -> {
            horizontal = !horizontal;
            actualizarTextos();
        });
        JLabel leyenda = EstiloArcade.crearTexto("<html><font color='" + Colores.hex(Colores.ROJO) + "'>X = TOCADO</font>"
                + " &nbsp; <font color='" + Colores.hex(Colores.AZUL) + "'>O = AGUA</font>"
                + " &nbsp; <font color='" + Colores.hex(Colores.GRIS) + "'>TOCADO 10 PTS &nbsp; HUNDIDO 30 PTS</font></html>",
                Colores.GRIS, 13);
        abajo.add(btnDireccion);
        abajo.add(leyenda);
        add(abajo, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void clicCasilla(int fila, int columna) {
        if (colocando) {
            colocarBarco(fila, columna);
        } else {
            disparar(fila, columna);
        }
    }

    //----------------- FASE 1: esconder barcos -----------------

    private void empezarColocacion(int jugador) {
        this.turno = jugador;
        this.colocando = true;
        this.barcoActual = 0;
        this.horizontal = true;
        this.flota = new LinkedList<>();
        this.flota.add(new Barco("Acorazado", 3));
        this.flota.add(new Barco("Submarino", 2));
        this.flota.add(new Barco("Lancha", 2));

        taparTablero();
        lblTitulo.setText("ESCONDER BARCOS");
        lblInfo.setText("");
        mostrarMensaje(nombreTurno() + ", te toca esconder tus barcos.\nQue el otro jugador no mire la pantalla.");

        btnDireccion.setVisible(true);
        pintarTablero(this.tableros.get(turno), true);
        actualizarTextos();
    }

    private void colocarBarco(int fila, int columna) {
        Barco barco = this.flota.get(barcoActual);
        boolean colocado = this.tableros.get(turno).colocarBarco(barco, fila, columna, horizontal);
        if (!colocado) {
            mostrarMensaje("El barco se sale del tablero o choca con otro barco.");
            return;
        }

        barcoActual++;
        pintarTablero(this.tableros.get(turno), true);
        if (barcoActual < this.flota.size()) {
            actualizarTextos();
            return;
        }

        //ya escondio todos sus barcos
        mostrarMensaje("Listo " + nombreTurno() + ", tus barcos están escondidos.");
        if (turno == 0) {
            empezarColocacion(1);
        } else {
            colocando = false;
            btnDireccion.setVisible(false);
            empezarTurnoDisparo(0);
        }
    }

    //----------------- FASE 2: disparar -----------------

    private void empezarTurnoDisparo(int jugador) {
        this.turno = jugador;
        taparTablero();
        lblTitulo.setText("CAMBIO DE TURNO");
        lblInfo.setText("");
        mostrarMensaje("Turno de " + nombreTurno() + " para disparar.\nQue el otro jugador no mire la pantalla.");

        //solo se ve el tablero del rival, sin sus barcos
        pintarTablero(this.tableros.get(1 - turno), false);
        actualizarTextos();
        this.inicioTurno = System.currentTimeMillis();
    }

    private void disparar(int fila, int columna) {
        int rival = 1 - turno; //si turno es 0 el rival es 1, y al reves
        Tablero tableroRival = this.tableros.get(rival);
        Jugador jugador = this.jugadores.get(turno);

        String resultado = tableroRival.disparar(fila, columna);
        if (resultado.equals("REPETIDO")) {
            mostrarMensaje("Ya disparaste ahí, escoge otra casilla.");
            return;
        }

        jugador.sumarMovimiento();
        String texto;
        if (resultado.equals("AGUA")) {
            texto = "¡Agua!";
        } else if (resultado.equals("TOCADO")) {
            texto = "¡Tocado! +10 puntos";
            jugador.sumarPuntos(10);
        } else {
            texto = "¡Hundido! +30 puntos";
            jugador.sumarPuntos(30);
        }
        jugador.sumarTiempo(System.currentTimeMillis() - inicioTurno);

        pintarTablero(tableroRival, false);
        actualizarTextos();

        if (tableroRival.todosHundidos()) {
            this.ganador = jugador.getNombre();
            pintarTablero(tableroRival, true); //al final se ensena todo
            mostrarMensaje(texto + "\n\n" + this.ganador + " hundió todos los barcos y GANA!");
            terminarPartida();
            return;
        }

        mostrarMensaje(texto);
        empezarTurnoDisparo(rival);
    }

    //----------------- dibujo -----------------

    //pinta cada boton segun lo que tiene la casilla
    private void pintarTablero(Tablero tablero, boolean mostrarBarcos) {
        for (int f = 0; f < Tablero.TAMANO; f++) {
            for (int c = 0; c < Tablero.TAMANO; c++) {
                char simbolo = tablero.getCasilla(f, c);

                if (simbolo == 'X') {
                    pintarCasilla(botones[f][c], Colores.ROJO, Colores.ROJO.brighter(), "X", Colores.FONDO);
                } else if (simbolo == 'o') {
                    pintarCasilla(botones[f][c], Colores.AZUL, Colores.CIAN, "O", Color.WHITE);
                } else if (mostrarBarcos && tablero.hayBarco(f, c)) {
                    pintarCasilla(botones[f][c], Colores.BARCO, Colores.GRIS, "", Color.WHITE);
                } else {
                    pintarCasilla(botones[f][c], Colores.AGUA, Colores.BORDE_AGUA, "", Color.WHITE);
                }
            }
        }
    }

    //deja todo oscuro para que no se vea nada entre turnos
    private void taparTablero() {
        for (int f = 0; f < Tablero.TAMANO; f++) {
            for (int c = 0; c < Tablero.TAMANO; c++) {
                pintarCasilla(botones[f][c], Colores.TAPADO, Colores.BORDE_CELDA, "", Color.WHITE);
            }
        }
    }

    private void pintarCasilla(JButton boton, Color fondo, Color borde, String texto, Color colorTexto) {
        boton.setBackground(fondo);
        boton.setBorder(BorderFactory.createLineBorder(borde, 2));
        boton.setText(texto);
        boton.setForeground(colorTexto);
    }

    private void actualizarTextos() {
        Jugador jugador = this.jugadores.get(turno);
        if (colocando) {
            Barco barco = this.flota.get(barcoActual);
            lblTitulo.setText(nombreTurno().toUpperCase() + ": ESCONDE TUS BARCOS");
            lblInfo.setText("CLIC DONDE EMPIEZA EL " + barco.getNombre().toUpperCase()
                    + " (TAMAÑO " + barco.getTamano() + ")");
            btnDireccion.setText("DIRECCION: " + (horizontal ? "HORIZONTAL" : "VERTICAL") + "  [CAMBIAR]");
        } else {
            lblTitulo.setText(nombreTurno().toUpperCase() + " DISPARA!");
            lblInfo.setText("TABLERO DE " + this.jugadores.get(1 - turno).getNombre().toUpperCase()
                    + "   |   PUNTOS " + String.format("%04d", jugador.getPuntos())
                    + "   DISPAROS " + jugador.getMovimientos());
        }
    }

    private String nombreTurno() {
        return this.jugadores.get(turno).getNombre();
    }
}
