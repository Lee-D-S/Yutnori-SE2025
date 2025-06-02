package model;

import model.GameOverEvent;
import model.GameStartedEvent;
import model.PieceCapturedEvent;
import model.PieceMovedEvent;
import model.StackFormedEvent;
import model.TurnChangedEvent;
import model.YutThrownEvent;

public interface GameListener {
    default void pieceMoved(PieceMovedEvent e) {}
    default void pieceCaptured(PieceCapturedEvent e) {}
    default void stackFormed(StackFormedEvent e) {}
    default void gameStarted(GameStartedEvent e) {}
    default void gameEnded(GameOverEvent e) {}
    default void turnChanged(TurnChangedEvent e) {}
    default void yutThrown(YutThrownEvent e) {}
    default void log(String message) {}
}
