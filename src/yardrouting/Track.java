package yardrouting;

import java.util.*;

public class Track {

    private final ArrayList<Arc> Arcs;
    private final int TrackID;

    public Track(int TrackID) {
        Arcs = new ArrayList<>();
        this.TrackID = TrackID;
    }

    public int getTrackID() {
        return TrackID;
    }

    public void addArc(Arc a) {
        Arcs.add(a);
    }
}
