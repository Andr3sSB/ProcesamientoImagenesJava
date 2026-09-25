package procesamiento;

import java.awt.image.BufferedImage;

public class DetectorPatrones{

    //Recorrer los vecinos(lados) de un pixel en pareja por posicion
    private static final int [] DESPLAZAMIENTO_X = {-1, 0, 1, -1, 1, -1, 0, 1};
    private static final int [] DESPLAZAMIENTO_Y = {-1, -1, -1, 0, 0, 1, 1, 1};

    private static final int COLOR_CONTORNO = 0xFFFF0000; //Color rojo

    private DetectorPatrones() {
        //clase de utilidad
    }

    public static BufferedImage detectar (BufferedImage imagenGris, int tonalidad) {
        BufferedImage resultado = copiarImagen(imagenGris);
        procesarRango(imagenGris, resultado, tonalidad, 0, imagenGris.getHeight());
        
        return resultado;
    }

    static void procesarRango(BufferedImage origen, BufferedImage destino, int tonalidad, int filaInicioEscritura, int filaFinEscritura) {
        int ancho = origen.getWidth();
        int alto = origen.getHeight();

        for(int y = filaInicioEscritura; y < filaFinEscritura; y++) {
            for (int x = 0; x < ancho; x++) {
                if (obtenerLuminancia(origen, x, y) <= tonalidad) {
                    continue;
                }
                if (tieneVecinoQueSuperaUmbral(origen, x, y, tonalidad, ancho, alto)) {
                    destino.setRGB(x, y, COLOR_CONTORNO);
                }
            }
        }
    }

    private static boolean tieneVecinoQueSuperaUmbral(BufferedImage origen, int x, int y, int tonalidad, int ancho, int alto) {
        for (int i = 0; i < DESPLAZAMIENTO_X.length; i++) {
            int xVecino = x + DESPLAZAMIENTO_X[i];
            int yVecino = y + DESPLAZAMIENTO_Y[i];

            if (!dentroDeLimites(xVecino, yVecino, ancho, alto)) {
                continue;
            }
            if (obtenerLuminancia(origen, xVecino, yVecino) > tonalidad) {
                return true;
            }
        }
        return false;
    }

    private static boolean dentroDeLimites(int x, int y, int ancho, int alto) { //Valida que la coordenada sea valida, y no se salga de la imagen
        return x >= 0 && x < ancho && y >= 0 && y < alto;
    }

    private static int obtenerLuminancia(BufferedImage imagenGris, int x, int y) { //Se lee la tonalidad del pixel solo en el canal rojo (deja 16 posiciones) y aplicando mascara 0xFF para mantener solo esos 8bits
        int pixel = imagenGris.getRGB(x, y);
        return (pixel >> 16) & 0xFF;
    }

    /* .getRGB
    int rojo = (color >> 16) & 0xFF;   -> extrae el rojo
    int verde = (color >> 8) & 0xFF;   -> extrae el verde
    int azul = color & 0xFF;           -> extrae el azul
    */

    static BufferedImage copiarImagen(BufferedImage original) { //se crea una imagen identica a la original
        int ancho = original.getWidth(); //copia el ancho de la original
        int alto = original.getHeight(); //copia el alto de la original
        BufferedImage copia = new  BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB); //Se crea una "imagen" con las dimensiones de la original

        for (int y = 0; y < alto; y++) { //Se termina de copiar los pixeles de la original
            for (int x = 0; x < ancho; x++) {
                copia.setRGB(x, y, original.getRGB(x, y));
            }
        }
        return copia; //retorna la imagen copiada
    }
}