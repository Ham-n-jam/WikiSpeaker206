package menus;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;



//public class ProduceCreation extends Task<String> {
public class creationCreator {
	/*
	 * @Override protected String call() throws IOException, InterruptedException{
	 * // combine audio files into on file String combineAudioFiles =
	 * "sox *.wav combinedAudio.wav"; ProcessBuilder combineAudioFile = new
	 * ProcessBuilder("bash","-c",combineAudioFiles); combineAudioFile.start();
	 * combineAudioFile.wait();
	 * 
	 * 
	 * 
	 * return null; }
	 */
	//File file = new File("C:\\Users\\gjw10\\eclipse-workspace\\testFlickr\\src\\test\\bo_ku_ga_shi_no_u_to_o_mo_ta_no_ha  .wav");

	public double audioDuration() {
		
		File file = new File("./TempFiles/combinedAudio.wav");
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
			AudioFormat format = audioInputStream.getFormat();
			long frames = audioInputStream.getFrameLength();

			double durationInSeconds = ((frames+0.0) / format.getFrameRate()); 
			

			
			return durationInSeconds;
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 1.0;
		
	}


	public void combineImages(double duration, int number) {
		
		
		
		String combineImageFiles = "ffmpeg -r " + ((double)number/duration)
				+ " -pattern_type glob -i './TempImages/*.jpg' -c:v libx264 -r 30 -pix_fmt yuv420p -vf scale=400:200 './TempFiles/combinedImages.mp4'";
		ProcessBuilder combineImages = new ProcessBuilder("bash", "-c", combineImageFiles);
		WikiSpeaker.doProcess(combineImages);
		

		
	}


	// combine Images to a video file
	// duration is the time length of the combined audio file




	public void combine() {
		// combine Audio and video file to create a mkv file
		String combineAudioVideoFiles = "ffmpeg -i './TempFiles/combinedImages.mp4' -i './TempFiles/combinedAudio.wav' -c copy './TempFiles/combine.mkv'";
		ProcessBuilder combineAudioVideo = new ProcessBuilder("bash", "-c", combineAudioVideoFiles);
		WikiSpeaker.doProcess(combineAudioVideo);

	}

	// need the keyword that shown in the creation
	public void addTerm(String term) {
		
		// add keyword to the video file
		String addKeywordFile = "ffmpeg -i './TempFiles/combine.mkv' -vf drawtext=\"fontfile=./font.ttf: \\\n"
				+ "text='"+ term +"': fontcolor=white: fontsize=24: box=1: boxcolor=black@0.5: \\\n"
				+ "boxborderw=5: x=(w-text_w)/2: y=(h-text_h)/2\" -codec:a copy './TempFiles/final.mkv'";
		ProcessBuilder addKeyword = new ProcessBuilder("bash", "-c", addKeywordFile);
		WikiSpeaker.doProcess(addKeyword);

	}



	public void createFinal(String fileName) {
		// convert mkv file to mp4 file
		String convertToMP4 = "ffmpeg -y -i './TempFiles/final.mkv' -b:v 2000k './Creations/" + fileName + ".mp4'";
		ProcessBuilder conversion = new ProcessBuilder("bash", "-c", convertToMP4);
		WikiSpeaker.doProcess(conversion);

	}

}
