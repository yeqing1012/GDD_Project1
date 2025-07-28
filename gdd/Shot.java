package gdd;

import java.awt.*;
import javax.swing.*;

/** 玩家或敌人子弹 */
public class Shot extends Sprite {
    private final int vy;
    private final int vx;

    // 构造函数 1: 玩家的直线子弹 (vx=0, vy=-8)
    public Shot(int x, int y) {
        this(x, y, 0, false);
    }

    // 构造函数 2: 玩家的斜向子弹 (vy=-8)
    public Shot(int x, int y, int vx) {
        this(x, y, vx, false);
    }

    // 构造函数 3: 通用构造函数，用于创建敌我双方的直线子弹
    public Shot(int x, int y, boolean enemy) {
        this(x, y, 0, enemy);
    }

    // 构造函数 4 (私有): 所有构造函数的最终实现
    private Shot(int x, int y, int vx, boolean enemy) {
        this.x = x; this.y = y; w = 8; h = 16;
        this.vx = vx;
        this.vy = enemy ? 5 : -8; // 敌人子弹向下，玩家子弹向上
        img = new ImageIcon("images/Lasers/laserBlue04.png").getImage();
    }

    @Override
    public void update() {
        y += vy;
        x += vx;
        // 子弹飞出屏幕后消失
        if (y < -h || y > Global.HEIGHT || x < -w || x > Global.WIDTH) {
            alive = false;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (alive) g.drawImage(img, x, y, null);
    }

    public void hit() {
        alive = false;
    }
}