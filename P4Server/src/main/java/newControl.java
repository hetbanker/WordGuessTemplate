

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
                    playerStatus.getItems().clear();

                    for(PlayerInfo i : Server.clientInfo)
                    {
                        playerStatus.getItems().add(i.outString);
                    }
                });
            },//End of playerStatus Passing

            data1 ->{
                Platform.runLater(()->{
                    gameStatus.setText(data1.toString());
                });
            }//End of gameStatus Passing

            //TODO: Modify Server class to take in a port Number and the
            //gamestatus field.
        );      
    }
}
