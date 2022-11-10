package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Test;
import sun.reflect.generics.tree.Tree;

import java.util.Set;
import java.util.TreeSet;

public class StationIntegrationTestIvann {
    private Station s;

    @Test
    public void testConstructeur(){
        s = new Station("Route Arc-en-ciel", 47.6, 6.5, 10);
        Assert.assertEquals(s.getNom(), "Route Arc-en-ciel");
        Assert.assertEquals((s.capacite()), 10);
    }

    @Test
    public void testEquilibrage(){
        s = new Station("Route Arc-en-ciel", 47.6, 6.5, 10);
        s.setRegistre(new JRegistre());
        Set<IVelo> velos = new TreeSet<>();
        Assert.assertEquals(10, s.nbBornesLibres());
        s.equilibrer(velos);
        Assert.assertEquals(5, s.nbBornesLibres());


    }
}
