import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.Socket;

/**Parth's Scene Controller */
public class GameplayController {
    @FXML
    private Button sendBtn;

    @FXML
    private Button chooseAnimals = new Button();

    @FXML
    private Button chooseFood = new Button();

    @FXML
    private Button chooseCities = new Button();

    @FXML
    private TextField guessInput;

    @FXML
    private ListView<String> messages;

    // Connection info
    private String ipAddr;
    private int port;
    private Client clientConnection;

    //PlayerInfo to be send to the server
    PlayerInfo plInfo = new PlayerInfo(0);

    public GameplayController(String ipAddr, int port) {
        this.ipAddr = ipAddr;
        this.port = port;
    }

    @FXML
    private void initialize() {
        this.clientConnection = new Client(this.ipAddr, this.port,
                data->{
                    Platform.runLater(()->{
                        messages.getItems().add(data.toString());
                    });
                },
                data1->{
                    Platform.runLater(()->{
                        String btnNumsStr = data1.toString();
                        String[] nums = btnNumsStr.split("&");
                        for (String s : nums) {
                            if (s.equals("1")) {
                                chooseAnimals.setDisable(false);
                            } else if (s.equals("2")) {
                                chooseFood.setDisable(false);
                            } else if (s.equals("3")) {
                                chooseCities.setDisable(false);
                            }
                        }
                    });
                });

        clientConnection.start();
    }

    @FXML
    private void sendToServer(ActionEvent event) {
        clientConnection.send(plInfo);
    }

    @FXML
    private void handleAnimalChoice(ActionEvent event) {
        plInfo.setCategory("Animals");
        disableCategoryBtns();
        clientConnection.send(plInfo);
    }

    @FXML
    private void handleFoodChoice(ActionEvent event) {
        plInfo.setCategory("Food");
        disableCategoryBtns();
    }

    @FXML
    private void handleCitiesChoice(ActionEvent event) {
        plInfo.setCategory("Cities");
        disableCategoryBtns();
    }

    private void disableCategoryBtns() {
        chooseAnimals.setDisable(true);
        chooseFood.setDisable(true);
        chooseCities.setDisable(true);
    }
}
