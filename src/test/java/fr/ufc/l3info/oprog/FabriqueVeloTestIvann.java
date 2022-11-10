package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Test;

public class FabriqueVeloTestIvann {

    @Test
    public void testInstance(){
        FabriqueVelo factory = FabriqueVelo.getInstance();
        FabriqueVelo second_factory = FabriqueVelo.getInstance();

        Assert.assertEquals(factory, second_factory);
    }

    @Test
    public void testFabriqueHommeNOOPT(){
        FabriqueVelo factory = FabriqueVelo.getInstance();

        IVelo v = factory.construire('h', null);
        Assert.assertTrue(v.toString().equals("Vélo cadre homme - 0.0 km"));

        v = factory.construire('H', null);
        Assert.assertTrue(v.toString().equals("Vélo cadre homme - 0.0 km"));
    }

    @Test
    public void testFabriqueFemmeNOOPT(){
        FabriqueVelo factory = FabriqueVelo.getInstance();

        IVelo v = factory.construire('f', null);
        Assert.assertTrue(v.toString().equals("Vélo cadre femme - 0.0 km"));

        v = factory.construire('F', null);
        Assert.assertTrue(v.toString().equals("Vélo cadre femme - 0.0 km"));
    }

    @Test
    public void testFabriqueMixteNOOPT(){
        FabriqueVelo factory = FabriqueVelo.getInstance();

        IVelo v = factory.construire('a', null);
        Assert.assertTrue(v.toString().equals("Vélo cadre mixte - 0.0 km"));

        v = factory.construire(',', null);
        Assert.assertTrue(v.toString().equals("Vélo cadre mixte - 0.0 km"));

        v = factory.construire(' ', null);
        Assert.assertTrue(v.toString().equals("Vélo cadre mixte - 0.0 km"));
    }

    @Test
    public void testFabriqueAssistanceElectrique(){
        FabriqueVelo factory = FabriqueVelo.getInstance();
        IVelo v = factory.construire('m', "ASSISTANCE_ELECTRIQUE");
        Assert.assertTrue(v.toString().contains("assistance électrique"));
    }

    @Test
    public void testFabriqueCadreAlu(){
        FabriqueVelo factory = FabriqueVelo.getInstance();
        IVelo v = factory.construire('m', "CADRE_ALUMINIUM");
        Assert.assertTrue(v.toString().contains("cadre aluminium"));
    }

    @Test
    public void testFabriqueFreinsDisque(){
        FabriqueVelo factory = FabriqueVelo.getInstance();
        IVelo v = factory.construire('m', "FREINS_DISQUE");
        Assert.assertTrue(v.toString().contains("freins à disque"));
    }

    @Test
    public void testFabriqueSuspensionAvant(){
        FabriqueVelo factory = FabriqueVelo.getInstance();
        IVelo v = factory.construire('m', "SUSPENSION_AVANT");
        Assert.assertTrue(v.toString().contains("suspension avant"));
    }

    @Test
    public void testFabriqueSuspensionArriere(){
        FabriqueVelo factory = FabriqueVelo.getInstance();
        IVelo v = factory.construire('m', "SUSPENSION_ARRIERE");
        Assert.assertTrue(v.toString().contains("suspension arrière"));
    }

    @Test
    public void testFabriqueUneOption(){
        FabriqueVelo factory = FabriqueVelo.getInstance();
        IVelo v = factory.construire('m', "CADRE_ALUMINIUM");
        Assert.assertEquals("Vélo cadre mixte, cadre aluminium - 0.0 km", v.toString());
    }

    @Test
    public void testFabriquePlusieursOptions(){
        FabriqueVelo factory = FabriqueVelo.getInstance();
        IVelo v = factory.construire('m', "SUSPENSION_ARRIERE", "SUSPENSION_AVANT");
        Assert.assertEquals("Vélo cadre mixte, suspension arrière, suspension avant - 0.0 km", v.toString());
    }

    @Test
    public void testFabriqueErreur(){
        FabriqueVelo factory = FabriqueVelo.getInstance();
        IVelo v = factory.construire('m', "FREDERIC_DADEAU", "SUSPeNSION_AVANT", "FRAIN_DYSKE", "A_SIX_TANCE_AILE_ET_KTRIKE");
        Assert.assertEquals("Vélo cadre mixte - 0.0 km", v.toString());
    }

    @Test
    public void testDoubleOption() {
        FabriqueVelo factory = FabriqueVelo.getInstance();
        IVelo v = factory.construire('m', "SUSPENSION_ARRIERE", "SUSPENSION_ARRIERE");
        Assert.assertEquals("Vélo cadre mixte, suspension arrière - 0.0 km", v.toString());

        v = factory.construire('m', "SUSPENSION_AVANT", "SUSPENSION_AVANT");
        Assert.assertEquals("Vélo cadre mixte, suspension avant - 0.0 km", v.toString());

        v = factory.construire('m', "FREINS_DISQUE", "FREINS_DISQUE");
        Assert.assertEquals("Vélo cadre mixte, freins à disque - 0.0 km", v.toString());

        v = factory.construire('m', "ASSISTANCE_ELECTRIQUE", "ASSISTANCE_ELECTRIQUE");
        Assert.assertEquals("Vélo cadre mixte, assistance électrique - 0.0 km", v.toString());

        v = factory.construire('m', "CADRE_ALUMINIUM", "CADRE_ALUMINIUM");
        Assert.assertEquals("Vélo cadre mixte, cadre aluminium - 0.0 km", v.toString());
    }

    @Test
    public void testToutesOptions(){
        FabriqueVelo factory = FabriqueVelo.getInstance();
        IVelo v = factory.construire('m', "SUSPENSION_ARRIERE", "SUSPENSION_AVANT", "FREINS_DISQUE", "FRED_DADEAU", "CADRE_ALUMINIUM", "ASSISTANCE_ELECTRIQUE");
        Assert.assertEquals("Vélo cadre mixte, suspension arrière, suspension avant, freins à disque, cadre aluminium, assistance électrique - 0.0 km", v.toString());
    }


}
