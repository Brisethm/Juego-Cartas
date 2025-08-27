import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Carta {
    public int indice;

    public Carta(Random r){
        indice = r.nextInt(52)+1;
    }
    public void mostrar(JPanel pnl, int x, int y){
        JLabel lblCarta= new JLabel();
        String archivoCarta="images/CARTA"+indice+".jpg";
        ImageIcon imgCarta=new ImageIcon(getClass().getResource(archivoCarta));
        lblCarta.setIcon(imgCarta);
        lblCarta.setBounds(x, y, imgCarta.getIconWidth(), imgCarta.getIconHeight());
        pnl.add(lblCarta);
    }
}
