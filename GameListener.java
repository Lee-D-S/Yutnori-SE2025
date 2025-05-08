
public interface GameListener {

	void pieceMoved(PieceMovedEvent e);
    void stackFormed(StackFormedEvent e);
    void pieceCaptured(PieceCapturedEvent e);
    void gameStarted(GameStartedEvent e);
    void turnChanged(TurnChangedEvent e);
    void gameOver(GameOverEvent e);
    
}
