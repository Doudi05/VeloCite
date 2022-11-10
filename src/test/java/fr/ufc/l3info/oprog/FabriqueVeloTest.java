package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

public class FabriqueVeloTest {
    final String regex_toString = "^Vélo cadre (homme|femme|mixte)(, (cadre aluminium|freins à disque|assistance électrique|suspension arrière|suspension avant)){0,5} - \\d+.\\d km$";

    private FabriqueVelo fabriqueVelo;

    final String[][] options = {
            {"CADRE_ALUMINIUM", "cadre aluminium"},
            {"SUSPENSION_AVANT", "suspension avant"},
            {"SUSPENSION_ARRIERE", "suspension arrière"},
            {"FREINS_DISQUE", "freins à disque"},
            {"ASSISTANCE_ELECTRIQUE", "assistance électrique"}
    };

    final private FabriqueVelo OBJECT = FabriqueVelo.getInstance();
    final private String DEFAULT_STRING = "Vélo cadre mixte - 0.0 km";

    @Before
    public void initFabrique() {
        fabriqueVelo = FabriqueVelo.getInstance();
    }

    @Test
    public void notNull() {
        assertNotNull(fabriqueVelo);
    }

    @Test
    public void singleton() {
        assertSame(fabriqueVelo, OBJECT);
    }

    @Test
    public void constructeurPrive() {
        try {
            FabriqueVelo.class.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            fail("Le constructeur de FabriqueVelo est privé");
        }
    }

    @Test
    public void uneSeuleInstance() {
        assertEquals(FabriqueVelo.getInstance(), FabriqueVelo.getInstance());
    }

    @Test
    public void veloNotNull() {
        assertNotNull(fabriqueVelo.construire('m', "ASSISTANCE_ELECTRIQUE"));
    }

    @Test
    public void optionValide(){
        IVelo velo = fabriqueVelo.construire('f',"CADRE_ALUMINIUM");
        assertTrue(Pattern.matches(regex_toString,velo.toString()));
        assertTrue(velo.toString().contains("cadre aluminium"));
        assertEquals(2,velo.toString().split(",").length);
        assertEquals(2.2,velo.tarif(),1e-3);
    }

    @Test
    public void optionInvalide(){
        IVelo velo = fabriqueVelo.construire('f',"CADRE_ALUMINIUM","CADRE_ALUMINIUM");
        assertTrue(Pattern.matches(regex_toString,velo.toString()));
        assertTrue(velo.toString().contains("cadre aluminium"));
        assertEquals(2,velo.toString().split(",").length);
        assertEquals(2.2,velo.tarif(),1e-3);
    }

    @Test
    public void memeOptionPlusieursFois() {
        for (int i = 0; i < options.length; i++) {
            String[] opt = new String[(i + 1) * 2];

            for (int j = 0; j <= i * 2; j = j + 2) {
                opt[j] = options[j / 2][0];
                opt[j + 1] = options[j / 2][0];
            }

            IVelo velo = fabriqueVelo.construire('m', opt);

            assertTrue(Pattern.matches(regex_toString, velo.toString()));
            assertEquals(i + 2, velo.toString().split(",").length);

            for (int j = 0; j < i; j++) {
                assertTrue(velo.toString().contains(options[j][1]));
            }

        }
    }

    @Test
    public void optionCadreHomme() {
        assertEquals("Vélo cadre homme, assistance électrique - 0.0 km", fabriqueVelo.construire('H', "ASSISTANCE_ELECTRIQUE").toString());
        assertEquals("Vélo cadre homme, freins à disque - 0.0 km", fabriqueVelo.construire('h', "FREINS_DISQUE").toString());
    }

    @Test
    public void optionCadreFemme() {
        assertEquals("Vélo cadre femme, assistance électrique - 0.0 km", fabriqueVelo.construire('F', "ASSISTANCE_ELECTRIQUE").toString());
        assertEquals("Vélo cadre femme, freins à disque - 0.0 km", fabriqueVelo.construire('f', "FREINS_DISQUE").toString());
    }

    @Test
    public void optionCadreFemme2() {
        IVelo velo = fabriqueVelo.construire('F', "FREINS_DISQUE");
        assertEquals("Vélo cadre femme, freins à disque - 0.0 km", velo.toString());
        assertEquals(2.3, velo.tarif(),1e-3);
    }

