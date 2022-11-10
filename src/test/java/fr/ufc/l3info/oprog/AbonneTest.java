package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test unitaire pour les abonnés.
 */
public class AbonneTest {

    final static String VALID_USER_NAME = "Ward Akel";
    final static String VALID_RIB = "12345-98765-12345678912-21";
    final static String INVALID_RIB = "548127-rib";

    @Test
    public void NomValide() throws IncorrectNameException{
        new Abonne("Ward-Akel");
    }

    @Test (expected = IncorrectNameException.class)
    public void NomInvalide_DoubleTirets() throws IncorrectNameException {
        new Abonne("Ward--Akel");
    }

    @Test (expected = IncorrectNameException.class)
    public void NomInvalide_Nombres() throws IncorrectNameException {
        new Abonne("Ward05");
    }

    @Test (expected = IncorrectNameException.class)
    public void NomInvalide_DoubleEspace() throws IncorrectNameException {
        new Abonne("Ward  Akel");
    }

    @Test (expected = IncorrectNameException.class)
    public void NomInvalide_Symboles() throws IncorrectNameException {
        new Abonne("W@rd<;/");
    }

    @Test (expected = IncorrectNameException.class)
    public void NomInvalide_Vide() throws IncorrectNameException {
        new Abonne("");
    }

    @Test
    public void RibValide() throws IncorrectNameException{
        String rib = "10278-08000-00022270603-02";
        Abonne a = new Abonne("Ward",rib);
        assertFalse(a.estBloque());
    }

    @Test
    public void RibInvalide_MauvaiseCle() throws IncorrectNameException{
        String rib = "10278-08000-00022270603-72";
        Abonne a = new Abonne("Ward",rib);
        assertTrue(a.estBloque());
    }

    @Test
    public void RibInvalide_Lettres() throws IncorrectNameException{
        String rib = "102A8-08000-00022270603-02";
        Abonne a = new Abonne("Ward",rib);
        assertTrue(a.estBloque());
    }

    @Test
    public void RibInvalide_Symboles() throws IncorrectNameException{
        String rib = "102%8-08000-00022270603-02";
        Abonne a = new Abonne("Ward",rib);
        assertTrue(a.estBloque());
    }

    @Test
    public void RibInvalide_MauvaisFormat() throws IncorrectNameException{
        String rib = "10278 08000 00022270603 02";
        Abonne a = new Abonne("Ward",rib);
        assertTrue(a.estBloque());
    }

    /*@Test
    public void RibNull() throws IncorrectNameException {
        Abonne a = new Abonne("Ward");
        assertTrue(a.estBloque());
        a.miseAJourRIB(null);
        assertTrue(a.estBloque());
    }*/

    @Test
    public void Id() throws IncorrectNameException{
        Abonne user1 = new Abonne("Ward Akel");
        Abonne user2 = new Abonne("Ivann Roux");

        assertTrue(user1.getID() >= 0);
        assertEquals(user2.getID(), user1.getID()+1);
    }

    @Test
    public void Equals() throws IncorrectNameException{
        Abonne user1 = new Abonne("Ward Akel");
        Abonne user2 = new Abonne("Ivann Roux");

        assertEquals(user1, user1);
        assertNotEquals(user2, user1);
    }

    @Test
    public void Equals_MauvaisType() throws IncorrectNameException{
        Abonne user1 = new Abonne("Ward Akel");

        Assert.assertNotEquals(user1, new String());
        assertEquals(user1, (Object) user1);
    }

    @Test
    public void Equals_null() throws IncorrectNameException{
        Abonne user1 = new Abonne("Ward Akel");

        assertNotEquals(null, user1);
    }

    @Test
    public void Hash() throws IncorrectNameException{
        Abonne user1 = new Abonne("Ward Akel");
        Abonne user2 = new Abonne("Ivann Roux");

        assertEquals(user1.hashCode(),user1.hashCode());
        Assert.assertNotEquals(user2.hashCode(),user1.hashCode());
        Assert.assertNotEquals(user2.hashCode(),new Object().hashCode());
    }

    @Test
    public void Nom() throws IncorrectNameException {
        Abonne a = new Abonne("Ward");
        assertEquals("Ward", a.getNom());
    }

    @Test(expected = IncorrectNameException.class)
    public void NomNull() throws IncorrectNameException {
        Abonne a = new Abonne(null);
    }

    @Test
    public void NomTrim() throws IncorrectNameException {
        Abonne a = new Abonne("   Ward    ");
        assertEquals(a.getNom(),"Ward");
    }


    @Test
    public void MiseAJourRib() throws  IncorrectNameException{
        Abonne a = new Abonne("Ward Akel");
        assertTrue(a.estBloque());
        a.miseAJourRIB("10278-08000-00022270603-02");
        assertFalse(a.estBloque());
    }

