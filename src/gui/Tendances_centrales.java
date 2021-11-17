package gui;

public class Tendances_centrales {

    public Double moyenne;
    public Double moy_tronquee;
    public Double mode;
    public Double mediane;
    public Double milieu;

    public Tendances_centrales(Double moyenne, Double moy_tronquee, Double mode, Double mediane, Double milieu) {
        this.moyenne = moyenne;
        this.moy_tronquee = moy_tronquee;
        this.mode = mode;
        this.mediane = mediane;
        this.milieu = milieu;
    }

    public Double getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(Double moyenne) {
        this.moyenne = moyenne;
    }

    public Double getMoy_tronquee() {
        return moy_tronquee;
    }

    public void setMoy_tronquee(Double moy_tronquee) {
        this.moy_tronquee = moy_tronquee;
    }

    public Double getMode() {
        return mode;
    }

    public void setMode(Double mode) {
        this.mode = mode;
    }

    public Double getMediane() {
        return mediane;
    }

    public void setMediane(Double mediane) {
        this.mediane = mediane;
    }

    public Double getMilieu() {
        return milieu;
    }

    public void setMilieu(Double milieu) {
        this.milieu = milieu;
    }
}
