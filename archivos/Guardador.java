package archivos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//guarda imagen en disco, en la misma carpeta y con el mismo nombre base del archivo original agregando un numero consecutivo para no sobreescribir

public class Guardador {
    private static  final int MAXIMO_INTENTOS = 1000; //evitar bucle infinito

    private Guardador() {

    }

        //Guarda la imagen:   foto1  , foto2  ,  foto3  ,  foto4
    public static File guardarConsecutivo(BufferedImage imagen, File archivoOriginal) throws IOException {
        String rutaCompleta = archivoOriginal.getAbsolutePath(); //Toma la ruta completa del archivo original

            //Se busca la posición del ultimo separador de carpeta y la posición del ultimo punto en la ruta
        int posicionSeparador = Math.max(
            rutaCompleta.lastIndexOf(File.separatorChar),
            rutaCompleta.lastIndexOf('/'));
        int posicionPunto = rutaCompleta.lastIndexOf('.');

        String nombreBase;
        String extension;

        //Si el último punto aparece después del ultimo separador, ese punto si pertenece al nombre del archivo, y ahí se corta
        if (posicionPunto > posicionSeparador) {
            nombreBase = rutaCompleta.substring(0, posicionPunto);
            extension = rutaCompleta.substring(posicionPunto +1);
        } else {
            //El archivo no tiene extension detectable
            nombreBase = rutaCompleta;
            extension = "png";
        }

        File archivoDestino = buscarNombreDisponible(nombreBase, extension); //Delega a un método aparte la búsqueda del primer nombre libre

        //ImageIO.write no lanza excepción si simplemente no sabe escribir ese formato devuelve false
        boolean escrito = ImageIO.write(imagen, extension, archivoDestino);
        if (!escrito) {
            throw new IOException("No se encontro un codec para escribir el formato '" + extension + "'.");
        }

        return archivoDestino; //Devuelve el archivo creado
    }

        //Prueba nombreBase_1.extension, luego _2, _3, hasta encontrar uno que no exista en el disco. Tiene limite de 1000 para que en caso de llegar no quede en bucle infinito
    private static File buscarNombreDisponible(String nombreBase, String extension) throws IOException{
        for (int contador = 1; contador <=MAXIMO_INTENTOS; contador++) {
            File candidato = new File(nombreBase + "_" + contador + "." + extension);
            if(!candidato.exists()) {
                return candidato;
            }
        }
        throw new IOException(
            "No se pudo generar un nombre de archvo disponible tras " + MAXIMO_INTENTOS + " intentos.");
    }
}