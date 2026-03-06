import processing.core.PImage;

import java.util.List;

public class Stump extends Entity {
    public static final String STUMP_KEY = "stump";
    public static final int STUMP_PARSE_PROPERTY_COUNT = 0;

    public Stump(String id, Point position, List<PImage> images) {
        super(id, position, images, 0.0, 0.0);
    }

    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        throw new UnsupportedOperationException(String.format("executeBehavior not supported for %s", this.getClass()));
    }

    public void scheduleTasks(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {}

}
