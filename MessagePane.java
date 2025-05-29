package application;

import javafx.scene.control.ScrollPane;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class MessagePane extends VBox implements GameListener {
	private TextArea logArea;
	public MessagePane(GameModel model) {
		 this.logArea = new TextArea();
		 	logArea.setPrefRowCount(6);     
	        logArea.setPrefColumnCount(80);
	        logArea.setEditable(false);
	        ScrollPane scrollPane = new ScrollPane(logArea);
	        scrollPane.setFitToWidth(true);
	        scrollPane.setFitToHeight(true);
	        this.getChildren().add(scrollPane);
		
	}
	  public void gameStarted(GameStartedEvent e) {
	    	logArea.appendText("GAME START!");
	    }
	    public void gameEnded(GameOverEvent e) {
	    	logArea.appendText("WE GOT A WINNER!");
	    }
	    public void turnChanged(TurnChangedEvent e) {
	    	logArea.appendText("Turn changed : " + e.next());
	    }
	    
	    @Override
	    public void pieceMoved(PieceMovedEvent e) {
	    	if (e.to() == null) { logArea.appendText(e.player().id() + "'s" + e.pieces().size() + " piece(s) completed race!\n");}
	    	else {logArea.appendText(e.player().id() + " moved " + e.pieces().size() + " piece(s)\n"); }
	    }

	    @Override
	    public void pieceCaptured(PieceCapturedEvent e) {
	        logArea.appendText(e.attacker().id() + " captured " + e.victims().size() + " piece(s) of" + e.target().id() + "\n");
	    }

	    @Override
	    public void stackFormed(StackFormedEvent e) {
	        logArea.appendText(e.owner().id() + " formed a stack of " + e.stack().size() + "\n");
	    }
	    
	    public void yutThrown(YutThrownEvent e) {
	        logArea.appendText(e.player().id() + "got" + e.result().toString() +"\n");
	    }
	    
	    public void log(String message) {
	        logArea.appendText(message + "\n");
	    }

}
