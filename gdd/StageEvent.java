package gdd;

public class StageEvent {
    public int frame;
    public String entityType; // 例如 "ENEMY", "POWERUP", "BOSS"
    public String subtype;    // 例如 "1", "2" (敌人类型) 或 "MULTI", "SHIELD" (道具类型)
    public int x;

    public StageEvent(int frame, String entityType, String subtype, int x) {
        this.frame = frame;
        this.entityType = entityType;
        this.subtype = subtype;
        this.x = x;
    }
}
