import processing.core.PImage;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Dinosaur extends Entity implements UpdateImageEntity {
    public static final String DINOSAUR_KEY = "dinosaur";
    public static final int DINOSAUR_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 0;
    public static final int DINOSAUR_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 1;
    public static final int DINOSAUR_PARSE_PROPERTY_COUNT = 2;

    private int mushroomCount = 0;

    public Dinosaur(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod) {
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
        Optional<Entity> dinosaurTarget = findDinosaurTarget(world);

        if (dinosaurTarget.isPresent()) {
            boolean moveResult = moveToDinosaur(world, dinosaurTarget.get(), scheduler);

            if (moveResult) {
                if (this.mushroomCount < 3) {
                    Entity mushroomStump = new MushroomStump(MushroomStump.MUSHROOM_STUMP_KEY + "_" + dinosaurTarget.get().getId(), dinosaurTarget.get().getPosition(), imageLibrary.get(MushroomStump.MUSHROOM_STUMP_KEY));
                    world.removeEntity(scheduler, dinosaurTarget.get());
                    world.addEntity(mushroomStump);
                    this.mushroomCount++;
                }
            }

            moveToDinosaur(world, dinosaurTarget.get(), scheduler);
            scheduleBehavior(scheduler, world, imageLibrary);
        }
    }

    public Optional<Entity> findDinosaurTarget(World world) {
        return world.findNearest(getPosition(), List.of(Mushroom.class));
    }


    public boolean moveToDinosaur(World world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacentTo(target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPositionDinosaur(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }

            return false;
        }
    }

    public Point nextPositionDinosaur(World world, Point destination) {
        Point start = getPosition();
        Predicate<Point> canPassThrough = (p) -> world.inBounds(p) && (!world.isOccupied(p) || world.getOccupant(p).get().getClass() == MushroomStump.class);
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
