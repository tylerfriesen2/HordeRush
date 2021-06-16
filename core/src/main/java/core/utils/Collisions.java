package core.utils;

import core.entities.Entity;

public class Collisions {

    private static float separationvel = 0.4f;

    /**
     * Separate two entities by moving both entities
     * @param e1 first entity
     * @param e2 second entity
     */
    public static void separateBoth(Entity e1, Entity e2) {
        Point p1 = new Point(e1.getX(), e1.getY()), p2 = new Point(e2.getX(), e2.getY());
        float theta1 = getSeparationTheta(p1, p2), theta2 = getSeparationTheta(p2, p1);
        if (theta1 == 0) { theta2 = (float) Math.PI; }
        e1.getSprite().translate(separationvel * (float) Math.cos(theta1), separationvel * (float) Math.sin(theta1));
        e2.getSprite().translate(separationvel * (float) Math.cos(theta2), separationvel * (float) Math.sin(theta2));
    }

    /**
     * Separate two entities by moving the second entity
     * @param e1 immovable entity
     * @param e2 entity move
     */
    public static void separateOne(Entity e1, Entity e2) {
        Point p1 = new Point(e1.getX(), e1.getY()), p2 = new Point(e2.getX(), e2.getY());
        float theta1 = getSeparationTheta(p1, p2);
    }

    private static float getSeparationTheta(Point p1, Point p2) {
        return (float) Math.atan2(p1.y - p2.y, p1.x - p2.x);
    }

}
