package menus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class HomeController extends Controller {
    public Stage _mainStage;
    @FXML private Button createButton;
    @FXML private Button viewButton;

    /**
        Handles what happens when the Create Creation button is pressed
     */
    @FXML
    private void handleCreateButton(){
        //Go to create menu
        changeScene("/menus/CreateMenu.fxml");
    }

    @FXML
    private void handleViewButton(){
        //Go to list menu
        changeScene("/menus/ListMenu.fxml");
    }


}
