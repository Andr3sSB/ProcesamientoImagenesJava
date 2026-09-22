import javax.swing.SwingUtilities;
import interfaz.VentanaPrincipal;

public class Main{

    //no ejecuta codigo directamente, solo crea la venta principal y la hace visible
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }//fin main
}//fin clase Main