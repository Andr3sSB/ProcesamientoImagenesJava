package modelo;

import java.time.LocalDateTime;

public final class RegistroEstadistica {

    private final String operacion;
    private final long tamañoArchivoBytes;
    private final int ancho;
    private final int alto;
    private final long cantidadPixeles;
    private final int cantidadHilos;
    private final long tiempoMiliSegundos; 
    private final LocalDateTime fechaHora;

    public RegistroEstadistica(String operacion, long tamañoArchivoBytes, int ancho, int alto, int cantidadHilos, long timepoMiliSegundos, LocalDateTime fechaHora) {

        this.operacion = operacion;
        this.tamañoArchivoBytes = tamañoArchivoBytes;
        this.ancho = ancho;
        this.alto = alto;

        this.cantidadPixeles = (long) ancho * (long) alto;
        this.cantidadHilos = cantidadHilos;
        this.tiempoMiliSegundos = timepoMiliSegundos;
        this.fechaHora = fechaHora;
    }

    public String getOperacion() {
        return  operacion;
    }

    public long getTamañoArchivoBytes() {
        return tamañoArchivoBytes;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public long getCantidadPixeles() {
        return cantidadPixeles;
    }

    public int getCantidadHilos() {
        return cantidadHilos;
    }

    public long getTiempoMiliSegundos() {
        return tiempoMiliSegundos;
    }

    public LocalDateTime getFechaHora() {
        return  fechaHora;
    }
}