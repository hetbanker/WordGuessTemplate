import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import javafx.scene.Node;



public class WordGuessServer extends Application {
	//Login Screen Variable
	@FXML
	private Button helpButton = new Button();

    @FXML
    private Button startButton = new Button();

    @FXML
	private TextField portIn = new TextField("TODO: REPLACE");


	//Server Side Variables
	@FXML
    private TextField serverStatus = new TextField();

    @FXML
    private ListView<String> playersStatus = new ListView<String>();


	String sceneType = "LoginScene";
	static int newPort;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("loginScreen.fxml"));
		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Word Guess Game (Server)");

		primaryStage.show();

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
	}

	MediaPlayer mediaPlayer;  
	@FXML
	public void initialize()
	{

		//TODO Remove on ServerSide
		String path;
		if(sceneType == "LoginScene")
		{
			path = "src/main/resources/tasty.mp3";

			Media media = new Media(new File(path).toURI().toString());  
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();  
		}
		else
		{
			path = "src/main/resources/tasty.mp3";

			Media media = new Media(new File(path).toURI().toString());  
			
			MediaPlayer mediaPlayer = new MediaPlayer(media);  
			
			mediaPlayer.play();  
		}
          
	}

    @FXML
    void helpScreen(ActionEvent event) throws IOException {
		Stage helpWindow = new Stage();
		Parent newRoot = FXMLLoader.load(getClass().getResource("help.fxml"));

		helpWindow.setTitle("Help");
		helpWindow.setScene(new Scene(newRoot));
		helpWindow.show();
	}
	
    @FXML
    void StartServer(ActionEvent event) {
		try
		{
			newPort = Integer.parseInt(portIn.getText());
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("serverSide.fxml"));
			loader.setController(new newControl());

			Parent nRoot = loader.load();

			Scene nScene = new Scene(nRoot);
			Stage nStage = (Stage)((Node)event.getSource()).getScene().getWindow();

			nStage.setScene(nScene);
			
			nStage.show();

			
		}
		catch(Exception ie)
		{
			System.out.println(ie.getLocalizedMessage());
			portIn.clear();
			portIn.setPromptText("Not a valid Port #");
		}
    }

}
