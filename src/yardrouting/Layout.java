package yardrouting;

import ilog.concert.IloNumVar;
import java.util.*;

public class Layout {

    private final ArrayList<Node> allNodes;
    private final ArrayList<Arc> allArcs;
    private final ArrayList<Arc> allArcs2;
    private final ArrayList<Track> allTracks;

    public ArrayList<Node> getAllNodes() {
        return allNodes;
    }

    public ArrayList<Arc> getAllArcs() {
        return allArcs;
    }

    public ArrayList<Arc> getAllArcs2() {
        return allArcs;
    }

    public Layout() {
        allNodes = new ArrayList<>();
        allArcs = new ArrayList<>();
        allArcs2 = new ArrayList<>();
        allTracks = new ArrayList<>();
    }

    public int getEndNodeID(int ArcIndex) {
        return getEndNodeID(allArcs.get(ArcIndex));
    }

    public int getEndNodeID(Arc A) {
        return A.getEndNodeID();
    }

    public int getStartNodeID(int ArcID) {
        return getStartNodeID(allArcs.get(ArcID));
    }

    public int getStartNodeID(Arc A) {
        return A.getStartNodeID();
    }

    public double getEndNodeXCoordinate(int ArcIndex) {
        return getEndNodeXCoordinate(allArcs.get(ArcIndex));
    }

    public double getEndNodeXCoordinate(Arc a) {
        return getXCoordinate(a.getEndNode());
    }

    public double getStartNodeXCoordinate(int ArcIndex) {
        return getStartNodeXCoordinate(allArcs.get(ArcIndex));
    }

    public double getStartNodeXCoordinate(Arc a) {
        return getXCoordinate(a.getStartNode());
    }

    public double getEndNodeYCoordinate(int ArcIndex) {
        return getEndNodeYCoordinate(allArcs.get(ArcIndex));
    }

    public double getEndNodeYCoordinate(Arc a) {
        return getYCoordinate(a.getEndNode());
    }

    public double getStartNodeYCoordinate(int ArcIndex) {
        return getStartNodeYCoordinate(allArcs.get(ArcIndex));
    }

    public double getStartNodeYCoordinate(Arc a) {
        return getYCoordinate(a.getStartNode());
    }

    public int getTrackID(int TrackIndex) {
        return getTrackID(allTracks.get(TrackIndex));
    }

    public int getTrackID(Track a) {
        return a.getTrackID();
    }

    public int getNodeID(int NodeIndex) {
        return getNodeID(allNodes.get(NodeIndex));
    }

    public int getNodeID(Node a) {
        return a.getNodeID();
    }

    public double getXCoordinate(int NodeIndex) {
        return getXCoordinate(allNodes.get(NodeIndex));
    }

    public double getXCoordinate(Node a) {
        return a.getXCoordinate();
    }

    public double getYCoordinate(int NodeIndex) {
        return getYCoordinate(allNodes.get(NodeIndex));
    }

    public double getYCoordinate(Node a) {
        return a.getYCoordinate();
    }

    public void addArcToTrack(Arc a, int TrackIndex) {
        addArcToTrack(a, allTracks.get(TrackIndex));
    }

    public void addArcToTrack(Arc a, Track b) {
        b.addArc(a);
    }

    public int getNodeSize() {
        return allNodes.size();
    }

    public int getArcSize() {
        return allArcs.size();
    }

    public int getTrackSize() {
        return allTracks.size();
    }

    public void addNewNode(Node a) {
        allNodes.add(a);
    }

    public void addNewArc(Arc a) {
        allArcs.add(a);
    }

    public void addNewArc2(Arc a) {
        allArcs2.add(a);
    }

    public void addNewTrack(Track a) {
        allTracks.add(a);
    }

    public Node getNode(int NodeIndex) {
        return allNodes.get(NodeIndex);
    }

    public Arc getArc(int ArcIndex) {
        return allArcs.get(ArcIndex);
    }

    public Track getTrack(int TrackIndex) {
        return allTracks.get(TrackIndex);
    }

    public int getNodeDegree(int NodeIndex) {
        return getNodeDegree(allNodes.get(NodeIndex));
    }

    public int getNodeDegree(Node a) {
        return a.getNodeDegree();
    }

    public void setNodeType(int NodeIndex, int value) {
        setNodeType(allNodes.get(NodeIndex), value);
    }

    public void setNodeType(Node a, int value) {
        a.setNodeType(value);
    }

    public IloNumVar getX(int ArcIndex) {
        return getX(allArcs.get(ArcIndex));
    }

    public IloNumVar getX(Arc A) {
        return A.getX();
    }

}
