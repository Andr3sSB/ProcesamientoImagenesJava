package modelo;

public class Configuracion {

    private static final int REGIONES_POR_DEFECTO = 2;
    private static final int TONALIDAD_POR_DEFECTO = 128;

    private static final int TONALIDAD_MINIMA = 0;
    private static final int TONALIDAD_MAXIMA = 255;

    private int cantidadRegiones = REGIONES_POR_DEFECTO;
    private int tonalidad = TONALIDAD_POR_DEFECTO;

    //cantidad de regiones

    public int getCantidadRegiones() {
        return cantidadRegiones;
    }

        //Cantidad de regiones (minimo 2 - maximo: nucleos del equipo)
    public void setCantidadRegiones(int cantidad) {
        int maximo = obtenerMaximoRegiones();

        if(cantidad < 2) {
            throw new IllegalArgumentException(
                "La cantidad de regiones debe ser al menos 2.");
        }
        if (cantidad % 2 != 0) {
            throw new IllegalArgumentException(
                "La cantidad de regiones debe ser un numero par");
        }
        if (cantidad > maximo) {
            throw new  IllegalArgumentException(
                "La cantidad de regiones no puede superar " + maximo + " (nucleos disponibles en este equipo)");
        }

        this.cantidadRegiones = cantidad;
    }

    public static int obtenerMaximoRegiones() {
        return Runtime.getRuntime().availableProcessors();
    }



    //tonalidad
    public int getTonalidad() {
        return tonalidad;
    }

    //tonalidad umbral, entre 0 y 255
    public void setTonalidad(int valor) {
        if(valor < TONALIDAD_MINIMA || valor > TONALIDAD_MAXIMA) {
            throw new IllegalArgumentException(
                "La tonalidad debe estar entre " + TONALIDAD_MINIMA +" y " + TONALIDAD_MAXIMA);
        }
        this.tonalidad = valor;
    }
}