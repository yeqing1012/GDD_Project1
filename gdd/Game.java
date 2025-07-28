package gdd;

import javax.swing.*;

import gdd.scene.Scene;
import gdd.scene.TitleScene;

public class Game extends JFrame {
    private gdd.scene.Scene cur;        // ← 引用基类需全限定或 import gdd.Scene

    public Game(){
        setSize(Global.WIDTH,Global.HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        switchScene(new TitleScene(this));

        /* 简易 60fps 循环 */
        new javax.swing.Timer(16,e->{ if(cur!=null){cur.update();cur.repaint();}}).start();
        setVisible(true);
        Global.game=this;
    }

    public void switchScene(gdd.scene.Scene s){
        if(cur!=null) remove(cur);
        cur=s;
        setContentPane(cur);
        revalidate(); repaint();
        cur.requestFocusInWindow();
    }

    public static void main(String[] a){ new Game(); }
}
