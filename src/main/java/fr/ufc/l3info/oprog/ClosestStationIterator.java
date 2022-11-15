package fr.ufc.l3info.oprog;
import java.util.*;

public class ClosestStationIterator implements Iterator<Station>{

    private Station station;
    private Set<Station> stations;

    public ClosestStationIterator(Set<Station> stations, Station s){
        if (stations == null || s == null){
            this.stations = new HashSet<>();
            this.station = null;
            return;
        }
        this.stations = new HashSet<>(stations);
        this.station = s;
    }

    @Override
    public boolean hasNext() {
        return this.stations.size() != 0;
    }

    @Override
    public Station next() {
        Station s1 = this.station;
        this.stations.remove(s1);

        Station proche = null;
        double distance = 0;
        for (Station s2 : this.stations){
            double d = s1.distance(s2);
            if (proche == null || d < distance){
                distance = d;
                proche = s2;
            }
        }
        this.station = proche;
        return s1;
    }

    @Override
    public void remove() {
        //do nothing
    }
}
