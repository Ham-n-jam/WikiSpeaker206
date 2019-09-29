package menus;


import java.io.File;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class PicsAndNameMenuController extends Controller {

    @FXML private TextField searchTextArea;
    @FXML protected Text topText;
    @FXML protected Text middleText;
    @FXML protected Button overwriteButton;
    @FXML protected Button actionButton;
    @FXML protected Button newCreationButton;
    protected int numImages;
    protected String fileName;
    @FXML protected ProgressIndicator loadingWheel;
    protected Step currentStep; 
    protected int audioDuration;

    private enum Step {enterPicsNumber, waitForDownload, enterDifferentFlickrSearch, enterFileName, creatingCreation, done}

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
    	//Action button has different functionality depending on the currentStep variable
    	
    	if(currentStep.equals(PicsAndNameMenuController.Step.enterPicsNumber)) {
    		numImages = Integer.parseInt(searchTextArea.getText());
    		//Check the number is valid
    		if((numImages < 1)||(numImages > 10)) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setTitle("Invalid number");
    			alert.setHeaderText(null);
    			alert.setContentText("Please enter a number between 1 and 10.");
    			alert.showAndWait();
    		}else {	
	    		currentStep = PicsAndNameMenuController.Step.waitForDownload;
	    		//Start downloading pics from flickr
	    		Thread thread = new Thread(new flickrInBackground(this));
		        thread.start();
    		}
	        
	        
	        
    	}else if(currentStep.equals(PicsAndNameMenuController.Step.enterFileName)) {
    		fileName = searchTextArea.getText();
    		//Check if a creation by that name already exists
        	boolean check = new File("./Creations/" + fileName + ".mp4").exists();
            
            if(check){
                topText.setText( fileName + " is already in use. Please type a new one, or click overwrite:");
                overwriteButton.setVisible(true);
                overwriteButton.setDisable(false);
            }else{
                //Start making the creation;
                handleOverwriteButton();//Disable overwrite button and start making creation
            }    		
    	}else {
    	}
    	
    	
    }
    
    
    
    
    
    
    @FXML
    public void handleOverwriteButton(){
        //delete any existing Creation that uses the same fileName
    	
    	String file = "./Creations/" + fileName + ".mp4";
    	ProcessBuilder deleteCreation = new ProcessBuilder("bash", "-c", "rm -f " + file);
    	WikiSpeaker.doProcess(deleteCreation);


        currentStep = PicsAndNameMenuController.Step.creatingCreation;
        overwriteButton.setVisible(false);
        overwriteButton.setDisable(true);

        //Start creating Creation
        topText.setText("Creating Creation...");
        

        Thread thread = new Thread(new CreateCreationInBackground(this));
        thread.start();
    }
    
    public void flickrDownloadComplete() {
    	loadingWheel.setVisible(false);
    	topText.setText(numImages + " image(s) will be included in the creation.");
    	middleText.setText("Enter a file name for the Creation:");
		currentStep = PicsAndNameMenuController.Step.enterFileName;	
    }
    
    public void finishedMakingCreation() {
    	topText.setText("Creation created!");
    	middleText.setText("");
    	newCreationButton.setVisible(true);
    	newCreationButton.setDisable(false);
    	actionButton.setDisable(true);
    	searchTextArea.setDisable(true);
    }
    

    private class flickrInBackground extends Task<Void> {
    	private PicsAndNameMenuController controller;
    	
    	public flickrInBackground(PicsAndNameMenuController controller) {
    		this.controller = controller;
    	}
    	
        @Override
        protected Void call() throws Exception {
        	//Download images here
        	loadingWheel.setVisible(true);
        	Flickr_Download downloader = new Flickr_Download();
        	downloader.download(CreateMenuController._search, numImages);
        	return null;
        }
        
        @Override
        protected void done(){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                	//Check images are actually in TempImages folder AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                	controller.flickrDownloadComplete(); 
                }
            });
        }

    }
    
    
    
    private class CreateCreationInBackground extends Task<Void> {
    	
    	private PicsAndNameMenuController controller;
    	
    	public CreateCreationInBackground(PicsAndNameMenuController controller) {
    		this.controller = controller;
    	}
    	
    	
    	@Override
		protected Void call() throws Exception {
    		//Make MP3 file
        	//controller.topText.setText("Combining images...");
        	//controller.middleText.setText("");
        	creationCreator creator = new creationCreator();
         	
        	
        	creator.combineImages(creator.audioDuration(), numImages);
        	//controller.topText.setText("Combining audio and video...");
        	creator.combine();
        	
        	
        	//controller.topText.setText("Adding subtitle...");
        	creator.addTerm(CreateMenuController._search);
        	
        	//controller.topText.setText("Finalising Creation...");
        	creator.createFinal(fileName);
			
        	return null;
    	}
    	
    	@Override
        protected void done(){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                	controller.finishedMakingCreation(); 
                }
            });
    	}
    	
    }
    
    

}
