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

                if (guessInput.getText().length() > 1) {
                    guessInput.clear();
                    guessInput.setPromptText("Only one letter please.");
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
                        handleImages();
                        messages.getItems().clear();
                        messages.getItems().add("Category: "+ plInfo.category + System.lineSeparator()+
                                                "Guesses-Left: "+ plInfo.numOfGuesses + System.lineSeparator()+
                                                "Wins: " +plInfo.numCorrectGuessses+ System.lineSeparator()+
                                                "Fails: "+ plInfo.numWrongGuesses + System.lineSeparator() + System.lineSeparator() +
                                                newHiddenWord
                                                );
                    }); 
                },
                
                data1->{
                    Platform.runLater(()->{

                        //New Word
                        if(plInfo.needsWord)
                        {   
                            messagesFromServer.setText(plInfo.backForthMessage);
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
                            plInfo.needsWord = false;
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
        plInfo.needsWord = true;
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
        plInfo.needsWord = true;
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
        plInfo.needsWord = true;
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
        sendBtn.setDisable(false);
    }

    /**This function will check if the player guessed the correct word
     * If yes, then images and buttons are adjusted appropriately.
     * 
     * It also check that the player still has guesses available, if not the images and
     * buttons are also adjusted.
     */
    void handleImages() {

        //When the player chose the right word
        if (plInfo.userGuessedWord == true) {
            /**Add the corrently guessed category onto our Array */
            if(plInfo.choseAnimal)
            {
                plInfo.animal.remove(plInfo.word2Guess);
                correctlyGuessedCategories.add("Animal");
                img1.setEffect(colorAdjust);
                plInfo.choseAnimal = false;
            }
            else if(plInfo.choseFood)
            {
                plInfo.food.remove(plInfo.word2Guess);
                correctlyGuessedCategories.add("Food");
                img2.setEffect(colorAdjust);
                plInfo.choseFood = false;
            }
            else if(plInfo.choseCity)
            {
                plInfo.city.remove(plInfo.word2Guess);
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

            plInfo.numCorrectGuessses += 1;
            //The player won the game
            if(plInfo.numCorrectGuessses == 3)
            {
                mediaPlayer.pause();
                messagesFromServer.setText("You WIN Celebrate!");
                plInfo.updateOutString();
                playNotification("celebrate.mp3");
            }
            else 
            {
                // Let the player know he guesssed correctly
                messagesFromServer.setText("YOU GOT ONE WORD BUD!");
                plInfo.updateOutString();
                playNotification("upgrade.mp3");
            }

            plInfo.numWrongGuesses = 0;
            plInfo.updateOutString();

        } //End of the Player choosing the right word

        //When the player has run out of Guesses
        else if(plInfo.numOfGuesses <= 0)
        {
            plInfo.numWrongGuesses += 1;

            if(!correctlyGuessedCategories.contains("Animal") && plInfo.numWrongGuesses != 3)
            {
                chooseAnimals.setDisable(false);
                img1.setEffect(colorAdjust1);
            }
            if(!correctlyGuessedCategories.contains("Food") && plInfo.numWrongGuesses != 3)
            {
                chooseFood.setDisable(false);
                img2.setEffect(colorAdjust1);
            }
            if(!correctlyGuessedCategories.contains("Cities") && plInfo.numWrongGuesses != 3)
            {
                chooseCities.setDisable(false);
                img3.setEffect(colorAdjust1);
            }



            //Player Loses
            if(plInfo.numWrongGuesses == 3)
            {
                mediaPlayer.stop();
                messagesFromServer.setText("RIP_ You Lose ;(");
                plInfo.updateOutString();
                guessInput.setDisable(true);
                playLost();
                sendBtn.setDisable(true);
                img1.setEffect(colorAdjust);
                img2.setEffect(colorAdjust);
                img3.setEffect(colorAdjust);
            }   
            else
            {
                mediaPlayer.pause();
                messagesFromServer.setText("Sorry, that's wrong ;(");
                plInfo.updateOutString();
                playNotification("downgrade.mp3");
                plInfo.numOfGuesses = 7;

                guessInput.setDisable(true);
                sendBtn.setDisable(true);
            }

        }//End of when the player has run out of guesses
    }

    /**Play notification: A function meant to facilitate the act of letting the player
     * Know though sound
     */

     private void playNotification(String musicFile)
     {
        mediaPlayer.pause();
        String path = "src/main/resources/"+musicFile;

        Media media = new Media(new File(path).toURI().toString());
        notification = new MediaPlayer(media);

        notification.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.play();
            }
        });

        notification.play();

     }

     private void playLost()
     {
        String path = "src/main/resources/dark.mp3";

        Media media = new Media(new File(path).toURI().toString());
        notification = new MediaPlayer(media);

        notification.play();
     }
}