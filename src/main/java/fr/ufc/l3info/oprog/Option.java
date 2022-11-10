package fr.ufc.l3info.oprog;

public abstract class Option implements IVelo{

    IVelo _originale;
    double tarif;
    String description;

    public Option(IVelo originale, double tarif, String description){
        this._originale = originale;
        this.tarif = tarif;
        this.description = description;
    }

    @Override
    public double kilometrage() {
        return this._originale.kilometrage();
    }

    @Override
    public double prochaineRevision() {
        return this._originale.prochaineRevision();
    }

    @Override
    public void parcourir(double km) {
        this._originale.parcourir(km);
    }

    @Override
    public double tarif() {
        return this._originale.tarif() + this.tarif;
    }

    @Override
    public int decrocher() {
        return this._originale.decrocher();
    }

    @Override
    public int arrimer() {
        return this._originale.arrimer();
    }

    @Override
    public void abimer() {
        this._originale.abimer();
    }

    @Override
    public boolean estAbime() {
        return this._originale.estAbime();
    }

    @Override
    public int reviser() {
        return this._originale.reviser();
    }

    @Override
    public int reparer() {
        return this._originale.reparer();
    }

    @Override
    public String toString(){
        String [] res = this._originale.toString().split(" - ");

        return res[0] + ", " + this.description + " - " + res[1];
    }
}