    @Test
    public void MiseAJourRib_invalide() throws  IncorrectNameException{
        Abonne a = new Abonne("Ward Akel","10278-08000-00022270603-02");
        assertFalse(a.estBloque());
        a.miseAJourRIB("XX");
        assertFalse(a.estBloque());
    }

    @Test
    public void MiseAJourRib_invalideBis() throws  IncorrectNameException{
        Abonne a = new Abonne("Ward Akel");
        assertTrue(a.estBloque());
        a.miseAJourRIB("XX");
        assertTrue(a.estBloque());
    }

    @Test
    public void BloquerDebloquer() throws IncorrectNameException{
        Abonne a = new Abonne("Ivann rr","10278-08000-00022270603-02");
        assertFalse(a.estBloque());
        a.bloquer();
        assertTrue(a.estBloque());
        a.debloquer();
        assertFalse(a.estBloque());

    }

    @Test
    public void BlockerDebloquerSansRib() throws IncorrectNameException{
        Abonne a = new Abonne("Ivann rr");
        assertTrue(a.estBloque());
        a.debloquer();
        assertTrue(a.estBloque());

    }

    public void BlockerMAJRib() throws IncorrectNameException{
        Abonne a = new Abonne("Ivann rrr");
        assertTrue(a.estBloque());
        a.bloquer();
        a.miseAJourRIB("10278-08000-00022270603-02");
        assertTrue(a.estBloque());
    }

    @Test
    public void NouvelAbonne_RibCorrect() throws IncorrectNameException {
        Abonne a = new Abonne("Ivann rrr", "12345-98765-12345678912-21");
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void NouvelAbonne_RibIncorrect_Format() throws IncorrectNameException {
        Abonne a = new Abonne("Ivann rrr", "12345-fdets");
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void NouvelAbonne_RibIncorrect_Cle() throws IncorrectNameException {
        Abonne a = new Abonne("Ivann rrr", "12345-98765-12345678912-42");
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void MiseAJourRib_CorrectVersCorrect() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);
        Assert.assertFalse(a.estBloque());
        a.miseAJourRIB(AbonneTest.VALID_RIB);
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void MiseAJourRiv_NullVersCorrect() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME);
        Assert.assertTrue(a.estBloque());
        a.miseAJourRIB(AbonneTest.VALID_RIB);
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void MiseAJourRib_CorrectVersCorrect_Bloque() throws IncorrectNameException {
        Abonne a = new Abonne("Ivann rrr", "12345-98765-12345678912-42");
        a.bloquer();
        Assert.assertTrue(a.estBloque());
        a.miseAJourRIB("12345-98765-12345678912-42");
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void MiseAJourRib_IncorrectVersCorrect_Bloque() throws IncorrectNameException {
        Abonne a = new Abonne("Ivann rrr", "12345-98765-12345678912-42");
        a.bloquer();
        Assert.assertTrue(a.estBloque());
        a.miseAJourRIB("12345-98765-12345678912-42");
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void MiseAJourRib_CorrectVersIncorrect() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);
        Assert.assertFalse(a.estBloque());
        a.miseAJourRIB(AbonneTest.INVALID_RIB);
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void MiseAJourRib_CorrectVersNull() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);
        Assert.assertFalse(a.estBloque());
        a.miseAJourRIB(null);
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void MiseAJourRib_IncorrectVersCorrect() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.INVALID_RIB);
        Assert.assertTrue(a.estBloque());
        a.miseAJourRIB(AbonneTest.VALID_RIB);
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void Bloquer_RibCorrect() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);
        Assert.assertFalse(a.estBloque());
        a.bloquer();
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void Bloquer_RibIncorrect() throws IncorrectNameException {
        Abonne a = new Abonne("Ivann rrr", "12345-fdets");
        Assert.assertTrue(a.estBloque());
        a.bloquer();
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void Debloquer_Simple_OuiVersOui() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);
        a.bloquer();
        Assert.assertTrue(a.estBloque());
        a.debloquer();
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void Debloquer_Simple_NonVersOui() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);
        Assert.assertFalse(a.estBloque());
        a.debloquer();
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void Debloquer_Echec_RibNull() throws IncorrectNameException {
        Abonne a = new Abonne("Ivann rrr");

        Assert.assertTrue(a.estBloque());
        a.debloquer();
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void Debloquer_Echec_RibIncorrect() throws IncorrectNameException {
        Abonne a = new Abonne("Ivann rrr", "12345-fdets");

        Assert.assertTrue(a.estBloque());
        a.debloquer();
        Assert.assertTrue(a.estBloque());
    }
}
