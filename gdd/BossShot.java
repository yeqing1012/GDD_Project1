package gdd;

import javax.swing.*;
import java.awt.*;

public class BossShot extends Sprite {
    private final double dx, dy;

    public BossShot(int x, int y, double angle) {
        this.x = x;
        this.y = y;
        this.w = 16;
        this.h = 16;
        this.img = new ImageIcon("images\\Lasers\\laserRed10.png").getImage();

        // 根据角度计算移动向量
        double speed = 5.0;
        this.dx = Math.cos(angle) * speed;
        this.dy = Math.sin(angle) * speed;
    }

    @Override
    public void update() {
        x += dx;
        y += dy;

        // 越界判断
        if (x < -w || x > Global.WIDTH || y < -h || y > Global.HEIGHT) {
            alive = false;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (alive) {
            g.drawImage(img, x, y, null);
        }
    }

    public void hit() {
        alive = false;
    }
}
