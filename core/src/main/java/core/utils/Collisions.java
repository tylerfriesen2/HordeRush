package core.utils;

import core.entities.Entity;
import core.entities.Player;

public class Collisions {

    /**
     * Separate two entities by moving both entities
     * @param e1 first entity
     * @param e2 second entity
     */
    public static void twoWaySeparation(Entity e1, Entity e2) {
        Point p1 = new Point(e1.getX() + e1.getWidth() / 2, e1.getY() + e1.getHeight() / 2), p2 = new Point(e2.getX() + e2.getWidth() / 2, e2.getY() + e2.getHeight() / 2);
        float separationvel1 = p1.distance(p2) / e1.getRadius(), separationvel2 = p2.distance(p1) / e2.getRadius();
        float theta1 = getSeparationTheta(p1, p2), theta2 = getSeparationTheta(p2, p1);
        if (theta1 == 0) { theta2 = (float) Math.PI; }
        e1.getSprite().translate(separationvel1 * (float) Math.cos(theta1), separationvel1 * (float) Math.sin(theta1));
        e2.getSprite().translate(separationvel2 * (float) Math.cos(theta2), separationvel2 * (float) Math.sin(theta2));
    }

    /**
     * Separate two entities by moving the second entity
     * @param e1 immovable entity
     * @param e2 entity to move
     */
    public static void oneWaySeparation(Entity e1, Entity e2) {
        Point p1 = new Point(e1.getX(), e1.getY()), p2 = new Point(e2.getX(), e2.getY());
        float separationvel = p2.distance(p1) / e1.getRadius();
        float theta = getSeparationTheta(p2, p1);
        e2.getSprite().translate(separationvel * (float) Math.cos(theta), separationvel * (float) Math.sin(theta));
    }

    private static float getSeparationTheta(Point p1, Point p2) {
        return (float) Math.atan2(p1.y - p2.y, p1.x - p2.x);
    }


}
