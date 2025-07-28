package gdd;

import java.awt.*;
import javax.swing.*;

import gdd.scene.HasShotList;

import java.awt.event.KeyEvent;
import java.util.List;

public class Player extends Sprite {
    private final HasShotList scene;
    private int multiShotLevel = 1, shotCD = 0, speedBoostTimer = 0;
    private int speedLevel = 1;         // 移动速度等级
    private int baseShotCooldown = 15; // 基础射击间隔
    private boolean shieldActive = false;
    private final Image shieldImage;
    private final Image[] animFrames = new Image[2]; // 固定两帧为同一张，避免闪烁
    private int animTick = 0;
    private int hp = 3; // 新增生命值

    public Player(HasShotList sc) {
        scene = sc;
        x = 368; y = 500;
        w = 48; h = 48;
        // 玩家贴图现在也使用双帧动画
        Image shipImg = new ImageIcon("images/Ships/spaceShips_005.png").getImage();
        animFrames[0] = shipImg;
        animFrames[1] = shipImg;
        shieldImage = new ImageIcon("images/Effects/shield2.png").getImage();
    }

    @Override
    public void update() {
        // 计时器处理
        if (speedBoostTimer > 0) {
            speedBoostTimer--;
        }

        // 移动
        int currentSpeed = 4 + (speedLevel - 1) * 2;
        if (Global.key[KeyEvent.VK_LEFT]) x = Math.max(0, x - currentSpeed);
        if (Global.key[KeyEvent.VK_RIGHT]) x = Math.min(Global.WIDTH - w, x + currentSpeed);
        if (Global.key[KeyEvent.VK_UP]) y = Math.max(0, y - currentSpeed);
        if (Global.key[KeyEvent.VK_DOWN]) y = Math.min(Global.HEIGHT - h, y + currentSpeed);

        // 自动开火
        shotCD++;
        int currentShotCooldown = (speedBoostTimer > 0) ? baseShotCooldown / 2 : baseShotCooldown;
        if (shotCD > currentShotCooldown) {
            shotCD = 0;
            AudioPlayer.playEffect("audio\\mixkit-short-laser-gun-shot-1670.wav");
            List<Shot> list = scene.getShots();
            if (multiShotLevel == 1) {
                list.add(new Shot(x + w / 2 - 4, y));
            } else if (multiShotLevel == 2) {
                list.add(new Shot(x, y));
                list.add(new Shot(x + w - 8, y));
            } else {
                list.add(new Shot(x + w / 2 - 4, y));
                list.add(new Shot(x, y, -1));
                list.add(new Shot(x + w - 8, y, 1));
            }
        }
        animTick++;
    }

    @Override
    public void draw(Graphics g) {
        if (alive) {
            g.drawImage(animFrames[0], x, y, w, h, null);
            if (shieldActive) {
                // 绘制护盾效果
                g.drawImage(shieldImage, x - 8, y - 8, w + 16, h + 16, null);
            }
        }
    }

    public void takeDamage() {
        if (shieldActive) {
            shieldActive = false; // 消耗护盾
            AudioPlayer.playEffect("audio\\mixkit-shuffling-gear-mech-item-3152.wav");
            return;
        }
        hp--;
        System.out.println("[Player] takeDamage, hp=" + hp);
        if (hp <= 0) {
            alive = false;
            System.out.println("[Player] DEAD");
        }
        Global.explosions.add(new Explosion(x + w / 2, y + h / 2));
    }

    public void upgradeShot() { if (multiShotLevel < 3) multiShotLevel++; }
    public void activateShield() { shieldActive = true; }
    public void activateSpeedBoost() {
        speedBoostTimer = 420; // 7秒 (7 * 60fps)
    }

    public void upgradeMoveSpeed() {
        if (speedLevel < 4) {
            speedLevel++;
        }
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public int getMultiShotLevel() {
        return multiShotLevel;
    }


    public void resetStats() {
        multiShotLevel = 1;
        speedLevel = 1;
    }

    public boolean isDead() { return !alive; }
}
