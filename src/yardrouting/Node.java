package yardrouting;

import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import static ilog.concert.IloNumVarType.Float;
import ilog.cplex.IloCplex;
import java.util.*;

public class Node {

    private final int NodeID;
    private int Type; //0 no conflict, 1 with conflict 
    private int FirstNeighbor; //the neighbor that does not form an acute angle, for nodes with 3 neighbors
    private int FlowBalanceValue;
    private double Length;
    private final double XCoordinate;
    private final double YCoordinate;
    private final ArrayList<Arc> FSArcs;
    private final ArrayList<Arc> RSArcs;
    private final ArrayList<Node> Neighbors;
    private IloNumVar TB1;
    private IloNumVar TB2;
    private IloNumVar TF1;
    private IloNumVar TF2;
    private IloNumVar T;
    private final ArrayList<IloNumVar> X;
    private IloNumVar PreY;
    private IloNumVar PreX;
    private IloNumVar Space;

    public Node(int NodeID, double XCoordinate, double YCoordinate) {
        this.NodeID = NodeID;
        this.Type = 0; // 1:okay   0:not okay
        this.Length = 0;
        this.FirstNeighbor = 0;
        this.XCoordinate = XCoordinate;
        this.YCoordinate = YCoordinate;
        this.FlowBalanceValue = 0;
        FSArcs = new ArrayList<>();
        RSArcs = new ArrayList<>();
        Neighbors = new ArrayList<>();
        X = new ArrayList<>();
    }

    public int getNodeID() {
        return NodeID;
    }

    public double getLength() {
        return Length;
    }

    public double getXCoordinate() {
        return XCoordinate;
    }

    public double getYCoordinate() {
        return YCoordinate;
    }

    public ArrayList<Arc> getFSArcs() {
        return FSArcs;
    }

    public ArrayList<Arc> getRSArcs() {
        return RSArcs;
    }

    public int getOutdegree() {
        return FSArcs.size();
    }

    public int getIndegree() {
        return RSArcs.size();
    }

    public int getNodeDegree() {
        return Neighbors.size();
    }

    public void FSAdd(Arc a) {
        FSArcs.add(a);
    }

    public int getType() {
        return Type;
    }

    public int getFirstNeighborIndex() {
        return FirstNeighbor;
    }

    public void RSAdd(Arc a) {
        RSArcs.add(a);
    }

    public void AddNeighbor(Node a) {
        Neighbors.add(a);
    }

    public void setNodeType(int Type) {
        this.Type = Type;
    }

    public void setNodeLength(double L) {
        this.Length = L;
    }

    public void setFirstNeighborIndex(int Index) {
        FirstNeighbor = Index;
    }

    public void setNodeFlowBalanceValue(int FlowBalanceValue) {
        this.FlowBalanceValue = FlowBalanceValue;
    }

    public Node getNeighbor(int index) {
        return Neighbors.get(index);
    }

    public Node getFirstNeighbor() {
        return getNeighbor(FirstNeighbor);
    }

    public IloNumVar getPreX() {
        return PreX;
    }

    public IloNumVar getPreY() {
        return PreY;
    }

    public IloNumVar getSpace() {
        return Space;
    }

    public IloNumVar getTF1() {
        return TF1;
    }

    public IloNumVar getTF2() {
        return TF2;
    }

    public IloNumVar getTB1() {
        return TB1;
    }

    public IloNumVar getTB2() {
        return TB2;
    }

    public IloNumVar getT() {
        return T;
    }

    public ArrayList<IloNumVar> getX() {
        return X;
    }

    public ArrayList<Node> getNeighbors() {
        return Neighbors;
    }

    public IloNumVar getX(int XIndex) {
        return getX().get(XIndex);
    }

    int getFlowBalanceValue() {
        return FlowBalanceValue;
    }

    public void LPCplexInitialize(IloCplex LPModel) throws IloException {
        IloNumVar h;
        for (int j = 0; j < 12; j++) {
            h = LPModel.numVar(0, 1, Float, "NodeX_" + Integer.toString(j) + "_" + Integer.toString(NodeID));
            X.add(h);
        }
    }

    public void PreCplexInitialize(IloCplex PreModel) throws IloException {
        PreX = PreModel.numVar(0, Double.POSITIVE_INFINITY, Float, "X_" + Integer.toString(NodeID));
        PreY = PreModel.numVar(0, Double.POSITIVE_INFINITY, Float, "Y_" + Integer.toString(NodeID));
        Space = PreModel.numVar(0, Double.POSITIVE_INFINITY, Float, "Space_" + Integer.toString(NodeID));
    }

    public void MYRPInitialize(IloCplex Model) throws IloException {
        IloNumVar h;
        for (int j = 0; j < 12; j++) {
            h = Model.intVar(0, 1, "NodeX_" + Integer.toString(j) + "_" + Integer.toString(NodeID));
            X.add(h);
        }
    }

    public void ScheduleInitialize(IloCplex Model) throws IloException {
        TF1 = Model.numVar(0, Double.POSITIVE_INFINITY, Float, "T1_" + Integer.toString(NodeID));
        TF2 = Model.numVar(0, Double.POSITIVE_INFINITY, Float, "T1_" + Integer.toString(NodeID));
        TB1 = Model.numVar(0, Double.POSITIVE_INFINITY, Float, "T1_" + Integer.toString(NodeID));
        TB2 = Model.numVar(0, Double.POSITIVE_INFINITY, Float, "T1_" + Integer.toString(NodeID));
        T = Model.intVar(0, 1, "T_" + Integer.toString(NodeID));
    }
}
