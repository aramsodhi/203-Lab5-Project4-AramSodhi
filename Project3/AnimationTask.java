public class AnimationTask extends Task {
    private int repeatCount;

    public AnimationTask(Entity entity, int repeatCount) {
        super(entity);
        this.repeatCount = repeatCount;
    }

    public void execute(EventScheduler scheduler) {
        UpdateImageEntity entity = (UpdateImageEntity) getEntity();
        entity.updateImage();

        Entity casted_entity = (Entity) entity;

        if (repeatCount != 1) {
            scheduler.scheduleEvent(casted_entity, new AnimationTask(casted_entity, Math.max(this.repeatCount - 1, 0)), casted_entity.getAnimationPeriod());
        }
    }
}
