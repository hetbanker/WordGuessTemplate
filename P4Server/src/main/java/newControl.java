import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class newControl{
    @FXML
    private TextField gameStatus = new TextField();
    @FXML
    private ListView<String> playerStatus = new ListView<String>();
	Server serverConnection;

    @FXML
    public void initialize()
    {
        serverConnection = new Server(
            data ->{
                Platform.runLater(()->{
                    playerStatus.getItems().add(data.toString());
                });
            }//End of playerStatus Passing

            //TODO: Modify Server class to take in a port Number and the
            //gamestatus field.
        );      
    }
}
