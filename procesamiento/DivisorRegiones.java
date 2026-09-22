package procesamiento;

import java.util.ArrayList;
import  java.util.List;

public class DivisorRegiones {

    private DivisorRegiones() {
        //clase de utilidad
    }

    public static List<Region> dividirSinMargen(int totalFilas, int cantidadRegiones) {
        //Validacion
        if(cantidadRegiones < 1) {
            throw new IllegalArgumentException("La cantidad de regiones debe ser al menos 1");
        }

            //Lista donde se va a ir acumulando una Region por cada hilko que se vaya a crear
        List<Region> regiones = new ArrayList<>();

        int filasPorRegion = totalFilas / cantidadRegiones; //Da cuantas filas le tocan a cada region como minimo
        int filasSobrantes = totalFilas % cantidadRegiones; //Cuantas filas quedaron sin repartir

        //Lleva la cuenta de en que fila empieza la proxima region a crear. Arranca en 0
        int inicio = 0;
        for (int i = 0; i < cantidadRegiones; i++) {
            int filasDeEstaRegion = filasPorRegion + (i < filasSobrantes ? 1 : 0); //Aca se reparten las filas sobrantes
            int fin = inicio + filasDeEstaRegion; //Calcula donde termina la region sumando a incio la cantida de filas que le tocaron

            regiones.add(new Region(inicio, fin, inicio, fin));// Crea la region y la agrega a la lista

            inicio = fin; //inicio se actualiza al valor donde termino esta region, asi la siguiente arranca donde la anterior quedo
        }

        return regiones; //Devuelve la lista
    }
}