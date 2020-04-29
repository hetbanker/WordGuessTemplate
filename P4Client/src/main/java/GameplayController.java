import javafx.application.Platform; 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.scene.control.TextField;
import java.io.File;



/**Scene Controller */
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
    private ListView<String> messages = new ListView<String>();

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

    MediaPlayer mediaPlayer;
    @FXML
    private void initialize() {
        /**Lets Get Some Music */
    	try{
			String path = "src/main/resources/survive.mp3";

			Media media = new Media(new File(path).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

			mediaPlayer.setOnEndOfMedia(new Runnable(){
			
				@Override
				public void run() {
					mediaPlayer.seek(Duration.ZERO);
				}
            });

			mediaPlayer.play();


        }catch(Exception ie){System.out.println(ie.getLocalizedMessage());}
        /**End of the music ;( Back to the boring stuff.*/

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
        plInfo.userletter = guessInput.getText();
    	clientConnection.send(plInfo);
        //guessInput.getText();
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
        clientConnection.send(plInfo);
    }

    @FXML
    private void handleCitiesChoice(ActionEvent event) {
        plInfo.setCategory("Cities");
        disableCategoryBtns();
        clientConnection.send(plInfo);
    }

    private void disableCategoryBtns() {
        chooseAnimals.setDisable(true);
        chooseFood.setDisable(true);
        chooseCities.setDisable(true);
    }
}