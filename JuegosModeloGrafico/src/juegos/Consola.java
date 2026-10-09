package juegos;

import juegos.batallanaval.BatallaNaval;
import juegos.estilo.BotonArcade;
import juegos.estilo.Colores;
import juegos.estilo.EstiloArcade;
import juegos.estilo.TextoNeon;
import juegos.tetris.Tetris;
import cityblock.JuegoCityBlock;
import pacman.JuegoPacman;
import snake.JuegoSnake;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

//La CONSOLA de juegos: es la ventana del menu y es la que guarda los juegos.
//CONDICION: solo acepta objetos Jugable (que tengan el metodo start()).
//Cualquier otro objeto se rechaza y no aparece en el menu.
public class Consola extends JFrame {
    private LinkedList<Jugable> juegos;
    private Estadisticas estadisticas;

    public Consola() {
        super("Consola de Juegos");
        this.juegos = new LinkedList<>();
        this.estadisticas = new Estadisticas();

        cargarJuegos();
        armarVentana();
    }

    //==================================================================
    //  AQUI SE INSERTAN LOS JUEGOS EN LA CONSOLA
    //  Para agregar el juego de un compañero:
    //    1) copiar su package dentro de la carpeta src
    //    2) agregar una linea aqui:  insertarJuego(new ElJuegoDeEllos());
    //==================================================================
    private void cargarJuegos() {
        //nuestros juegos
        insertarJuego(new BatallaNaval(this, estadisticas));
        insertarJuego(new Tetris(this, estadisticas));

        //juegos de los compañeros
        insertarJuego(new JuegoSnake(this, estadisticas));
        insertarJuego(new JuegoPacman(this, estadisticas));
        insertarJuego(new JuegoCityBlock(estadisticas));

        //ej. de como se agrega otro:
        //insertarJuego(new ahorcado.Ahorcado());

        //PRUEBA DE LA CONDICION: un Jugador NO tiene start(), asi que la consola lo rechaza
        insertarJuego(new Jugador("Ana"));
    }

    //recibe CUALQUIER objeto y revisa si es Jugable antes de guardarlo
    public boolean insertarJuego(Object objeto) {
        if (objeto instanceof Jugable) {
            Jugable juego = (Jugable) objeto; //se convierte a Jugable para poder guardarlo
            this.juegos.add(juego);
            System.out.println("[CONSOLA] Juego insertado: " + juego.getNombre());
            return true;
        } else {
            System.out.println("[CONSOLA] ERROR: " + objeto.getClass().getSimpleName()
                    + " no es Jugable (no tiene start()), no se puede insertar.");
            return false;
        }
    }

    //arranca el juego que esta en esa posicion con su metodo fijo start()
    public void jugar(int indice) {
        if (indice >= 0 && indice < this.juegos.size()) {
            this.juegos.get(indice).start();
        }
    }

    public LinkedList<Jugable> getJuegos() {
        return juegos;
    }

    //----------------- la ventana del menu -----------------
    //La ventana tiene 2 pantallas que se cambian con un CardLayout (como pasar de una tarjeta a otra):
    //  "MENU"   -> Lanzar Juego / Ver Estadisticas / Salir
    //  "JUEGOS" -> la lista de juegos de la consola + Volver

    private CardLayout pantallas;
    private JPanel contenedor;

    private void armarVentana() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(460, Math.max(600, 330 + juegos.size() * 75)); //crece si hay muchos juegos
        setLocationRelativeTo(null); //centrada en la pantalla

        this.pantallas = new CardLayout();
        this.contenedor = new JPanel(pantallas);
        contenedor.add(crearPantallaMenu(), "MENU");
        contenedor.add(crearPantallaJuegos(), "JUEGOS");
        add(contenedor);

