
public class BehaviorTask extends Task {
    private final World world;
    private final ImageLibrary imageLibrary;

    public BehaviorTask(Entity entity, World world, ImageLibrary imageLibrary) {
        super(entity);
        this.world = world;
        this.imageLibrary = imageLibrary;
    }

    public void execute(EventScheduler scheduler) {
        Entity entity = getEntity();
        entity.executeBehavior(world, imageLibrary, scheduler);
    }
}