    @Test
    public void sansOption() {
        assertEquals("Vélo cadre mixte - 0.0 km", fabriqueVelo.construire('\0').toString());
    }

    @Test
    public void optionsNull() {
        IVelo velo = fabriqueVelo.construire('H', null, "SUSPENSION_ARRIERE", null);
        assertEquals("Vélo cadre homme, suspension arrière - 0.0 km", velo.toString());
        assertEquals(2.5, velo.tarif(), 1e-3);
    }

    @Test
    public void optionUnknown() {
        IVelo velo = fabriqueVelo.construire('F', "gfdgfdg", "SUSPENSION_ARRIERE", "fsdfsds");
        assertEquals("Vélo cadre femme, suspension arrière - 0.0 km", velo.toString());
        assertEquals(2.5, velo.tarif(), 1e-3);
    }

    @Test
    public void optionNull() {
        IVelo velo = fabriqueVelo.construire('f', null);
        assertEquals("Vélo cadre femme - 0.0 km", velo.toString());
        assertEquals(2.0, velo.tarif(), 1e-3);
    }

    @Test
    public void toutesOptions(){
        IVelo velo = fabriqueVelo.construire('\\',"CADRE_ALUMINIUM",null,"FREINS","FREINS_DISQUE","SUSPENSION_ARRIERE","SUSPENSION_AVANT","ASSISTANCE_ELECTRIQUE","SUSPENSION_ARRIERE");
        assertTrue(Pattern.matches(regex_toString,velo.toString()));
        assertEquals(5.5,velo.tarif(),1e-3);
        assertEquals(6,velo.toString().split(",").length);
        for(String[] opts : options){
            assertTrue(velo.toString().contains(opts[1]));
        }
        assertTrue(velo.toString().startsWith("Vélo cadre mixte"));
    }

    @Test
    public void testCreateSeveralFabrique() {
        FabriqueVelo f2 = FabriqueVelo.getInstance();
        Assert.assertEquals(OBJECT, f2);
    }

    @Test
    public void testCreateVelo_MAN() {
        IVelo v = OBJECT.construire('H');
        Assert.assertEquals(v.toString(), "Vélo cadre homme - 0.0 km");
    }

    @Test
    public void testCreateVelo_man() {
        IVelo v = OBJECT.construire('h');
        Assert.assertEquals(v.toString(), "Vélo cadre homme - 0.0 km");
    }

    @Test
    public void testCreateVelo_WOMAN() {
        IVelo v = OBJECT.construire('F');
        Assert.assertEquals(v.toString(), "Vélo cadre femme - 0.0 km");
    }

    @Test
    public void testCreateVelo_woman() {
        IVelo v = OBJECT.construire('f');
        Assert.assertEquals(v.toString(), "Vélo cadre femme - 0.0 km");
    }

    @Test
    public void testCreateVelo_mixed() {
        IVelo v = OBJECT.construire('P');
        Assert.assertEquals(v.toString(), "Vélo cadre mixte - 0.0 km");
    }

    @Test
    public void testCreateVelo_OneNull() {
        IVelo v = OBJECT.construire('m', null);
        Assert.assertEquals(v.toString(), DEFAULT_STRING);
    }

    @Test
    public void testCreateVelo_SeveralNull() {
        IVelo v = OBJECT.construire('m', null, null);
        Assert.assertEquals(v.toString(), DEFAULT_STRING);
    }

    @Test
    public void testCreateVelo_SeveralNull2() {
        IVelo v = OBJECT.construire('m', null, null, "CADRE_ALUMINIUM");
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, cadre aluminium - 0.0 km");
    }

