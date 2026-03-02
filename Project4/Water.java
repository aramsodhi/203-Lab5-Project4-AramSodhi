import processing.core.PImage;

import java.util.List;

public class Water extends Entity {
    public static final String WATER_KEY = "water";
    public static final int WATER_PARSE_PROPERTY_COUNT = 0;

    public Water(String id, Point position, List<PImage> images) {
        super(id, position, images, 0.0, 0.0);
    }

    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        throw new UnsupportedOperationException(String.format("executeBehavior not supported for %s", this.getClass()));
    }

    public void scheduleTasks(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {}
}
