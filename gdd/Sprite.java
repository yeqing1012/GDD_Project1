package gdd;

import java.awt.*;

/** 所有精灵的公共父类 */
public abstract class Sprite {
    public int x, y, w, h;
    protected Image img;
    protected boolean alive = true;

    /** 简单矩形碰撞 */
    public Rectangle getBounds() { return new Rectangle(x, y, w, h); }

    public boolean isAlive() { return alive; }
    public void kill()       { alive = false; }

    public abstract void update();
    public abstract void draw(Graphics g);
}