        pantallas.show(contenedor, "MENU");
    }

    //pantalla 1: el menu del pizarron
    private JPanel crearPantallaMenu() {
        JPanel panel = crearPanelArcade(7);

        panel.add(new TextoNeon("CONSOLA", Colores.VERDE, 52));
        panel.add(new TextoNeon("DE JUEGOS", Colores.ROSA, 20));
        JLabel parpadeo = EstiloArcade.crearTexto("- PRESIONA UN BOTON -", Colores.AMARILLO, 15);
        panel.add(parpadeo);

        BotonArcade btnLanzar = new BotonArcade("LANZAR JUEGO", Colores.CIAN, 18);
        BotonArcade btnEstadisticas = new BotonArcade("VER ESTADISTICAS", Colores.AMARILLO, 18);
        BotonArcade btnSalir = new BotonArcade("SALIR", Colores.ROJO, 18);
        btnLanzar.addActionListener(e -> pantallas.show(contenedor, "JUEGOS"));
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());
        btnSalir.addActionListener(e -> System.exit(0));

        panel.add(btnLanzar);
        panel.add(btnEstadisticas);
        panel.add(btnSalir);
        panel.add(EstiloArcade.crearTexto("(C) 2026  INSERT COIN", Colores.GRIS, 12));

        //Timer: cada 500 milisegundos el texto se prende y se apaga, como en las maquinitas
        Timer timer = new Timer(500, e -> {
            if (parpadeo.getForeground().equals(Colores.AMARILLO)) {
                parpadeo.setForeground(Colores.FONDO);
            } else {
                parpadeo.setForeground(Colores.AMARILLO);
            }
        });
        timer.start();
        return panel;
    }

    //pantalla 2: un boton por cada juego que acepto la consola
    private JPanel crearPantallaJuegos() {
        JPanel panel = crearPanelArcade(Math.max(7, juegos.size() + 3));

        panel.add(new TextoNeon("LANZAR JUEGO", Colores.CIAN, 30));
        panel.add(EstiloArcade.crearTexto("- ELIGE TU JUEGO -", Colores.AMARILLO, 15));

        if (juegos.isEmpty()) {
            panel.add(EstiloArcade.crearTexto("NO HAY JUEGOS EN LA CONSOLA", Colores.GRIS, 14));
        }

        Color[] colores = {Colores.VERDE, Colores.ROSA, Colores.AZUL, Colores.CIAN};
        for (int i = 0; i < juegos.size(); i++) {
            int indice = i; //se copia porque el ActionListener necesita una variable fija
            BotonArcade boton = new BotonArcade(juegos.get(i).getNombre().toUpperCase(),
                    colores[i % colores.length], 18);
            boton.addActionListener(e -> {
                pantallas.show(contenedor, "MENU"); //al volver del juego se ve el menu
                jugar(indice);                      //aqui se usa el start() del juego
            });
            panel.add(boton);
        }

        //espacios vacios para que los botones no queden estirados si hay pocos juegos
        for (int i = juegos.size() + 3; i < Math.max(7, juegos.size() + 3); i++) {
            panel.add(new JLabel(""));
        }

        BotonArcade btnVolver = new BotonArcade("< VOLVER", Colores.GRIS, 16);
        btnVolver.addActionListener(e -> pantallas.show(contenedor, "MENU"));
        panel.add(btnVolver);
        return panel;
    }

    //panel negro con borde rosa y una columna de filas
    private JPanel crearPanelArcade(int filas) {
        JPanel panel = new JPanel(new GridLayout(filas, 1, 10, 14));
        panel.setBackground(Colores.FONDO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colores.ROSA, 4),
                BorderFactory.createEmptyBorder(20, 50, 20, 50)));
        return panel;
    }

    private void mostrarEstadisticas() {
        String texto = "========= ESTADISTICAS =========\n\n" + estadisticas.toString();
        JOptionPane.showMessageDialog(this, EstiloArcade.crearPantallaTexto(texto.toUpperCase(), 560, 340),
                "Estadisticas", JOptionPane.PLAIN_MESSAGE);
    }
}
