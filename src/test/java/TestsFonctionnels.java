import fr.ufc.l3info.oprog.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

public class TestsFonctionnels {
    final int CAPACITE = 10;
    IRegistre registre;
    Abonne Me;
    IVelo iVelo;
    Station station;

    private Abonne a;
    private Station s;
    private IRegistre jReg;

    private Set<IVelo> fillSet(int size, boolean ok) {
        Set<IVelo> setVelos = new HashSet<>();
        for (int i = 0; i < size; i++) {
            IVelo v = Mockito.mock(IVelo.class);
            if (!ok) {
                v.decrocher();
                v.parcourir(600);
                v.arrimer();
            }

            setVelos.add(v);
        }

        return setVelos;
    }

    private void addBikes(Station st, int nb) {
        Set<IVelo> setVelos = fillSet(nb, true);
        int i = 1;
        for (IVelo v : setVelos) {
            st.arrimerVelo(v, i);
            ++i;
        }
    }

    @Before
    public void setUp() throws Exception {
        this.registre = new JRegistre();
        this.Me = Mockito.mock(Abonne.class);
        assertFalse(this.Me.estBloque());
        this.iVelo = Mockito.mock(IVelo.class);
        this.station = new Station("Centre du monde",0,0,CAPACITE);
        this.station.setRegistre(registre);

        for(int i = 1 ; i <= CAPACITE ; ++i){
            if( i % 2 == 0){
                assertEquals(-4,station.arrimerVelo(Mockito.mock(IVelo.class),i));
                assertNotNull(station.veloALaBorne(i));
                continue;
            }
            assertNull(station.veloALaBorne(i));
        }

        this.a = Mockito.mock(Abonne.class);

        Station stationTMP = new Station("Station Matz Ruby", 6, 7, 42);
        this.jReg = new JRegistre();
        stationTMP.setRegistre(jReg);
        this.s = Mockito.spy(stationTMP);
    }

    @Test
    public void emprunteur_default(){

        registre.emprunter(Me,iVelo,10);
        assertEquals(Me,registre.emprunteur(iVelo));
        registre.retourner(iVelo,20);

    }

    @Test
    public void emprunteur_nonEmprunte(){
        assertNull(registre.emprunteur(iVelo));
    }

    @Test
    public void emprunteur_retourne(){
        registre.emprunter(Me,iVelo,10);
        registre.retourner(iVelo,20);
        assertNull(registre.emprunteur(iVelo));
    }

    @Test
    public void emprunter_plusieursEmprunts(){
        IVelo trottinette = Mockito.mock(IVelo.class);

        registre.emprunter(Me,trottinette,10);
        registre.emprunter(Me,iVelo,15);

        assertEquals(Me,registre.emprunteur(iVelo));
        assertEquals(Me,registre.emprunteur(trottinette));

        registre.retourner(iVelo,20);

        assertNull(registre.emprunteur(iVelo));
        assertEquals(Me,registre.emprunteur(trottinette));

        registre.retourner(trottinette,25);
        assertNull(registre.emprunteur(iVelo));
        assertNull(registre.emprunteur(trottinette));
    }

    @Test
    public void emprunteur_veloNull(){
        assertNull(registre.emprunteur(null));
    }

    @Test
    public void retourner_abime(){
        IVelo velo = station.emprunterVelo(Me,2);
        assertNotNull(velo);
        assertFalse(velo.estAbime());

        velo.abimer();

        assertEquals(0,station.arrimerVelo(velo,7));

        assertTrue(velo.estAbime());
        assertTrue(Me.estBloque());
    }

    @Test
    public void retourner_nonAbime(){
        IVelo velo = station.emprunterVelo(Me,2);
        assertNotNull(velo);
        assertFalse(velo.estAbime());

        assertEquals(0,station.arrimerVelo(velo,7));

        assertFalse(velo.estAbime());
        assertFalse(Me.estBloque());
    }

    @Test
    public void emprunter_abime(){
        station.veloALaBorne(4).abimer();

        assertFalse(Me.estBloque());
        assertNull(station.emprunterVelo(Me,4));
        assertFalse(Me.estBloque());
    }

    @Test
    public void testTarif_Simple() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;

        addBikes(this.s, this.s.capacite());

        IVelo v;
        v = this.s.emprunterVelo(this.a, 1);
        Assert.assertNotNull(v);
        newTime += (60 * 60 * 1000 + 10); // + 1h
        Mockito.when(this.s.maintenant()).thenReturn(newTime);
        Assert.assertEquals(0, this.s.arrimerVelo(v, 1));

