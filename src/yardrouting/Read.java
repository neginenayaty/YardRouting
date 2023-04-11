package yardrouting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Read {

    private BufferedReader x;
    private final Layout p;

    public Read(Layout p) {
        this.p = p;
    }

    public void OpenFile() throws IOException {
        x = new BufferedReader(new FileReader("Input.txt"));
    }

    public void ReadFile() throws IOException {
        double XLeft;
        double YLeft;
        double XRight;
        double YRight;
        boolean found1;
        boolean found2;
        int TrackID;
        int EdgeID;
        int SeqNumber;
        int index1 = 0;
        int index2 = 0;
        Node a;
        Arc b;
        Track c;
        String Line = "";
        while ((Line = x.readLine()) != null) {
            String[] LineList = Line.split("\t");
            EdgeID = Integer.parseInt(LineList[0]);
            TrackID = Integer.parseInt(LineList[1]);
            SeqNumber = Integer.parseInt(LineList[2]);
            XLeft = Double.parseDouble(LineList[3]);
            YLeft = Double.parseDouble(LineList[4]);
            XRight = Double.parseDouble(LineList[5]);
            YRight = Double.parseDouble(LineList[6]);
            found1 = false;
            found2 = false;
            double RN = Math.random();
            for (int j = 0; j < p.getNodeSize(); j++) {
                if (p.getXCoordinate(j) == XLeft && p.getYCoordinate(j) == YLeft) {
                    found1 = true;
                    index1 = j;
                }
                if (p.getXCoordinate(j) == XRight && p.getYCoordinate(j) == YRight) {
                    found2 = true;
                    index2 = j;
                }
                if (found1 && found2) {
                    break;
                }
            }
            if (!found1) {
                a = new Node(p.getNodeSize() + 1, XLeft, YLeft);
                p.addNewNode(a);
                index1 = p.getNodeSize() - 1;
            }
            if (!found2) {
                a = new Node(p.getNodeSize() + 1, XRight, YRight);
                p.addNewNode(a);
                index2 = p.getNodeSize() - 1;
            }
            b = new Arc(EdgeID, TrackID, SeqNumber, p.getNode(index1), p.getNode(index2));

            for (Arc A : p.getAllArcs()) {
                if (A.getStartNodeID() == p.getNodeID(index1) && A.getEndNodeID() == p.getNodeID(index2)) {
                    System.out.println("Repititive edge in the data set!");
                    break;
                }
                if (A.getStartNodeID() == p.getNodeID(index2) && A.getEndNodeID() == p.getNodeID(index1)) {
                    System.out.println("Repititive edge in the data set!");
                    break;
                }
            }
            p.addNewArc(b);
            b = new Arc(EdgeID, TrackID, SeqNumber, p.getNode(index2), p.getNode(index1));
            p.addNewArc(b);
            found1 = false;
            index1 = p.getTrackSize();
            for (int j = 0; j < p.getTrackSize(); j++) {
                if (p.getTrackID(j) == TrackID) {
                    found1 = true;
                    index1 = j;
                    break;
                }
            }
            if (!found1) {
                c = new Track(TrackID);
                p.addNewTrack(c);
                p.addArcToTrack(b, index1);
            } else {
                p.addArcToTrack(b, index1);
            }

        }

    }

    public void CloseFile() throws IOException {
        x.close();
    }

    public void ReadMadeUpNetwork1() { //This is a small made up network
        Node a;
        Arc b;
        a = new Node(1, -1, 0);
        p.addNewNode(a);
        a = new Node(2, 1, 0);
        p.addNewNode(a);
        a = new Node(3, 3, 0);
        p.addNewNode(a);
        a = new Node(4, 2, 1.732050808);
        p.addNewNode(a);
        a = new Node(5, 1, 3.464101615);
        p.addNewNode(a);
        a = new Node(6, 5, 0);
        p.addNewNode(a);
        a = new Node(7, -3, 0);
        p.addNewNode(a);
        a = new Node(8, 7, 0);
        p.addNewNode(a);
        b = new Arc(1, 1, 1, p.getNode(0), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(2, 1, 2, p.getNode(1), p.getNode(0));
        p.addNewArc(b);
        b = new Arc(3, 1, 3, p.getNode(1), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(4, 1, 4, p.getNode(2), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(5, 1, 5, p.getNode(2), p.getNode(3));
        p.addNewArc(b);
        b = new Arc(6, 1, 6, p.getNode(3), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(7, 1, 7, p.getNode(3), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(8, 1, 8, p.getNode(1), p.getNode(3));
        p.addNewArc(b);
        //b = new Arc(9, 1, 9, p.getNode(3), p.getNode(4));
        //p.addNewArc(b);
        //b = new Arc(10, 1, 10, p.getNode(4), p.getNode(3));
        //p.addNewArc(b);
        b = new Arc(11, 1, 11, p.getNode(5), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(12, 1, 12, p.getNode(2), p.getNode(5));
        p.addNewArc(b);
        b = new Arc(13, 1, 13, p.getNode(6), p.getNode(0));
        p.addNewArc(b);
        b = new Arc(14, 1, 14, p.getNode(0), p.getNode(6));
        p.addNewArc(b);
        // b = new Arc(15, 1, 15, p.getNode(7), p.getNode(5));
        //p.addNewArc(b);
        //b = new Arc(16, 1, 16, p.getNode(5), p.getNode(7));
        //p.addNewArc(b);

    }

    public void ReadMadeUpNetwork2() { //This is a small made up network
        Node a;
        Arc b;
        a = new Node(1, -1, 0);
        p.addNewNode(a);
        a = new Node(2, 1, 0);
        p.addNewNode(a);
        a = new Node(3, 3, 0);
        p.addNewNode(a);
        a = new Node(4, -1, 1);
        p.addNewNode(a);
        a = new Node(5, 5, 1);
        p.addNewNode(a);
        a = new Node(6, 5, 0);
        p.addNewNode(a);
        a = new Node(7, 7, 0);
        p.addNewNode(a);
        a = new Node(8, -3, 0);
        p.addNewNode(a);
        b = new Arc(1, 1, 1, p.getNode(0), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(2, 1, 2, p.getNode(1), p.getNode(0));
        p.addNewArc(b);
        b = new Arc(3, 1, 3, p.getNode(1), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(4, 1, 4, p.getNode(2), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(5, 1, 5, p.getNode(2), p.getNode(4));
        p.addNewArc(b);
        b = new Arc(6, 1, 6, p.getNode(4), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(7, 1, 7, p.getNode(3), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(8, 1, 8, p.getNode(1), p.getNode(3));
        p.addNewArc(b);
        b = new Arc(11, 1, 11, p.getNode(5), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(12, 1, 12, p.getNode(2), p.getNode(5));
        p.addNewArc(b);
        b = new Arc(13, 1, 13, p.getNode(0), p.getNode(7));
        p.addNewArc(b);
        b = new Arc(14, 1, 14, p.getNode(7), p.getNode(0));
        p.addNewArc(b);
        b = new Arc(15, 1, 15, p.getNode(5), p.getNode(6));
        p.addNewArc(b);
        b = new Arc(16, 1, 16, p.getNode(6), p.getNode(5));
        p.addNewArc(b);

    }

    public void ReadMadeUpNetwork3() { //This is a small made up network
        Node a;
        Arc b;
        a = new Node(1, -1, 0);
        p.addNewNode(a);
        a = new Node(2, -1, 1);
        p.addNewNode(a);
        a = new Node(3, 1, 0);
        p.addNewNode(a);
        a = new Node(4, 3, 0);
        p.addNewNode(a);
        a = new Node(5, 5, 0);
        p.addNewNode(a);
        a = new Node(6, 3, 1);
        p.addNewNode(a);
        a = new Node(7, 7, 0);
        p.addNewNode(a);
        a = new Node(8, 5, 1);
        p.addNewNode(a);
        a = new Node(9, 9, 0);
        p.addNewNode(a);
        a = new Node(10, -3, 0);
        p.addNewNode(a);
        a = new Node(11, -3, 2);
        p.addNewNode(a);
        a = new Node(12, 11, 0);
        p.addNewNode(a);
        b = new Arc(1, 1, 1, p.getNode(0), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(2, 1, 2, p.getNode(2), p.getNode(0));
        p.addNewArc(b);
        b = new Arc(3, 1, 3, p.getNode(1), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(4, 1, 4, p.getNode(2), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(5, 1, 5, p.getNode(2), p.getNode(3));
        p.addNewArc(b);
        b = new Arc(6, 1, 6, p.getNode(3), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(7, 1, 7, p.getNode(4), p.getNode(3));
        p.addNewArc(b);
        b = new Arc(8, 1, 8, p.getNode(3), p.getNode(4));
        p.addNewArc(b);
        b = new Arc(11, 1, 11, p.getNode(5), p.getNode(4));
        p.addNewArc(b);
        b = new Arc(12, 1, 12, p.getNode(4), p.getNode(5));
        p.addNewArc(b);
        b = new Arc(13, 1, 13, p.getNode(4), p.getNode(6));
        p.addNewArc(b);
        b = new Arc(14, 1, 14, p.getNode(6), p.getNode(4));
        p.addNewArc(b);
        b = new Arc(15, 1, 15, p.getNode(7), p.getNode(6));
        p.addNewArc(b);
        b = new Arc(16, 1, 16, p.getNode(6), p.getNode(7));
        p.addNewArc(b);
        b = new Arc(15, 1, 15, p.getNode(8), p.getNode(6));
        p.addNewArc(b);
        b = new Arc(16, 1, 16, p.getNode(6), p.getNode(8));
        p.addNewArc(b);
        b = new Arc(17, 1, 15, p.getNode(0), p.getNode(9));
        p.addNewArc(b);
        b = new Arc(18, 1, 16, p.getNode(9), p.getNode(0));
        p.addNewArc(b);
        b = new Arc(19, 1, 15, p.getNode(1), p.getNode(10));
        p.addNewArc(b);
        b = new Arc(20, 1, 16, p.getNode(10), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(21, 1, 15, p.getNode(8), p.getNode(11));
        p.addNewArc(b);
        b = new Arc(22, 1, 16, p.getNode(11), p.getNode(8));
        p.addNewArc(b);

    }

    public void ReadMadeUpNetwork4() { //This is a small made up network
        Node a;
        Arc b;
        a = new Node(1, 0, 0);
        p.addNewNode(a);
        a = new Node(2, 2, 0);
        p.addNewNode(a);
        a = new Node(3, 10, 0);
        p.addNewNode(a);
        a = new Node(4, 12, 0);
        p.addNewNode(a);
        a = new Node(5, 14, 0);
        p.addNewNode(a);
        a = new Node(6, 16, 0);
        p.addNewNode(a);
        a = new Node(7, 4, 0);
        p.addNewNode(a);
        a = new Node(8, 2, 1);
        p.addNewNode(a);
        a = new Node(9, 0, 2);
        p.addNewNode(a);
        a = new Node(10, -2, 3);
        p.addNewNode(a);
        a = new Node(11, 10, 1);
        p.addNewNode(a);
        a = new Node(12, -3, 4);
        p.addNewNode(a);
        b = new Arc(1, 1, 1, p.getNode(0), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(2, 1, 2, p.getNode(1), p.getNode(0));
        p.addNewArc(b);
        b = new Arc(3, 1, 3, p.getNode(1), p.getNode(6));
        p.addNewArc(b);
        b = new Arc(4, 1, 4, p.getNode(6), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(5, 1, 5, p.getNode(2), p.getNode(6));
        p.addNewArc(b);
        b = new Arc(6, 1, 6, p.getNode(6), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(7, 1, 7, p.getNode(2), p.getNode(3));
        p.addNewArc(b);
        b = new Arc(8, 1, 8, p.getNode(3), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(11, 1, 11, p.getNode(3), p.getNode(4));
        p.addNewArc(b);
        b = new Arc(12, 1, 12, p.getNode(4), p.getNode(3));
        p.addNewArc(b);
        b = new Arc(13, 1, 13, p.getNode(4), p.getNode(5));
        p.addNewArc(b);
        b = new Arc(14, 1, 14, p.getNode(5), p.getNode(4));
        p.addNewArc(b);
        b = new Arc(15, 1, 15, p.getNode(10), p.getNode(3));
        p.addNewArc(b);
        b = new Arc(16, 1, 16, p.getNode(3), p.getNode(10));
        p.addNewArc(b);
        b = new Arc(15, 1, 15, p.getNode(8), p.getNode(10));
        p.addNewArc(b);
        b = new Arc(16, 1, 16, p.getNode(10), p.getNode(8));
        p.addNewArc(b);
        b = new Arc(17, 1, 15, p.getNode(6), p.getNode(7));
        p.addNewArc(b);
        b = new Arc(18, 1, 16, p.getNode(7), p.getNode(6));
        p.addNewArc(b);
        b = new Arc(19, 1, 15, p.getNode(7), p.getNode(8));
        p.addNewArc(b);
        b = new Arc(20, 1, 16, p.getNode(8), p.getNode(7));
        p.addNewArc(b);
        b = new Arc(21, 1, 15, p.getNode(8), p.getNode(9));
        p.addNewArc(b);
        b = new Arc(22, 1, 16, p.getNode(9), p.getNode(8));
        p.addNewArc(b);
        b = new Arc(23, 1, 1, p.getNode(9), p.getNode(11));
        p.addNewArc(b);
        b = new Arc(24, 1, 2, p.getNode(11), p.getNode(9));
        p.addNewArc(b);
    }

    public void ReadMadeUpNetwork6() {
        Node a;
        Arc b;
        a = new Node(1, 0, 0);
        p.addNewNode(a);
        a = new Node(2, 2, 0);
        p.addNewNode(a);
        a = new Node(3, 4, 0);
        p.addNewNode(a);
        a = new Node(4, 6, 2);
        p.addNewNode(a);
        a = new Node(5, 8, 0);
        p.addNewNode(a);
        a = new Node(6, 10, 0);
        p.addNewNode(a);
        a = new Node(7, 12, 0);
        p.addNewNode(a);

        b = new Arc(1, 1, 1, p.getNode(0), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(2, 1, 2, p.getNode(1), p.getNode(0));
        p.addNewArc(b);
        b = new Arc(3, 1, 1, p.getNode(1), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(4, 1, 2, p.getNode(2), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(5, 1, 1, p.getNode(2), p.getNode(3));
        p.addNewArc(b);
        b = new Arc(6, 1, 2, p.getNode(3), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(7, 1, 1, p.getNode(3), p.getNode(4));
        p.addNewArc(b);
        b = new Arc(8, 1, 1, p.getNode(4), p.getNode(3));
        p.addNewArc(b);
        b = new Arc(9, 1, 2, p.getNode(4), p.getNode(5));
        p.addNewArc(b);
        b = new Arc(10, 1, 2, p.getNode(5), p.getNode(4));
        p.addNewArc(b);
        b = new Arc(11, 1, 1, p.getNode(5), p.getNode(6));
        p.addNewArc(b);
        b = new Arc(12, 1, 2, p.getNode(6), p.getNode(5));
        p.addNewArc(b);
        b = new Arc(13, 1, 1, p.getNode(2), p.getNode(4));
        p.addNewArc(b);
        b = new Arc(14, 1, 2, p.getNode(4), p.getNode(2));
        p.addNewArc(b);
    }

    public void ReadMadeUpNetwork5() {
        Node a;
        Arc b;
        a = new Node(1, 0, 0);
        p.addNewNode(a);
        a = new Node(2, 2, 0);
        p.addNewNode(a);
        a = new Node(3, 10, 0);
        p.addNewNode(a);
        a = new Node(4, 12, 0);
        p.addNewNode(a);
        a = new Node(5, 14, 0);
        p.addNewNode(a);
        a = new Node(6, 16, 0);
        p.addNewNode(a);
        a = new Node(7, 4, 0);
        p.addNewNode(a);
        a = new Node(8, 2, 1);
        p.addNewNode(a);
        a = new Node(9, 0, 2);
        p.addNewNode(a);
        a = new Node(10, -2, 3);
        p.addNewNode(a);
        a = new Node(11, 10, 2);
        p.addNewNode(a);
        a = new Node(12, -3, 4);
        p.addNewNode(a);
        a = new Node(13, 8, -1);
        p.addNewNode(a);
        a = new Node(14, 3, -1);
        p.addNewNode(a);
        a = new Node(15, 12, -1);
        p.addNewNode(a);
        a = new Node(16, 14, -1);
        p.addNewNode(a);

        b = new Arc(1, 1, 1, p.getNode(0), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(2, 1, 2, p.getNode(1), p.getNode(0));
        p.addNewArc(b);
        b = new Arc(3, 1, 3, p.getNode(1), p.getNode(6));
        p.addNewArc(b);
        b = new Arc(4, 1, 4, p.getNode(6), p.getNode(1));
        p.addNewArc(b);
        b = new Arc(5, 1, 5, p.getNode(2), p.getNode(6));
        p.addNewArc(b);
        b = new Arc(6, 1, 6, p.getNode(6), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(7, 1, 7, p.getNode(2), p.getNode(3));
        p.addNewArc(b);
        b = new Arc(8, 1, 8, p.getNode(3), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(11, 1, 9, p.getNode(3), p.getNode(4));
        p.addNewArc(b);
        b = new Arc(12, 1, 10, p.getNode(4), p.getNode(3));
        p.addNewArc(b);
        b = new Arc(13, 1, 11, p.getNode(4), p.getNode(5));
        p.addNewArc(b);
        b = new Arc(14, 1, 12, p.getNode(5), p.getNode(4));
        p.addNewArc(b);
        b = new Arc(15, 1, 13, p.getNode(10), p.getNode(3));
        p.addNewArc(b);
        b = new Arc(16, 1, 14, p.getNode(3), p.getNode(10));
        p.addNewArc(b);
        b = new Arc(15, 1, 15, p.getNode(8), p.getNode(10));
        p.addNewArc(b);
        b = new Arc(16, 1, 16, p.getNode(10), p.getNode(8));
        p.addNewArc(b);
        b = new Arc(17, 1, 17, p.getNode(6), p.getNode(7));
        p.addNewArc(b);
        b = new Arc(18, 1, 18, p.getNode(7), p.getNode(6));
        p.addNewArc(b);
        b = new Arc(19, 1, 19, p.getNode(7), p.getNode(8));
        p.addNewArc(b);
        b = new Arc(20, 1, 20, p.getNode(8), p.getNode(7));
        p.addNewArc(b);
        b = new Arc(21, 1, 21, p.getNode(8), p.getNode(9));
        p.addNewArc(b);
        b = new Arc(22, 1, 22, p.getNode(9), p.getNode(8));
        p.addNewArc(b);
        b = new Arc(23, 1, 23, p.getNode(9), p.getNode(11));
        p.addNewArc(b);
        b = new Arc(24, 1, 24, p.getNode(11), p.getNode(9));
        p.addNewArc(b);

//        b = new Arc(17, 1, 17, p.getNode(10), p.getNode(7));
//        p.addNewArc(b);
//        b = new Arc(18, 1, 18, p.getNode(7), p.getNode(10));
//        p.addNewArc(b);

        b = new Arc(25, 1, 25, p.getNode(2), p.getNode(12));
        p.addNewArc(b);
        b = new Arc(26, 1, 26, p.getNode(12), p.getNode(2));
        p.addNewArc(b);
        b = new Arc(27, 1, 27, p.getNode(12), p.getNode(13));
        p.addNewArc(b);
        b = new Arc(28, 1, 28, p.getNode(13), p.getNode(12));
        p.addNewArc(b);
        b = new Arc(29, 1, 29, p.getNode(12), p.getNode(14));
        p.addNewArc(b);
        b = new Arc(30, 1, 30, p.getNode(14), p.getNode(12));
        p.addNewArc(b);
        b = new Arc(31, 1, 31, p.getNode(14), p.getNode(15));
        p.addNewArc(b);
        b = new Arc(32, 1, 32, p.getNode(15), p.getNode(14));
        p.addNewArc(b);
    }
}
