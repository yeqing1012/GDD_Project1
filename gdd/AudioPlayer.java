// Java program to play an Audio
// file using Clip Object
package gdd;

import javax.sound.sampled.*;
import java.io.File;


public class AudioPlayer {
    /** 测试阶段如需完全关闭声音，将此开关设为 false */
    private static final boolean SOUND_ENABLED = true;
    private static Clip bgClip;

    /** 循环播放背景音乐 */
    public static void playBackground(String path) {
        if (!SOUND_ENABLED) return;
        stopBackground();
        try {
            bgClip = AudioSystem.getClip();
            bgClip.open(AudioSystem.getAudioInputStream(new File(path)));
            bgClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) { e.printStackTrace(); }
    }
    public static void stopBackground() {
        if (!SOUND_ENABLED) return;
        if (bgClip!=null) { bgClip.stop(); bgClip.close(); bgClip=null; }
    }

    /** 播放一次性音效 */
    public static void playEffect(String path) {
        if (!SOUND_ENABLED) return;
        try {
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(new File(path)));
            c.start();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
