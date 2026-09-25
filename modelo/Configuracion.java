package modelo;

public class Configuracion {

    private static final int REGIONES_POR_DEFECTO = 2;
    private static final int TONALIDAD_POR_DEFECTO = 128;

    private static final int TONALIDAD_MINIMA = 0;
    private static final int TONALIDAD_MAXIMA = 255;

    private int cantidadRegiones = REGIONES_POR_DEFECTO;
    private int tonalidad = TONALIDAD_POR_DEFECTO;

    //cantidad de regiones

    public int getCantidadRegiones() { //lector
        return cantidadRegiones;
    }

    public void setCantidadRegiones(int cantidad) { //Cantidad de regiones (minimo 2 - maximo: nucleos del equipo)
        int maximo = obtenerMaximoRegiones(); //Consulta nucleos del procesador

        if(cantidad < 2) { //valida que minimo sean 2
            throw new IllegalArgumentException(
                "La cantidad de regiones debe ser al menos 2.");
        }
        if (cantidad % 2 != 0) { //valida que sean par
            throw new IllegalArgumentException(
                "La cantidad de regiones debe ser un numero par");
        }
        if (cantidad > maximo) { //no exceder nucleos disponibles
            throw new  IllegalArgumentException(
                "La cantidad de regiones no puede superar " + maximo + " (nucleos disponibles en este equipo)");
        }

        this.cantidadRegiones = cantidad; //Solo se guarda el valor si pasa las validaciones
    }
     
    public static int obtenerMaximoRegiones() { //Averigua cuantos nucleos disponibles hay en el equipo
        return Runtime.getRuntime().availableProcessors();
    }

    public int getTonalidad() { //tonalidad
        return tonalidad;
    }
    
    public void setTonalidad(int valor) { //Se usan variables como TONALIDA_MAXIMA/MINIMA para verificar la condicion de mayor a 0 y menor a 255
        if(valor < TONALIDAD_MINIMA || valor > TONALIDAD_MAXIMA) {
            throw new IllegalArgumentException(
                "La tonalidad debe estar entre " + TONALIDAD_MINIMA +" y " + TONALIDAD_MAXIMA);
        }
        this.tonalidad = valor;
    }
}