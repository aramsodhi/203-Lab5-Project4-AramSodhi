import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mushroom extends Entity {
    public static final String MUSHROOM_KEY = "mushroom";
    public static final int MUSHROOM_PARSE_BEHAVIOR_PERIOD_INDEX = 0;
    public static final int MUSHROOM_PARSE_PROPERTY_COUNT = 1;

    public Mushroom(String id, Point position, List<PImage> images, double behaviorPeriod) {
        super(id, position, images, 0.0, behaviorPeriod);
    }

    public void scheduleTasks(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
        scheduleBehavior(scheduler, world, imageLibrary);
    }

    /** Executes Mushroom specific Logic. */
    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        Point position = getPosition();
        List<Point> adjacentPositions = new ArrayList<>(List.of(
                new Point(position.x - 1, position.y),
                new Point(position.x + 1, position.y),
                new Point(position.x, position.y - 1),
                new Point(position.x, position.y + 1)
        ));
        Collections.shuffle(adjacentPositions);

        List<Point> mushroomBackgroundPositions = new ArrayList<>();
        List<Point> mushroomEntityPositions = new ArrayList<>();
        for (Point adjacentPosition : adjacentPositions) {
            if (world.inBounds(adjacentPosition) && !world.isOccupied(adjacentPosition) && world.hasBackground(adjacentPosition)) {
                Background bg = world.getBackgroundCell(adjacentPosition);
                if (bg.getId().equals("grass")) {
                    mushroomBackgroundPositions.add(adjacentPosition);
                } else if (bg.getId().equals("grass_mushrooms")) {
                    mushroomEntityPositions.add(adjacentPosition);
                }
            }
        }

        if (!mushroomBackgroundPositions.isEmpty()) {
            Point backgroundPosition = mushroomBackgroundPositions.get(0);

            Background background = new Background("grass_mushrooms", imageLibrary.get("grass_mushrooms"), 0);
            world.setBackgroundCell(backgroundPosition, background);
        } else if (!mushroomEntityPositions.isEmpty()) {
            Point adjacentPosition = mushroomEntityPositions.get(0);

            Mushroom mushroom = new Mushroom(MUSHROOM_KEY, adjacentPosition, imageLibrary.get(MUSHROOM_KEY), getBehaviorPeriod() * 4.0);

            world.addEntity(mushroom);
            mushroom.scheduleTasks(scheduler, world, imageLibrary);
        }

        scheduleBehavior(scheduler, world, imageLibrary);
    }
}
