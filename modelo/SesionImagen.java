package modelo;

import  java.awt.image.BufferedImage;
import  java.io.File;

public  class SesionImagen {
    private BufferedImage imagenOriginal;
    private BufferedImage imagenGris;
    private BufferedImage imagenContornos;
    private File archivoOriginal;

    
    public void  setImagenOriginal(BufferedImage imagen, File archivo) {
        //imagen original
        // Valida que ni imagen ni archivo sea nulo, y deja en null imagenGris e 
        // imagenContornos para que al cargar una foto nueva no se muestre la anterior
        if (imagen == null || archivo == null) {
            throw new IllegalArgumentException(
                "La imagen y el archivo de origen no pueden ser nulos");
        }
        this.imagenOriginal = imagen;
        this.archivoOriginal = archivo;

        this.imagenGris = null;
        this.imagenContornos = null;
    }
         
    public BufferedImage getImagenOriginal() { //lector
        return imagenOriginal;
    }

    public File getArchivoOriginal() {
        return archivoOriginal;
    }
     
    public boolean hayImagenCargada(){ //lector
        return imagenOriginal != null;
    }

    public void setImagenGris(BufferedImage imagen) { //imagen esacala grises
        if (imagen == null) {
            throw new IllegalArgumentException("La imagen en grises no puede ser nula");
        }
        this.imagenGris = imagen;
        this.imagenContornos = null;
    }
       
    public BufferedImage getImagenGris() { //lector
        return imagenGris;
    }

    public boolean hayImagenGris() {
        return imagenGris != null;
    }
   
    public void setImagenContornos(BufferedImage imagen) { //imagen contornos
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