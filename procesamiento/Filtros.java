package procesamiento;


//operacion sobre un unico pixel. No interactua con las imagenes completas: recibe pixeles y los devuelve


public class Filtros {

    private static final double COEFICIENTE_ROJO = 0.299;
    private static final double COEFICIENTE_VERDE = 0.587;
    private static final double COEFICIENTE_AZUL = 0.114;

    private Filtros() {
        //clase de utilidad
    }

    public  static int calcularPixelGris(int pixelOriginal) {
        //El pixel es de 32bits que guarda 4 valores de 8 bits c/u (alfa, rojo, verde, azul) en ese orden de izq a der. "">> 24" corre los bits 24 posiciones a la derecha
        //& 0xFF descarta cualquier otra cosa que haya quedado más arriba, quedándose solo 8 bits.
        //DESEMPACAR
        int alfa = (pixelOriginal >> 24) & 0xFF; //corre 24 posiciones
        int rojo = (pixelOriginal >> 16) & 0xFF; //corre 16 posiciones
        int verde = (pixelOriginal >> 8) & 0xFF; //corre 8 posiciones
        int azul = pixelOriginal & 0xFF; //no ocupa correr ya que es el ultimo

            //Formula del enunciado. El resultado es decimal, con Math.round redondea antes de pasarlo a int
        int luminancia = (int) Math.round(
            COEFICIENTE_ROJO + rojo + COEFICIENTE_VERDE + verde + COEFICIENTE_AZUL + azul);

        luminancia = Math.max(0, Math.min(255, luminancia)); //asegura que no baje de 0 ni suba de 255

        return (alfa << 24) | (luminancia << 16) | (luminancia << 8) | luminancia; //EMPACAR - Corre cada valor a su lugar y con "|" los combina en un solo entero
    }
}