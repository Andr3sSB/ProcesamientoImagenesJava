package procesamiento;

import java.awt.image.BufferedImage;

//Convierte imagen completa a grises

public class ConversorGrises {

        private ConversorGrises() {

        }
            //recorre la imagen pixel por pixel
        public static BufferedImage convertir(BufferedImage original) {
                //Se guardan las dimensiones para no consultarlas en cada ciclo
            int ancho = original.getWidth();
            int alto = original.getHeight();

                // Se crea la imagen destino, del mismo tamaño que la original, que es distinta en memoria, nunca se sobrescribe la original
            BufferedImage resultado = new  BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            //Doble "for" para recorrer cada pixel de la imagen (filas y columnas)
            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    //Por cada posición: se lee el pixel de la imagen fuente, se le aplica la formula, y se escribe el resultado en la imagen destino
                    int pixelOriginal = original.getRGB(x, y);
                    int pixelGris = Filtros.calcularPixelGris(pixelOriginal);
                    resultado.setRGB(x, y, pixelGris);
                }
            }
            return resultado; // Devuelve la imagen completa
        }
}