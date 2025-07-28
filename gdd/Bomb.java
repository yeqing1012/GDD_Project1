package gdd;

import java.awt.*;
import javax.swing.*;


public class Bomb extends Sprite {
    public Bomb(int x,int y){
        this.x=x; this.y=y; w = 20; h = 20;
        img = new ImageIcon("Project materials\\合集2\\PNG\\Sprites\\Building\\spaceBuilding_004.png").getImage();
    }
    @Override public void update(){
        y+=4;
        if (y>Global.HEIGHT) alive=false;
    }
    @Override public void draw(Graphics g){
        if(!alive) return;
        g.drawImage(img,x,y,null);
        // 调试 hitbox (删除即可)
        /*g.setColor(new Color(255,0,0,80));
        g.fillRect(x, y, w, h);*/
    }
    public void hit() { alive = false; }
}
