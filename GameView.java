package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.application.Platform;

public class GameView {
	private final GameModel model;
    private final GameManager manager;
	private final int playerCount;
	private final BoardType boardType;
	private final int pieceCount;
	
	private final BoardPane boardPane;
    private final MessagePane messagePane;
    private final ControlPane controlPane;
	
	public GameView(int playerCount,int pieceCount, BoardType boardType) {
        this.playerCount = playerCount;
        this.pieceCount = pieceCount;
        this.boardType = boardType;
        
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= playerCount; i++) {
            players.add(new Player("Player " + i, pieceCount));
        }
        this.model = new GameModel(boardType, players);
        this.manager = new GameManager(model); 
        
        this.boardPane = new BoardPane(model, manager);
        this.messagePane = new MessagePane(model);
        this.controlPane = new ControlPane(manager, model);
        
        model.addGameListener(boardPane);
        model.addGameListener(messagePane);
        model.addGameListener(new GameListener() {
            @Override
            public void gameEnded(GameOverEvent e) {
                List<String> ranking = List.of(e.winner().id());  
                Platform.runLater(() -> showPlacementBoard(ranking));
            }
        });     
    }
	
	public void show(Stage stage) {
		
		Label infoLabel = new Label("Players: " + playerCount +",Pieces: "+pieceCount+", Board: " + boardType);
	
        BorderPane root = new BorderPane();
        root.setTop(infoLabel);
        root.setCenter(boardPane);
        root.setRight(controlPane);
        root.setBottom(messagePane);
     

		Scene scene = new Scene(root, 900, 700);
		stage.setScene(scene);
		stage.setTitle("Yutnori Game - In Progress");
		stage.show();
		
	}
	private void showPlacementBoard(List<String> ranking) {
     
    }
	
}
