import processing.core.PImage;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Chicken extends Entity implements UpdateImageEntity {
    public static final String CHICKEN_KEY = "chicken";
    public static final int CHICKEN_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 0;
    public static final int CHICKEN_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 1;
    public static final int CHICKEN_PARSE_PROPERTY_COUNT = 2;

    public Chicken(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod) {
        super(id, position, images, animationPeriod, behaviorPeriod);
    }

    public void updateImage() { setImageIndex(getImageIndex() + 1); }

    @Override
    void scheduleTasks(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
        scheduleAnimation(scheduler);
        scheduleBehavior(scheduler, world, imageLibrary);
    }

    @Override
    void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        Optional<Entity> chickenTarget = findChickenTarget(world);

        if (chickenTarget.isPresent() && moveToChicken(world, chickenTarget.get(), scheduler)) {
            world.removeEntity(scheduler, chickenTarget.get());
        }

        scheduleBehavior(scheduler, world, imageLibrary);
    }

    public Optional<Entity> findChickenTarget(World world) {
        return world.findNearest(getPosition(), List.of(Dude.class));
    }

    public boolean moveToChicken(World world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacentTo(target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPositionChicken(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }

            return false;
        }
    }

    public Point nextPositionChicken(World world, Point destination) {
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

        // move randomly if no path exists
        List<Point> openNeighbors = PathingStrategy.CARDINAL_NEIGHBORS.apply(getPosition()).filter((p) -> world.inBounds(p) && !world.isOccupied(p)).toList();

        if (!openNeighbors.isEmpty()) {
            return openNeighbors.get(new Random().nextInt(openNeighbors.size()));
        }

        return start;
    }

}
