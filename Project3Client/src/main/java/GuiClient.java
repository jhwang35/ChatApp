import java.io.IOException;
import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.text.Text;
import javafx.geometry.Pos;



public class GuiClient extends Application{


	TextField c1, username;
	Button b1, connectBtn;
	HashMap<String, Scene> sceneMap;
	VBox clientBox;
	Client clientConnection;
	String enteredName;

	ListView<String> listItems2;


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// our client thread which allows us to communicate with server
		// lambda expression handles messages sent from server
		clientConnection = new Client(data->{
				Platform.runLater(()->{
					if (data instanceof Message) {
						Message message = (Message) data;
						String displayString = message.getMessageContent();
						if(displayString.equals("OK")){
							primaryStage.setScene(sceneMap.get("client"));
							primaryStage.setTitle("Client");
							primaryStage.show();
						}
						else if (displayString.equals("NO")){
							showAlert("Login Error", "Username is already taken. Please enter another");
						}
						listItems2.getItems().add(displayString);
					} else {
						// Handle the case where data is not a Message object
						// For example, display an error message or handle it appropriately
						System.err.println("Received data is not a Message object: " + data);
					}
			});
		});





		clientConnection.start();


		listItems2 = new ListView<String>();

		c1 = new TextField();
		b1 = new Button("Send"); // we want to send the message class and use methods to get the strings and stuff
		b1.setOnAction(e->{clientConnection.send(new Message(enteredName,c1.getText(), true,MessageType.CHAT)); c1.clear();});
		sceneMap = new HashMap<String, Scene>();
		connectBtn = new Button("Connect");
		username = new TextField();


		// when button is pressed, name in textfield is sent to the server
		connectBtn.setOnAction(e->{
			enteredName =  username.getText();
			if (enteredName.isEmpty()){
				showAlert("Login Error", "No name was entered");
			}
			else {
				clientConnection.send(new Message(enteredName, MessageType.LOGIN));
				username.clear();
			}
		});

		sceneMap.put("client", createClientGui());
		sceneMap.put ("login", createLoginGui());
		String css = this.getClass().getResource("styles.css").toExternalForm();
		sceneMap.get("login").getStylesheets().add(css);


		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

		primaryStage.setScene(sceneMap.get("login"));
		primaryStage.setTitle("login");
		primaryStage.show();

	}

	public Scene createLoginGui() throws IOException {
		BorderPane borderPane = new BorderPane();
		borderPane.setId("borderPane");

		Button privateMsg = new Button("Private Message");
		Button publicMsg = new Button("Public Message");
		Text welcomeText = new Text("Welcome to the messaging app \n" +
				"Please enter your username in the text field below\n" +
				"A unique username is required,\nif the username entered is already taken, you will be prompted " +
				"to enter another username\n"
		);
		welcomeText.setTextAlignment(TextAlignment.LEFT);
		welcomeText.setId("welcomeText");

		username.setId("usernameField");
		username.setAlignment(Pos.CENTER);

		VBox loginVbox = new VBox();
		loginVbox.setAlignment(Pos.CENTER);
		loginVbox.setId("loginVbox");

		HBox nameConnect = new HBox();
		nameConnect.setId("nameConnect");
		nameConnect.getChildren().addAll(username, connectBtn);
		nameConnect.setAlignment(Pos.CENTER_LEFT);

		loginVbox.getChildren().addAll(welcomeText, nameConnect);
		borderPane.setCenter(loginVbox);

		return new Scene(borderPane, 1000, 1000);
	}

	public Scene createClientGui() {

		clientBox = new VBox(10, c1,b1,listItems2);
		clientBox.setStyle("-fx-background-color: blue;"+"-fx-font-family: 'serif';");
		return new Scene(clientBox, 400, 300);

	}
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
