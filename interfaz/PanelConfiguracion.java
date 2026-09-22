package interfaz;

import java.awt.Component;
import javax.swing.JOptionPane;

import modelo.Configuracion;


// VALIDACIONES GENERALES


public  class PanelConfiguracion {

    private PanelConfiguracion() {
        //No instanciar una clase que solo ofrece metodos estaticos
        }

    public static void solicitarCantidadRegiones(Component padre, Configuracion configuracion)
        {
                //Muestra el texto que incluye el máximo disponible de hilos
            int maximo = Configuracion.obtenerMaximoRegiones();
            String mensaje = "Cantidad de regiones para procesar la imagen \n"
            + "Debe ser un numero par, entre 2 y " + maximo + " (nucleos disponibles).";

            while (true) { //Ciclo infinito hasta que el usuario cancele o ingrese un valor valido

                    //Muestra el texto con el valor actual precargado como sugerencia
                Object entrada = JOptionPane.showInputDialog(
                    padre, mensaje, "Configurar regiones",
                    JOptionPane.QUESTION_MESSAGE, null, null,
                    configuracion.getCantidadRegiones());

                        //Si el usuario cierra el cuadro o presiona “Cancelar”, se devuelve “null” para que no muestre un error
                if (entrada == null) {
                    return;
                }

                //Se intenta convertir el texto a número, trim() quita espacios, y se lo pasa a Configuración,
                //que aplica nuevas validaciones. Si se cumple todo, return termina el método con éxito
                try {
                    int valor = Integer.parseInt(entrada.toString().trim());
                    configuracion.setCantidadRegiones(valor);
                    return;
                    //Dos validaciones: NumberFormatException, si el texto no era un numero en absoluto
                    //IllegalArgumentException, Si era un numero pero no cumplía las reglas de Configuracion. En ningún caso hay return por lo que muestra el mensaje y reinicia el while

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