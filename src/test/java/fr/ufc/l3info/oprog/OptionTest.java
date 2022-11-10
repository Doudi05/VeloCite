package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

public class OptionTest {

    private IVelo velo, veloOriginal, veloOption;
    private OptCadreAlu defaultOptCadreAlu;
    final private IVelo DEFAULT = new Velo();
    final String regex_toString = "^Vélo cadre (homme|femme|mixte)(, (cadre aluminium|freins à disque|assistance électrique|suspension arrière|suspension avant))* - \\d+.\\d km$";

    @Before
    public void InitVelos() {
        veloOriginal = new Velo();

        veloOption = new Velo();
        veloOption = new OptFreinsDisque(veloOption);
        veloOption = new OptSuspensionArriere(veloOption);
        veloOption = new OptAssistanceElectrique(veloOption);
        veloOption = new OptCadreAlu(veloOption);
        veloOption = new OptSuspensionAvant(veloOption);

        this.velo = new Velo();
        this.defaultOptCadreAlu = new OptCadreAlu(this.velo);
    }

    @Test
    public void testFonctions() {
        assertEquals(veloOption.decrocher(), -1);
        veloOption.parcourir(102.34);
        assertEquals(veloOption.kilometrage(), 102.34, 1e-3);
        assertEquals(veloOption.prochaineRevision(), 500 - 102.34, 1e-3);
        assertEquals(veloOption.arrimer(), 0);
        assertEquals(veloOption.arrimer(), -1);
        veloOption.abimer();
        assertTrue(veloOption.estAbime());
        assertEquals(veloOption.reviser(), -1);
        assertEquals(veloOption.decrocher(), 0);
        assertEquals(veloOption.reviser(), 0);
        assertEquals(veloOption.prochaineRevision(), 500, 1e-3);
        assertEquals(veloOption.reparer(), -2);
    }

    @Test
    public void toStringveloOriginal(){
        // Pas de virgules
        assertTrue(Pattern.matches(regex_toString,veloOriginal.toString()));
        assertEquals(1,veloOriginal.toString().split(",").length);

        veloOriginal = new OptFreinsDisque(veloOriginal);

        // Contient une virgule, et le nom de l'option seulement
        assertTrue(Pattern.matches(regex_toString,veloOriginal.toString()));
        assertEquals(2,veloOriginal.toString().split(",").length);
        assertTrue(veloOriginal.toString().contains("freins à disque"));
        assertFalse(veloOriginal.toString().contains("suspension arrière"));

        // Contient deux virgules, et le nom des deux options seulement
        veloOriginal = new OptSuspensionArriere(veloOriginal);
        assertTrue(Pattern.matches(regex_toString,veloOriginal.toString()));
        assertEquals(3,veloOriginal.toString().split(",").length);
        assertTrue(veloOriginal.toString().contains("freins à disque"));
        assertTrue(veloOriginal.toString().contains("suspension arrière"));
    }

    @Test
    public void toStringVeloOptions(){
        assertTrue(Pattern.matches(regex_toString,veloOption.toString()));
        assertEquals(6,veloOption.toString().split(",").length);
        assertTrue(veloOption.toString().contains("freins à disque"));
        assertTrue(veloOption.toString().contains("suspension arrière"));
        assertTrue(veloOption.toString().contains("suspension avant"));
        assertTrue(veloOption.toString().contains("assistance électrique"));
        assertTrue(veloOption.toString().contains("cadre aluminium"));
    }

    @Test
    public void tarifVeloOriginal() {
        assertEquals(2, veloOriginal.tarif(), 1e-3);
        veloOriginal = new OptSuspensionArriere(veloOriginal);
        assertEquals(2.5, veloOriginal.tarif(), 1e-3);
        veloOriginal = new OptSuspensionAvant(veloOriginal);
        assertEquals(3, veloOriginal.tarif(), 1e-3);
        veloOriginal = new OptCadreAlu(veloOriginal);
        assertEquals(3.2, veloOriginal.tarif(), 1e-3);
        veloOriginal = new OptFreinsDisque(veloOriginal);
        assertEquals(3.5, veloOriginal.tarif(), 1e-3);
        veloOriginal = new OptAssistanceElectrique(veloOriginal);
        assertEquals(5.5, veloOriginal.tarif(), 1e-3);
    }

    @Test
    public void tarifVeloToutesOptions(){
        assertEquals(5.5, veloOption.tarif(),1e-3);
    }

