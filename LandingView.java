package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LandingView {
	public void show(Stage stage) {
		BorderPane root = new BorderPane();
		Label label = new Label("Welcome to Yutnori!");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		root.setCenter(label);
		
		Button startButton = new Button("Start Game");
		startButton.setOnAction(e -> {
			stage.close(); 
			SettingView settingView = new SettingView();
			settingView.show(new Stage()); 
		});
		root.setBottom(startButton);
		BorderPane.setAlignment(startButton, Pos.CENTER);
		
		Scene scene = new Scene(root, 350, 350);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		stage.setScene(scene);
		stage.setTitle("Yutnori Game - Landing");
		stage.show();
}
}
