package procesamiento;

import java.awt.image.BufferedImage;
import java.util.List;

public class Procesar {

    private Procesar() {
        //clase de utilidad
    }

    public static BufferedImage convertirGrisesParalelo(BufferedImage original, int cantidadRegiones)
        throws InterruptedException {
            //Se guardan las dimensiones y se cre la imagen destino, del mismo tamaño
            int ancho = original.getWidth();
            int alto = original.getHeight();
            BufferedImage resultado = new  BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            List<Region> regiones = DivisorRegiones.dividirSinMargen(alto, cantidadRegiones);// Le pide a DivisorRegiones que calcule en que fila empieza y termimna cada region

            Thread[] hilos = new Thread[regiones.size()]; //Arreglo donde se guarda cada hilo creado

            Exception[] errores = new Exception[regiones.size()]; //Arreglo donde cada hilo deja registrada la excepcion que ocrrure

            //Recorre cada region para armar su hilo correspondiente. region guarda la region de esta vuelta
            //indice guarda una copia del numero de vuelta actual
            for(int i = 0; i < regiones.size(); i++) {
                Region region = regiones.get(i);
                int indice = i;

                //Se crea un hilo nuevo pasandole como tarea una funcion lambda en vez de escribir una clase que extienda Thread
                hilos[i] = new Thread(() -> {
                    try {
                        procesarRegion(original, resultado, region);
                    } catch (Exception ex) {
                        errores[indice] = ex;
                    }
                });
            }

                //Aqui se arrancan todos los hilos uno por uno con start()
            for (Thread hilo : hilos) {
                hilo.start();
            }

                //el hilo principal se detiene a esperar uno por uno a que cada hilo de la lista termine. importante que sea ciclo separado de start()
            for(Thread hilo : hilos) {
                hilo.join();
            }

                //Aqui recien se revisa el arreglo de errores, si alguna posicion no quedo en null significa que ese hilo falló
            for(Exception error : errores) {
                if (error != null) {
                    throw new RuntimeException("Falló el procesamiento de una region en paralelo", error);
                }
            }
            return resultado; // si no hubo error devuelve la imagen ya completa
        }

        public static BufferedImage detectarPatronesParalelo(BufferedImage imagenGris, int tonalidad, int cantidadRegiones) throws InterruptedException {
            int alto = imagenGris.getHeight();
            BufferedImage resultado = DetectorPatrones.copiarImagen(imagenGris);

            List<Region> regiones = DivisorRegiones.dividirConMargen(alto, cantidadRegiones, 1);

            Thread[] hilos = new Thread[regiones.size()];
            Exception[] errores = new Exception[regiones.size()];

            for (int i = 0; i < regiones.size(); i++) {
                Region region = regiones.get(i);
                int indice = i;

                hilos[i] = new Thread(() -> {
                    try {
                        DetectorPatrones.procesarRango(imagenGris, resultado, tonalidad, region.getFilaInicioEscritura(), region.getFilaFinEscritura());
                    } catch (Exception ex) {
                        errores[indice] = ex;
                    }
                });
            }

            for (Thread hilo : hilos) {
                hilo.start();
            }

            for (Thread hilo : hilos) {
                hilo.join();
            }

            for (Exception error : errores) {
                if (error != null) {
                    throw new RuntimeException("Falló la busqueda de patrones en una región en paralelo. ", error);
                }
            }

            return resultado;
        }

        private static  void procesarRegion(BufferedImage original, BufferedImage destino, Region region) {
            int ancho = original.getWidth(); // se guarda el ancho una sola vez para no volver a pedirlo

            for(int y = region.getFilaInicioEscritura(); y < region.getFilaFinEscritura(); y++) { // aca el ciclo de filas va desde donde empieza esta region especifica hasta donde termina. segun calculó DivisorRegiones
                //recorre todas las columnas de esa fila, lee el píxel original, le aplica la fórmula, y lo escribe en la imagen destino, en la misma posición.
                for(int x = 0; x < ancho; x++) {
                    int pixelOriginal = original.getRGB(x, y);
                    int pixelGris = Filtros.calcularPixelGris(pixelOriginal);
                    destino.setRGB(x, y, pixelGris);
                }
            }
        }
}