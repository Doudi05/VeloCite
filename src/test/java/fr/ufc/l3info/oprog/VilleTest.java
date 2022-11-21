package fr.ufc.l3info.oprog;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;


public class VilleTest {
    private Ville ville;
    private Abonne Me;

    final String path = "./target/classes/data/";
    final static String VALID_RIB = "43653-17457-19365836593-50";
    final static String INVALID_RIB = "543543-sdfsg";


    @Before
    public void initVille() throws IOException {
        this.ville = new Ville();
        File f = new File(path + "stationsBesancon.txt");
        ville.initialiser(f);
    }

    @Test
    public void initialiser_mauvaisFichier() throws IOException {
        Ville empty = new Ville();
        File f = new File(path + "stationsEgal.txt");
        empty.initialiser(f);
        assertFalse(empty.iterator().hasNext());
        assertNull(empty.getStationPlusProche(0, 0));
    }

    @Test
    public void initialiser_bonOrdre() {
        assertTrue(ville.iterator().hasNext());
        String[] stations = {   "Gare Viotte",
                "Square Bouchot",
                "Isembart",
                "Beaux Arts",
                "Médiathèque",
                "Bersot",
                "Morand",
                "8 Septembre",
                "Révolution",
                "Madeleine",
                "Marulaz",
                "Belfort",
                "Place Leclerc",
                "Xavier Marmier",
                "La City",
                "Saint Jacques",
                "Mairie",
                "Granvelle",
                "Victor Hugo",
                "Jean Cornet",
                "Tassigny",
                "Gare de la Mouillère",
                "Office de tourisme",
                "Place Flore",
                "Place de la Liberté",
                "Jacobins",
                "Rivotte",
                "Déportés",
                "Beauregard",
                "Gare d'Eau"
        };
        int i = 0;
        for (Station s : ville) {
            assertTrue(i <= 29);
            assertEquals(s.getNom(),stations[i]);
            i++;
        }
        assertEquals( 30,i);
    }

    @Test
    public void initialiser_stationPrincipaleDefaut() {
        Iterator<Station> it = ville.iterator();
        Station first = it.next();
        assertEquals("Gare Viotte", first.getNom());
    }

    @Test
    public void setStationPrincipale_null(){
        ville.setStationPrincipale(ville.getStation("Bersot"));
        ville.setStationPrincipale(null);
        assertEquals("Bersot",ville.iterator().next().getNom());
    }

    @Test
    public void stationPrincipaleChoisie(){
        ville.setStationPrincipale(ville.getStation("8 Septembre"));
        String [] stations = {  "8 Septembre",
                "Morand",
                "Médiathèque",
                "Bersot",
                "Tassigny",
                "Gare de la Mouillère",
                "Office de tourisme",
                "Beaux Arts",
                "Isembart",
                "Square Bouchot",
                "Gare Viotte",
                "Place Flore",
                "Place de la Liberté",
                "Révolution",
                "Madeleine",
                "Marulaz",
                "Belfort",
                "Place Leclerc",
                "Xavier Marmier",
                "La City",
                "Saint Jacques",
                "Mairie",
                "Granvelle",
                "Victor Hugo",
                "Jean Cornet",
                "Jacobins",
                "Rivotte",
                "Déportés",
                "Beauregard",
                "Gare d'Eau"
        };

        int i = 0;
        for (Station s : ville) {
            assertTrue(i <= 29);
            assertEquals(s.getNom(),stations[i]);
            i++;
        }
        assertEquals( 30,i);
    }

    @Test
    public void getStation() {
        assertNotNull(ville.getStation("Gare Viotte"));
        assertNotNull(ville.getStation("Saint Jacques"));
        assertNotNull(ville.getStation("Granvelle"));
        assertNotNull(ville.getStation("Victor Hugo"));
        assertNotNull(ville.getStation("Mairie"));
        assertNotNull(ville.getStation("Jean Cornet"));
        assertNull(ville.getStation("Time Square"));
        assertNull(ville.getStation("Musée Ghibli"));
        assertNull(ville.getStation(null));
    }

    @Test
    public void creerAbonne_ok() {
        Abonne a = ville.creerAbonne("Me Jobs", VALID_RIB);
        assertNotNull(a);

        assertEquals("Me Jobs", a.getNom());
        //assertFalse(a.estBloque());
    }

