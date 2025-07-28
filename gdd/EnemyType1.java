package gdd;

import javax.swing.*;
import java.awt.*;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

public class EnemyType1 extends Enemy {
    @Override
    public int getScore() {
        return 10;
    }

    private int fireCD = 0;

    public EnemyType1(int x, int y) {
        // 调用正确的父类构造函数，并传入贴图路径
        super(x, y, 2, "images/Enemies/enemyBlack1.png", "images/Enemies/enemyBlack2.png");
    }

    @Override
    public void update() {
        super.update(); // 调用基类的移动和动画逻辑
        fireCD++;
        if (fireCD > 80) { // 每隔80帧发射一次
            fireCD = 0;
            Global.enemyBombs.add(new Bomb(x + w / 2 - 8, y + h));
            AudioPlayer.playEffect("audio\\mixkit-game-whip-shot-1512.wav");
        }
    }
}
