package interfaz;

import modelo.SesionImagen;
import modelo.Configuracion;
import archivos.Guardador;
import procesamiento.Procesar;

import java.awt.Cursor;
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
import javax.swing.SwingWorker;

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

        //Llama las funciones de la interfaz
    public VentanaPrincipal() {
        super("Editor de Imágenes - Procesamiento");

        configurarVentana();
        construirMenu();
        construirPaneles();

        actualizarEstadoMenu();
    }

        //Configuracion tamaño de la ventana y al salir "X" se termina la ejecucion
    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }


        //Configurar menu superior con sus respectivas opciones
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
            //centra la imagen en el panel y le pone titulo
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
                //Crea el selector de archivos
            JFileChooser selector = new JFileChooser();
            selector.setDialogTitle("Seleccionar imagen");
            selector.setFileFilter(new FileNameExtensionFilter(
                "Imágenes (jpg, jpeg, png, bmp, gif)", "jpg", "jpeg", "png", "bmp", "gif"));

                //cierra el explorador sin hacer nada
            int resultado = selector.showOpenDialog(this);
            if (resultado != JFileChooser.APPROVE_OPTION) {
                return;
            }
                //Obtiene el archivo seleccionado
            File archivoSeleccionado = selector.getSelectedFile();

                //Intenta leer la imagen con ImageIO.read, si no puede devuelve null
            try{
                BufferedImage imagenLeida = ImageIO.read(archivoSeleccionado);
                if (imagenLeida == null) {
                    mostrarError("El archivo seleccionado no tiene el formato correcto");
                    return;
                }

                    //Si la lectura de imagen fue exitosa se guarda en la sesion (ejecicion), y de haber, se borra la imagen de la derecha
                sesion.setImagenOriginal(imagenLeida, archivoSeleccionado);
                mostrarImagenEnEtiqueta(etiquetaImagenOriginal, sesion.getImagenOriginal());
                etiquetaImagenResultado.setIcon(null);
                actualizarEstadoMenu();

                //Manejo de errores inesperados durante la manipulacion de la imagen
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

            //verifica que haya imagen
        private void modificarImagen() {
            if (!sesion.hayImagenCargada()) { //Valida que haya imagen
                return;
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); //Cambia el cursor al de espera
            itemModificarImagen.setEnabled(false); //Deshabilita la opcion del menu "Modificar Imagen" para que  no pueda acceder 2 veces

            SwingWorker<BufferedImage, Void> tarea = new SwingWorker<>() { //Se crea objeto tipo "SwingWorker" produciendo una <BufferedImage, void>
                @Override //Remplaza metodo existente en la clase padre
                protected BufferedImage doInBackground() throws Exception { //ejecuta doInBackground() en hilo separado evitando congelar la ventana
                    BufferedImage resultado = Procesar.convertirGrisesParalelo(sesion.getImagenOriginal(), configuracion.getCantidadRegiones()); //Llama al metodo: Imagen original -> escala de grises
                    Guardador.guardarConsecutivo(resultado, sesion.getArchivoOriginal()); //Guarda la imagen en el disco con nombre_1
                    return resultado;
                }

                @Override 
                protected void done() {
                    setCursor(Cursor.getDefaultCursor()); //Cursor a forma normal
                    try { //Verifica que escalado a grises ocurrio correctamente y guarda en BufferedImage
                        BufferedImage resultado = get();
                        sesion.setImagenGris(resultado);//Guarda la imagen gris en SesionImagen
                        mostrarImagenEnEtiqueta(etiquetaImagenResultado, resultado);//Muestra la imagen en panel derecho
                    } catch (Exception ex) {
                        Throwable causa = (ex.getCause() != null) ? ex.getCause() : ex;
                        mostrarError("No se pudo generar la imagen en escala de grises: " + causa.getMessage());
                    } finally {
                        actualizarEstadoMenu();
                    }
                }
            };
            tarea.execute();
        }

        private void mostrarImagenEnEtiqueta(JLabel etiqueta, java.awt.image.BufferedImage imagen) {
            //limites de la imagen
            int anchoMaximo = 460;
            int altoMaximo = 440;

                    //calcula cuanto hay que reducir la imagen para que quepa en los paneles
            double factor = Math.min(
                (double) anchoMaximo / imagen.getWidth(),
                (double) altoMaximo / imagen.getHeight());
                //evita agrandar una imagen pequeña, por lo que se deja en 1 (tamaño original)
                factor = Math.min(factor, 1.0);

                    //Aplica el factor de escalado - Mantiene la proporcion
                int anchoEscalado = (int) (imagen.getWidth() * factor);
                int altoEscalado = (int) (imagen.getHeight() * factor);

                    //Genera una version reducida y la coloca en la etiqueta, esto no modifica la imagen de SesionImagen
                Image escalada = imagen.getScaledInstance(anchoEscalado, altoEscalado, Image.SCALE_SMOOTH);
                etiqueta.setIcon(new ImageIcon(escalada));
        }

            //habilita opciones dependiendo de factores - modificar imagen depende de que haya una imagen cargada
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