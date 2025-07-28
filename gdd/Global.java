package gdd;

import java.util.*;

public class Global {
    public static Game game;
    public static final int WIDTH = 480, HEIGHT = 600;
    public static final boolean[] key=new boolean[256];
    public static final List<Bomb> enemyBombs=new ArrayList<>();
    public static final List<BossShot> bossShots = new ArrayList<>();
    public static final List<Explosion> explosions = new ArrayList<>();
    public static int score = 0;

    public static void reset(Player player){
        enemyBombs.clear();
        bossShots.clear();
        explosions.clear();
    if (player != null) player.resetStats();  // 仅归零玩家速度/武器等级
    }
}
