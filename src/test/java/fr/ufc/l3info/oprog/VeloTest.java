package fr.ufc.l3info.oprog;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class VeloTest {
    private Velo velo;

    @Before
    public void InitVelo(){
        velo = new Velo();
    }

    @Test
    public void Kilometrage(){
        assertEquals(0,velo.kilometrage(),1e-3);
    }

    @Test
    public void Tarif(){
        assertEquals(2.0,velo.tarif(),1e-3);
    }

    @Test
    public void EstDecrocher(){
        assertEquals(-1,velo.decrocher());
    }

    @Test
    public void EstArrimer(){
        assertEquals(0,velo.arrimer());
    }

    @Test
    public void EtatArrimerDecrocher(){
        assertEquals(0,velo.arrimer());
        assertEquals(-1,velo.arrimer());
        assertEquals(0,velo.decrocher());
        assertEquals(-1,velo.decrocher());
    }

    @Test
    public void EtatArrimerAbimerDecrocher(){
        assertEquals(0,velo.arrimer());
        assertFalse(velo.estAbime());
        velo.abimer();
        assertTrue(velo.estAbime());
        assertEquals(0,velo.decrocher());
        assertTrue(velo.estAbime());
    }
}
