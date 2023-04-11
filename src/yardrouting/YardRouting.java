package yardrouting;

import ilog.concert.IloException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class YardRouting {

    public static void main(String[] args) throws UnsupportedEncodingException, IOException, IloException {
        Layout CSX = new Layout();
        Read Reader = new Read(CSX);
        Problem p;
        Output Writer;
        int WhatProblem = 1;
        switch (WhatProblem) {
            case 1:
                Reader.OpenFile();
                Reader.ReadFile();
                Reader.CloseFile();
                p = new Problem(CSX);
                p.AnglePreprocessing();
                p.PreLPSolver();
                p.SolveEveryPairs();
                Writer = new Output(CSX, p.getLPModel());
                //Writer.OutputLayout(); //Latex Input
                //Writer.OutputSolution(); //Latex Input
                break;
            case 2:
                Reader.OpenFile();
                Reader.ReadFile();
                Reader.CloseFile();
                p = new Problem(CSX);
                p.AnglePreprocessing();
                p.ShortestCycle();
                break;
                break;
            default:
                // do nothing
                break;
        }
    }

}
