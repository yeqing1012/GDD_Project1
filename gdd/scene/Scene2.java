package gdd.scene;

import gdd.*;
import gdd.Sprite.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

/** 第二关：前半刷怪，30 s 后 Boss 登场 */
public class Scene2 extends Scene implements HasShotList, HasPlayer {

    private final Player        player;
    private final List<Enemy>   enemies = new ArrayList<>();
    private final List<Shot>    shots   = new ArrayList<>();
    private final List<PowerUp> items   = new ArrayList<>();

    private final StageLoader   loader;     // stage2.csv（前 30 s 刷怪）
    private       Boss          boss   = null;
    private       int           frame  = 0;
    private       int           scrollY= 0;

    public Scene2(Game game) {
        super(game);
        bg     = new ImageIcon("Project materials\\SNES - Star Fox 2 Prototype - Deep Space - Gray.png").getImage();
        player = new Player(this);
        loader = new StageLoader("gdd/scene/stage2_new.csv", this);

        AudioPlayer.playBackground("audio\\S2.wav");
    }

    private void checkCollisions() {
        // 玩家子弹 vs 敌人
        for (Shot shot : shots) {
            for (Enemy enemy : enemies) {
                if (shot.getBounds().intersects(enemy.getBounds())) {
                    shot.hit();
                    enemy.takeDamage(1);
                }
            }
        }

        // 玩家 vs 敌人
        for (Enemy enemy : enemies) {
            if (player.getBounds().intersects(enemy.getBounds())) {
                player.takeDamage();
                enemy.takeDamage(100);
            }
        }

        // 玩家 vs 敌机炸弹
        for (Bomb bomb : Global.enemyBombs) {
            if (player.getBounds().intersects(bomb.getBounds())) {
                player.takeDamage();
                bomb.hit();
            }
        }

        if (boss != null && boss.isAlive()) {
            // 玩家子弹 vs Boss
            for (Shot shot : shots) {
                if (shot.getBounds().intersects(boss.getBounds())) {
                    shot.hit();
                    boss.takeDamage(1);
                }
            }

            // 玩家 vs Boss
            if (player.getBounds().intersects(boss.getBounds())) {
                player.takeDamage();
            }

            // 玩家 vs Boss子弹
            for (BossShot bossShot : Global.bossShots) {
                if (player.getBounds().intersects(bossShot.getBounds())) {
                    player.takeDamage();
                    bossShot.hit();
                }
            }
        }
    }

    @Override public void update() {
        frame++;
        scrollY = (scrollY + 2) % Global.HEIGHT;

        /* 正常更新 */
        player.update();
        loader.update(enemies, items);
        enemies.forEach(Enemy::update);
        shots  .forEach(Shot ::update);
        items  .forEach(PowerUp::update);
        Global.enemyBombs.forEach(Bomb::update);
        Global.bossShots.forEach(BossShot::update);
        Global.explosions.forEach(Explosion::update);

        // 碰撞检测
        checkCollisions();

        enemies.removeIf(e -> !e.isAlive());
        shots  .removeIf(s -> !s.isAlive());
        items  .removeIf(p -> !p.isAlive());
        Global.enemyBombs.removeIf(b -> !b.isAlive());
        Global.bossShots.removeIf(bs -> !bs.isAlive());
        Global.explosions.removeIf(e -> !e.isAlive());

        /* 30 秒后出现 Boss */
        if (boss == null && frame >= 1800) {
            boss = new Boss(Global.WIDTH / 2 - 96, -150);
            AudioPlayer.stopBackground();
            AudioPlayer.playBackground("audio\\Boss.wav");
        }
        if (boss != null) {
            boss.update();
            if (!boss.isAlive()) {
                AudioPlayer.stopBackground();
                game.switchScene(new GameOverScene(game, true)); // 胜利
            }
        }

        /* 玩家死亡 */
        if (player.isDead()) {
            AudioPlayer.stopBackground();
            game.switchScene(new GameOverScene(game, false));
        }
    }

    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, scrollY - Global.HEIGHT, null);
        g.drawImage(bg, 0, scrollY,                 null);

        player.draw(g);
        enemies.forEach(e -> e.draw(g));
        shots  .forEach(s -> s.draw(g));
        Global.enemyBombs.forEach(b -> b.draw(g));
        Global.bossShots.forEach(bs -> bs.draw(g));
        items  .forEach(p -> p.draw(g));
        Global.explosions.forEach(e -> e.draw(g));
        if (boss != null) boss.draw(g);

        // 绘制 UI
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        g.drawString("Score: " + Global.score, 10, 25);
        g.drawString("Speed: LV " + player.getSpeedLevel(), 10, 50);
        g.drawString("Weapon: LV " + player.getMultiShotLevel(), 10, 75);
    }

    @Override public List<Shot> getShots() { return shots; }
    public List<Enemy>   getEnemies() { return enemies; }
    @Override
    public List<PowerUp> getItems() {
        return items;
    }

     public Player getPlayer() {
        return player;
    }
}