    @Test
    public void creerAbonne_mauvaisRib() {
        Abonne a = ville.creerAbonne("Me Gates", INVALID_RIB);
        assertNotNull(a);
        assertTrue(a.estBloque());
    }

    @Test
    public void creerAbonne_mauvaisNom() {
        Abonne a = ville.creerAbonne("P0G", VALID_RIB);
        assertNull(a);
    }

    @Test
    public void creerAbonne_nomNull(){
        assertNull(ville.creerAbonne(null,VALID_RIB));
    }

    @Test
    public void creerAbonne_ribNull(){
        Abonne a = ville.creerAbonne("Maradona",null);
        assertTrue(a.estBloque());
    }

    @Test
    public void getStationPlusProche(){
        assertEquals("Gare de la Mouillère",ville.getStationPlusProche(47.23982496297786,6.033786686212529).getNom());
        assertEquals("Xavier Marmier",ville.getStationPlusProche(47,5).getNom());
    }

    public void emprunterABersot(){
        Station bersot = Mockito.spy(ville.getStation("Bersot"));
        assertEquals(11,bersot.capacite());
        bersot.arrimerVelo(new Velo('h'),6);
        this.Me = ville.creerAbonne("Me",VALID_RIB);
        Mockito.when(bersot.maintenant()).thenReturn(1608114888000L);
        IVelo velo = bersot.emprunterVelo(this.Me,6);
        Mockito.when(bersot.maintenant()).thenReturn(1608118668000L);
        bersot.arrimerVelo(velo,2);
    }

    @Test
    public void facturation_0emprunt(){
        emprunterABersot();
        Map<Abonne, Double> fact = ville.facturation(11,2020);
        assertEquals(1,fact.size());
        assertTrue(fact.containsValue(0.0));
        assertTrue(fact.containsKey(this.Me));
    }



    @Test
    public void facturation_mauvaisMois(){
        emprunterABersot();

        Map<Abonne, Double> fact = ville.facturation(0,2020);
        assertEquals(1,fact.size());
        assertTrue(fact.containsValue(0.0));
        assertTrue(fact.containsKey(this.Me));

        fact = ville.facturation(13,2020);
        assertEquals(1,fact.size());
        assertTrue(fact.containsValue(0.0));
        assertTrue(fact.containsKey(this.Me));
    }

    @Test
    public void facturation(){
        emprunterABersot();
        Abonne one = ville.creerAbonne("one",INVALID_RIB);
        Map<Abonne, Double> fact = ville.facturation(12,2020);
        assertEquals(2,fact.size());
        assertEquals(fact.get(one),0.0,1e-3);
        assertEquals(fact.get(this.Me),0.0,1e-3);
    }

    @Test
    public void facturation_2emprunts(){
        emprunterABersot();

        Station city = Mockito.spy(ville.getStation("La City"));
        IVelo velo = new Velo('f');
        velo = new OptAssistanceElectrique(velo);
        city.arrimerVelo(velo,1);
        Mockito.when(city.maintenant()).thenReturn(1608463987000L);
        velo = city.emprunterVelo(this.Me,1);
        Mockito.when(city.maintenant()).thenReturn(1608472567000L);
        city.arrimerVelo(velo,2);

        Map<Abonne,Double> fact = ville.facturation(12,2020);
        assertEquals(1,fact.size());
        assertEquals(0.0,fact.get(this.Me),1e-3);
    }

    @Test
    public void facturation_empruntEnCourt(){
        emprunterABersot();

        Station city = Mockito.spy(ville.getStation("La City"));
        IVelo velo = new Velo('f');
        velo = new OptAssistanceElectrique(velo);
        city.arrimerVelo(velo,1);
        Mockito.when(city.maintenant()).thenReturn(1609435897000L);
        velo = city.emprunterVelo(this.Me,1);

        Map<Abonne,Double> fact = ville.facturation(12,2020);
        assertEquals(1,fact.size());
        assertEquals(fact.get(this.Me),0.0,1e-3);

        Mockito.when(city.maintenant()).thenReturn(1609500787000L);
        city.arrimerVelo(velo,1);

        fact = ville.facturation(12,2020);
        assertEquals(1,fact.size());
        assertEquals(fact.get(this.Me),0.0,1e-3);

        fact = ville.facturation(1,2021);
        assertEquals(1,fact.size());
        assertEquals(fact.get(this.Me),0.0,1e-3);
    }
}
