package juegos;

//Interfaz Jugable: es el "contrato" que deben cumplir TODOS los juegos,
//los nuestros y los que traigan los compañeros.
//
//Para que un juego de otra persona funcione en este menu, su clase solo tiene que:
//   1) importar juegos.Jugable
//   2) poner "implements Jugable"
//   3) escribir su propio metodo start()
//
//IMPORTANTE: todos deben usar ESTE MISMO archivo, en el package "juegos".
public interface Jugable {

    //metodo inicial fijo: el menu lo llama para arrancar el juego
    Puntuacion start();

    //nombre que aparece en el boton del menu.
    //Es "default": si el juego no lo escribe, se usa el nombre de la clase (ej. "Ahorcado").
    default String getNombre() {
        return getClass().getSimpleName();
    }
}
