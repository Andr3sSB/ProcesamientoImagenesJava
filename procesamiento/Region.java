package procesamiento;

public class Region {

    private final int filaInicioEscritura;
    private final int filaFinEscritura;
    private final int filaInicioLectura;
    private final int filaFinLectura;

    
    public Region(int filaInicioEscritura, int filaFinEscritura, int filaInicioLectura, int filaFinLectura) { //Guarda y devuelve 4 numeros
        this.filaInicioEscritura = filaInicioEscritura;
        this.filaFinEscritura = filaFinEscritura;
        this.filaInicioLectura = filaInicioLectura;
        this.filaFinLectura = filaFinLectura;
    }

    public int getFilaInicioEscritura() {
        return filaInicioEscritura;
    }

    public int getFilaFinEscritura() {
        return filaFinEscritura;
    }

    public int getFilaInicioLectura() {
        return filaInicioLectura;
    }

    public int getFilaFinLectura() {
        return filaFinLectura;
    }
}