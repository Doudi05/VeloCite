package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Test;

public class JRegistreTestIvann {
    private IVelo v;
    private Abonne a;
    private Station s;
    private IRegistre r;
    //tests emprunter
    @Test
    public void testEmprunterVeloNull() throws IncorrectNameException{
        r = new JRegistre();
        a = new Abonne("Sacha du bourg palette");
        Assert.assertEquals(-1, r.emprunter(a, v, 1234000));
    }

    @Test
    public void testEmprunterAbonneNull(){
        r = new JRegistre();
        v = new Velo();
        Assert.assertEquals(-1, r.emprunter(a, v, 1234000));
    }

    @Test
    public void testEmprunterReussi() throws IncorrectNameException{
        r = new JRegistre();
        v = new Velo();
        a = new Abonne("Mickey mousse");
        Assert.assertEquals(0, r.emprunter(a, v, 1234000));
    }

    @Test
    public void testEmpruntDejaEmprunte() throws IncorrectNameException{
        r = new JRegistre();
        v = new Velo();
        r.emprunter(new Abonne("Darque Vadaure"), v, 1232000);
        a = new Abonne("Donna lduck");
        Assert.assertEquals(-2, r.emprunter(a, v, 1234000));
    }

    @Test
    public void testEmpruntDejaEmprunteMaisRendu() throws IncorrectNameException{
        r = new JRegistre();
        v = new Velo();
        r.emprunter(new Abonne("Darque Vadaure"), v, 1234000);
        r.retourner(v, 1345000);
        a = new Abonne("Donna lduck");
        Assert.assertEquals(-2, r.emprunter(a, v, 1234000));
    }

    @Test
    public void testEmpruntDejaEmprunteMaisRendu2() throws IncorrectNameException{
        r = new JRegistre();
        v = new Velo();
        r.emprunter(new Abonne("Darque Vadaure"), v, 1234000);
        r.retourner(v, 1345000);
        a = new Abonne("Donna lduck");
        Assert.assertEquals(0, r.emprunter(a, v, 1456000));
    }
    //tests retourner

    @Test
    public void testRetournerVeloNull(){
        r = new JRegistre();
        Assert.assertEquals(-1, r.retourner(v, 1342000));
    }

    @Test
    public void testRetournerNonEmprunte(){
        v = new Velo();
        r = new JRegistre();
        Assert.assertEquals(-2, r.retourner(v, 1234000));
    }

    @Test
    public void testRetourner() throws IncorrectNameException{
        v = new Velo();
        a = new Abonne("Dwayne Johnson");
        r = new JRegistre();

        r.emprunter(a, v, 1234000);
        Assert.assertEquals(-3, r.retourner(v, 1000000));
    }

    @Test
    public void testRetournerPeriodeEmprunt() throws IncorrectNameException{
        v = new Velo();
        a = new Abonne("Dwayne Johnson");
        r = new JRegistre();

        r.emprunter(a, v, 5000000);
        r.retourner(v, 5100000);

        r.emprunter(new Abonne("Kevin Hart"), v, 2000000);
        Assert.assertEquals(-3, r.retourner(v, 5099000));
    }

    @Test
    public void testRetournerPeriodeEmprunt2() throws IncorrectNameException{
        v = new Velo();
        a = new Abonne("Dwayne Johnson");
        r = new JRegistre();

        r.emprunter(a, v, 5000000);
        r.retourner(v, 5100000);

        r.emprunter(new Abonne("Kevin Hart"), v, 2000000);
        Assert.assertEquals(-3, r.retourner(v, 5123000));
    }

    @Test
    public void testRetournerPeriodeEmprunt3() throws IncorrectNameException{
        v = new Velo();
        a = new Abonne("Dwayne Johnson");
        r = new JRegistre();

        r.emprunter(a, v, 5000000);
        r.retourner(v, 5100000);

        IVelo v2 = new Velo();
        r.emprunter(a, v2, 5101000);
        Assert.assertEquals(0, r.retourner(v2, 5123000));
    }

    //test nbEmpruntsEnCours

    @Test
    public void testNbEmprunts() throws IncorrectNameException{
        v = new Velo();
        r = new JRegistre();
        IVelo v2 = new Velo();
        IVelo v3 = new Velo();
        a = new Abonne("mary poppins");
        Assert.assertEquals(0, r.nbEmpruntsEnCours(a));
        r.emprunter(a, v, 1234000);
        Assert.assertEquals(1, r.nbEmpruntsEnCours(a));
        r.emprunter(a, v2, 1234000);
        Assert.assertEquals(2, r.nbEmpruntsEnCours(a));
        r.emprunter(a, v3, 1234000);
        Assert.assertEquals(3, r.nbEmpruntsEnCours(a));
        r.retourner(v, 2345000);
        Assert.assertEquals(2, r.nbEmpruntsEnCours(a));
        r.retourner(v2, 2345000);
        Assert.assertEquals(1, r.nbEmpruntsEnCours(a));
        r.retourner(v3, 2345000);
        Assert.assertEquals(0, r.nbEmpruntsEnCours(a));
    }

    //test facturation

    @Test
    public void testFacturationNul() throws IncorrectNameException{
        v = new Velo();
        r = new JRegistre();
        a = new Abonne("Jean Lassalle");
        r.emprunter(a, v, 1234000);
        r.retourner(v, 1234000);
        Assert.assertEquals(0.0, r.facturation(a, 1234000, 1234000), 0.001);
        Assert.assertEquals(0.0, r.facturation(a, 1000000, 2234000), 0.001);

    }

    @Test
    public void testFacturationNoOpt() throws IncorrectNameException{
        v = new Velo();
        r = new JRegistre();
        a = new Abonne("Mario brosse");

        r.emprunter(a, v, 1234000);
        r.retourner(v, 2234000);
        Assert.assertEquals(0.533, r.facturation(a, 1234000, 2234000), 0.001);
    }

    @Test
    public void testFacturationAvecOption() throws IncorrectNameException{
        v = new Velo();
        v = new OptSuspensionArriere(v);
        r = new JRegistre();
        a = new Abonne("Luiji brosse");

        r.emprunter(a, v, 1234000);
        r.retourner(v, 2234000);
        Assert.assertEquals(0.666, r.facturation(a, 1234000, 2234000), 0.001);
    }

    @Test
    public void testFacturationEnCours() throws IncorrectNameException{
        v = new Velo();
        r = new JRegistre();
        a = new Abonne("Mario brosse");

        r.emprunter(a, v, 1234000);

        Assert.assertEquals(0.0, r.facturation(a, 0000000, 2234000), 0.001);
    }
}
