import javafx.application.Platform; 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

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

    @FXML
    private ImageView img1 = new ImageView();
    @FXML
    private ImageView img2 = new ImageView();
    @FXML
    private ImageView img3 = new ImageView();

    @FXML
    private TextField messagesFromServer;

    // Connection info
    private String ipAddr;
    private int port;
    private Client clientConnection;

    //PlayerInfo to be send to the server
    static PlayerInfo plInfo = new PlayerInfo(0);

    public GameplayController(String ipAddr, int port) {
        this.ipAddr = ipAddr;
        this.port = port;
    }

    MediaPlayer mediaPlayer;
    MediaPlayer notification;
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
                        messages.getItems().clear();
                        messages.getItems().add("Category: "+ plInfo.category + System.lineSeparator()+
                                                "Guesses-Left: "+ plInfo.numOfGuesses + System.lineSeparator()+
                                                "Wins: " +plInfo.numCorrectGuessses+ System.lineSeparator()+
                                                "Fails: "+ plInfo.numWrongGuesses + System.lineSeparator() + System.lineSeparator() +
                                                GameLogicClient.getHiddenWord(plInfo)
                                                );
                    });
                },
                data1->{
                    Platform.runLater(()->{
                        messagesFromServer.setText(plInfo.backForthMessage);

                        //New Word
                        if(plInfo.numOfGuesses == 6)
                        {   
                            //Play a Notification
                            mediaPlayer.pause();
			                String path = "src/main/resources/alert.mp3";

			                Media media = new Media(new File(path).toURI().toString());
                            notification = new MediaPlayer(media);

                            notification.setOnEndOfMedia(new Runnable(){
                                @Override
                                public void run() {
                                    mediaPlayer.play();
                                }
                            });
                            notification.play();
                            //End of notification

                        }
                        
                    });
                });

        clientConnection.start();
    }

    @FXML
    private void sendToServer(ActionEvent event) {
        plInfo.numOfGuesses -= 1;
        plInfo.userletter = guessInput.getText();
        guessInput.clear();
        clientConnection.send(plInfo);
    }

    @FXML
    private void handleAnimalChoice(ActionEvent event) {
        img2.setImage(null);
        img3.setImage(null);
        plInfo.setCategory("Animals");
        plInfo.numOfGuesses = 6;
        disableCategoryBtns();

        clientConnection.send(plInfo);
        
    }

    @FXML
    private void handleFoodChoice(ActionEvent event) {
        img1.setImage(null);
        img3.setImage(null);
        plInfo.setCategory("Food");
        plInfo.numOfGuesses = 6;
        disableCategoryBtns();
        clientConnection.send(plInfo);
    }

    @FXML
    private void handleCitiesChoice(ActionEvent event) {
        img1.setImage(null);
        img2.setImage(null);
        plInfo.setCategory("Cities");
        plInfo.numOfGuesses = 6;
        disableCategoryBtns();
        clientConnection.send(plInfo);
    }

    private void disableCategoryBtns() {
        chooseAnimals.setDisable(true);
        chooseFood.setDisable(true);
        chooseCities.setDisable(true);

        sendBtn.setDisable(false);
        guessInput.setDisable(false);
    }
}