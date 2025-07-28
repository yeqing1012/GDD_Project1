package gdd;

import java.awt.*;
import javax.swing.*;

public class Boss extends Sprite {

    private final Image img = new ImageIcon("Project materials\\合集2\\PNG\\Sprites\\Station\\spaceStation_021.png").getImage();
    private int maxHp = 200;
    private int hp = maxHp;
    private int phase = 1; // 1: 入场, 2: 正常攻击, 3: 狂暴攻击
    private boolean invincible = true;
    private int moveDirection = 1;
    private int fireCooldown = 0;

    public Boss(int x, int y) {
        this.x = x;
        this.y = y;
        this.w = 192;
        this.h = 128;
    }

    @Override
    public void update() {
        switch (phase) {
            case 1: // 入场
                if (y < 50) {
                    y += 1;
                } else {
                    phase = 2;
                    invincible = false;
                }
                break;
            case 2: // 正常攻击
            case 3: // 狂暴攻击
                // 左右移动
                x += moveDirection * 2;
                if (x <= 0 || x >= Global.WIDTH - w) {
                    moveDirection *= -1;
                }

                // 发射逻辑
                fireCooldown--;
                if (fireCooldown <= 0) {
                    fire();
                    fireCooldown = (phase == 2) ? 80 : 50; // 狂暴模式下射速更快
                }
                break;
        }
    }

    private void fire() {
        int bulletCount = (phase == 2) ? 8 : 12; // 狂暴模式下子弹更多
        double angleIncrement = Math.PI / (bulletCount - 1);
        double startAngle = Math.PI / 4;

        for (int i = 0; i < bulletCount; i++) {
            double angle = startAngle + i * angleIncrement;
            Global.bossShots.add(new BossShot(x + w / 2, y + h, angle));
        }
    }

    public void takeDamage(int damage) {
        if (invincible) return;

        hp -= damage;
        if (hp <= 0) {
            alive = false;
            Global.score += 1000; // 击败Boss加1000分
            // 制造一连串的爆炸
            for (int i = 0; i < 5; i++) {
                int offsetX = new java.util.Random().nextInt(w) - w / 2;
                int offsetY = new java.util.Random().nextInt(h) - h / 2;
                Global.explosions.add(new Explosion(x + w / 2 + offsetX, y + h / 2 + offsetY));
            }
        } else if (hp <= maxHp / 2 && phase == 2) {
            phase = 3; // 进入狂暴模式
        }
    }

    @Override
    public void draw(Graphics g) {
        if (alive) {
            g.drawImage(img, x, y, null);
            drawHealthBar(g);
        }
    }

    private void drawHealthBar(Graphics g) {
        // 血条背景
        g.setColor(Color.GRAY);
        g.fillRect(x, y - 20, w, 10);

        // 血条前景
        double hpRatio = (double) hp / maxHp;
        g.setColor(hpRatio > 0.5 ? Color.GREEN : (hpRatio > 0.2 ? Color.ORANGE : Color.RED));
        g.fillRect(x, y - 20, (int) (w * hpRatio), 10);

        // 血条边框
        g.setColor(Color.WHITE);
        g.drawRect(x, y - 20, w, 10);
    }
}
