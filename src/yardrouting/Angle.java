package yardrouting;

public class Angle {

    private final double x;
    private final double y;

    public Angle(Node a) {
        this.x = a.getXCoordinate();
        this.y = a.getYCoordinate();
    }

    public float getAngle(Node b, Node c) {
        float angle1 = (float) Math.toDegrees(Math.atan2(b.getYCoordinate() - y, b.getXCoordinate() - x));
        float angle2 = (float) Math.toDegrees(Math.atan2(c.getYCoordinate() - y, c.getXCoordinate() - x));
        float FinalAngle;
        FinalAngle = Math.abs(angle1 - angle2);
        if (FinalAngle > 180) {
            FinalAngle = 360 - FinalAngle;
        }
        return FinalAngle;
    }

}
