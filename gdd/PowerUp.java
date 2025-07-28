package gdd;

import gdd.scene.HasPlayer;
import gdd.scene.Scene1;
import gdd.scene.Scene2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import gdd.scene.HasShotList;
import gdd.Player;

/** 道具：实现多重子弹、护盾、速射三种 */
public class PowerUp extends Sprite {

    public static final int MULTI = 1;
    public static final int SHIELD = 2;
    public static final int SPEED_BOOST = 3;
    public static final int MOVE_SPEED = 4;

    private final int type;

    public PowerUp(int x, int y, int type) {
        this.x = x; this.y = y; w = 32; h = 32;
        this.type = type;
        switch (type) {
            case MULTI:
                img = new ImageIcon("images/Power-ups/powerupYellow_bolt.png").getImage();
                break;
            case SHIELD:
                img = new ImageIcon("images/Power-ups/powerupBlue_shield.png").getImage();
                break;
            case SPEED_BOOST:
                img = new ImageIcon("images/Power-ups/powerupRed_bolt.png").getImage();
                break;
            case MOVE_SPEED:
                img = new ImageIcon("images/Power-ups/powerupGreen_star.png").getImage();
                break;
        }
    }

    @Override
    public void update() {
        y += 2;
        if (y > Global.HEIGHT) alive = false;

        /* 取当前场景的 Player */
        Player p = null;
        if (Global.game.getContentPane() instanceof HasPlayer) {
            p = ((HasPlayer) Global.game.getContentPane()).getPlayer();
        }
        if (p == null) return;

        if (getBounds().intersects(p.getBounds())) {
            switch (type) {
                case MULTI:
                    p.upgradeShot();
                    break;
                case SHIELD:
                    p.activateShield();
                    break;
                case SPEED_BOOST:
                    p.activateSpeedBoost();
                    break;
                case MOVE_SPEED:
                    p.upgradeMoveSpeed();
                    break;
            }
            alive = false;
            AudioPlayer.playEffect("audio\\mixkit-robot-positive-item-acquired-3205.wav");
        }
    }

    @Override
    public void draw(Graphics g) {
        if (alive) g.drawImage(img, x, y, null);
    }
}
