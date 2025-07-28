package gdd.scene;

import gdd.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/** 标题页，可返回空子弹表满足接口 */
public class TitleScene extends Scene implements HasShotList {

    @Override
    public List<PowerUp> getItems() {
        return new java.util.ArrayList<>(); // 返回一个空列表
    }

    private final List<Shot> dummy = new ArrayList<>();
    private final Image bg = new ImageIcon("Project materials\\Sample.jpg").getImage();
    private int blinkCounter = 0;

    public TitleScene(Game game) {
        super(game);
        AudioPlayer.playBackground("audio\\Title (2).wav");

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    AudioPlayer.stopBackground();
                    game.switchScene(new Scene1(game));
                }
            }
        });
    }

    @Override
    public void update() {
        blinkCounter++;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, null);

        // 游戏标题
        g.setFont(new Font("Arial", Font.BOLD, 54));
        g.setColor(Color.YELLOW);
        String title = "Vertiacal Shooter";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (Global.WIDTH - titleWidth) / 2, 150);

        // 开始提示 (闪烁)
        if ((blinkCounter / 30) % 2 == 0) {
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.setColor(Color.WHITE);
            String startText = "Press ENTER to Start";
            int startTextWidth = g.getFontMetrics().stringWidth(startText);
            g.drawString(startText, (Global.WIDTH - startTextWidth) / 2, 300);
        }

        // 开发人员名单
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.LIGHT_GRAY);
        String credits = "Created by: Haoang Zhao,Yeqing Chen";
        int creditsWidth = g.getFontMetrics().stringWidth(credits);
        g.drawString(credits, (Global.WIDTH - creditsWidth) / 2, 550);
    }

    /* 实现接口 —— 标题页没有子弹 */
    @Override
    public List<Shot> getShots() {
        return dummy;
    }
}