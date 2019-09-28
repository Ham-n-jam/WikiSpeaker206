package menus;


import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class PicsAndNameMenuController extends Controller {

    @FXML private TextField searchTextArea;
    @FXML protected Text topText;
    @FXML protected Text middleText;
    protected int numImages;
    protected String fileName;
    @FXML protected ProgressIndicator loadingWheel;
    protected Step currentStep; 

    private enum Step {enterPicsNumber, enterDifferentFlickrSearch, enterFileName}

    @FXML
	private void initialize() {
		//Preload things before the scene
		currentStep = PicsAndNameMenuController.Step.enterPicsNumber;
		
		topText.setText(CreateAudioController._audioCount + " audio file(s) will be included in the creation.");
	}
    
    @FXML
    public void handleMainMenuButton() {
        //Back to main menu
        changeScene("/menus/Home.fxml");
    }

    @FXML
    public void handleNewCreationButton() {
        //Back to main menu
        changeScene("/menus/CreateMenu.fxml");
    }

    
    @FXML
    public void handleActionButton() {
    	
    }
    
    
    
    
    
    
    
    
    
    public void flickrDownloadComplete() {
    	loadingWheel.setVisible(false);
    	topText.setText(numImages + " image(s) will be included in the creation.");
    	middleText.setText("Enter a file name for the Creation:");
    	
    }

    private class flickrInBackground extends Task<Void> {
    	private PicsAndNameMenuController controller;
    	
    	public flickrInBackground(PicsAndNameMenuController controller) {
    		this.controller = controller;
    	}
    	
        @Override
        protected Void call() throws Exception {
        	//Download images here
        	//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
        	
        	loadingWheel.setVisible(true);
        	
        	return null;
        }
        
        @Override
        protected void done(){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                	controller.flickrDownloadComplete(); 
                }
            });
        }

    }

}
