package menus;

import java.io.File;
import java.io.IOException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

//public class ProduceCreation extends Task<String> {
public class test {
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
	
	

	public void combineImages(int duration, int number) {
		String combineImageFiles = "ffmpeg -r " + (1/(duration / number))
				+ " -pattern_type glob -i '*.jpg' -c:v libx264 -r 30 -pix_fmt yuv420p -vf scale=400:200 combinedImages.mp4";
		ProcessBuilder combineImages = new ProcessBuilder("bash", "-c", combineImageFiles);
		try {
			combineImages.start();
			// combineImages.wait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// combine audio files into on file
	public void combineAudios() {
		String combineAudioFiles = "sox *.wav combinedAudio.wav";
		ProcessBuilder combineAudio = new ProcessBuilder("bash", "-c", combineAudioFiles);
		try {
			combineAudio.start();
			// combineAudio.wait();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// combine Images to a video file
	// duration is the time length of the combined audio file




		public void combine() {
		// combine Audio and video file to create a mkv file
		String combineAudioVideoFiles = "ffmpeg -i combinedImages.mp4 -i combinedAudio.wav -c copy combine.mkv";
		ProcessBuilder combineAudioVideo = new ProcessBuilder("bash", "-c", combineAudioVideoFiles);
		try {
			combineAudioVideo.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// p2.waitFor();
		}
		
		// need the keyword that shown in the creation
		public void addTerm(String term) {
			
			// add keyword to the video file
			String addKeywordFile = "ffmpeg -i combine.mkv -vf drawtext=\"fontfile=/path/to/font.ttf: \\\n"
					+ "text='" + term + "': fontcolor=white: fontsize=24: box=1: boxcolor=black@0.5: \\\n"
					+ "boxborderw=5: x=(w-text_w)/2: y=(h-text_h)/2\" -codec:a copy final.mkv\n" + "";
			ProcessBuilder addKeyword = new ProcessBuilder("bash", "-c", addKeywordFile);
			try {
				addKeyword.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		public void createFinal() {
		// convert mkv file to mp4 file
		String convertToMP4 = "ffmpeg -y -i final.mkv -b:v 2000k final.mp4";
		ProcessBuilder conversion = new ProcessBuilder("bash", "-c", convertToMP4);
		try {
			conversion.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// p3.waitFor();
		}

	

	
	 public static void main(String[] args) throws InterruptedException { 
		/*Flickr_Download f = new Flickr_Download();
		 f.download("apple", 3);*/
		 
	 test a = new test();
		 a.combineAudios(); 
		 
		 
		 Thread.sleep(3000);
		 System.out.println("1");
		 
		 
		 
		 a.combineImages(19, 5); 
		 Thread.sleep(3000);
		 System.out.println("2");
		 
		 
		 a.combine();
	
	 Thread.sleep(3000);
	 System.out.println("3");
	 
	 
	  a.addTerm("hello"); 
	 Thread.sleep(3000);
	 System.out.println("4");
	 
	 
	 a.createFinal();
	 
	 System.out.println("Done");
	 
	 
	/* File fileUrl = new File("final.mp4");
	 
	 Media video = new Media(fileUrl.toURI().toString());
		MediaPlayer player = new MediaPlayer(video);
		player.setAutoPlay(true);
		MediaView mediaView = new MediaView(player);
	 */
	 
	 }
	 
}
