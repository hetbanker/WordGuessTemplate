import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.Socket;

public class GameplayController {
    @FXML
    private Button sendBtn;

    @FXML
    private Button chooseAnimals;

    @FXML
    private Button chooseFood;

    @FXML
    private Button chooseCities;

    @FXML
    private TextField guessInput;

    @FXML
    private ListView<String> messages;

    // Connection info
    private String ipAddr;
    private int port;
    private Client clientConnection;

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
                data->{
                    Platform.runLater(()->{
                        String btnNumsStr = data.toString();
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
        String guess = guessInput.getText();
        clientConnection.send(guess);
    }

    @FXML
    private void handleAnimalChoice(ActionEvent event) {
        clientConnection.setCategory("Animals");
        disableCategoryBtns();
    }

    @FXML
    private void handleFoodChoice(ActionEvent event) {
        clientConnection.setCategory("Food");
        disableCategoryBtns();
    }

    @FXML
    private void handleCitiesChoice(ActionEvent event) {
        clientConnection.setCategory("Cities");
        disableCategoryBtns();
    }

    private void disableCategoryBtns() {
        chooseAnimals.setDisable(true);
        chooseFood.setDisable(true);
        chooseCities.setDisable(true);
    }
}