    @Test
    public void testAS(){
        IVelo velo = new Velo();
        velo = new OptAssistanceElectrique(velo);
        assertTrue(velo.toString().contains("assistance électrique"));
    }

    @Test
    public void testAD(){
        IVelo velo = new Velo();
        velo = new OptAssistanceElectrique(velo);
        velo = new OptFreinsDisque(velo);
        assertTrue(velo.toString().contains("assistance électrique"));
        assertTrue(velo.toString().contains("freins à disque"));
    }

    @Test
    public void testASAD(){
        IVelo velo = new Velo();
        velo = new OptAssistanceElectrique(velo);
        velo = new OptFreinsDisque(velo);
        velo = new OptSuspensionArriere(velo);
        assertTrue(velo.toString().contains("assistance électrique"));
        assertTrue(velo.toString().contains("freins à disque"));
        assertTrue(velo.toString().contains("suspension arrière"));
    }

    @Test
    public void testASADSA(){
        IVelo velo = new Velo();
        velo = new OptAssistanceElectrique(velo);
        velo = new OptFreinsDisque(velo);
        velo = new OptSuspensionArriere(velo);
        velo = new OptSuspensionAvant(velo);
        assertTrue(velo.toString().contains("assistance électrique"));
        assertTrue(velo.toString().contains("freins à disque"));
        assertTrue(velo.toString().contains("suspension arrière"));
        assertTrue(velo.toString().contains("suspension avant"));
    }

    @Test
    public void testASADSAAD(){
        IVelo velo = new Velo();
        velo = new OptAssistanceElectrique(velo);
        velo = new OptFreinsDisque(velo);
        velo = new OptSuspensionArriere(velo);
        velo = new OptSuspensionAvant(velo);
        velo = new OptCadreAlu(velo);
        assertTrue(velo.toString().contains("assistance électrique"));
        assertTrue(velo.toString().contains("freins à disque"));
        assertTrue(velo.toString().contains("suspension arrière"));
        assertTrue(velo.toString().contains("suspension avant"));
        assertTrue(velo.toString().contains("cadre aluminium"));
    }

    @Test
    public void testVeloMethods_kilometrage() {
        Assert.assertEquals(defaultOptCadreAlu.kilometrage(), 0, 0.001);
        Assert.assertEquals(defaultOptCadreAlu.prochaineRevision(), 500, 0.001);
        defaultOptCadreAlu.parcourir(100);
        Assert.assertEquals(defaultOptCadreAlu.kilometrage(), 100, 0.001);
        defaultOptCadreAlu.parcourir(-100);
        Assert.assertEquals(defaultOptCadreAlu.kilometrage(), 100, 0.001);
        Assert.assertEquals(defaultOptCadreAlu.prochaineRevision(), 500 - 100, 0.001);
    }

    @Test
    public void testVeloMethods_parcours() {
        Assert.assertEquals(defaultOptCadreAlu.decrocher(), -1);
        Assert.assertEquals(defaultOptCadreAlu.arrimer(), 0);
        Assert.assertEquals(defaultOptCadreAlu.arrimer(), -1);
        Assert.assertEquals(defaultOptCadreAlu.decrocher(), 0);
    }

    @Test
    public void testVeloMethods_parcours2() {
        Assert.assertFalse(defaultOptCadreAlu.estAbime());
        velo.abimer();
        Assert.assertTrue(defaultOptCadreAlu.estAbime());
        Assert.assertEquals(defaultOptCadreAlu.arrimer(), 0);
        Assert.assertEquals(defaultOptCadreAlu.reviser(), -1);
        Assert.assertEquals(defaultOptCadreAlu.decrocher(), 0);
        Assert.assertEquals(defaultOptCadreAlu.reviser(), 0);
        Assert.assertFalse(defaultOptCadreAlu.estAbime());
    }

    @Test
    public void testVeloMethods_parcours3() {
        Assert.assertFalse(defaultOptCadreAlu.estAbime());
        defaultOptCadreAlu.abimer();
        Assert.assertTrue(defaultOptCadreAlu.estAbime());
        defaultOptCadreAlu.arrimer();
        Assert.assertEquals(defaultOptCadreAlu.reparer(), -1);
        defaultOptCadreAlu.decrocher();
        Assert.assertEquals(defaultOptCadreAlu.reparer(), 0);
        Assert.assertEquals(defaultOptCadreAlu.reparer(), -2);
    }

    @Test
    public void testOptionCadreAlu_Tarif2() {
        velo = new OptCadreAlu(velo);
        assertEquals(2.2, velo.tarif(), 1e-3);
    }

