package menus;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class CreateMenuController extends Controller {

    @FXML protected Text headerText;
    @FXML private Button mainMenuButton;
    @FXML private TextField searchTextArea;
    static protected String _search;
    static protected String _lines;
 

    @FXML
    public void handleMainMenuButton() {
        //Back to main menu
        changeScene("/menus/Home.fxml");
    }

    /**
     This method handles the main textArea and uses the entered text in different ways when the enter key is hit
     depending on what the current _step value is.
     */
    @FXML
    public void handleSearchAreaText() {
        searchTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                //Save the search term when the enter key is hit
                if (keyEvent.getCode() == KeyCode.ENTER) {

                        //Check for valid input here
                        if (!((searchTextArea.getText() == "\\n") || (searchTextArea.getText() == ""))) {
                            String searchTerm = searchTextArea.getText();
                            searchTerm = searchTerm.replaceAll("[\\n\\t]", ""); //Remove tabs and newlines
                            searchTextArea.setText(searchTerm); //remove the newline made from hitting enter from display

                            //Search the wiki for the entered text using wikit
                            headerText.setText("Searching " + searchTerm + "...");
                            wikitSearch(searchTerm);

                        }
                        searchTextArea.setText("");
                    }
              
            }
        });
    }


    private void wikitSearch(String search){
        CreateMenuController._search = search;        
        Thread thread = new Thread(new wikitInBackground(this));
    	thread.start();
    }
    
    public void wikitSearchComplete() {
        CreateMenuController._lines = CreateMenuController._lines.replace(". ", ".\n");
    	

        if (CreateMenuController._lines.contains("not found :^(")){
            headerText.setText("No results found for " + _search + ". Try another search:");
        }else{
        	CreateMenuController._lines = CreateMenuController._lines.trim();
        	changeScene("/menus/CreateAudio.fxml");
           
            //Save lines to file
        /*  try {
			new ProcessBuilder("echo", lines).redirectOutput(new File("./TempFiles/temp.txt")).start();
           } catch (IOException e) {}
		*/

        }
    }
    
    
    private class wikitInBackground extends Task<Void> {
    	private CreateMenuController controller;
    	
    	public wikitInBackground(CreateMenuController controller) {
    		this.controller = controller;
    	}
    	
        @Override
        protected Void call() throws Exception {
        	String searchWiki = "wikit " + _search;
            ProcessBuilder wikit = new ProcessBuilder("bash", "-c", searchWiki);
        	CreateMenuController._lines = WikiSpeaker.doProcess(wikit);
        	return null;
        }
        
        @Override
        protected void done(){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                	controller.wikitSearchComplete(); 
                }
            });
        }

    }

}
