package menus;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class CreateAudioController extends Controller {

	@FXML protected Text creationName;
	@FXML protected Text creatingAudio;
	@FXML private Button previewButton;
	@FXML private Button nextStepButton;
	@FXML protected Button addAudioButton;
	@FXML protected TextArea wikiText;
	@FXML protected ChoiceBox<String> voiceList;
	private String _selectedVoice;
	static protected int _audioCount;
	protected String highlightedText;

	@FXML
	private void initialize() {
		//Preload things before the scene
		CreateAudioController._audioCount = 0; //0 audio files have been made to begin with
		creatingAudio.setVisible(false);

		creationName.setText(CreateMenuController._search);
		wikiText.setText(CreateMenuController._lines);


		List<String> voices = getVoiceList();
		voiceList.getItems().setAll(voices); //fill in the ChoiceBox
		voiceList.getSelectionModel().clearAndSelect(0);

		_selectedVoice = voiceList.getSelectionModel().getSelectedItem();
	}

	private List<String> getVoiceList() {			
		String cmd = "echo \"(voice.list)\" | festival -i | grep \"festival> (\"";
		ProcessBuilder getVoices = new ProcessBuilder("bash", "-c", cmd);
		String process = WikiSpeaker.doProcess(getVoices);

		//process String will only have names of voices, separated by spaces
		process = process.substring(11, process.length() - 2); 
		List<String> list = Arrays.asList(process.split(" "));

		return list;	
	}

	private int wc(String input) {
		if (input == null || input.isEmpty()) {
			return 0;
		}

		String[] words = input.split("\\s+");
		return words.length;
	}

	@FXML
	public void handleMainMenuButton() {
		//Back to main menu
		changeScene("/menus/Home.fxml");
	}

	@FXML
	public void handlePreviewButton() {
		creatingAudio.setVisible(true);

		//Get highlighted text and play it using the selected festival voice
		_selectedVoice = voiceList.getSelectionModel().getSelectedItem();
		highlightedText = wikiText.getSelectedText();
		int numWords = wc(highlightedText);
		//Show error if the text is too long
		if(numWords > 40) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Too many words");
			alert.setHeaderText(null);
			alert.setContentText("Please highlight a maximum of 40 words.");
			alert.showAndWait();
		}else if(numWords < 1) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("No words highlighted");
			alert.setHeaderText(null);
			alert.setContentText("Please highlight words before continuing.");
			alert.showAndWait();
		}else {
			Thread thread = new Thread(new CreateAudioInBackground(this));
			thread.start();
		}
	}



	@FXML
	public void handleAddAudioButton() {
		creatingAudio.setVisible(true);

		_selectedVoice = voiceList.getSelectionModel().getSelectedItem();
		highlightedText = wikiText.getSelectedText();
		int numWords = wc(highlightedText);

		if(numWords > 40) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Too many words");
			alert.setHeaderText(null);
			alert.setContentText("Please highlight a maximum of 40 words.");
			alert.showAndWait();
		}else if(numWords < 1) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("No words highlighted");
			alert.setHeaderText(null);
			alert.setContentText("Please highlight words before continuing.");
			alert.showAndWait();
		}else {
			//Get highlighted text and save it using the selected festival voice
			addAudioButton.setDisable(true);
			Thread thread = new Thread(new SaveAudioInBackground(this));
			thread.start();

		}
	}



	private class CreateAudioInBackground extends Task<Void> {

		boolean error;

		private CreateAudioController controller;

		public CreateAudioInBackground(CreateAudioController controller) {
			this.controller = controller;
		}


		@Override
		protected Void call() throws Exception {
			error = false;
			//Make MP3 file
			controller.creatingAudio.setText("Previewing audio...");

			String cmd = "echo \"(voice_" + _selectedVoice + ") (SayText \\\"" + highlightedText + "\\\")\" | festival 2>&1";
			ProcessBuilder previewAudio = new ProcessBuilder("bash", "-c", cmd);
			String process = WikiSpeaker.doProcess(previewAudio);

			//Show error if the text cannot be read
			if(process.contains("SIOD ERROR")) {
				error = true;
			}
			return null;
		}

		@Override
		protected void done(){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if(error) {
						controller.creatingAudio.setText("Playback error.");
						controller.cannotPlayTextError();

					}else {
						controller.creatingAudio.setText("Playback finished.");
					}
				}
			});
		}

	}




	private class SaveAudioInBackground extends Task<Void> {

		boolean error;

		private CreateAudioController controller;

		public SaveAudioInBackground(CreateAudioController controller) {
			this.controller = controller;
		}


		@Override
		protected Void call() throws Exception {
			error = false;
			//Make MP3 file
			controller.creatingAudio.setText("Creating audio...");

			String cmd = "echo \"(voice_" + _selectedVoice + ") (utt.save.wave (SayText \\\"" + highlightedText + "\\\") \\\"./TempFiles/tempAudio" + CreateAudioController._audioCount + ".wav\\\" 'riff)\" | festival 2>&1";
			ProcessBuilder previewAudio = new ProcessBuilder("bash", "-c", cmd);
			String process = WikiSpeaker.doProcess(previewAudio);

			//Show error if the text cannot be read
			if(process.contains("SIOD ERROR")) {
				error = true;
			}else {
				CreateAudioController._audioCount++;
			}
			return null;
		}

		@Override
		protected void done(){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					controller.addAudioButton.setDisable(false);
					if(error) {
						controller.creatingAudio.setText("Audio file not created.");
						controller.cannotPlayTextError();

					}else {
						controller.creatingAudio.setText("Audio file " + CreateAudioController._audioCount + " created!");
						nextStepButton.setDisable(false);
					}

				}
			});
		}

	}


	private class CombineAudioInBackground extends Task<Void> {

		private CreateAudioController controller;

		public CombineAudioInBackground(CreateAudioController controller) {
			this.controller = controller;
		}


		@Override
		protected Void call() throws Exception {


			String combineAudioFiles = "sox './TempFiles/*.wav' './TempFiles/combinedAudio.wav'";
			ProcessBuilder combineAudio = new ProcessBuilder("bash", "-c", combineAudioFiles);
			try {
				combineAudio.start();
				// combineAudio.wait();
			} catch (IOException e) {
				e.printStackTrace();
			}



			return null;
		}

		@Override
		protected void done(){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					//Go to the next step
					changeScene("/menus/PicsAndNameMenu.fxml");

				}
			});
		}

	}	




	public void cannotPlayTextError() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Festival error");
		alert.setHeaderText(null);
		alert.setContentText("The highlighted text may not be able to be read by the selected voice.");
		alert.showAndWait();
	}


	@FXML
	public void handleNextStepButton() {
		creatingAudio.setText("Combining audio files...");
		Thread thread = new Thread(new CombineAudioInBackground(this));
		thread.start();
	}

}
