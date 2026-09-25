package interfaz;

import modelo.SesionImagen;
import modelo.Configuracion;
import archivos.Guardador;
import procesamiento.Procesar;
import archivos.Estadisticas;
import modelo.RegistroEstadistica;

import java.awt.Cursor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.SwingWorker;
import javax.swing.BorderFactory;



public class VentanaPrincipal extends JFrame {


    private  JMenuItem itemConfigurarRegiones;
    private  JMenuItem itemConfigurarTonalidad;
    private  JMenuItem itemModificarImagen;
    private  JMenuItem itemBuscarPatrones;

        //muestra las imagenes
    private JLabel etiquetaImagenOriginal;
    private JLabel etiquetaImagenResultado;

    private JLabel etiquetaEstado;

    private final SesionImagen sesion = new SesionImagen();
    private final Configuracion configuracion = new Configuracion();

        
    public VentanaPrincipal() { //Llama las funciones de la interfaz
        super("Editor de Imágenes - Procesamiento");

        configurarVentana();
        construirMenu();
        construirPaneles();
        construirBarraEstado();

        actualizarEstadoMenu();
        mostrarEstado("Listo");
    }
        
    private void configurarVentana() { //Configuracion tamaño de la ventana y al salir "X" se termina la ejecucion
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }
        
