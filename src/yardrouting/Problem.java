package yardrouting;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import static ilog.concert.IloNumVarType.Float;
import ilog.cplex.IloCplex;
import static ilog.cplex.IloCplex.Status.Infeasible;
import static ilog.cplex.IloCplex.Status.InfeasibleOrUnbounded;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Problem {

    private final Layout L;
    private final double Epsilon;
    private final IloCplex Model;
    private final IloCplex LPModel;
    private final IloCplex IPModel;
    private final IloCplex PreModel;
    private double Length;
    private final int DestinationID;
    private final int OriginID;

    public Problem(Layout L) throws IloException {
        this.Model = new IloCplex();
        this.LPModel = new IloCplex();
        this.IPModel = new IloCplex();
        this.PreModel = new IloCplex();
        this.L = L;
        Epsilon = 0.0001;
        Length = 100;
        DestinationID = 3;  //Destination  256-6-3
        OriginID = 4;       //Origin  227-1-4
    }

    public void setLength(double L) {
        this.Length = L;
    }

    public Layout getLayout() {
        return L;
    }

    public IloCplex getModel() {
        return Model;
    }

    public IloCplex getPreModel() {
        return PreModel;
    }

    public IloCplex getLPModel() {
        return LPModel;
    }

    public IloCplex getIPModel() {
        return IPModel;
    }

    public Arc getOppositeArcInItsLay(Arc A) {
        Arc C = L.getArc(0);
        for (Arc B : A.getEndNode().getFSArcs()) {
            if (B.getEndNodeID() == A.getStartNodeID()) {
                C = B;
                break;
            }
        }
        return C;
    }

    public void AnglePreprocessing() {
        Angle b;
        int negin = 0;
        for (Node N : L.getAllNodes()) {
            if (N.getNodeID() == DestinationID) {
                //N.setNodeFlowBalanceValue(-1);
            } else if (N.getNodeID() == OriginID) {
                //N.setNodeFlowBalanceValue(1);
            } else {
                //N.setNodeFlowBalanceValue(0);
            }
            if (N.getNodeDegree() > 3) {
                System.out.println("Node with more than 3 neighbors!!! " + N.getXCoordinate());
            }
            switch (N.getNodeDegree()) {
                case 1:
                    // System.out.println("Node with one neighbor!!!  " + N.getNodeID() + "  " + N.getXCoordinate() + " " + N.getYCoordinate());
                    break;
                case 2:
                    b = new Angle(N);
                    if (b.getAngle(N.getNeighbor(0), N.getNeighbor(1)) < 90) {
                        //System.out.println("Node with just two acute edges!  " + N.getNodeID() + "  " + N.getXCoordinate() + " " + N.getYCoordinate());
                    }
                    break;
                case 3:
                    N.setNodeType(1);
                    // negin++;
                    //System.out.println(negin);
                    for (int i = 0; i < N.getNodeDegree(); i++) {
                        for (int k = i + 1; k < N.getNodeDegree(); k++) {
                            b = new Angle(N);
                            if (b.getAngle(N.getNeighbor(i), N.getNeighbor(k)) < 90) {
                                if (i == 1) {
                                    N.setFirstNeighborIndex(0);
                                } else if (k == 2) {
                                    N.setFirstNeighborIndex(1);
                                } else {
                                    N.setFirstNeighborIndex(2);
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void LPModel(Node SourceNode, Node DestinationNode) throws IloException {
        SourceNode.setNodeFlowBalanceValue(1);
        DestinationNode.setNodeFlowBalanceValue(-1);
        ArrayList<IloNumVar> DV = new ArrayList<>();
        IloNumVar SDV;
        for (int i = 0; i < 8; i++) {
            if (i < 4) {
                SDV = LPModel.numVar(0, 1, Float, "DV_O");
            } else {
                SDV = LPModel.numVar(0, 1, Float, "DV_D");
            }
            DV.add(SDV);
        }
        IloLinearNumExpr Expr1 = LPModel.linearNumExpr();
        IloLinearNumExpr Expr2 = LPModel.linearNumExpr();
        IloLinearNumExpr Expr3 = LPModel.linearNumExpr();
        IloLinearNumExpr Expr4 = LPModel.linearNumExpr();
        IloLinearNumExpr Ex;
        IloLinearNumExpr DEx = LPModel.linearNumExpr();
        IloLinearNumExpr obj2 = LPModel.linearNumExpr();
        ArrayList<IloLinearNumExpr> Expr = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Ex = LPModel.linearNumExpr();
            Expr.add(Ex);
        }
        for (Arc A : L.getAllArcs()) {
            A.LPCplexInitialize(LPModel);
        }
        for (Node N : L.getAllNodes()) {
            N.LPCplexInitialize(LPModel);
        }
        for (Arc A : L.getAllArcs()) {
            Expr1.addTerm(A.getLength(), A.getLX());
            Expr1.addTerm(A.getLength(), A.getSX());
        }
        for (Node N : L.getAllNodes()) {
            if (N.getNodeDegree() == 3) {
                Expr1.addTerm(Length, N.getX(0));
                Expr1.addTerm(Length, N.getX(1));
                Expr1.addTerm(Length, N.getX(2));
                Expr1.addTerm(Length, N.getX(3));
                obj2.addTerm(1.0, N.getX(0));
                obj2.addTerm(1.0, N.getX(1));
                obj2.addTerm(1.0, N.getX(2));
                obj2.addTerm(1.0, N.getX(3));
            }
        }
        LPModel.addMinimize(Expr1);
        //LPModel.addMinimize(obj2);
        for (Node N : L.getAllNodes()) {
            Expr1.clear();
            Expr2.clear();
            Expr3.clear();
            Expr4.clear();
            for (int i = 0; i < 8; i++) {
                Expr.get(i).clear();
            }
            switch (N.getNodeDegree()) {
                case 1:
                    for (Arc A : N.getFSArcs()) {
                        Expr1.addTerm(1.0, A.getLX());
                        Expr4.addTerm(1.0, A.getSX());
                    }
                    for (Arc A : N.getRSArcs()) {
                        Expr3.addTerm(-1.0, A.getSX());
                        Expr2.addTerm(-1.0, A.getLX());
                    }
                    LPModel.addEq(Expr1, N.getFlowBalanceValue());
                    LPModel.addEq(Expr2, N.getFlowBalanceValue());
                    LPModel.addEq(Expr3, N.getFlowBalanceValue());
                    LPModel.addEq(Expr4, N.getFlowBalanceValue());
                    break;
                case 2:
                    for (Arc A : N.getFSArcs()) {
                        if (A.getEndNodeID() == N.getNeighbor(0).getNodeID()) {
                            Expr1.addTerm(1.0, A.getLX());
                            Expr4.addTerm(1.0, A.getSX());
                        } else {
                            Expr2.addTerm(1.0, A.getLX());
                            Expr3.addTerm(1.0, A.getSX());
                        }
                    }
                    for (Arc A : N.getRSArcs()) {
                        if (A.getStartNodeID() == N.getNeighbor(1).getNodeID()) {
                            Expr1.addTerm(-1.0, A.getLX());
                            Expr4.addTerm(-1.0, A.getSX());
                        } else {
                            Expr2.addTerm(-1.0, A.getLX());
                            Expr3.addTerm(-1.0, A.getSX());
                        }
                    }
                    switch (N.getFlowBalanceValue()) {
                        case 1: // source node
                            DEx.clear();
                            Expr1.addTerm(-1.0, DV.get(0));
                            Expr2.addTerm(-1.0, DV.get(1));
                            Expr3.addTerm(-1.0, DV.get(2));
                            Expr4.addTerm(-1.0, DV.get(3));
                            for (int i = 0; i < 4; i++) {
                                DEx.addTerm(1.0, DV.get(i));
                            }
                            LPModel.addEq(DEx, 1.0);
                            break;
                        case -1: // desination node
                            DEx.clear();
                            Expr1.addTerm(1.0, DV.get(4));
                            Expr2.addTerm(1.0, DV.get(5));
                            Expr3.addTerm(1.0, DV.get(6));
                            Expr4.addTerm(1.0, DV.get(7));
                            for (int i = 0; i < 4; i++) {
                                DEx.addTerm(-1.0, DV.get(i + 4));
                            }
                            LPModel.addEq(DEx, -1.0);
                            break;
                        default:
                            break;
                    }
                    N.setNodeFlowBalanceValue(0);
                    LPModel.addEq(Expr1, N.getFlowBalanceValue());
                    LPModel.addEq(Expr2, N.getFlowBalanceValue());
                    LPModel.addEq(Expr3, N.getFlowBalanceValue());
                    LPModel.addEq(Expr4, N.getFlowBalanceValue());
                    break;
                case 3:
                    int id = 0;
                    boolean first = true;
                    for (Arc A : N.getFSArcs()) {
                        if (A.getEndNodeID() == N.getFirstNeighbor().getNodeID()) {
                            Expr2.addTerm(1.0, A.getLX());
                            Expr3.addTerm(1.0, A.getSX());
                        } else {
                            if (first) {
                                id = A.getEndNodeID();
                                Expr.get(0).addTerm(1.0, A.getLX());
                                if (N.getLength() > Length) {
                                    Expr.get(0).addTerm(-1.0, N.getX(0));
                                }
                                Expr.get(0).addTerm(-1.0, N.getX(4));
                                Expr.get(7).addTerm(1.0, A.getSX());
                                if (N.getLength() > Length) {
                                    Expr.get(7).addTerm(-1.0, N.getX(3));
                                }
                                Expr.get(7).addTerm(-1.0, N.getX(11));
                                first = false;
                            } else {
                                Expr.get(2).addTerm(1.0, A.getLX());
                                if (N.getLength() > Length) {
                                    Expr.get(2).addTerm(-1.0, N.getX(2));
                                }
                                Expr.get(2).addTerm(-1.0, N.getX(5));
                                Expr.get(5).addTerm(1.0, A.getSX());
                                if (N.getLength() > Length) {
                                    Expr.get(5).addTerm(-1.0, N.getX(1));
                                }
                                Expr.get(5).addTerm(-1.0, N.getX(10));
                            }
                        }
                    }
                    for (Arc A : N.getRSArcs()) {
                        if (A.getStartNodeID() == N.getFirstNeighbor().getNodeID()) {
                            Expr1.addTerm(-1.0, A.getLX());
                            Expr4.addTerm(-1.0, A.getSX());
                        } else {
                            if (A.getStartNodeID() == id) {
                                Expr.get(1).addTerm(-1.0, A.getLX());
                                if (N.getLength() > Length) {
                                    Expr.get(1).addTerm(1.0, N.getX(1));
                                }
                                Expr.get(1).addTerm(1.0, N.getX(6));
                                Expr.get(6).addTerm(-1.0, A.getSX());
                                if (N.getLength() > Length) {
                                    Expr.get(6).addTerm(1.0, N.getX(2));
                                }
                                Expr.get(6).addTerm(1.0, N.getX(9));
                            } else {
                                Expr.get(3).addTerm(-1.0, A.getLX());
                                if (N.getLength() > Length) {
                                    Expr.get(3).addTerm(1.0, N.getX(3));
                                }
                                Expr.get(3).addTerm(1.0, N.getX(7));
                                Expr.get(4).addTerm(-1.0, A.getSX());
                                if (N.getLength() > Length) {
                                    Expr.get(4).addTerm(1.0, N.getX(0));
                                }
                                Expr.get(4).addTerm(1.0, N.getX(8));
                            }

                        }

                    }
                    Expr1.addTerm(1.0, N.getX(4));
                    Expr1.addTerm(1.0, N.getX(5));
                    Expr2.addTerm(-1.0, N.getX(6));
                    Expr2.addTerm(-1.0, N.getX(7));
                    Expr3.addTerm(-1.0, N.getX(8));
                    Expr3.addTerm(-1.0, N.getX(9));
                    Expr4.addTerm(1.0, N.getX(10));
                    Expr4.addTerm(1.0, N.getX(11));
                    LPModel.addEq(Expr1, N.getFlowBalanceValue());
                    LPModel.addEq(Expr2, N.getFlowBalanceValue());
                    LPModel.addEq(Expr3, N.getFlowBalanceValue());
                    LPModel.addEq(Expr4, N.getFlowBalanceValue());
                    for (int i = 0; i < 8; i++) {
                        LPModel.addEq(Expr.get(i), N.getFlowBalanceValue());
                    }
                    break;
                default:
                    break;
            }
        }
        LPModel.exportModel("ExpansionModel.lp");
    }

    public void SolveEveryPairs() throws IloException, FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("results.csv"));
        StringBuilder sb = new StringBuilder();
        int switchnumber;
        int counter = 0;
        int problemkhuba = 0;
        for (int i = 0; i < L.getNodeSize(); i++) {
            for (int j = i + 1; j < L.getNodeSize(); j++) {
                if (L.getNodeDegree(i) == 2 && L.getNodeDegree(j) == 2 && i >= 826 && j >= 1652) { //&& i >= 826 && j >= 1652
                    //if (L.getNode(i).getXCoordinate() < 10000 && L.getNode(i).getXCoordinate() > 8000 && L.getNode(i).getYCoordinate() < 15000 && L.getNode(i).getYCoordinate() > 10000 && L.getNode(j).getXCoordinate() < 14000 && L.getNode(j).getXCoordinate() > 10000 && L.getNode(j).getYCoordinate() < 20000 && L.getNode(j).getYCoordinate() > 10000) {
                    if (Math.sqrt((L.getNode(i).getXCoordinate() - L.getNode(j).getXCoordinate()) * (L.getNode(i).getXCoordinate() - L.getNode(j).getXCoordinate()) + (L.getNode(i).getYCoordinate() - L.getNode(j).getYCoordinate()) * (L.getNode(i).getYCoordinate() - L.getNode(j).getYCoordinate())) > 3000) {
                        LPModel.clearModel();
                        counter++;
                        System.out.println("**********new pair**********");
                        System.out.println("From: " + L.getNodeID(i) + " To: " + L.getNodeID(j));
                        System.out.println("Counter = " + counter);
                        switchnumber = 0;
                        LPModel(L.getNode(i), L.getNode(j));
                        LPModel.setOut(null);
                        long startTime = System.nanoTime();
                        LPModel.solve();
                        long endTime = System.nanoTime();
                        long duration = (endTime - startTime);
                        sb.append(counter);
                        sb.append(',');
                        sb.append(L.getNodeID(i));
                        sb.append(',');
                        sb.append(L.getNodeID(j));
                        sb.append(',');
                        sb.append((1.0 * duration) / 1000000000.0);
                        sb.append(',');
                        if (LPModel.getStatus() == Infeasible || LPModel.getStatus() == InfeasibleOrUnbounded) {
                            System.out.println("This pair is not feasible");
                            sb.append(-1);
                            sb.append(',');
                            sb.append(-1);
                            sb.append('\n');
                        } else {
                            System.out.println("This pair is feasible");
                            System.out.println("OF = " + LPModel.getObjValue());
                            System.out.println("Solution Time: " + (1.0 * duration) / 1000000000.0);
                            for (Node N : L.getAllNodes()) {
                                if (N.getNodeDegree() == 3) {
                                    if (LPModel.getValue(N.getX(0)) == 1 || LPModel.getValue(N.getX(1)) == 1 || LPModel.getValue(N.getX(2)) == 1 || LPModel.getValue(N.getX(3)) == 1) {
                                        switchnumber++;
                                    }
                                }
                            }
                            if (switchnumber > 0) {
                                problemkhuba++;
                            }
                            double obj2 = 0; // pure length for the case with minimizing the switch node
                            for (Arc A : L.getAllArcs()) {
                                obj2 = obj2 + LPModel.getValue(A.getLX()) * A.getLength();
                                obj2 = obj2 + LPModel.getValue(A.getSX()) * A.getLength();
                            }
                            System.out.println("Number of switch nodes used in the solution = " + switchnumber);
                            sb.append(LPModel.getObjValue());
                            //sb.append(obj2);
                            sb.append(',');
                            sb.append(switchnumber);
                            sb.append('\n');
                        }
                        L.getNode(i).setNodeFlowBalanceValue(0);
                        L.getNode(j).setNodeFlowBalanceValue(0);
                    }
                    //}
                }
                j = j + 1;
                i = i + 1;
                if (counter == 200) {
                    System.out.println("problemkhuba:" + problemkhuba);
                    System.out.println("i=" + i);
                    System.out.println("j=" + j);
                    i = 100000;
                    j = 100000;
                }
            }
        }
        pw.write(sb.toString());
        pw.close();
    }

    public void PreLPFormulation(Node SN) throws IloException {
        IloLinearNumExpr Expr1 = PreModel.linearNumExpr();
        IloLinearNumExpr Expr2 = PreModel.linearNumExpr();
        ArrayList<Arc> VisitedArc = new ArrayList<>();
        for (Node N : L.getAllNodes()) {
            N.PreCplexInitialize(PreModel);
        }
        Node N2;
        Node N1;
        boolean found;
        int counter = 0;
        double MergedLength = 0;
        for (Arc A : SN.getFSArcs()) {
            if (A.getEndNodeID() == SN.getFirstNeighbor().getNodeID()) {
                Expr2.addTerm(1.0, SN.getSpace());
                MergedLength = MergedLength + A.getLength();
                N2 = SN.getFirstNeighbor();
                N1 = SN;
                while (N2.getNodeDegree() == 2) {
                    for (Arc B : N2.getFSArcs()) {
                        if (B.getEndNodeID() != N1.getNodeID()) {
                            N1 = N2;
                            N2 = B.getEndNode();
                            MergedLength = MergedLength + B.getLength();
                            break;
                        }
                    }
                }
                if (N2.getNodeDegree() == 1 || N2.getNodeID() == SN.getNodeID()) {
                    PreModel.addEq(Expr2, MergedLength);
                } else {
                    if (N2.getFirstNeighbor() == N1) {
                        Expr2.addTerm(-1.0, N2.getPreX());
                        Expr1.addTerm(1.0, N2.getPreX());
                        PreModel.addEq(Expr2, MergedLength);
                        for (Arc B : N2.getFSArcs()) {
                            if (B.getEndNodeID() != N1.getNodeID()) {
                                VisitedArc.add(B);
                            }
                        }
                    } else {
                        Expr2.addTerm(-1.0, N2.getPreY());
                        PreModel.addEq(Expr2, MergedLength);
                        for (Arc B : N2.getFSArcs()) {
                            if (B.getEndNodeID() == N2.getFirstNeighbor().getNodeID()) {
                                VisitedArc.add(B);
                            }
                        }
                    }
                }
            }
        }
        while (counter < VisitedArc.size()) {
            found = false;
            Expr2.clear();
            N2 = VisitedArc.get(counter).getEndNode();
            N1 = VisitedArc.get(counter).getStartNode();
            int direction;
            if (N1.getFirstNeighbor().getNodeID() == N2.getNodeID()) {
                direction = 1;
                Expr2.addTerm(1.0, N1.getPreY());
            } else {
                direction = 0;
                Expr2.addTerm(1.0, N1.getPreX());
            }
            MergedLength = VisitedArc.get(counter).getLength();
            while (N2.getNodeDegree() == 2) {
                for (Arc B : N2.getFSArcs()) {
                    if (B.getEndNodeID() != N1.getNodeID()) {
                        N1 = N2;
                        N2 = B.getEndNode();
                        MergedLength = MergedLength + B.getLength();
                        break;
                    }
                }
            }

            if (N2.getNodeDegree() == 1 || N2.getNodeID() == SN.getNodeID()) {
                if (direction == 1) {
                    PreModel.addEq(Expr2, MergedLength);
                } else {
                    PreModel.addGe(Expr2, MergedLength);
                }

            } else {
                if (N2.getFirstNeighbor() == N1) {
                    Expr2.addTerm(-1.0, N2.getPreX());
                    Expr1.addTerm(1.0, N2.getPreX());
                    if (direction == 1) {
                        PreModel.addEq(Expr2, MergedLength);
                    } else {
                        PreModel.addGe(Expr2, MergedLength);
                    }
                    for (Arc B : N2.getFSArcs()) {
                        if (B.getEndNodeID() != N1.getNodeID()) {
                            for (int i = 0; i < VisitedArc.size(); i++) {
                                if (B == VisitedArc.get(i)) {
                                    found = true;
                                }
                            }
                            if (found == false) {
                                VisitedArc.add(B);
                            }
                        }
                    }
                } else {
                    Expr2.addTerm(-1.0, N2.getPreY());
                    if (direction == 1) {
                        PreModel.addEq(Expr2, MergedLength);
                    } else {
                        PreModel.addGe(Expr2, MergedLength);
                    }
                    for (Arc B : N2.getFSArcs()) {
                        if (B.getEndNodeID() == N2.getFirstNeighbor().getNodeID()) {
                            for (int i = 0; i < VisitedArc.size(); i++) {
                                if (B == VisitedArc.get(i)) {
                                    found = true;
                                }
                            }
                            if (found == false) {
                                VisitedArc.add(B);
                            }
                        }
                    }
                }
            }
            counter++;
        }
        PreModel.addMinimize(Expr1);
        //PreModel.exportModel("PreLPModel.lp");
    }

    public void PreLPSolver() throws IloException {
        for (Node N : L.getAllNodes()) {
            if (N.getNodeDegree() == 3) {
                PreModel.clearModel();
                PreLPFormulation(N);
                //PreModel.exportModel("PreLPModel.lp");
                PreModel.setOut(null);
                PreModel.solve();
                //System.out.println("PreModel Status: " + PreModel.getStatus());
                N.setNodeLength(PreModel.getValue(N.getSpace()));
            }
        }
    }

    public void ShortestCycle() throws IloException {
        Node N2;
        Node N1;
        int counter = 0;
        double MergedLength;
        for (Node SN : L.getAllNodes()) {
            if (SN.getNodeDegree() == 3) {
                counter++;
                System.out.println("Switch number:  " + counter);
                for (Arc A : SN.getFSArcs()) {
                    MergedLength = 0;
                    if (A.getEndNodeID() == SN.getFirstNeighbor().getNodeID()) {
                        MergedLength = MergedLength + A.getLength();
                        N2 = SN.getFirstNeighbor();
                        N1 = SN;
                        while (N2.getNodeDegree() == 2) {
                            for (Arc B : N2.getFSArcs()) {
                                if (B.getEndNodeID() != N1.getNodeID()) {
                                    N1 = N2;
                                    N2 = B.getEndNode();
                                    MergedLength = MergedLength + B.getLength();
                                    break;
                                }
                            }
                        }
                        if (N2.getNodeDegree() == 1 || N2.getNodeID() == SN.getNodeID()) {
                            if (N2.getNodeID() == SN.getNodeID()) {
                                System.out.println(MergedLength);
                            }
                        } else {
                            if (N2.getFirstNeighbor() == N1) {
                                for (Arc B : N2.getFSArcs()) {
                                    if (B.getEndNodeID() != N1.getNodeID()) {
                                        f(SN, MergedLength, B);
                                    }
                                }
                            } else {
                                for (Arc B : N2.getFSArcs()) {
                                    if (B.getEndNodeID() == N2.getFirstNeighbor().getNodeID()) {
                                        f(SN, MergedLength, B);
                                    }
                                }
                            }
                        }
                    } else {
                        MergedLength = MergedLength + A.getLength();
                        N2 = A.getEndNode();
                        N1 = SN;
                        while (N2.getNodeDegree() == 2) {
                            for (Arc B : N2.getFSArcs()) {
                                if (B.getEndNodeID() != N1.getNodeID()) {
                                    N1 = N2;
                                    N2 = B.getEndNode();
                                    MergedLength = MergedLength + B.getLength();
                                    break;
                                }
                            }
                        }
                        if (N2.getNodeDegree() == 1 || N2.getNodeID() == SN.getNodeID()) {
                            if (N2.getNodeID() == SN.getNodeID()) {
                                System.out.println(MergedLength);
                            }
                        } else {
                            if (N2.getFirstNeighbor() == N1) {
                                for (Arc B : N2.getFSArcs()) {
                                    if (B.getEndNodeID() != N1.getNodeID()) {
                                        f(SN, MergedLength, B);
                                    }
                                }
                            } else {
                                for (Arc B : N2.getFSArcs()) {
                                    if (B.getEndNodeID() == N2.getFirstNeighbor().getNodeID()) {
                                        f(SN, MergedLength, B);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    public void f(Node SN, double MergedLength, Arc C) {
        if (MergedLength > 5000) {
        } else {
            Node N2;
            Node N1;
            N2 = C.getEndNode();
            N1 = C.getStartNode();
            MergedLength = MergedLength + C.getLength();
            while (N2.getNodeDegree() == 2) {
                for (Arc B : N2.getFSArcs()) {
                    if (B.getEndNodeID() != N1.getNodeID()) {
                        N1 = N2;
                        N2 = B.getEndNode();
                        MergedLength = MergedLength + B.getLength();
                        break;
                    }
                }
            }
            if (N2.getNodeDegree() == 1 || N2.getNodeID() == SN.getNodeID()) {
                if (N2.getNodeID() == SN.getNodeID()) {
                    System.out.println(MergedLength);
                }
            } else {
                if (N2.getFirstNeighbor() == N1) {
                    for (Arc B : N2.getFSArcs()) {
                        if (B.getEndNodeID() != N1.getNodeID()) {
                            f(SN, MergedLength, B);
                        }
                    }
                } else {
                    for (Arc B : N2.getFSArcs()) {
                        if (B.getEndNodeID() == N2.getFirstNeighbor().getNodeID()) {
                            f(SN, MergedLength, B);
                        }
                    }
                }
            }
        }
    }
}