    @Test
    public void testCreateVelo() {
        IVelo v = OBJECT.construire('m');
        Assert.assertEquals(v.tarif(), 2, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte - 0.0 km");
    }

    @Test
    public void testCreateVelo_CadreAlu_Once() {
        IVelo v = OBJECT.construire('m', "CADRE_ALUMINIUM");
        Assert.assertEquals(v.tarif(), 2.2, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, cadre aluminium - 0.0 km");
    }

    @Test
    public void testCreateVelo_CadreAlu_Several() {
        IVelo v = OBJECT.construire('m', "CADRE_ALUMINIUM", "CADRE_ALUMINIUM");
        Assert.assertEquals(v.tarif(), 2.2, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, cadre aluminium - 0.0 km");
    }

    @Test
    public void testCreateVelo_FreinDisque_Once() {
        IVelo v = OBJECT.construire('m', "FREINS_DISQUE");
        Assert.assertEquals(v.tarif(), 2.3, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, freins à disque - 0.0 km");
    }

    @Test
    public void testCreateVelo_FreinDisque_Several() {
        IVelo v = OBJECT.construire('m', "FREINS_DISQUE", "FREINS_DISQUE");
        Assert.assertEquals(v.tarif(), 2.3, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, freins à disque - 0.0 km");
    }

    @Test
    public void testCreateVelo_SuspensionAvant_Once() {
        IVelo v = OBJECT.construire('m', "SUSPENSION_AVANT");
        Assert.assertEquals(v.tarif(), 2.5, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, suspension avant - 0.0 km");
    }

    @Test
    public void testCreateVelo_SuspensionAvant_Several() {
        IVelo v = OBJECT.construire('m', "SUSPENSION_AVANT", "SUSPENSION_AVANT");
        Assert.assertEquals(v.tarif(), 2.5, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, suspension avant - 0.0 km");
    }

    @Test
    public void testCreateVelo_SuspensionArriere_Once() {
        IVelo v = OBJECT.construire('m', "SUSPENSION_ARRIERE");
        Assert.assertEquals(v.tarif(), 2.5, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, suspension arrière - 0.0 km");
    }

    @Test
    public void testCreateVelo_SuspensionArriere_Several() {
        IVelo v = OBJECT.construire('m', "SUSPENSION_ARRIERE", "SUSPENSION_ARRIERE");
        Assert.assertEquals(v.tarif(), 2.5, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, suspension arrière - 0.0 km");
    }

    @Test
    public void testCreateVelo_AssistanceElectrique_Once() {
        IVelo v = OBJECT.construire('m', "ASSISTANCE_ELECTRIQUE");
        Assert.assertEquals(v.tarif(), 4.0, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, assistance électrique - 0.0 km");
    }

    @Test
    public void testCreateVelo_AssistanceElectrique_Several() {
        IVelo v = OBJECT.construire('m', "ASSISTANCE_ELECTRIQUE", "ASSISTANCE_ELECTRIQUE");
        Assert.assertEquals(v.tarif(), 4.0, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, assistance électrique - 0.0 km");
    }

    @Test
    public void testCreateVelo_OtherOption() {
        IVelo v = OBJECT.construire('m', "DHTHTFHD");
        Assert.assertEquals(v.tarif(), 2.0, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte - 0.0 km");
    }

    @Test
    public void testMultipleOptions_TwoOptions() {
        IVelo v = OBJECT.construire('m', "CADRE_ALUMINIUM", "FREINS_DISQUE");

        Pattern p = Pattern.compile("Vélo cadre mixte, ((cadre aluminium, freins à disque)|(freins à disque, cadre aluminium)) - 0\\.0 km");
        Matcher m = p.matcher(v.toString());
        Assert.assertTrue(m.matches());

        final double veloTarif = 2.0;
        final double aluminiumTarif = 0.2;
        final double freinsTarif = 0.3;
        Assert.assertEquals(veloTarif + aluminiumTarif + freinsTarif, v.tarif(), 0.001);
    }

    @Test
    public void testMultipleOptions_TwoOptions_DifferentOrder() {
        IVelo v = OBJECT.construire('m', "FREINS_DISQUE", "CADRE_ALUMINIUM");

        Pattern p = Pattern.compile("Vélo cadre mixte, ((cadre aluminium, freins à disque)|(freins à disque, cadre aluminium)) - 0\\.0 km");
        Matcher m = p.matcher(v.toString());
        Assert.assertTrue(m.matches());

        final double veloTarif = 2.0;
        final double aluminiumTarif = 0.2;
        final double freinsTarif = 0.3;
        Assert.assertEquals(veloTarif + aluminiumTarif + freinsTarif, v.tarif(), 0.001);
    }

    @Test
    public void testCreateVelo_CaseSensitive() {
        IVelo v = OBJECT.construire('m', "cAdEe_aLuMiNiUm");
        Assert.assertEquals(v.tarif(), 2.0, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte - 0.0 km");
    }

    @Test
    public void testCreateVelo_CaseSensitive_SeveralTimesSameOption() {
        IVelo v = OBJECT.construire('m', "CADRE_ALUMINIUM", "cAdEe_aLuMiNiUm");
        Assert.assertEquals(v.tarif(), 2.2, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, cadre aluminium - 0.0 km");
    }
}
