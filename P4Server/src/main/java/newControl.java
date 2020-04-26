import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class newControl{
    @FXML
    private TextField gameStatus = new TextField();
    @FXML
    private ListView<String> playerStatus = new ListView<String>();
    private List<String> temp;
	Server serverConnection;

    @FXML
    public void initialize()
    {
        
        serverConnection = new Server(
            data ->{
                Platform.runLater(()->{
                    playerStatus.getItems().add(data.toString());
                    List<String> temp = new ArrayList<String>(playerStatus.getItems());
                    playerStatus.getItems().clear();
                    for(String i : temp)
                    {
                        playerStatus.getItems().add(i);
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