        Assert.assertEquals(1 * v.tarif(), this.jReg.facturation(this.a, timeNow, newTime), 0.001);
    }

    @Test
    public void testTarif_PlusieursVelos() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;
        long oneHour = 60 * 60 * 1000 + 10;

        addBikes(this.s, this.s.capacite());

        int nbVelos = 5;
        IVelo v = null;
        for (int i = 0; i < nbVelos; i++) {
            newTime += oneHour; // + 1h
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            v = this.s.emprunterVelo(this.a, 1);
            Assert.assertNotNull(v);
            newTime += oneHour; // + 1h
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.s.arrimerVelo(v, 1));
        }

        Assert.assertEquals(nbVelos * v.tarif(), this.jReg.facturation(this.a, timeNow, newTime), 0.001);
    }

    @Test
    public void testTarif_Niveaux() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;
        long oneHour = 60 * 60 * 1000 + 10;

        addBikes(this.s, this.s.capacite());

        IVelo v = null;
        for (int i = 0; i < 12; i++) {
            newTime += oneHour; // + 1h
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            v = this.s.emprunterVelo(this.a, 1);
            Assert.assertNotNull(v);
            newTime += oneHour; // + 1h
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.s.arrimerVelo(v, 1));
        }

        double tarif = 10 * v.tarif() + (2 * v.tarif() * 0.9);
        Assert.assertEquals(tarif, this.jReg.facturation(this.a, timeNow, newTime), 0.001);

        for (int i = 0; i < 10; i++) {
            newTime += oneHour; // + 1h
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            v = this.s.emprunterVelo(this.a, 1);
            Assert.assertNotNull(v);
            newTime += oneHour; // + 1h
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.s.arrimerVelo(v, 1));
        }

        tarif = 10 * v.tarif() + (10 * v.tarif() * 0.9) + (2 * v.tarif() * 0.8);
        Assert.assertEquals(tarif, this.jReg.facturation(this.a, timeNow, newTime), 0.001);
    }

    @Test
    public void testTarif_PlusDe50() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;
        long oneHour = 60 * 60 * 1000 + 10;

        addBikes(this.s, this.s.capacite());

        int nbVelos = 65;
        IVelo v = null;
        for (int i = 0; i < nbVelos; i++) {
            newTime += oneHour; // + 1h
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            v = this.s.emprunterVelo(this.a, 1);
            Assert.assertNotNull(v);
            newTime += oneHour; // + 1h
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.s.arrimerVelo(v, 1));
        }

        double tarif = 10 * v.tarif(); // 1 à 10
        tarif += 10 * v.tarif() * 0.9; // 11 à 20
        tarif += 10 * v.tarif() * 0.8; // 21 à 30
        tarif += 10 * v.tarif() * 0.7; // 31 à 40
        tarif += 10 * v.tarif() * 0.6; // 41 à 50
        tarif += 15 * v.tarif() * 0.5; // 51 à 65
        Assert.assertEquals(tarif, this.jReg.facturation(this.a, timeNow, newTime), 0.001);
    }

    @Test
    public void testTarif_Moins5Min() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;

        addBikes(this.s, this.s.capacite());

        IVelo v;
        v = this.s.emprunterVelo(this.a, 1);
        Assert.assertNotNull(v);
        newTime += (3 * 60 * 1000 + 10); // + 3min
        Mockito.when(this.s.maintenant()).thenReturn(newTime);
        Assert.assertEquals(0, this.s.arrimerVelo(v, 1));

        Assert.assertEquals(v.tarif() / 20, this.jReg.facturation(this.a, timeNow, newTime), 0.001);
    }

    @Test
    public void testTarif_Moins5Min_Niveaux() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;
        long threeMin = 3 * 60 * 1000 + 10;

        addBikes(this.s, this.s.capacite());

        int nbVelos = 20;
        IVelo v = null;
        for (int i = 0; i < nbVelos; i++) {
            newTime += threeMin; // + 3min
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            v = this.s.emprunterVelo(this.a, 1);
            Assert.assertNotNull(v);
            newTime += threeMin; // + 3min
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.s.arrimerVelo(v, 1));
        }

        Assert.assertEquals(nbVelos * (v.tarif() / 20), this.jReg.facturation(this.a, timeNow, newTime), 0.001);
    }

    @Test
    public void testTarif_Mix_Niveaux() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;
        long threeMin = 3 * 60 * 1000 + 10, oneHour = 60 * 60 * 1000 + 10;

        addBikes(this.s, this.s.capacite());

        IVelo v = null;
        for (int i = 0; i < 15; i++) {
            newTime += threeMin; // + 3min
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            v = this.s.emprunterVelo(this.a, 1);
            Assert.assertNotNull(v);
            newTime += threeMin; // + 3min
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.s.arrimerVelo(v, 1));
        }

        for (int i = 0; i < 12; i++) {
            newTime += oneHour; // + 1h
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            v = this.s.emprunterVelo(this.a, 1);
            Assert.assertNotNull(v);
            newTime += oneHour; // + 1h
            Mockito.when(this.s.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.s.arrimerVelo(v, 1));
        }

        double tarif = 15 * (v.tarif() / 20);
        tarif += 10 * v.tarif();
        tarif += 2 * v.tarif() * 0.9;
        Assert.assertEquals(tarif, this.jReg.facturation(this.a, timeNow, newTime), 0.1);
    }
}