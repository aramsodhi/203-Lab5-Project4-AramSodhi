import processing.core.PImage;

import java.awt.*;
import java.util.List;

public class House extends Entity {
    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_PARSE_PROPERTY_COUNT = 0;

    public House(String id, Point position, List<PImage> images) {
        super(id, position, images, 0.0, 0.0);
    }

    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        throw new UnsupportedOperationException(String.format("executeBehavior not supported for %s", this.getClass()));
    }

    public void scheduleTasks(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {}
}
