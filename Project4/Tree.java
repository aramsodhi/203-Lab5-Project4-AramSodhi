import processing.core.PImage;
import java.util.List;

public class Tree extends HealthEntity implements UpdateImageEntity {
    public static final String TREE_KEY = "tree";
    public static final int TREE_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 0;
    public static final int TREE_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 1;
    public static final int TREE_PARSE_PROPERTY_HEALTH_INDEX = 2;
    public static final int TREE_PARSE_PROPERTY_COUNT = 3;
    public static final double TREE_RANDOM_BEHAVIOR_PERIOD_MIN = 0.01;
    public static final double TREE_RANDOM_BEHAVIOR_PERIOD_MAX = 0.10;
    public static final int TREE_RANDOM_HEALTH_MIN = 1;
    public static final int TREE_RANDOM_HEALTH_MAX = 3;
    public static final double TREE_RANDOM_ANIMATION_PERIOD_MIN = 0.1;
    public static final double TREE_RANDOM_ANIMATION_PERIOD_MAX = 1.0;

    public Tree(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod, int health) {
        super(id, position, images, animationPeriod, behaviorPeriod, health);
    }

    public void updateImage() {
        setImageIndex(getImageIndex() + 1);
    }

    /** Executes Tree specific Logic. */
    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        if (!transformTree(world, scheduler, imageLibrary)) {
            scheduleBehavior(scheduler, world, imageLibrary);
        }
    }

    public void scheduleTasks(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
        scheduleAnimation(scheduler);
        scheduleBehavior(scheduler, world, imageLibrary);
    }

    public boolean transformTree(World world, EventScheduler scheduler, ImageLibrary imageLibrary) {
        if (getHealth() <= 0) {
            Entity stump = new Stump(Stump.STUMP_KEY + "_" + getId(), getPosition(), imageLibrary.get(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }
}
