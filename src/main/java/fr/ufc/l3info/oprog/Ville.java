package fr.ufc.l3info.oprog;

import fr.ufc.l3info.oprog.parser.ASTNode;
import fr.ufc.l3info.oprog.parser.ASTStationBuilder;
import fr.ufc.l3info.oprog.parser.StationParser;
import fr.ufc.l3info.oprog.parser.StationParserException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Ville implements Iterable<Station>{
    private Station stationPrincipal;
    private Map<String, Station> stations;
    private List<Abonne> abonnes;
    private IRegistre registre;

    public Ville(){
        this.stationPrincipal = null;
        this.stations = new HashMap<>();
        this.abonnes = new LinkedList<>();
        this.registre = new JRegistre();
    }

    void initialiser(File f) throws IOException {
        StationParser parser = StationParser.getInstance();
        this.stations.clear();

        try {
            ASTNode n = parser.parse(f);
            ASTStationBuilder builder = new ASTStationBuilder();
            n.accept(builder);

            String nomStation = n.getChild(0).getChild(0).toString();
            nomStation = nomStation.substring(1, nomStation.length()-1);

            for (Station s : builder.getStations()) {
                s.setRegistre(this.registre);
                this.stations.put(s.getNom(), s);
                if (s.getNom().equals(nomStation)) {
                    this.stationPrincipal = s;
                }
            }
        } catch (StationParserException e) {
            throw new RuntimeException(e);
        }
        System.out.println(this.stationPrincipal.getNom());
    }

    void setStationPrincipale(String st){
        if (st != null && stations.containsKey(st)){
            this.stationPrincipal = stations.get(st);
        }
    }

    Station getStation(String nom){
        return this.stations.get(nom);
    }

    Station getStationPlusProche(double lat, double lon){
        Station station = new Station("station", lat, lon, 0);

        Station s1 = this.stationPrincipal;
        double distance = station.distance(s1);

        for (Station s2 : this.stations.values()){
            double d = station.distance(s2);
            if (d < distance){
                distance = d;
                s1 = s2;
            }
        }
        return s1;
    }

    Abonne creerAbonne(String nom, String RIB){
        Abonne a;
        try {
            a = new Abonne(nom, RIB);
            this.abonnes.add(a);
        } catch (IncorrectNameException e) {
            return null;
        }
        return a;
    }

    @Override
    public Iterator<Station> iterator() {
        return new ClosestStationIterator((Set<Station>) this.stations.values(), this.stationPrincipal);
    }

    Map<Abonne, Double> facturation(int mois, int annee){
        long start = 0;
        long end = 0;
        if (mois < 1 || mois > 12){
            Date date = new Date(annee - 1900, mois - 1, 1);
            start = date.getTime();
            if (mois == 12) {
                mois = 0;
                annee++;
            } else {
                mois++;
            }
            date = new Date(annee - 1900, mois - 1, 1);
            end = date.getTime();
        }
        Map<Abonne, Double> facts = new HashMap<>();
        for (Abonne a : this.abonnes){
            facts.put(a, this.registre.facturation(a, start, end));
        }
        return facts;
    }
}
