import processing.core.PImage;
import java.util.List;

public abstract class Entity {
    // Constant save file column positions for properties required by all entities.
    public static final int ENTITY_PROPERTY_KEY_INDEX = 0;
    public static final int ENTITY_PROPERTY_ID_INDEX = 1;
    public static final int ENTITY_PROPERTY_POSITION_X_INDEX = 2;
    public static final int ENTITY_PROPERTY_POSITION_Y_INDEX = 3;
    public static final int ENTITY_PROPERTY_COLUMN_COUNT = 4;

    // instance variables required by all entities
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex = 0;
    private double animationPeriod;
    private double behaviorPeriod;

    // base Entity constructor
    public Entity (String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.animationPeriod = animationPeriod;
        this.behaviorPeriod = behaviorPeriod;
    }

    public String log(){
        if (id.isEmpty()) {
            return null;
        } else {
            return String.format("%s %d %d %d", id, position.x, position.y, imageIndex);
        }
    }

    /** Begins all animation updates for the entity. */
    public void scheduleAnimation(EventScheduler scheduler) {
        scheduler.scheduleEvent(this, new AnimationTask(this, 0), animationPeriod);
    }

    /** Schedules a single behavior update for the entity. */
    public void scheduleBehavior(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
        scheduler.scheduleEvent(this, new BehaviorTask(this, world, imageLibrary), behaviorPeriod);
    }

    public List<PImage> getImages() {
        return this.images;
    }

    public int getImageIndex() {
        return this.imageIndex;
    }

    public void setImageIndex(int index) {
        this.imageIndex = index;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public String getId() {
        return this.id;
    }

    public double getAnimationPeriod() {
        if (animationPeriod != 0) {
            return animationPeriod;
        } else {
            throw new UnsupportedOperationException(String.format("getAnimationPeriod not supported for %s", this.getClass()));
        }
    }

    public double getBehaviorPeriod() {
        if (behaviorPeriod != 0) {
            return behaviorPeriod;
        } else {
            throw new UnsupportedOperationException(String.format("getBehaviorPeriod not supported for %s", this.getClass()));
        }
    }

    abstract void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler);
    abstract void scheduleTasks(EventScheduler scheduler, World world, ImageLibrary imageLibrary);
}
