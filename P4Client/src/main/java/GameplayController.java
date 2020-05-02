import javafx.application.Platform; 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.util.ArrayList;


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

    /**This array will be used for when it comes time to reenable buttons. Only the categories that are not present will be added */
    ArrayList<String> correctlyGuessedCategories = new ArrayList<String> ();

    // Connection info
    private String ipAddr;
    private int port;
    private Client clientConnection;
    
    ColorAdjust colorAdjust = new ColorAdjust();
    ColorAdjust colorAdjust1 = new ColorAdjust();
    

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
    		
    		guessInput.setOnKeyTyped(t -> {

                if (guessInput.getText().length() > 0) {
                    int pos = guessInput.getCaretPosition();
                    guessInput.setText(guessInput.getText(0, 0));
                    guessInput.positionCaret(pos); //To reposition caret since setText sets it at the beginning by default
                }

            });
    		
			String path = "src/main/resources/survive.mp3";

			Media media = new Media(new File(path).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            
            colorAdjust.setBrightness(-0.65); 
            colorAdjust1.setBrightness(0); 

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
                        String newHiddenWord = GameLogicClient.getHiddenWord(plInfo);
                        messages.getItems().clear();
                        messages.getItems().add("Category: "+ plInfo.category + System.lineSeparator()+
                                                "Guesses-Left: "+ plInfo.numOfGuesses + System.lineSeparator()+
                                                "Wins: " +plInfo.numCorrectGuessses+ System.lineSeparator()+
                                                "Fails: "+ plInfo.numWrongGuesses + System.lineSeparator() + System.lineSeparator() +
                                                newHiddenWord
                                                );
                        handleImages();
                    }); 
                },
                
                data1->{
                    Platform.runLater(()->{
                        messagesFromServer.setText(plInfo.backForthMessage);

                        //New Word
                        if(plInfo.numOfGuesses == 7)
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

    
    /** This will only enable when the user inputs something in the textField*/
    @FXML
    private void handleEnterPressed (KeyEvent event)
    {
        
            sendBtn.setDisable(false);
        
    }
    
    
    @FXML
    private void sendToServer(ActionEvent event) {
        plInfo.numOfGuesses -= 1;
        plInfo.userletter = guessInput.getText();
        guessInput.clear();
        sendBtn.setDisable(true);
        clientConnection.send(plInfo);
    }

    @FXML
    private void handleAnimalChoice(ActionEvent event) {
        plInfo.userGuessedWord = false;
        img2.setEffect(colorAdjust);
        img3.setEffect(colorAdjust);
        plInfo.setCategory("Animals");
        plInfo.numOfGuesses = 7;
        plInfo.choseAnimal = true;
        disableCategoryBtns();

        clientConnection.send(plInfo);
        
    }

    @FXML
    private void handleFoodChoice(ActionEvent event) {
        plInfo.userGuessedWord = false;
        img1.setEffect(colorAdjust);
        img3.setEffect(colorAdjust);
        plInfo.setCategory("Food");
        plInfo.numOfGuesses = 7;
        plInfo.choseFood = true;
        disableCategoryBtns();
        clientConnection.send(plInfo);
    }

    @FXML
    private void handleCitiesChoice(ActionEvent event) {
        plInfo.userGuessedWord = false;
        img1.setEffect(colorAdjust);
        img2.setEffect(colorAdjust);
        plInfo.setCategory("Cities");
        plInfo.numOfGuesses = 7;
        plInfo.choseCity = true;
        disableCategoryBtns();
        clientConnection.send(plInfo);
    }

    private void disableCategoryBtns() {
        chooseAnimals.setDisable(true);
        chooseFood.setDisable(true);
        chooseCities.setDisable(true);

        
        guessInput.setDisable(false);
    }

    void handleImages() {

        if (plInfo.userGuessedWord == true) {
            /**Add the corrently guessed category onto our Array */
            if(plInfo.choseAnimal)
            {
                correctlyGuessedCategories.add("Animal");
                img1.setEffect(colorAdjust);
                plInfo.choseAnimal = false;
            }
            else if(plInfo.choseFood)
            {
                correctlyGuessedCategories.add("Food");
                img2.setEffect(colorAdjust);
                plInfo.choseFood = false;
            }
            else if(plInfo.choseCity)
            {
                correctlyGuessedCategories.add("Cities");
                img3.setEffect(colorAdjust);
                plInfo.choseCity = false;
            }

            /**Now that the corrently guessed category is in the array, enable all other things not yet in the Array */

            if(!correctlyGuessedCategories.contains("Animal"))
            {
                chooseAnimals.setDisable(false);
                img1.setEffect(colorAdjust1);
            }
            if(!correctlyGuessedCategories.contains("Food"))
            {
                chooseFood.setDisable(false);
                img2.setEffect(colorAdjust1);
            }
            if(!correctlyGuessedCategories.contains("Cities"))
            {
                chooseCities.setDisable(false);
                img3.setEffect(colorAdjust1);
            }
        } 
    }
}