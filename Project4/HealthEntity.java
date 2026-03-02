import processing.core.PImage;

import java.awt.*;
import java.util.List;

public abstract class HealthEntity extends Entity {
    private int health = 0;

    public HealthEntity(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod, int health) {
        super(id, position, images, animationPeriod, behaviorPeriod);
        this.health = health;
    }

    public abstract void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler);
    public abstract void scheduleTasks(EventScheduler scheduler, World world, ImageLibrary imageLibrary);

    public int getHealth() { return this.health; }
    public void setHealth(int health) { this.health = health; }
}
