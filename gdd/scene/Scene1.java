package gdd.scene;

import gdd.*;
import gdd.Sprite.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

/** 第一关：40 s 左右普通敌人 + 道具 */
public class Scene1 extends Scene implements HasShotList, HasPlayer {

    private final Player          player;
    private final List<Enemy>     enemies = new ArrayList<>();
    private final List<Shot>      shots   = new ArrayList<>();
    private final List<PowerUp>   items   = new ArrayList<>();
    private final StageLoader     loader;     // 读取 stage1.csv
    private       int             scrollY = 0;

    public Scene1(Game game) {
        super(game);
        bg      = new ImageIcon("images\\Background\\背景1.png").getImage();
        player  = new Player(this);
        loader  = new StageLoader("gdd/scene/stage1_new.csv", this);

        // 初始背景音乐
        AudioPlayer.playBackground("audio\\S1.wav");
    }

    /*=========================== 每帧逻辑 ===========================*/
    @Override public void update() {
        // 背景滚动
        int bgH = bg.getHeight(null);
        scrollY = (scrollY + 2) % bgH;

        /* 玩家 & 敌人/道具 更新 */
        player.update();
        loader.update(enemies, items);

        enemies.forEach(Enemy::update);
        shots  .forEach(Shot ::update);
        items  .forEach(PowerUp::update);
        Global.enemyBombs.forEach(Bomb::update);
        Global.explosions.forEach(Explosion::update);

        // 碰撞检测
        checkCollisions();

        // 清理死亡对象
        enemies.removeIf(e -> !e.isAlive());
        shots  .removeIf(s -> !s.isAlive());
        items  .removeIf(p -> !p.isAlive());
        Global.enemyBombs.removeIf(b -> !b.isAlive());
        Global.explosions.removeIf(e -> !e.isAlive());

        /* 简单通关条件：关卡脚本执行完毕且场上无敌人 */
        if (loader.isEmpty() && enemies.isEmpty()) {
            Global.reset(player); // 重置玩家状态
            game.switchScene(new Scene2(game)); // 进入第二关
        }

        /* 玩家死亡 */
        if (player.isDead()) {
            AudioPlayer.stopBackground();
            game.switchScene(new GameOverScene(game, false));
        }
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
                System.out.println("[Collision] player hit by enemy");
                player.takeDamage();
                enemy.takeDamage(100); // 撞上玩家的敌机直接死亡
            }
        }

        // 玩家 vs 敌机炸弹
        for (Bomb bomb : Global.enemyBombs) {
            if (player.getBounds().intersects(bomb.getBounds())) {
                System.out.println("[Collision] player hit by bomb");
                player.takeDamage();
                bomb.hit();
            }
        }
    }

    /*=========================== 绘图 ===========================*/
    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 绘制滚动背景
        int bgH = bg.getHeight(null);
        g.drawImage(bg, 0, scrollY - bgH, null);
        g.drawImage(bg, 0, scrollY, null);

        // 绘制所有单位
        player.draw(g);
        enemies.forEach(e -> e.draw(g));
        shots.forEach(s -> s.draw(g));
        Global.enemyBombs.forEach(b -> b.draw(g));
        items.forEach(p -> p.draw(g));
        Global.explosions.forEach(e -> e.draw(g));

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

    /*=========================== 接口 ===========================*/
    @Override public List<Shot> getShots() { return shots; }
    /* 供 StageLoader 调用 */
    public List<Enemy>   getEnemies() { return enemies; }
    @Override
    public List<PowerUp> getItems() {
        return items;
    }

     public Player getPlayer() {
        return player;
    }
}
