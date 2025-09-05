
import java.util.Random;
import javax.swing.JPanel;

public class Jugador {

    private final int TOTAL_CARTAS = 10;
    private final int SEPARACION = 40;
    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Random r = new Random();

    public void repartir() {
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i].mostrar(pnl, 10 + i * SEPARACION, 10);
        }
        pnl.repaint();
    }

    public String getGrupos() {
        String resultado = "No se encontraron grupos";
        //iniciar los contadores
        int[] contadores = new int[13];
        for (Carta carta : cartas) {
            contadores[carta.getNombre().ordinal()]++;
        }
        //obtener los resultados
        boolean hayGrupos = false;
        for (int c : contadores) {
            if (c >= 2) {
                hayGrupos = true;
            }
        }
        if (hayGrupos) {
            resultado = "Se encontraron los siguientes grupos:\n";
            int p = 0;
            for (int c : contadores) {
                if (c >= 2) {
                    resultado += Grupo.values()[c] + " de " + NombreCarta.values()[p] + "\n";
                }
                p++;
            }
        }
        return resultado;
    }

    public String getEscaleras() {
        String resultado = "No se encontraron escaleras\n";
        boolean hayEscalera=false;
        Carta[] cartasEnEscalera = new Carta[TOTAL_CARTAS];
        int totalCartasEnEscalera = 0;

        for (Pinta pinta : Pinta.values()) {
            // Filtrar cartas por pinta
            Carta[] cartasDePinta = new Carta[TOTAL_CARTAS];
            int count = 0;
            for (Carta carta : cartas) {
                if (carta.getPinta() == pinta) {
                    cartasDePinta[count++] = carta;
                }
            }

            if (count < 3) {
                continue; // los grupos requieren al menos 3 cartas
            }

            // Ordena por índice de menor a mayor
            for (int i = 0; i < count - 1; i++) {
                for (int j = 0; j < count - i - 1; j++) {
                    if (cartasDePinta[j].indice > cartasDePinta[j + 1].indice) {
                        Carta temp = cartasDePinta[j];
                        cartasDePinta[j] = cartasDePinta[j + 1];
                        cartasDePinta[j + 1] = temp;
                    }
                }
            }

            // Busca escaleras consecutivas
            int inicioEscalera = 0;
            int escaleraActual = 1;

            for (int i = 1; i <= count; i++) {
                if (i < count && cartasDePinta[i].indice == cartasDePinta[i - 1].indice + 1) {
                    escaleraActual++;
                } else {
                    if (escaleraActual >= 3) {
                        hayEscalera = true;
                        resultado = "Se encontraron las siguientes escaleras:\n";
                        resultado += Grupo.values()[escaleraActual] + " de " + pinta.name() + "\n";

                        // Agrega las cartas de la escalera
                        for (int j = inicioEscalera; j < inicioEscalera + escaleraActual; j++) {
                            if (totalCartasEnEscalera < TOTAL_CARTAS) {
                                cartasEnEscalera[totalCartasEnEscalera++] = cartasDePinta[j];
                            }
                        }
                    }
                    inicioEscalera = i;
                    escaleraActual = 1;
                }
            }
        }

        // Calcula puntos por cartas que NO están en escaleras
        int puntos = 0;
        for (Carta carta : cartas) {
            boolean enEscalera = false;

             for (int j = 0; j < totalCartasEnEscalera; j++) {
                if (cartasEnEscalera[j].getNombre() == carta.getNombre()
                        && cartasEnEscalera[j].getPinta() == carta.getPinta()) {
                    enEscalera = true;
                    break;
                }
            }

            if (!enEscalera) {
                puntos += carta.getNombre().ordinal() + 1;
            }
        }

        if (!hayEscalera) {
            resultado = "No se encontraron escaleras\n";
        }
        resultado += "Puntos por cartas no en escalera: " + puntos;
        return resultado;
    }

}
