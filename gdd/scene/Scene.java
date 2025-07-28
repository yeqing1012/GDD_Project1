package gdd.scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import gdd.Game;
import gdd.Global;
import gdd.PowerUp;

/**
 * 所有场景基类：负责键盘分发与简单背景绘制
 */
public abstract class Scene extends JPanel {
    public abstract List<PowerUp> getItems();
    protected final Game game;          // 当前窗口
    protected Image bg;                 // 背景图片，可为 null

    public Scene(Game game) {
        this.game = game;
        setFocusable(true);

        /* 把全局按键数组与 KeyListener 绑定 */
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e)  { Global.key[e.getKeyCode()] = true;  }
            public void keyReleased(KeyEvent e) { Global.key[e.getKeyCode()] = false; }
        });
    }

    /** 每帧逻辑 */
    public abstract void update();

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bg != null) g.drawImage(bg,0,0,null);
    }
}
