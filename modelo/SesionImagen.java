package modelo;

import  java.awt.image.BufferedImage;
import  java.io.File;

public  class SesionImagen {
    private BufferedImage imagenOriginal;
    private BufferedImage imagenGris;
    private BufferedImage imagenContornos;
    private File archivoOriginal;

    //imagen original
    public void  setImagenOriginal(BufferedImage imagen, File archivo) {
        if (imagen == null || archivo == null) {
            throw new IllegalArgumentException(
                "La imagen y el archivo de origen no pueden ser nulos");
        }
        this.imagenOriginal = imagen;
        this.archivoOriginal = archivo;

        this.imagenGris = null;
        this.imagenContornos = null;
    }

    public BufferedImage getImagenOriginal() {
        return imagenOriginal;
    }

    public File getArchivoOriginal() {
        return archivoOriginal;
    }

    public boolean hayImagenCargada(){
        return imagenOriginal != null;
    }

    //imagen esacala grises
    public void setImagenGris(BufferedImage imagen) {
        if (imagen == null) {
            throw new IllegalArgumentException("La imagen en grises no puede ser nula");
        }
        this.imagenGris = imagen;
        this.imagenContornos = null;
    }

    public BufferedImage getImagenGris() {
        return imagenGris;
    }

    public boolean hayImagenGris() {
        return imagenGris != null;
    }

    //imagen contornos
    public void setImagenContornos(BufferedImage imagen) {
        if (imagen == null) {
            throw new IllegalArgumentException("La imagen de contornos no puede ser nula");
        }
        this.imagenContornos = imagen;
    }

    public BufferedImage getImagenContornos() {
        return imagenContornos;
    }
    
    public boolean hayImagenContornos() {
        return imagenContornos != null;
    }
}