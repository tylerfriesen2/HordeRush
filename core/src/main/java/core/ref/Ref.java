package core.ref;

public class Ref {

    public static final String title = "Horde Rush";
    public static final int WIDTH = 640, HEIGHT = 480;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public enum State {
        RUNNING, PAUSED, STOPPED
    }
}
