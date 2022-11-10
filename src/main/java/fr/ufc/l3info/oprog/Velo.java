package fr.ufc.l3info.oprog;

import java.util.Locale;

public class Velo implements IVelo{

    private final double tarif = 2.0;
    private final double distance_entre_revision = 500.0;

    private boolean estAbime;
    private boolean estDecroche;
    private final char cadre;
    private double kilometrage;
    private double kmDerniereRevision;

    public Velo(){
        this('m');
    }

    public Velo(char t){
        this.kilometrage = 0;
        this.estAbime = false;
        this.estDecroche = true;
        t = Character.toLowerCase(t);

        if (t == 'h') {
            this.cadre = 'h';
        } else if (t == 'f') {
            this.cadre = 'f';
        } else {
            this.cadre = 'm';
        }
    }

    @Override
    public double kilometrage() {

        return this.kilometrage;
    }

    @Override
    public double prochaineRevision() {
        return distance_entre_revision - (this.kilometrage() - this.kmDerniereRevision);
    }

    @Override
    public void parcourir(double km) {
        if (this.estDecroche && km > 0) {
            this.kilometrage += km;
        }
    }

    @Override
    public double tarif() {
        return tarif;
    }

    @Override
    public int decrocher() {
        if (this.estDecroche) {
            return -1;
        } else {
            this.estDecroche = true;
            return 0;
        }
    }

    @Override
    public int arrimer() {
        if (this.estDecroche) {
            this.estDecroche = false;
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public void abimer() {
        this.estAbime = true;
    }

    @Override
    public boolean estAbime() {
        return this.estAbime;
    }

    @Override
    public int reviser() {
        if (!this.estDecroche) {
            return -1;
        }

        this.estAbime = false;
        this.kmDerniereRevision = this.kilometrage;
        return 0;
    }

    @Override
    public int reparer() {
        if (!this.estDecroche) {
            return -1;
        } else if (!this.estAbime) {
            return -2;
        } else {
            this.estAbime = false;
            return 0;
        }
    }

    public String toString() {

        String res = "Vélo cadre ";

        if (this.cadre == 'f') {
            res += "femme - ";
        } else if (this.cadre == 'h') {
            res += "homme - ";
        } else {
            res += "mixte - ";
        }

        res += String.format(Locale.ENGLISH,"%.1f km", this.kilometrage);

        if (prochaineRevision() <= 0) {
            res += " (révision nécessaire)";
        }

        return res;
    }
}
