package menus;

import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class VideoPlayerController extends Controller{
	
	
    @FXML
    private Text text;

    @FXML
    private Button creationListButton;

    @FXML
    private Button muteButton;

    @FXML
    private Button playPauseButton;

    @FXML
    private Button backButton;

    @FXML
    private Button fowardButton;

    @FXML
    private MediaView mv;

    @FXML
    private Button timer;
	

	
	private File fileUrl;
	
	private Media video;
	
	private MediaPlayer player;

	
	
	@FXML
    private void initialize() {
        //Preload things before the scene
		fileUrl = new File("Creations/"+ListMenuController._selection); 
		//fileUrl = new File("aaa.mp4");
		video = new Media(fileUrl.toURI().toString());
		player = new MediaPlayer(video);
		player.setAutoPlay(true);
		mv.setMediaPlayer(player);
		handlePlayPauseButton();
		handlePlayPauseButton();
		
    }
		

	
	public void handleCreationListButton() {
		 changeScene("/menus/ListMenu.fxml");
	}

	public void handleMuteButton() {
		player.setMute( !player.isMute() );
	}
	
	
	public void handleBackButton() {
		player.seek( player.getCurrentTime().add( Duration.seconds(-3)) );
	}
	
	
	public void handleFowardButton() {
		player.seek( player.getCurrentTime().add( Duration.seconds(3)) );

	}
	
	public void handlePlayPauseButton() {
		if (player.getStatus() == Status.PLAYING) {
			player.pause();
		} else {
			player.play();
		}
	
	
	
	player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
		@Override
		public void changed(ObservableValue<? extends Duration> observable, Duration oldValue,
				Duration newValue) {
//			System.out.println(newValue);
			String time = "";
			time += String.format("%02d", (int)newValue.toMinutes());
			time += ":";
			time += String.format("%02d", (int)newValue.toSeconds());
			timer.setText(time);
		}
	});
}
	

	
}
