package cityblock;

import java.util.Scanner;

/** La grua balancea el piso de lado a lado; el jugador lo suelta con ENTER. */
public class Grua {
    public static final int CAMPO = 41;
    public static final int CENTRO = 20;
    public static final int AMPLITUD = 15;

    private final long periodoMs;
    private long inicio;
    private volatile boolean activa;

    public Grua(long periodoMs) {
        this.periodoMs = periodoMs;
    }

    /** Posicion del piso en un instante dado (movimiento de vaiven). */
    public int posicion(long ahora) {
        double fase = 2 * Math.PI * (ahora - inicio) / periodoMs;
        return CENTRO + (int) Math.round(AMPLITUD * Math.sin(fase));
    }

    /** Anima el piso en pantalla hasta que se presione ENTER. Devuelve donde cayo. */
    public int soltar(Scanner entrada) {
        inicio = System.currentTimeMillis();
        activa = true;
        Thread animacion = new Thread(() -> {
            while (activa) {
                System.out.print("\r" + Bloque.linea(posicion(System.currentTimeMillis()), CAMPO, '=') + "  ");
                System.out.flush();
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        animacion.setDaemon(true);
        animacion.start();

        entrada.nextLine();
        long momento = System.currentTimeMillis();
        activa = false;
        try {
            animacion.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int pos = posicion(momento);
        System.out.print("\r" + Bloque.linea(pos, CAMPO, '=') + "  \n");
        return pos;
    }
}
