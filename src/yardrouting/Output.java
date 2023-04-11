package yardrouting;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Output {

    private final Layout p;
    private final IloCplex Model;

    public Output(Layout p, IloCplex m) {
        this.p = p;
        Model = m;
    }

    public void OutputLayout() throws UnsupportedEncodingException { //This is the latex input file
        String FileName = "Layout.txt";
        PrintWriter w;
        try {
            w = new PrintWriter(FileName, "UTF-8");
            for (Node N : p.getAllNodes()) {
                if (N.getXCoordinate() < 10000 && N.getXCoordinate() > 8000 && N.getYCoordinate() < 15000 && N.getYCoordinate() > 10000) {
                    if (N.getNodeDegree() == 3 && N.getType() == 1) {
                        w.println("\\node[draw, circle, fill=red, ,minimum size=1pt, scale=0.3] at (" + 80 * N.getXCoordinate() / 34000 + "," + 60 * N.getYCoordinate() / 34000 + ")(" + N.getNodeID() + "){};");
                    } else if (N.getNodeDegree() == 3 && N.getType() == 0) {
                        w.println("\\node[draw, circle, fill=green, ,minimum size=1pt, scale=0.3] at (" + 80 * N.getXCoordinate() / 34000 + "," + 60 * N.getYCoordinate() / 34000 + ")(" + N.getNodeID() + "){};");
                    } else {
                        w.println("\\node[draw, circle, fill=black, ,minimum size=1pt, scale=0.3] at (" + 80 * N.getXCoordinate() / 34000 + "," + 60 * N.getYCoordinate() / 34000 + ")(" + N.getNodeID() + "){};");
                    }
                }
            }
            for (Arc A : p.getAllArcs()) {
                if (A.getEndNodeXCoordinate() < 10000 && A.getEndNodeXCoordinate() > 8000 && A.getEndNodeYCoordinate() < 15000 && A.getEndNodeYCoordinate() > 10000 && A.getStartNodeXCoordinate() < 10000 && A.getStartNodeXCoordinate() > 8000 && A.getStartNodeYCoordinate() < 15000 && A.getStartNodeYCoordinate() > 10000) {
                    w.println("\\draw[color=black] (" + A.getStartNodeID() + ") -- (" + A.getEndNodeID() + ");");
                }
            }
            w.close();
        } catch (FileNotFoundException e) {
        }
    }

    public void OutputSolution() throws UnsupportedEncodingException, IloException {  //This is the latex input file
        String FileName = "graph.txt";
        PrintWriter w;
        try {
            w = new PrintWriter(FileName, "UTF-8");
            for (Node N : p.getAllNodes()) {

                for (Arc A : N.getFSArcs()) {
                    if (Model.getValue(A.getLX()) == 1 || Model.getValue(A.getSX()) == 1) {
                        w.println("\\node[draw, circle, fill=black, ,minimum size=1pt, scale=0.3] at (" + 80 * N.getXCoordinate() / 34000 + "," + 60 * N.getYCoordinate() / 34000 + ")(" + N.getNodeID() + "){};");
                    }
                }

                for (Arc A : N.getRSArcs()) {
                    if (Model.getValue(A.getLX()) == 1 || Model.getValue(A.getSX()) == 1) {
                        w.println("\\node[draw, circle, fill=black, ,minimum size=1pt, scale=0.3] at (" + 80 * N.getXCoordinate() / 34000 + "," + 60 * N.getYCoordinate() / 34000 + ")(" + N.getNodeID() + "){};");
                    }
                }

                //if (N.getXCoordinate() < 10000 && N.getXCoordinate() > 8000 && N.getYCoordinate() < 15000 && N.getYCoordinate() > 10000) {
//                if (N.getNodeID() == -1) { //|| N.getNodeID() == -1
//                    w.println("\\node[draw, circle, fill=black, ,minimum size=1pt, scale=0.3] at (" + 80 * N.getXCoordinate() / 34000 + "," + 60 * N.getYCoordinate() / 34000 + ")(" + N.getNodeID() + "){};");
//                } else if (N.getType() == 1) {
//                    w.println("\\node[draw, circle, fill=black, ,minimum size=1pt, scale=0.3] at (" + 80 * N.getXCoordinate() / 34000 + "," + 60 * N.getYCoordinate() / 34000 + ")(" + N.getNodeID() + "){};");
//                } else {
//                    w.println("\\node[draw, circle, fill=black, ,minimum size=1pt, scale=0.3] at (" + 80 * N.getXCoordinate() / 34000 + "," + 60 * N.getYCoordinate() / 34000 + ")(" + N.getNodeID() + "){};");
//                }
                //}
            }
            for (Arc A : p.getAllArcs()) {
                //if (A.getEndNodeXCoordinate() < 10000 && A.getEndNodeXCoordinate() > 8000 && A.getEndNodeYCoordinate() < 15000 && A.getEndNodeYCoordinate() > 10000 && A.getStartNodeXCoordinate() < 10000 && A.getStartNodeXCoordinate() > 8000 && A.getStartNodeYCoordinate() < 15000 && A.getStartNodeYCoordinate() > 10000) {
                if (Model.getValue(A.getLX()) == 1 || Model.getValue(A.getSX()) == 1) {
//                        if (A.getEndNode().getNodeDegree() == 3) {
//                            System.out.println("The switch node:  " + A.getEndNode().getNodeID());
//                        }
                    w.println("\\draw[->,color=red,line width=2pt] (" + A.getStartNodeID() + ") -- (" + A.getEndNodeID() + ");");
                } else {
                    //w.println("\\draw[color=black] (" + A.getStartNodeID() + ") -- (" + A.getEndNodeID() + ");");
                }
                //}
            }
            w.close();
        } catch (FileNotFoundException e) {
        }

    }

    public void OutputSolutionValues() throws UnsupportedEncodingException, IloException {
        String FileName = "SolutionValues.txt";
        PrintWriter w;
        try {
            w = new PrintWriter(FileName, "UTF-8");
            w.println("Nodes: ");
            for (Node N : p.getAllNodes()) {
                if (N.getNodeDegree() == 3) { //N.getType() == 1 &&
                    // w.println("Node " + N.getNodeID() + " : " + "Y = " + Model.getValue(N.getY()) + "   Z = " + Model.getValue(N.getZ()));
                }
            }
            w.println("Arcs: ");
            for (Arc A : p.getAllArcs()) {
                w.println("Arc( " + A.getStartNodeID() + " , " + A.getEndNodeID() + " ) = " + Model.getValue(A.getSX()));
            }
            w.close();
        } catch (FileNotFoundException e) {
        }
    }

}
