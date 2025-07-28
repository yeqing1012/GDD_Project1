package gdd;

public class EnemyType2 extends Enemy {
    @Override
    public int getScore() {
        return 20;
    }

    private int moveDirection = 1;
    private int moveCounter = 0;

    public EnemyType2(int x, int y) {
        // 血量更高，使用不同的贴图
        super(x, y, 5, "images/Enemies/enemyBlue3.png", "images/Enemies/enemyBlue4.png");
    }

    @Override
    public void update() {
        super.update(); // 调用基类的基础移动和动画

        // Z字形移动逻辑
        x += 2 * moveDirection;
        moveCounter++;
        if (moveCounter > 60) { // 每60帧改变一次方向
            moveCounter = 0;
            moveDirection *= -1;
        }

        // 边界限制
        if (x < 0 || x > Global.WIDTH - w) {
            moveDirection *= -1;
        }
    }
}
