package model;
import java.util.ArrayList;
import java.util.List;


public class GameModel {
    private final List<Player> players;
    private final Board board;
    private final int pieceCnt;
    private final List<GameListener> listeners = new ArrayList<>();
    private int currentPlayerIndex = 0;

    public GameModel(BoardType type, List<Player> players, int pieceCnt) {
        this.players = players;
        this.board = new Board(type);
        this.pieceCnt = pieceCnt;
    }

    public void addGameListener(GameListener l) { listeners.add(l); }
    public void removeGameListener(GameListener l) { listeners.remove(l); }

    public List<Player> players() { return players; }
    public Board board() { return board; }

    public Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    public int getPieceCnt() {
    	return this.pieceCnt;
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public void firePieceMoved(PieceMovedEvent e) {
        listeners.forEach(l -> l.pieceMoved(e));
    }

    public void firePieceCaptured(PieceCapturedEvent e) {
        listeners.forEach(l -> l.pieceCaptured(e));
    }

    public void fireStackFormed(StackFormedEvent e) {
        listeners.forEach(l -> l.stackFormed(e));
    }

    public int currentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void yutThrown(YutThrownEvent e) {
    	for (GameListener l : listeners) {
    			l.yutThrown(e);
    	}
    }
    
    public void fireLog(String message) {
        for (GameListener l : listeners) {
                l.log(message);
            }
        }
    
    public void fireGameOver(GameOverEvent e) {
        for (GameListener l : listeners) {
            l.gameEnded(e);
        }
    }

}
