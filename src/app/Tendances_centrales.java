package app;

public class Tendances_centrales {

    public String moyenne;
    public String moy_tronquee;
    public String mode;
    public String mediane;
    public String milieu;

    public Tendances_centrales(String moyenne, String moy_tronquee, String mode, String mediane, String milieu) {
        this.moyenne = moyenne;
        this.moy_tronquee = moy_tronquee;
        this.mode = mode;
        this.mediane = mediane;
        this.milieu = milieu;
    }

    public String getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(String moyenne) {
        this.moyenne = moyenne;
    }

    public String getMoy_tronquee() {
        return moy_tronquee;
    }

    public void setMoy_tronquee(String moy_tronquee) {
        this.moy_tronquee = moy_tronquee;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMediane() {
        return mediane;
    }

    public void setMediane(String mediane) {
        this.mediane = mediane;
    }

    public String getMilieu() {
        return milieu;
    }

    public void setMilieu(String milieu) {
        this.milieu = milieu;
    }
}
