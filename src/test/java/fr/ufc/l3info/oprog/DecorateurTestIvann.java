package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Test;



public class DecorateurTestIvann {

    @Test
    public void testAssistanceElectrique(){
        IVelo v = new Velo();
        v = new OptAssistanceElectrique(v);
        Assert.assertTrue(v.toString().contains("assistance électrique"));
    }

    @Test
    public void testCadreAlu(){
        IVelo v = new Velo();
        v = new OptCadreAlu(v);
        Assert.assertTrue(v.toString().contains("cadre aluminium"));
    }

    @Test
    public void testFreinsDisque(){
        IVelo v = new Velo();
        v = new OptFreinsDisque(v);
        Assert.assertTrue(v.toString().contains("freins à disque"));
    }

    @Test
    public void testSuspensionAvant(){
        IVelo v = new Velo();
        v = new OptSuspensionAvant(v);
        Assert.assertTrue(v.toString().contains("suspension avant"));
    }

    @Test
    public void testSuspensionArriere(){
        IVelo v = new Velo();
        v = new OptSuspensionArriere(v);
        Assert.assertTrue(v.toString().contains("suspension arrière"));
    }

    @Test
    public void testSansOption(){
        IVelo v = new Velo();
        Assert.assertEquals("Vélo cadre mixte - 0.0 km", v.toString());
    }

    @Test
    public void testUneOption(){
        IVelo v = new Velo();
        v = new OptCadreAlu(v);
        Assert.assertEquals("Vélo cadre mixte, cadre aluminium - 0.0 km", v.toString());
    }

    @Test
    public void testPlusieursOptions(){
        IVelo v = new Velo();
        v = new OptSuspensionArriere(v);
        v = new OptSuspensionAvant(v);
        Assert.assertEquals("Vélo cadre mixte, suspension arrière, suspension avant - 0.0 km", v.toString());
    }

    @Test
    public void testDoubleOption() {
        IVelo v = new Velo();
        v = new OptSuspensionArriere(v);
        v = new OptSuspensionArriere(v);
        Assert.assertEquals("Vélo cadre mixte, suspension arrière, suspension arrière - 0.0 km", v.toString());
    }

    @Test
    public void testToutesOptions(){
        IVelo v = new Velo();
        v = new OptSuspensionArriere(v);
        v = new OptSuspensionAvant(v);
        v = new OptFreinsDisque(v);
        v = new OptCadreAlu(v);
        v = new OptAssistanceElectrique(v);
        Assert.assertEquals("Vélo cadre mixte, suspension arrière, suspension avant, freins à disque, cadre aluminium, assistance électrique - 0.0 km", v.toString());
    }
}
