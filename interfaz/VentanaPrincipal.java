package interfaz;

import modelo.SesionImagen;
import modelo.Configuracion;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

public class VentanaPrincipal extends JFrame {


    private  JMenuItem itemConfigurarRegiones;
    private  JMenuItem itemConfigurarTonalidad;
    private  JMenuItem itemModificarImagen;
    private  JMenuItem itemBuscarPatrones;

        //muestra las imagenes
    private JLabel etiquetaImagenOriginal;
    private JLabel etiquetaImagenResultado;

    private final SesionImagen sesion = new SesionImagen();
    private final Configuracion configuracion = new Configuracion();

    public VentanaPrincipal() {
        super("Editor de Imágenes - Procesamiento");

        configurarVentana();
        construirMenu();
        construirPaneles();

        actualizarEstadoMenu();
    }

        //Configuracion de la Ventana
    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }


        //Construccion del Menu
        private void construirMenu(){
            JMenuBar barraMenu = new JMenuBar();
            JMenu menuArchivo = new JMenu("Archivo");
            JMenu menuProcesar = new JMenu("Procesar");

                //Buscar y cargar imagen
            JMenuItem itemCargarImagen = new JMenuItem("Cargar imagen...");
            itemCargarImagen.addActionListener(e -> cargarImagen());
            menuArchivo.add(itemCargarImagen);


                //Determinar la cantidad de regiones
            itemConfigurarRegiones = new  JMenuItem("Configurar regiones...");
            itemConfigurarRegiones.addActionListener(e -> configurarRegiones());
            menuProcesar.add(itemConfigurarRegiones);

                //Determinar tonalidad
            itemConfigurarTonalidad = new  JMenuItem("Configurar tonalidad...");
            itemConfigurarTonalidad.addActionListener(e -> configurarTonalidad());
            menuProcesar.add(itemConfigurarTonalidad);

            menuProcesar.addSeparator();

                //Escala de grises
            itemModificarImagen = new JMenuItem("Convertir a escala de grises");
            itemModificarImagen.addActionListener(e -> modificarImagen());
            menuProcesar.add(itemModificarImagen);

                //Busqueda de patrones
            itemBuscarPatrones = new JMenuItem("Buscar patrones");
            itemBuscarPatrones.addActionListener(e -> modificarImagen());
            menuProcesar.add(itemBuscarPatrones);

            barraMenu.add(menuArchivo);
            barraMenu.add(menuProcesar);
            setJMenuBar(barraMenu);
        }

        private void construirPaneles() {
            JPanel panelImagenes = new JPanel(new GridLayout(1, 2, 10, 0));

            etiquetaImagenOriginal = crearEtiquetaImagen();
            etiquetaImagenResultado = crearEtiquetaImagen();

            panelImagenes.add(envolverConTitulo(etiquetaImagenOriginal, "ImagenOriginal"));
            panelImagenes.add(envolverConTitulo(etiquetaImagenResultado, "Resultado"));

            add(panelImagenes, BorderLayout.CENTER);
        }

        private JLabel crearEtiquetaImagen() {
            JLabel etiqueta = new JLabel();
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
            etiqueta.setVerticalAlignment(SwingConstants.CENTER);
            return etiqueta;
        }

        private JScrollPane envolverConTitulo(JLabel etiqueta, String titulo) {
            JPanel contenedor = new JPanel(new BorderLayout());
            contenedor.add(new JLabel(titulo, SwingConstants.CENTER), BorderLayout.NORTH);
            contenedor.add(etiqueta, BorderLayout.CENTER);

            JScrollPane scroll = new JScrollPane(contenedor);
            scroll.setPreferredSize(new Dimension(480, 480));
            return scroll;
        }

        //Acciones del menu
        private void cargarImagen() {
            JFileChooser selector = new JFileChooser();
            selector.setDialogTitle("Seleccionar imagen");

            selector.setFileFilter(new FileNameExtensionFilter(
                "Imágenes (jpg, jpeg, png, bmp, gif)", "jpg", "jpeg", "png", "bmp", "gif"));

            int resultado = selector.showOpenDialog(this);
            if (resultado != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File archivoSeleccionado = selector.getSelectedFile();

            try{
                BufferedImage imagenLeida = ImageIO.read(archivoSeleccionado);

                if (imagenLeida == null) {
                    mostrarError("El archivo seleccionado no tiene el formato correcto");
                    return;
                }

                sesion.setImagenOriginal(imagenLeida, archivoSeleccionado);

                mostrarImagenEnEtiqueta(etiquetaImagenOriginal, sesion.getImagenOriginal());

                etiquetaImagenResultado.setIcon(null);

                actualizarEstadoMenu();
            } catch (IOException ex) {
                mostrarError("No se pudo leer el archivo: " + ex.getMessage());
            }
        }

        private void configurarRegiones() {
            PanelConfiguracion.solicitarCantidadRegiones(this, configuracion);
        }

        private void configurarTonalidad() {
            PanelConfiguracion.solicitarTonalidad(this, configuracion);
        }

        private void modificarImagen() {
            JOptionPane.showMessageDialog(this, 
                "Pendiente: busqueda de patrones con hilos",
                "Funcionalidad pendiente", JOptionPane.INFORMATION_MESSAGE);
        }

        private void mostrarImagenEnEtiqueta(JLabel etiqueta, java.awt.image.BufferedImage imagen) {
            int anchoMaximo = 460;
            int altoMaximo = 440;

            double factor = Math.min(
                (double) anchoMaximo / imagen.getWidth(),
                (double) altoMaximo / imagen.getHeight());
                factor = Math.min(factor, 1.0);

                int anchoEscalado = (int) (imagen.getWidth() * factor);
                int altoEscalado = (int) (imagen.getHeight() * factor);

                Image escalada = imagen.getScaledInstance(anchoEscalado, altoEscalado, Image.SCALE_SMOOTH);
                etiqueta.setIcon(new ImageIcon(escalada));
        }

        private void actualizarEstadoMenu () {


            itemConfigurarRegiones.setEnabled(true);
            itemConfigurarTonalidad.setEnabled(true);
            itemModificarImagen.setEnabled(sesion.hayImagenCargada());
            itemBuscarPatrones.setEnabled(sesion.hayImagenGris());
        }

        private void mostrarError(String mensaje) {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }

        
}