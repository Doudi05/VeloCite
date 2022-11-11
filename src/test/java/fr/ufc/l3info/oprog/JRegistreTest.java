package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JRegistreTest {
    private FabriqueVelo fabrique;
    private IRegistre Iregistre;
    private Abonne abonne1;
    private Abonne abonne2;
    private IVelo Ivelo;
    private JRegistre Jregistre;
    private Velo velo;
    final private String[] options = {
            "CADRE_ALUMINIUM",
            "SUSPENSION_AVANT",
            "SUSPENSION_ARRIERE",
            "FREINS_DISQUE",
            "ASSISTANCE_ELECTRIQUE"
    };

    public long timeMoinsMinute(long min){
        return System.currentTimeMillis() - 1000*60*min;
    }

    @Before
    public void initRegistreVide() throws IncorrectNameException {
        Iregistre = new JRegistre();

        Jregistre = new JRegistre();
        velo = new Velo();
        abonne2 = new Abonne("ww aa");
    }

    @Before
    public void initAbonne() throws IncorrectNameException {
        abonne1 = new Abonne("ww");
    }

    @Before
    public void initVelo(){
        fabrique = FabriqueVelo.getInstance();
        Ivelo = fabrique.construire('m',options[(int)(Math.random()*options.length)]);
    }

    @Test
    public void registreVide(){
        assertEquals(0, Iregistre.nbEmpruntsEnCours(abonne1));
    }

    @Test
    public void facturerSansEmprunt(){
        assertEquals(0,Iregistre.facturation(abonne1,0,timeMoinsMinute(0)),1e-3);
    }

    @Test
    public void emprunter(){
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(10)));
    }

    @Test
    public void emprunterDejaEmprunter() throws IncorrectNameException {
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(10)));
        assertEquals(-2,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(0)));
        assertEquals(-2,Iregistre.emprunter(new Abonne("ii rr"),Ivelo,timeMoinsMinute(2)));
    }

    @Test
    public void emprunterDejaEmprunteAvant(){
        assertEquals(0,Iregistre.emprunter(abonne1,velo,timeMoinsMinute(10)));
        assertEquals(0,Iregistre.retourner(velo,timeMoinsMinute(0)));
        assertEquals(-2,Iregistre.emprunter(abonne1,velo,timeMoinsMinute(4)));
    }

    @Test
    public void emprunterVeloNull(){
        assertEquals(-1,Iregistre.emprunter(abonne1,null,timeMoinsMinute(10)));
    }

    @Test
    public void emprunterAbonneNull(){
        assertEquals(-1,Iregistre.emprunter(null,Ivelo,timeMoinsMinute(3)));
    }

    @Test
    public void emprunterAvantAutreEmprunt(){
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(5)));
        assertEquals(0,Iregistre.retourner(Ivelo,timeMoinsMinute(2)));
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(10)));
        assertEquals(-3,Iregistre.retourner(Ivelo,timeMoinsMinute(3)));
        //assertEquals(0,Iregistre.retourner(Ivelo,timeMoinsMinute(0)));
    }

    @Test
    public void nombreEmpruntsApresRetour(){
        assertEquals(0,Iregistre.nbEmpruntsEnCours(abonne1));
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(10)));
        assertEquals(1,Iregistre.nbEmpruntsEnCours(abonne1));
        assertEquals(0,Iregistre.retourner(Ivelo,timeMoinsMinute(0)));
        assertEquals(0,Iregistre.nbEmpruntsEnCours(abonne1));
    }

    @Test
    public void retournerVeloNull(){
        assertEquals(-1,Iregistre.retourner(null,timeMoinsMinute(0)));
    }

    @Test
    public void retournerNonEmprunte(){
        assertEquals(-2,Iregistre.retourner(Ivelo,timeMoinsMinute(0)));
    }

    @Test
    public void retournerAvantEmprunt(){
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(10)));
        assertEquals(-3,Iregistre.retourner(Ivelo,timeMoinsMinute(11)));
    }

    @Test
    public void retournerMemeTempsAutreEmprunt(){
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(10)));
        assertEquals(0,Iregistre.retourner(Ivelo,timeMoinsMinute(0)));
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(20)));
        assertEquals(-3,Iregistre.retourner(Ivelo,timeMoinsMinute(8)));
        assertEquals(0,Iregistre.retourner(Ivelo,timeMoinsMinute(15)));
    }

    @Test
    public void nbEmpruntEnCours(){
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(10)));
        assertEquals(-2,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(9)));
        assertEquals(0,Iregistre.emprunter(abonne1,new Velo('h'),timeMoinsMinute(8)));
        assertEquals(0,Iregistre.emprunter(abonne1,new Velo('f'),timeMoinsMinute(7)));
        assertEquals(3,Iregistre.nbEmpruntsEnCours(abonne1));
    }

    @Test
    public void nbEmpruntPlusieursAbonne() throws IncorrectNameException {
        Abonne a = new Abonne("ww");

        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(10)));
        assertEquals(0,Iregistre.emprunter(a,new Velo('h'),timeMoinsMinute(9)));
        assertEquals(0,Iregistre.emprunter(abonne1,new Velo('f'),timeMoinsMinute(8)));
        assertEquals(2,Iregistre.nbEmpruntsEnCours(abonne1));
        assertEquals(1,Iregistre.nbEmpruntsEnCours(a));
    }

    @Test
    public void nbEmpruntFutur(){
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(-10)));
        assertEquals(1,Iregistre.nbEmpruntsEnCours(abonne1));
        assertEquals(0,Iregistre.retourner(Ivelo,timeMoinsMinute(-12)));
        assertEquals(0,Iregistre.nbEmpruntsEnCours(abonne1));
    }

    @Test
    public void facturerUnEmprunt(){
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(180)));
        assertEquals(0,Iregistre.retourner(Ivelo,timeMoinsMinute(60)));
        assertEquals(2 * Ivelo.tarif(),Iregistre.facturation(abonne1,timeMoinsMinute(200),timeMoinsMinute(0)),1e-3);
        assertEquals(0,Iregistre.facturation(abonne1,timeMoinsMinute(200),timeMoinsMinute(70)),1e-3);
    }

    @Test
    public void facturerAvantEmprunt(){
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(180)));
        assertEquals(0,Iregistre.retourner(Ivelo,timeMoinsMinute(60)));
        assertEquals(0,Iregistre.facturation(abonne1,timeMoinsMinute(300),timeMoinsMinute(200)),1e-3);
    }

    @Test
    public void facturerPlusieursEmprunt(){
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(180)));
        assertEquals(0,Iregistre.retourner(Ivelo,timeMoinsMinute(60)));
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(40)));
        assertEquals(0,Iregistre.retourner(Ivelo,timeMoinsMinute(10)));
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(250)));
        assertEquals(0,Iregistre.retourner(Ivelo,timeMoinsMinute(235)));
        assertEquals(2.75 * Ivelo.tarif(),Iregistre.facturation(abonne1,timeMoinsMinute(500),timeMoinsMinute(0)),1e-3);
        assertEquals(0.25 * Ivelo.tarif(),Iregistre.facturation(abonne1,timeMoinsMinute(300),timeMoinsMinute(200)),1e-3);
        assertEquals(2.5 * Ivelo.tarif(),Iregistre.facturation(abonne1,timeMoinsMinute(100),timeMoinsMinute(0)),1e-3);
        assertEquals(0,Iregistre.facturation(abonne1,timeMoinsMinute(-10),timeMoinsMinute(-100)),1e-3);
    }

    @Test
    public void facturerMinutePrecise(){
        assertEquals(0,Iregistre.emprunter(abonne1,Ivelo,timeMoinsMinute(180)));
        assertEquals(0,Iregistre.retourner(Ivelo,timeMoinsMinute(114)));
        assertEquals(1.1 * Ivelo.tarif(),Iregistre.facturation(abonne1,timeMoinsMinute(180),timeMoinsMinute(114)),1e-3);
    }

    @Test
    public void facturationEmpruntSimultane(){
        assertEquals(0,Iregistre.emprunter(abonne1,velo,timeMoinsMinute(180)));
        assertEquals(0,Iregistre.retourner(velo,timeMoinsMinute(60)));

        IVelo first = new Velo('f');
        assertEquals(0,Iregistre.emprunter(abonne1,first,timeMoinsMinute(150)));
        assertEquals(0,Iregistre.retourner(first,timeMoinsMinute(70)));

        IVelo second = new Velo('f');
        assertEquals(0,Iregistre.emprunter(abonne1,second,timeMoinsMinute(170)));
        assertEquals(0,Iregistre.retourner(second,timeMoinsMinute(30)));

        assertEquals(2 * velo.tarif() + 1.333 * first.tarif() + 2.333 * second.tarif(),Iregistre.facturation(abonne1,timeMoinsMinute(180),timeMoinsMinute(0)),1e-2);
        assertEquals(2 * velo.tarif() + 1.333 * first.tarif(),Iregistre.facturation(abonne1,timeMoinsMinute(160),timeMoinsMinute(40)),1e-2);
    }








    @Test
    public void testEmprunterEfectue()  {
        int ret = this.Jregistre.emprunter(this.abonne2, this.velo, System.currentTimeMillis());
        Assert.assertEquals(ret, 0);

        Velo v2 = new Velo();
        ret = this.Jregistre.emprunter(this.abonne2, v2, System.currentTimeMillis());
        Assert.assertEquals(ret, 0);

        Velo v3 = new Velo();
        ret = this.Jregistre.emprunter(this.abonne2, v3, System.currentTimeMillis() - 100);
        Assert.assertEquals(ret, 0);
    }

    @Test
    public void testEmprunterNull() {
        int ret;

        ret = this.Jregistre.emprunter(null, this.velo, System.currentTimeMillis());
        Assert.assertEquals(ret, -1);

        ret = this.Jregistre.emprunter(this.abonne2, null, System.currentTimeMillis());
        Assert.assertEquals(ret, -1);

        ret = this.Jregistre.emprunter(null, null, System.currentTimeMillis());
        Assert.assertEquals(ret, -1);
    }

    @Test
    public void testEmprunterAlready() throws IncorrectNameException {
        int ret;
        long date = System.currentTimeMillis();

        ret = this.Jregistre.emprunter(this.abonne2, this.velo, date);
        Assert.assertEquals(ret, 0);

        ret = this.Jregistre.emprunter(this.abonne2, this.velo, date);
        Assert.assertEquals(ret, -2);

        Abonne b = new Abonne("ii rr");
        ret = this.Jregistre.emprunter(b, this.velo, date + 10);
        Assert.assertEquals(ret, -2);

        this.Jregistre.retourner(this.velo, date + 100);
        ret = this.Jregistre.emprunter(this.abonne2, this.velo, date);
        Assert.assertEquals(ret, -2);

        ret = this.Jregistre.emprunter(this.abonne2, this.velo, date + 200);
        Assert.assertEquals(ret, 0);
    }






    @Test
    public void testRetournerEffectue() {
        long date = System.currentTimeMillis();

        this.Jregistre.emprunter(this.abonne2, this.velo, date);

        int ret;
        ret = this.Jregistre.retourner(this.velo, date + 5);
        Assert.assertEquals(ret, 0);

        this.Jregistre.emprunter(this.abonne2, this.velo, date + 50);
        ret = this.Jregistre.retourner(this.velo, date + 100);
        Assert.assertEquals(ret, 0);
    }

    @Test
    public void testRetournerNull() {
        long date = System.currentTimeMillis();

        this.Jregistre.emprunter(this.abonne2, this.velo, date);

        int ret;
        ret = this.Jregistre.retourner(null, date + 5);
        Assert.assertEquals(ret, -1);
    }

    @Test
    public void testRetournerErreur() {
        long date = System.currentTimeMillis();

        int ret;
        ret = Jregistre.retourner(this.velo, date + 5);
        Assert.assertEquals(ret, -2);

        this.Jregistre.emprunter(this.abonne2, this.velo, date + 10);
        ret = Jregistre.retourner(this.velo, date + 15);
        Assert.assertEquals(ret, 0);

        ret = Jregistre.retourner(this.velo, date + 20);
        Assert.assertEquals(ret, -2);
    }

    @Test
    public void testRetournerDate() {
        long date = System.currentTimeMillis();

        this.Jregistre.emprunter(this.abonne2, this.velo, date);

        int ret;

        ret = this.Jregistre.retourner(this.velo, date - 20);
        Assert.assertEquals(ret, -3);
        ret = this.Jregistre.retourner(this.velo, date + 20);
        Assert.assertEquals(ret, 0);
    }







    @Test
    public void testNbEmpruntsEnCours() {
        long date = System.currentTimeMillis();

        Assert.assertEquals(this.Jregistre.nbEmpruntsEnCours(this.abonne2), 0);

        this.Jregistre.emprunter(this.abonne2, this.velo, date - 20);

        Assert.assertEquals(this.Jregistre.nbEmpruntsEnCours(this.abonne2), 1);

        Velo v2 = new Velo();
        this.Jregistre.emprunter(this.abonne2, v2, date - 10);

        Assert.assertEquals(this.Jregistre.nbEmpruntsEnCours(this.abonne2), 2);

        this.Jregistre.retourner(v2, date - 5);

        Assert.assertEquals(this.Jregistre.nbEmpruntsEnCours(this.abonne2), 1);

        Velo v3 = new Velo();
        this.Jregistre.emprunter(this.abonne2, v3, date + 100000);
    }







    @Test
    public void testFacturationNothing() {
        long date = System.currentTimeMillis();

        double ret = this.Jregistre.facturation(this.abonne2, date, date + 10);
        Assert.assertEquals(0, ret, 0.001);
    }

    @Test
    public void testFacturationInProgress() {
        long date = System.currentTimeMillis();

        this.Jregistre.emprunter(this.abonne2, this.velo, date);

        double ret = this.Jregistre.facturation(this.abonne2, date, date + 10);
        Assert.assertEquals(0, ret, 0.001);
    }

    @Test
    public void testFacturationOne() {
        long date = System.currentTimeMillis();

        this.Jregistre.emprunter(this.abonne2, this.velo, date);
        this.Jregistre.retourner(this.velo, date + 1000*60*60);

        double ret = this.Jregistre.facturation(this.abonne2, date, date + 10000000);
        Assert.assertEquals(this.velo.tarif(), ret, 0.001);
    }

    @Test
    public void testFacturationOneBis() {
        long date = System.currentTimeMillis();

        this.Jregistre.emprunter(this.abonne2, this.velo, date);
        this.Jregistre.retourner(this.velo, date + 1000*60*30);

        double ret = this.Jregistre.facturation(this.abonne2, date, date + 10000000);
        Assert.assertEquals(this.velo.tarif() / 2, ret, 0.001);
    }

    @Test
    public void testFacturationDateFini() {
        long date = System.currentTimeMillis();

        this.Jregistre.emprunter(this.abonne2, this.velo, date);
        this.Jregistre.retourner(this.velo, date + 1000*60*30);

        double ret = this.Jregistre.facturation(this.abonne2, date, date + 300);
        Assert.assertEquals(0, ret, 0.001);
    }

    @Test
    public void testFacturationErreur() {
        long date = System.currentTimeMillis();

        this.Jregistre.emprunter(this.abonne2, this.velo, date);
        this.Jregistre.retourner(this.velo, date + 1000*60*30);

        double ret = this.Jregistre.facturation(this.abonne2, date + 10000000, date);
        Assert.assertEquals(0, ret, 0.001);
    }

    @Test
    public void testFacturationNull() {
        long date = System.currentTimeMillis();

        double ret = this.Jregistre.facturation(null, date, date + 200);
        Assert.assertEquals(0, ret, 0.001);
    }
}
