package interfaz;

import java.awt.Component;
import javax.swing.JOptionPane;

import modelo.Configuracion;

/**
 *   TODA LA VALIDACION
 * formato numerico, rango, valor valido,
**/

public  class PanelConfiguracion {

    private PanelConfiguracion() {
        //No instanciar una clase que solo ofrece metodos estaticos
        }

    public static void solicitarCantidadRegiones(Component padre, Configuracion configuracion)
        {
            int maximo = Configuracion.obtenerMaximoRegiones();
            String mensaje = "Cantidad de regiones para procesar la imagen \n"
            + "Debe ser un numero par, entre 2 y " + maximo + " (nucleos disponibles).";

            while (true) {
                Object entrada = JOptionPane.showInputDialog(
                    padre, mensaje, "Configurar regiones",
                    JOptionPane.QUESTION_MESSAGE, null, null,
                    configuracion.getCantidadRegiones());

                if (entrada == null) {
                    return;
                }

                try {
                    int valor = Integer.parseInt(entrada.toString().trim());
                    configuracion.setCantidadRegiones(valor);
                    return;
                } catch (NumberFormatException ex) {
                    mostrarError(padre, "Debe ingresar un numero entero.");
                } catch (IllegalArgumentException ex) {
                    mostrarError(padre, ex.getMessage());
                }
            }
        }

    public static void solicitarTonalidad(Component padre, Configuracion configuracion) {
        String mensaje = "Tonalidad umbral para la busqueda de patrones. \n" + "Debe ser un numero entero entre 0 y 255";

        while (true) {
            Object entrada = JOptionPane.showInputDialog(
                padre, mensaje, "Configurar tonalidad",
                JOptionPane.QUESTION_MESSAGE, null, null,
                configuracion.getTonalidad());

            if (entrada == null) {
                return;
            }

            try {
                int valor = Integer.parseInt(entrada.toString().trim());;
                configuracion.setTonalidad(valor);
                return; 
            } catch (NumberFormatException ex) {
                mostrarError (padre, "Debe ingresar un numero entero");
            } catch (IllegalArgumentException ex) {
                mostrarError(padre, ex.getMessage());
            }
        }
    } 

    private static void  mostrarError(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Valor invalido", JOptionPane.ERROR_MESSAGE);
    }
}