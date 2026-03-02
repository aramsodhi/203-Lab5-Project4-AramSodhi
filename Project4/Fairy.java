import processing.core.PImage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Fairy extends Entity implements UpdateImageEntity {
    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 0;
    public static final int FAIRY_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 1;
    public static final int FAIRY_PARSE_PROPERTY_COUNT = 2;

    public Fairy(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod) {
        super(id, position, images, animationPeriod, behaviorPeriod);
    }

    public void updateImage() {
        setImageIndex(getImageIndex() + 1);
    }

    public void scheduleTasks(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
        scheduleAnimation(scheduler);
        scheduleBehavior(scheduler, world, imageLibrary);
    }

    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = findFairyTarget(world);

        if (fairyTarget.isPresent()) {
            boolean moveResult = moveToFairy(world, fairyTarget.get(), scheduler);

            if (moveResult) {
                Entity sapling = new Sapling(Sapling.SAPLING_KEY + "_" + fairyTarget.get().getId(), fairyTarget.get().getPosition(), imageLibrary.get(Sapling.SAPLING_KEY));
                world.removeEntity(scheduler, fairyTarget.get());
                world.addEntity(sapling);
                sapling.scheduleTasks(scheduler, world, imageLibrary);
            }
        }

        scheduleBehavior(scheduler, world, imageLibrary);
    }

    public Optional<Entity> findFairyTarget(World world) {
        List<Class<?>> potentialTargets = List.of(Stump.class);
        return world.findNearest(getPosition(), potentialTargets);
    }

    /** Attempts to move the Fairy toward a target, returning True if already adjacent to it. */
    public boolean moveToFairy(World world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacentTo(target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPositionFairy(world, target.getPosition());
            //System.out.printf("current position: (%d, %d), next position: (%d, %d)%n", getPosition().x, getPosition().y, nextPos.x, nextPos.y);

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    /** Determines a Fairy's next position when moving. */
    public Point nextPositionFairy(World world, Point destination) {
        Point start = getPosition();
        Predicate<Point> canPassThrough = (p) -> world.inBounds(p) && !world.isOccupied(p);
        BiPredicate<Point, Point> withinReach = Point::adjacentTo;
        Function<Point, Stream<Point>> potentialNeighbors = PathingStrategy.CARDINAL_NEIGHBORS;

        PathingStrategy pathfinding = new AStarPathingStrategy();

        List<Point> path = pathfinding.computePath(
                start,
                destination,
                canPassThrough,
                withinReach,
                potentialNeighbors
        );

        if (!path.isEmpty()) {
            return path.getFirst();
        }

        return start;
    }
}
