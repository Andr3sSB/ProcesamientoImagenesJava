package archivos;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;

import modelo.RegistroEstadistica;

public  class Estadisticas {
    private static final String NOMBRE_ARCHIVO = "estadisticas.json";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Estadisticas() {
        //utilidad
    }

    public static synchronized void registrar (RegistroEstadistica registro) {
        File archivo = new File (NOMBRE_ARCHIVO);

        try {
            String contenidoActual = archivo.exists() ? Files.readString(archivo.toPath(), StandardCharsets.UTF_8) : "[]";
            String jsonRegistro = construirJson(registro);
            String contenidoNuevo = insertarEnArreglo (contenidoActual, jsonRegistro);

            Files.writeString(archivo.toPath(), contenidoNuevo, StandardCharsets.UTF_8);
        } catch (IOException | RuntimeException ex) {
            System.err.println("No se pudo registrar la estadistica: " + ex.getMessage());
        }
    }

    private static String insertarEnArreglo(String contenidoActual, String jsonRegistro) {
        String contenido = contenidoActual.trim();

        if (!contenido.startsWith("[") || !contenido.endsWith("]")) {
            return "[\n " + jsonRegistro + "\n]";
        }

        String interior = contenido.substring(1, contenido.length() - 1).trim();

        if (interior.isEmpty()) {
            return "[\n " + jsonRegistro + "\n]";
        }

        return  "[\n" + interior + ",\n " + jsonRegistro + "\n]";
    }

    private static String construirJson(RegistroEstadistica registro) {
        return "{\n"
                + "    \"operacion\": \"" + escapar(registro.getOperacion()) + "\",\n"
                + "    \"tamanoArchivoBytes\": " + registro.getTamañoArchivoBytes() + ",\n"
                + "    \"ancho\": " + registro.getAncho() + ",\n"
                + "    \"alto\": " + registro.getAlto() + ",\n"
                + "    \"cantidadPixeles\": " + registro.getCantidadPixeles() + ",\n"
                + "    \"cantidadHilos\": " + registro.getCantidadHilos() + ",\n"
                + "    \"tiempoMilisegundos\": " + registro.getTiempoMiliSegundos() + ",\n"
                + "    \"fechaHora\": \"" + registro.getFechaHora().format(FORMATO_FECHA) + "\"\n"
                + "  }";
    }

    private static String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("\"","\\\"");
    }
}