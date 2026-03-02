import processing.core.PImage;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Asteroid extends Entity implements UpdateImageEntity {
    public static final String ASTEROID_KEY = "asteroid";
    public static final int ASTEROID_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 0;
    public static final int ASTEROID_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 1;
    public static final int ASTEROID_PARSE_PROPERTY_COUNT = 2;

    public static int asteroidCount = 0;

    public Asteroid(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod) {
       super(id, position, images, animationPeriod, behaviorPeriod);
       asteroidCount++;
    }

    public void updateImage() { setImageIndex(getImageIndex() + 1); }

    @Override
    void scheduleTasks(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
        scheduleAnimation(scheduler);
        scheduleBehavior(scheduler, world, imageLibrary);
    }

    @Override
    void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        Optional<Entity> asteroidTarget = findAsteroidTarget(world);

        if (asteroidTarget.isPresent() && moveToAsteroid(world, asteroidTarget.get(), scheduler)) {
            Entity chicken = new Chicken(Chicken.CHICKEN_KEY +  "_" + asteroidTarget.get().getId(), asteroidTarget.get().getPosition(), imageLibrary.get(Chicken.CHICKEN_KEY), 0.250, 2.0);
            world.removeEntity(scheduler, asteroidTarget.get());
            world.addEntity(chicken);
            chicken.scheduleTasks(scheduler, world, imageLibrary);
        }

        scheduleBehavior(scheduler, world, imageLibrary);
    }

    public Optional<Entity> findAsteroidTarget(World world) {
        return world.findNearest(getPosition(), List.of(Dinosaur.class));
    }

    public boolean moveToAsteroid(World world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacentTo(target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPositionAsteroid(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }

            return false;
        }
    }

    public Point nextPositionAsteroid(World world, Point destination) {
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
