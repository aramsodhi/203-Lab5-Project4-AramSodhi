public abstract class Task {
    private final Entity entity;

    public Task(Entity entity) {
        this.entity = entity;
    }

    public abstract void execute(EventScheduler scheduler);
    public Entity getEntity() { return this.entity; }
}
