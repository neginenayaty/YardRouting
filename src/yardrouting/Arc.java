package yardrouting;

import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import static ilog.concert.IloNumVarType.Float;
import ilog.cplex.IloCplex;
import java.util.ArrayList;

public class Arc {

    private final Node Start;
    private final Node End;
    private final double Length;
    private final int SeqNumber;
    private final int EdgeID;
    private final int TrackID;
    private IloNumVar X;
    private IloNumVar LX;
    private IloNumVar SX;
    private IloNumVar Y;
    private IloNumVar Z;
    boolean alreadychecked;
    private final boolean congested;

    public Arc(int EdgeID, int TrackID, int SeqNumber, Node Start, Node End) {
        this.EdgeID = EdgeID;
        alreadychecked = false;
        this.Start = Start;
        this.End = End;
        this.TrackID = TrackID;
        this.SeqNumber = SeqNumber;
        Length = Math.sqrt((Start.getXCoordinate() - End.getXCoordinate()) * (Start.getXCoordinate() - End.getXCoordinate()) + (Start.getYCoordinate() - End.getYCoordinate()) * (Start.getYCoordinate() - End.getYCoordinate()));
        this.Start.FSAdd(this);
        this.End.RSAdd(this);
        this.Start.AddNeighbor(End);
        alreadychecked = false;
        if (Math.random() < 0.8) {
            congested = false;
        } else {
            congested = true;
        }
    }

    public IloNumVar getX() {
        return X;
    }

    public IloNumVar getZ() {
        return Z;
    }

    public IloNumVar getLX() {
        return LX;
    }

    public IloNumVar getSX() {
        return SX;
    }

    public IloNumVar getY() {
        return Y;
    }

    double getLength() {
        return Length;
    }

    double getEdgeID() {
        return EdgeID;
    }

    double getTrackID() {
        return TrackID;
    }

    double getSeqNumber() {
        return SeqNumber;
    }

    Node getEndNode() {
        return End;
    }

    Node getStartNode() {
        return Start;
    }

    int getEndNodeID() {
        return End.getNodeID();
    }

    int getStartNodeID() {
        return Start.getNodeID();
    }

    int getEndNodeOutdegree() {
        return End.getOutdegree();
    }

    int getStartNodeOutdegree() {
        return Start.getOutdegree();
    }

    int getEndNodeIndegree() {
        return End.getIndegree();
    }

    int getStartNodeIndegree() {
        return Start.getIndegree();
    }

    double getEndNodeXCoordinate() {
        return End.getXCoordinate();
    }

    double getStartNodeXCoordinate() {
        return Start.getXCoordinate();
    }

    double getEndNodeYCoordinate() {
        return End.getYCoordinate();
    }

    double getStartNodeYCoordinate() {
        return Start.getYCoordinate();
    }

    public ArrayList<Arc> getStartNodeFSArcs() {
        return Start.getFSArcs();
    }

    public ArrayList<Arc> getEndNodeFSArcs() {
        return End.getFSArcs();
    }

    public ArrayList<Arc> getStartNodeRSArcs() {
        return Start.getRSArcs();
    }

    public ArrayList<Arc> getEndNodeRSArcs() {
        return End.getRSArcs();
    }

    public void LPCplexInitialize(IloCplex LPmodel) throws IloException {
        LX = LPmodel.numVar(0, 1, Float, "LX_" + Integer.toString(Start.getNodeID()) + "_" + Integer.toString(End.getNodeID()));
        SX = LPmodel.numVar(0, 1, Float, "SX_" + Integer.toString(Start.getNodeID()) + "_" + Integer.toString(End.getNodeID()));
    }

    public void MYRPInitialize(IloCplex Model) throws IloException {
        LX = Model.intVar(0, 1, "LX_" + Integer.toString(Start.getNodeID()) + "_" + Integer.toString(End.getNodeID()));
        SX = Model.intVar(0, 1, "SX_" + Integer.toString(Start.getNodeID()) + "_" + Integer.toString(End.getNodeID()));
        Y = Model.intVar(0, 1, "ArcY_" + Integer.toString(Start.getNodeID()) + "_" + Integer.toString(End.getNodeID()));
        Z = Model.intVar(0, 1, "ArcZ_" + Integer.toString(Start.getNodeID()) + "_" + Integer.toString(End.getNodeID()));
    }
}
