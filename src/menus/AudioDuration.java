package menus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioDuration {
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
		
	

		
		//File file = new File("C:\\Users\\gjw10\\eclipse-workspace\\testFlickr\\src\\test\\bo_ku_ga_shi_no_u_to_o_mo_ta_no_ha  .wav");
		File file = new File("long.wav");
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
		AudioFormat format = audioInputStream.getFormat();
		long frames = audioInputStream.getFrameLength();
		int durationInSeconds = (int)((frames+0.0) / format.getFrameRate()); 
		System.out.println(durationInSeconds);
		
		
		
		
		
}
}
