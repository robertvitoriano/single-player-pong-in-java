package myGame;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer {

  HashMap<String, Clip> musics;

  public MusicPlayer() {
    musics = new HashMap<String, Clip>();
  }

  public void addMusic(String musicPath, String musicName)
      throws MalformedURLException, UnsupportedAudioFileException, IOException {
    File mainMusicFile = new File(musicPath);
    AudioInputStream audioInput = AudioSystem.getAudioInputStream(mainMusicFile.toURI().toURL());
    Clip mainMusicClip;
    try {
      mainMusicClip = AudioSystem.getClip();
      mainMusicClip.open(audioInput);
      musics.put(musicName, mainMusicClip);
    } catch (LineUnavailableException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public void play(String musicName) {
    Clip clip = musics.get(musicName);
    clip.start();
  }

  public void loop(String musicName) {
    Clip clip = musics.get(musicName);
    clip.loop(Clip.LOOP_CONTINUOUSLY);
  }

  public void stop(String musicName) {
    Clip clip = musics.get(musicName);
    clip.stop();
  }

}
