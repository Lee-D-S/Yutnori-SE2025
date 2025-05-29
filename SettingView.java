package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SettingView {
	public void show(Stage stage) {
		Label playerLabel = new Label("Number of Players (2-4):");
		Spinner<Integer> playerSpinner = new Spinner<>();
		playerSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 4, 2));
		
		Label pieceLabel = new Label("Number of Pieces (2-5):");
		Spinner<Integer> pieceSpinner = new Spinner<>();
		pieceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 5, 2));
		
		Label boardLabel = new Label("Board Type:");
		ComboBox<String>comboBox = new ComboBox<>();
		comboBox.getItems().addAll("SQUARE", "PENTAGON", "HEXAGON");
		comboBox.setValue("SQUARE");
		
		Button confirmButton = new Button("Confirm");
		confirmButton.setOnAction(e -> {
			int playerCount = playerSpinner.getValue();
		    int pieceCount = pieceSpinner.getValue(); 
		    String selectedBoard = comboBox.getValue();
		    
		    BoardType boardType = BoardType.valueOf(selectedBoard.toUpperCase()); 

		    stage.close(); 
		    
		    GameView gameView = new GameView(playerCount,pieceCount, boardType); 
		    gameView.show(new Stage());
		});
		
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(20));
		gridPane.setVgap(30);
		gridPane.setHgap(10);

		gridPane.add(playerLabel, 0, 0);
		gridPane.add(playerSpinner, 1, 0);
		gridPane.add(pieceLabel, 0, 1);
		gridPane.add(pieceSpinner, 1, 1);
		gridPane.add(boardLabel, 0, 2);
		gridPane.add(comboBox, 1, 2);
		gridPane.add(confirmButton, 1, 4);
		
		Scene scene = new Scene(new BorderPane(gridPane), 350, 250);
		stage.setScene(scene);
		stage.setTitle("Game Settings");
		stage.show();
	}
}
package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SettingView {
	public void show(Stage stage) {
		Label playerLabel = new Label("Number of Players (2-4):");
		Spinner<Integer> playerSpinner = new Spinner<>();
		playerSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 4, 2));
		
		Label pieceLabel = new Label("Number of Pieces (2-5):");
		Spinner<Integer> pieceSpinner = new Spinner<>();
		pieceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 5, 2));
		
		Label boardLabel = new Label("Board Type:");
		ComboBox<String>comboBox = new ComboBox<>();
		comboBox.getItems().addAll("SQUARE", "PENTAGON", "HEXAGON");
		comboBox.setValue("SQUARE");
		
		Button confirmButton = new Button("Confirm");
		confirmButton.setOnAction(e -> {
			int playerCount = playerSpinner.getValue();
		    int pieceCount = pieceSpinner.getValue(); 
		    String selectedBoard = comboBox.getValue();
		    
		    BoardType boardType = BoardType.valueOf(selectedBoard.toUpperCase()); 

		    stage.close(); 
		    
		    GameView gameView = new GameView(playerCount,pieceCount, boardType); 
		    gameView.show(new Stage());
		});
		
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(20));
		gridPane.setVgap(30);
		gridPane.setHgap(10);

		gridPane.add(playerLabel, 0, 0);
		gridPane.add(playerSpinner, 1, 0);
		gridPane.add(pieceLabel, 0, 1);
		gridPane.add(pieceSpinner, 1, 1);
		gridPane.add(boardLabel, 0, 2);
		gridPane.add(comboBox, 1, 2);
		gridPane.add(confirmButton, 1, 4);
		
		Scene scene = new Scene(new BorderPane(gridPane), 350, 250);
		stage.setScene(scene);
		stage.setTitle("Game Settings");
		stage.show();
	}
}
