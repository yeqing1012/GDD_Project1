package gdd.scene;

import gdd.Shot;
import java.util.List;

/** 让场景把子弹数组暴露给 Player */
public interface HasShotList {
    List<Shot> getShots();
}