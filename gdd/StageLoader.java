package gdd;

import java.io.*;
import java.util.*;

import gdd.scene.Scene;

public class StageLoader {
    private final List<StageEvent> events = new ArrayList<>();
    private int tick = 0;
    private final Scene scene;

    public StageLoader(String csv, Scene sc) {
        scene = sc;
        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String line;
            String header = br.readLine(); // 读取标题行，判断格式
            boolean newFormat = header != null && header.toLowerCase().startsWith("type");
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // 跳过空行
                String[] s = line.split(",");
                try {
                    if (!newFormat) {
                        // 旧格式：frame,entity_type,subtype,x_pos
                        events.add(new StageEvent(Integer.parseInt(s[0].trim()),
                                s[1].trim().toUpperCase(),
                                s[2].trim().toUpperCase(),
                                Integer.parseInt(s[3].trim())));
                    } else {
                        // 新格式：type,x,y,time,data
                        String entityType = s[0].trim().toUpperCase();
                        int x = Integer.parseInt(s[1].trim());
                        int frame = Integer.parseInt(s[3].trim());
                        String subtype = (s.length > 4 && !s[4].trim().isEmpty()) ? s[4].trim().toUpperCase() : "1"; // 缺省敌机类型设为1
                        events.add(new StageEvent(frame, entityType, subtype, x));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("[StageLoader] Invalid line: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 每帧调用：到时就生成实体
     */
    public void update(List<Enemy> es, List<PowerUp> ps) {
        tick++;
        Iterator<StageEvent> it = events.iterator();
        while (it.hasNext()) {
            StageEvent ev = it.next();
            if (ev.frame <= tick) {
                switch (ev.entityType) {
                    case "ENEMY":
                        switch (ev.subtype) {
                            case "1":
                                System.out.println("[StageLoader] spawn Enemy1 at x=" + ev.x + " frame=" + tick);
                                es.add(new EnemyType1(ev.x, -40));
                                break;
                            case "2":
                                es.add(new EnemyType2(ev.x, -40));
                                break;
                        }
                        break;
                    case "POWERUP":
                        int powerUpType;
                        switch (ev.subtype) {
                            case "SHIELD":
                                powerUpType = PowerUp.SHIELD;
                                break;
                            case "SPEED_BOOST":
                                powerUpType = PowerUp.SPEED_BOOST;
                                break;
                            case "MOVE_SPEED":
                                powerUpType = PowerUp.MOVE_SPEED;
                                break;
                            default: // 默认是多重射击
                                powerUpType = PowerUp.MULTI;
                                break;
                        }
                        System.out.println("[StageLoader] spawn PowerUp type="+powerUpType+" x="+ev.x+" frame="+tick);
                        ps.add(new PowerUp(ev.x, -40, powerUpType));
                        break;
                    case "BOSS":
                        // 未来添加Boss
                        break;
                }
                it.remove();
            }
        }
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }
}
