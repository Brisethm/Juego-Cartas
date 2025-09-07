
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

    public String getResumen() {
        String resultado = "";

        // GRUPOS

        int[] contadores = new int[13];
        for (Carta carta : cartas) {
            contadores[carta.getNombre().ordinal()]++;
        }

        boolean hayGrupos = false;
        boolean[] enGrupo = new boolean[TOTAL_CARTAS];

        for (int c : contadores) {
            if (c >= 2) {
                hayGrupos = true;
            }
        }

        if (hayGrupos) {
            for (int i = 0; i < contadores.length; i++) {
                int c = contadores[i];
                if (c >= 2) {
                    resultado += Grupo.values()[c] + " de " + NombreCarta.values()[i] + "\n";
                    // marcar cartas en grupo
                    int marcadas = 0;
                    for (int j = 0; j < TOTAL_CARTAS && marcadas < c; j++) {
                        if (cartas[j].getNombre().ordinal() == i) {
                            enGrupo[j] = true;
                            marcadas++;
                        }
                    }
                }
            }
        } else {
            resultado += "No se encontraron grupos\n";
        }

        // ESCALERAS

        boolean hayEscalera = false;
        boolean[] enEscalera = new boolean[TOTAL_CARTAS];

        for (Pinta pinta : Pinta.values()) {
            Carta[] cartasDePinta = new Carta[TOTAL_CARTAS];
            int count = 0;
            for (int i = 0; i < TOTAL_CARTAS; i++) {
                if (cartas[i].getPinta() == pinta) {
                    cartasDePinta[count++] = cartas[i];
                }
            }

            if (count < 3) {
                continue;
            }

            // ordenar
            for (int i = 0; i < count - 1; i++) {
                for (int j = 0; j < count - i - 1; j++) {
                    if (cartasDePinta[j].indice > cartasDePinta[j + 1].indice) {
                        Carta temp = cartasDePinta[j];
                        cartasDePinta[j] = cartasDePinta[j + 1];
                        cartasDePinta[j + 1] = temp;
                    }
                }
            }

            int inicioEscalera = 0;
            int escaleraActual = 1;

            for (int i = 1; i <= count; i++) {
                if (i < count && cartasDePinta[i].indice == cartasDePinta[i - 1].indice + 1) {
                    escaleraActual++;
                } else {
                    if (escaleraActual >= 3) {
                        hayEscalera = true;
                        resultado += "\n" + Grupo.values()[escaleraActual] + " de "
                                + cartasDePinta[inicioEscalera].getNombre() + " a "
                                + cartasDePinta[inicioEscalera + escaleraActual - 1].getNombre()
                                + " de " + pinta.name() + "\n";

                        // marcar cartas en escalera
                        for (int j = inicioEscalera; j < inicioEscalera + escaleraActual; j++) {
                            for (int k = 0; k < TOTAL_CARTAS; k++) {
                                if (cartas[k] == cartasDePinta[j]) {
                                    enEscalera[k] = true;
                                }
                            }
                        }
                    }
                    inicioEscalera = i;
                    escaleraActual = 1;
                }
            }
        }

        if (!hayEscalera) {
            resultado += "\n" + "No se encontraron escaleras\n";
        }

        // SOBRANTES

        resultado += "\nSobran:\n";
        int puntos = 0;
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            if (!enGrupo[i] && !enEscalera[i]) {
                resultado += cartas[i].getNombre() + " de " + cartas[i].getPinta() + "\n";
                puntos += cartas[i].getNombre().ordinal() + 1;
            }
        }


        // PUNTOS

        resultado += "\nPuntos: " + puntos;

        return resultado;
    }
}
