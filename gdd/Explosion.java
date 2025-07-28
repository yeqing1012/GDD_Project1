package gdd;

import javax.swing.*;
import java.awt.*;

public class Explosion extends Sprite {
    private static final Image IMG = new ImageIcon("Project materials\\合集2\\PNG\\Sprites\\Effects\\spaceEffects_015.png").getImage();
    private static final int FRAME_COUNT = 16; // 假设爆炸动画有16帧
    private static final int FRAME_WIDTH = 64; // 假设每一帧的宽度是64像素
    private static final int FRAME_HEIGHT = 64; // 假设每一帧的高度是64像素
    private static final int ANIMATION_SPEED = 2; // 每2个游戏帧切换一帧动画

    private int animTick = 0;

    public Explosion(int x, int y) {
        this.x = x - FRAME_WIDTH / 2; // 让爆炸中心对准目标
        this.y = y - FRAME_HEIGHT / 2;
        this.w = FRAME_WIDTH;
        this.h = FRAME_HEIGHT;
    }

    @Override
    public void update() {
        animTick++;
        if (animTick >= FRAME_COUNT * ANIMATION_SPEED) {
            alive = false; // 动画播放完毕
        }
    }

    @Override
    public void draw(Graphics g) {
        if (alive) {
            int frameIndex = animTick / ANIMATION_SPEED;
            int sx = (frameIndex % 4) * FRAME_WIDTH; // 计算当前帧在雪碧图中的x坐标 (假设是4x4)
            int sy = (frameIndex / 4) * FRAME_HEIGHT; // 计算当前帧在雪碧图中的y坐标

            g.drawImage(IMG, x, y, x + w, y + h, sx, sy, sx + FRAME_WIDTH, sy + FRAME_HEIGHT, null);
        }
    }
}