    @Test
    public void testOptionCadreAlu_ToString2() {
        velo = new OptCadreAlu(velo);
        assertTrue(velo.toString().contains("Vélo cadre mixte, cadre aluminium - 0.0 km"));
    }

    @Test
    public void testOptionFreinsDisque_Tarif2() {
        velo = new OptFreinsDisque(velo);
        assertEquals(2.3, velo.tarif(), 1e-3);
    }

    @Test
    public void testOptionFreinsDisque_ToString2() {
        velo = new OptFreinsDisque(velo);
        assertTrue(velo.toString().contains("Vélo cadre mixte, freins à disque - 0.0 km"));
    }

    @Test
    public void testOptionSuspensionArriere_Tarif2() {
        velo = new OptSuspensionArriere(velo);
        assertEquals(2.5, velo.tarif(), 1e-3);
    }

    @Test
    public void testOptionSuspensionArriere_ToString2() {
        velo = new OptSuspensionArriere(velo);
        assertTrue(velo.toString().contains("Vélo cadre mixte, suspension arrière - 0.0 km"));
    }

    @Test
    public void testOptionSuspensionAvant_Tarif2() {
        velo = new OptSuspensionAvant(velo);
        assertEquals(2.5, velo.tarif(), 1e-3);
    }

    @Test
    public void testOptionSuspensionAvant_ToString2() {
        velo = new OptSuspensionAvant(velo);
        assertTrue(velo.toString().contains("Vélo cadre mixte, suspension avant - 0.0 km"));
    }

    @Test
    public void testOptionAssistanceElectrique_Tarif2() {
        velo = new OptAssistanceElectrique(velo);
        assertEquals(4, velo.tarif(), 1e-3);
    }

    @Test
    public void testOptionAssistanceElectrique_ToString2() {
        velo = new OptAssistanceElectrique(velo);
        assertTrue(velo.toString().contains("Vélo cadre mixte, assistance électrique - 0.0 km"));
    }



    @Test
    public void testOptionCadreAlu_Tarif() {
        velo = new OptCadreAlu(velo);

        Assert.assertEquals(velo.tarif(), DEFAULT.tarif() + 0.2, 0);
    }

    @Test
    public void testOptionCadreAlu_ToString() {
        velo = new OptCadreAlu(velo);

        Assert.assertEquals(velo.toString(), "Vélo cadre mixte, cadre aluminium - 0.0 km");
    }

    /* ---------- Option Freins disque ---------- */

    @Test
    public void testOptionFreinsDisque_Tarif() {
        velo = new OptFreinsDisque(velo);

        Assert.assertEquals(velo.tarif(), DEFAULT.tarif() + 0.3, 0);
    }

    @Test
    public void testOptionFreinsDisque_ToString() {
        velo = new OptFreinsDisque(velo);

        Assert.assertEquals(velo.toString(), "Vélo cadre mixte, freins à disque - 0.0 km");
    }

    /* ---------- Option Suspension avant ---------- */

    @Test
    public void testOptionSuspensionAvant_Tarif() {
        velo = new OptSuspensionAvant(velo);

        Assert.assertEquals(velo.tarif(), DEFAULT.tarif() + 0.5, 0);
    }

    @Test
    public void testOptionSuspensionAvant_ToString() {
        velo = new OptSuspensionAvant(velo);

        Assert.assertEquals(velo.toString(), "Vélo cadre mixte, suspension avant - 0.0 km");
    }

    /* ---------- Option Suspension arrière ---------- */

    @Test
    public void testOptionSuspensionArriere_Tarif() {
        velo = new OptSuspensionArriere(velo);

        Assert.assertEquals(velo.tarif(), DEFAULT.tarif() + 0.5, 0);
    }

    @Test
    public void testOptionSuspensionArriere_ToString() {
        velo = new OptSuspensionArriere(velo);

        Assert.assertEquals(velo.toString(), "Vélo cadre mixte, suspension arrière - 0.0 km");
    }

    /* ---------- Option Assistance électrique ---------- */

    @Test
    public void testOptionAssistanceElectrique_Tarif() {
        velo = new OptAssistanceElectrique(velo);

        Assert.assertEquals(velo.tarif(), DEFAULT.tarif() + 2, 0);
    }

    @Test
    public void testOptionAssistanceElectrique_ToString() {
        velo = new OptAssistanceElectrique(velo);

        Assert.assertEquals(velo.toString(), "Vélo cadre mixte, assistance électrique - 0.0 km");
    }
}
