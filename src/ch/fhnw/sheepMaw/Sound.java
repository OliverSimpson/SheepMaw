package ch.fhnw.sheepMaw;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	Clip clip;
	Boolean state = true;
	AudioInputStream ais;

	public Sound() throws Exception {
		/*
		 * // open the sound file as a Java input stream //String gongFile =
		 * "/ScotchEgg.wav"; String gongFile =
		 * "/Users/Oliver/Desktop/Motions (Go Starfish Remix).wav"; // open the
		 * sound file as a Java input stream
		 * 
		 * InputStream in = new FileInputStream(gongFile);
		 * 
		 * // create an audiostream from the inputstream AudioStream audioStream
		 * = new AudioStream(in);
		 * 
		 * // play the audio clip with the audioplayer class
		 * AudioPlayer.player.start(audioStream);
		 */

		String f2 = "ch/fhnw/sheepMaw/sounds/ScotchEgg.wav";

		clip = AudioSystem.getClip();

		ais = AudioSystem.getAudioInputStream(getClass().getClassLoader()
				.getResource(f2));

		clip.open(ais);

		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void play_stop(Boolean state) {
		this.state = state;
		if (state == true) {
			clip.start();
			state = false;
		} else if (state == false) {
			clip.stop();
			state = true;
		}

	}

}