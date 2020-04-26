import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class WordGuessClient extends Application {
	@FXML
	private Button connectBtn;

	@FXML
	private TextField ipInput;

	@FXML
	private TextField portInput;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(Client) Word Guess!!!");

		FXMLLoader loader = FXMLLoader.load(getClass().getResource("resources/clientGUI.fxml"));
		Parent root = loader.load();
		
		Scene scene = new Scene(root,600,600);
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});
	}

	@FXML
	private void connectToServer(ActionEvent event) {
		String ipAddr = ipInput.getText();
		int port = Integer.parseInt(portInput.getText());

		try {
			// load the game play scene
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("resources/gameplayGUI.fxml"));
			loader.setController(new GameplayController());
			Parent root = loader.load();
			Scene scene = new Scene(root, 600, 400);

			// get the stage from the source of the ActionEvent and show it
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.show();

			Client clientConnection = new Client(ipAddr, port);
			clientConnection.start();
		} catch (Exception e) {
			System.out.println("Game Play scene not found");
		}
	}

}
