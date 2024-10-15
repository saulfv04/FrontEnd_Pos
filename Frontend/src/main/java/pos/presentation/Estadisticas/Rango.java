package pos.presentation.Estadisticas;

public class Rango {

    private int anioInicio;
    private int anioFinal;
    private int mesFinal;
    private int mesInicio;


    public Rango(){
        anioInicio = 0;
        anioFinal = 0;
        mesFinal = 0;
        mesInicio = 0;
    }


    public Rango(int anioInicio, int anioFinal, int mesInicio, int mesFinal){
        this.anioInicio = anioInicio;
        this.anioFinal = anioFinal;
        this.mesInicio = mesInicio;
        this.mesFinal = mesFinal;
    }

    public int getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(int anioInicio) {
        this.anioInicio = anioInicio;
    }

    public int getAnioFinal() {
        return anioFinal;
    }

    public void setAnioFinal(int anioFinal) {
        this.anioFinal = anioFinal;
    }

    public int getMesFinal() {
        return mesFinal;
    }

    public void setMesFinal(int mesFinal) {
        this.mesFinal = mesFinal;
    }

    public int getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(int mesInicio) {
        this.mesInicio = mesInicio;
    }


}
