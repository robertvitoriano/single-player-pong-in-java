package myGame;

import jplay.Sound;
import jplay.URL;

public class PlaySound {
	
	public static Sound sound;
	
	public static void play(String audio) {
		stop();
		sound = new Sound(URL.audio(audio));
		PlaySound.sound.play();
		PlaySound.sound.setRepeat(false);
	}
	public static void stop() {
		
		if(PlaySound.sound !=null)
			sound.stop();
		
		
		
	}
	

}
