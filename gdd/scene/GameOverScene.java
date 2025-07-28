package gdd.scene;

import gdd.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import gdd.Sprite.*;

/** 游戏结束 / 胜利 场景 */
public class GameOverScene extends Scene {

    @Override
    public List<PowerUp> getItems() {
        return new java.util.ArrayList<>(); // 返回一个空列表
    }


    private final boolean win;

    public GameOverScene(Game game, boolean win) {
        super(game);
        this.win = win;

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Global.reset(null);
                    game.switchScene(new Scene1(game));
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    Global.reset(null);
                    game.switchScene(new TitleScene(game));
                }
            }
        });

        String bgm = win ? "audio/victory.wav" : "audio/game_over.wav";
        AudioPlayer.playBackground(bgm);
    }

    @Override public void update() { /* 静态画面，无需逻辑 */ }

    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Global.WIDTH, Global.HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.setColor(win ? Color.CYAN : Color.RED);
        String txt = win ? "YOU WIN!" : "GAME OVER";
        int w = g.getFontMetrics().stringWidth(txt);
        g.drawString(txt, (Global.WIDTH - w) / 2, 260);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        String tip = "ENTER: One more round   ESC: Back to Title";
        int w2 = g.getFontMetrics().stringWidth(tip);
        g.drawString(tip, (Global.WIDTH - w2) / 2, 320);
    }
}

