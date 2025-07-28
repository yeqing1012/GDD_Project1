package gdd;

import java.awt.*;
import java.util.Random;
import gdd.scene.Scene;

import javax.swing.*;

/**
 * 敌机基类，提供基础属性和动画支持。
 */
public abstract class Enemy extends Sprite {
    protected Image img1, img2;
    protected int tick = 0;
    private int flashTick = 0;
    protected int hp = 1;

    public Enemy(int x, int y, int hp, String imgPath1, String imgPath2) {
        this.x = x;
        this.y = y;
        this.w = 48;
        this.h = 48;
        this.hp = hp;
        this.img1 = new ImageIcon(imgPath1).getImage();
        this.img2 = new ImageIcon(imgPath2).getImage();
    }

    @Override
    public void update() {
        tick++;
        if (flashTick > 0) flashTick--;  // 受击闪烁计时
        // 基础向下移动
        y += 2;
        if (y > Global.HEIGHT) {
            alive = false;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (alive) {
            Image drawImg = (flashTick > 0 && (flashTick % 2 == 0)) ? img2 : img1;
            g.drawImage(drawImg, x, y, null);
        }
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp > 0) {
            flashTick = 6; // 闪烁 6 帧
            AudioPlayer.playEffect("audio\\smoke-bomb-6761.mp3");
        }
        if (hp <= 0 && alive) { // 加一个alive判断防止重复掉落
            alive = false;
            Global.score += getScore(); // 增加分数
            Global.explosions.add(new Explosion(x + w / 2, y + h / 2)); // 增加爆炸效果

            // 20% 概率掉落道具
            if (new Random().nextInt(100) < 20) {
                int powerUpType = new Random().nextInt(3) + 1; // 1, 2, or 3
                PowerUp newItem = new PowerUp(x, y, powerUpType);
                
                // 将道具添加到当前场景的道具列表中
                if (Global.game.getContentPane() instanceof Scene s) {
                    s.getItems().add(newItem);
                }
            }
        }
    }

    public abstract int getScore();
}