        private void construirMenu(){ //Configurar menu superior con sus respectivas opciones
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
            itemBuscarPatrones.addActionListener(e -> buscarPatrones());
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

        private void construirBarraEstado() {
            etiquetaEstado = new  JLabel();
            etiquetaEstado.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            add(etiquetaEstado, BorderLayout.SOUTH);
        }

        private JLabel crearEtiquetaImagen() {
            JLabel etiqueta = new JLabel();
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
            etiqueta.setVerticalAlignment(SwingConstants.CENTER);
            return etiqueta;
        }
            
        private JScrollPane envolverConTitulo(JLabel etiqueta, String titulo) { //centra la imagen en el panel y le pone titulo
            JPanel contenedor = new JPanel(new BorderLayout());
            contenedor.add(new JLabel(titulo, SwingConstants.CENTER), BorderLayout.NORTH);
            contenedor.add(etiqueta, BorderLayout.CENTER);

            JScrollPane scroll = new JScrollPane(contenedor);
            scroll.setPreferredSize(new Dimension(480, 480));
            return scroll;
        }
        
        private void cargarImagen() { //Acciones del menu
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
                    mostrarEstado("El archivo seleccionado no es una imagen en un formato soportado");
                    return;
                }

                    //Si la lectura de imagen fue exitosa se guarda en la sesion (ejecicion), y de haber, se borra la imagen de la derecha
                sesion.setImagenOriginal(imagenLeida, archivoSeleccionado);
                mostrarImagenEnEtiqueta(etiquetaImagenOriginal, sesion.getImagenOriginal());
                etiquetaImagenResultado.setIcon(null);
                actualizarEstadoMenu();
                mostrarEstado("Imagen cargada: " + archivoSeleccionado.getName());

                //Manejo de errores inesperados durante la manipulacion de la imagen
            } catch (IOException ex) {
                mostrarError("No se pudo leer el archivo: " + ex.getMessage());
                mostrarEstado("Error al cargar la imagen.");
            }
        }

        private void configurarRegiones() {
            PanelConfiguracion.solicitarCantidadRegiones(this, configuracion);
        }

        private void configurarTonalidad() {
            PanelConfiguracion.solicitarTonalidad(this, configuracion);
        }
            
        private void modificarImagen() { //verifica que haya imagen
            if (!sesion.hayImagenCargada()) { //Valida que haya imagen
                return;
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); //Cambia el cursor al de espera
            itemModificarImagen.setEnabled(false); //Deshabilita la opcion del menu "Modificar Imagen" para que  no pueda acceder 2 veces
            mostrarEstado("Convirtiendo a esca de grises con " +configuracion.getCantidadRegiones() + " hilos");

            SwingWorker<ResultadoOperacion, Void> tarea = new SwingWorker<>() { //Se crea objeto tipo "SwingWorker" produciendo una <BufferedImage, void>
                @Override //Remplaza metodo existente en la clase padre
                protected BufferedImage doInBackground() throws Exception { //ejecuta doInBackground() en hilo separado evitando congelar la ventana
                    long inicioNano = System.nanoTime();

                    BufferedImage resultado = Procesar.convertirGrisesParalelo(sesion.getImagenOriginal(), configuracion.getCantidadRegiones());
                    Guardador.guardarConsecutivo(resultado, sesion.getArchivoOriginal());

                    long finNano = System.nanoTime();
                    long tiempoMilisegundos = (finNano - inicioNano) / 1_000_000;
                    registrarEstadistica("grises", sesion.getImagenOriginal(), inicioNano, finNano);

                    return new  ResultadoOperacion(resultado, tiempoMilisegundos);
                }

                @Override 
                protected void done() {
                    setCursor(Cursor.getDefaultCursor()); //Cursor a forma normal
                    try { //Verifica que escalado a grises ocurrio correctamente y guarda en BufferedImage
                        ResultadoOperacion resultadoOperacion = get();
                        sesion.setImagenGris(resultadoOperacion.imagen);//Guarda la imagen gris en SesionImagen
                        mostrarImagenEnEtiqueta(etiquetaImagenResultado, resultadoOperacion.imagen);//Muestra la imagen en panel derecho
                        mostrarEstado("Conversion completa en " + resultadoOperacion.tiempoMilisegundos + " ms");
                    } catch (Exception ex) {
                        Throwable causa = (ex.getCause() != null) ? ex.getCause() : ex;
                        mostrarError("No se pudo generar la imagen en escala de grises: " + causa.getMessage());
                        mostrarEstado("Error al convertir imagen.");
                    } finally {
                        actualizarEstadoMenu();
                    }
                }
            };
            tarea.execute();
        }

        private  void buscarPatrones() { //Evita acceder a esta funcionalidad si no hay imagen gris
            if (!sesion.hayImagenGris()) {
                return;
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); //Cambia el cursor y desactiva la opcion "Buscar patrones" del menu
            itemBuscarPatrones.setEnabled(false);
            mostrarEstado("Buscando patrones con " + configuracion.getCantidadRegiones() + " hilos.");

            SwingWorker <ResultadoOperacion, Void> tarea = new SwingWorker<>() { //Recorre toda la imagen buscando patrones, se ejecuta en un hilo aparte al de la interfaz para que no se congele
                @Override
                //reparte la imagen en regiones
                protected ResultadoOperacion doInBackground() throws Exception {
                    long inicioNano = System.nanoTime();

                    BufferedImage resultado = Procesar.detectarPatronesParalelo(sesion.getImagenGris(), configuracion.getTonalidad(), configuracion.getCantidadRegiones());

                    long finNano = System.nanoTime();
                    long tiempoMilisegundos = (finNano - inicioNano) / 1_000_000;
                    registrarEstadistica("patrones", sesion.getImagenGris(), tiempoMilisegundos);

                    return new ResultadoOperacion(resultado, tiempoMilisegundos);
                }

                // Si todo sale bien guarda el resultado en la sesion, se muestra en pantalla y consulta si se guarda la imagen con el metodo
                @Override 
                protected void done() {
                    setCursor(Cursor.getDefaultCursor());

                    try {
                        ResultadoOperacion resultadoOperacion = get();
                        sesion.setImagenContornos(resultadoOperacion.imagen);
                        mostrarImagenEnEtiqueta(etiquetaImagenOriginal, resultadoOperacion.imagen);
                        mostrarEstado("Busqueda de patrones completada en " + resultadoOperacion.tiempoMilisegundos + " ms");

                        preguntarSiGuardarContornos(resultadoOperacion.imagen);
                    } catch (Exception ex) {
                        Throwable causa = (ex.getCause() != null) ? ex.getCause() : ex;
                        mostrarError("No se pudo completar la busqueda de patrones: " +causa.getMessage());
                        mostrarEstado("Error en la busqueda de patrones");
                    }finally { //El finallly actualiza el menu si o si
                        actualizarEstadoMenu();
                    }
                }
            };
            tarea.execute();
        }
        
        private void registrarEstadistica(String operacion, BufferedImage imagenBase, long tiempoMilisegundos) {
            try {
                long tamañoArchivoBytes = sesion.getArchivoOriginal().length();

                RegistroEstadistica registro = new RegistroEstadistica(operacion, tamañoArchivoBytes, imagenBase.getWidth(), imagenBase.getHeight(), configuracion.getCantidadRegiones(), tiempoMilisegundos, LocalDateTime.now());
                
                Estadisticas.registrar(registro);
            } catch (RuntimeException ex) {
                System.err.println("No se pudo armar el registro de estadisticas: " + ex.getMessage());
            }
        }

        private static final class ResultadoOperacion {
            private final BufferedImage imagen;
            private final long tiempoMilisegundos;

            private ResultadoOperacion(BufferedImage imagen, long tiempoMilisegundos) {
                this.imagen = imagen;
                this.tiempoMilisegundos = tiempoMilisegundos;
            }
        }

        private void preguntarSiGuardarContornos(BufferedImage imagenContornos) {
            //Muestra opciones "Si" "No"
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea guardar la imagen con los contornos marcados?", "Guardar resultado", JOptionPane.YES_NO_OPTION);

            if (respuesta != JOptionPane.YES_OPTION) {
                return; //Se decidio guardar | "No" o se cierra el cuadro se termina el metodo sin guardar y sin mostrar error
            }
            try { //Si se guarda se reutiliza el guardador
                Guardador.guardarConsecutivo(imagenContornos, sesion.getArchivoOriginal());
            } catch (IOException ex) {
                mostrarError("No se pudo guardar la imagen: " + ex.getMessage());
            }
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
            
        private void actualizarEstadoMenu () { //habilita opciones dependiendo de factores - modificar imagen depende de que haya una imagen cargada
            itemConfigurarRegiones.setEnabled(true);
            itemConfigurarTonalidad.setEnabled(true);
            itemModificarImagen.setEnabled(sesion.hayImagenCargada());
            itemBuscarPatrones.setEnabled(sesion.hayImagenGris());
        }

        private void mostrarEstado(String mensaje) {
            etiquetaEstado.setText(mensaje);
        }

        private void mostrarError(String mensaje) {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
      
}