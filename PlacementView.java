package application;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PlacementView {
	
    private List<String> playerRanking;
    public PlacementView(List<String> playerRanking) {
        this.playerRanking = playerRanking;
    }
	public void show(Stage stage) {
		BorderPane root = new BorderPane();
		Label titleLabel = new Label("Game Over - Final Standings");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		BorderPane.setAlignment(titleLabel, Pos.CENTER);
        root.setTop(titleLabel);
        
        ObservableList<String> listModel = FXCollections.observableArrayList();
        int rank = 1;
        for (String player : playerRanking) {
            listModel.add(rank + ". " + player);
            rank++;
        }
        ListView<String> rankingList = new ListView<>(listModel);
        
        ScrollPane rankScrollPane = new ScrollPane(rankingList);
        root.setCenter(rankScrollPane);
		 
		 Button closeButton = new Button("Exit");
		 closeButton.setOnAction(e -> {
				stage.close(); 
			});
		 BorderPane.setAlignment(closeButton, Pos.CENTER);
		  root.setBottom(closeButton);

		Scene scene = new Scene(root, 400, 300);
		stage.setScene(scene);
		stage.setTitle("Game Result");
		stage.show();
	}
}
