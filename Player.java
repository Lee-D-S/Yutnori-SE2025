package application;

import java.util.ArrayList;
import java.util.List;

public final class Player {
	private final String id;
    private final List<Piece> reserve = new ArrayList<>();   // off-board
    private final List<Piece> active  = new ArrayList<>();   // on-board
    private int score;
    private Boolean isActive = false;
    private int finishedPieceCount = 0;
    
    public Player(String id, int pieceCount) {
        this.id = id;
        for (int i = 0; i < pieceCount; i++) reserve.add(new Piece(this)); // off-board
    }

    /* getters */
    public String id() {
    	return id;       
    }
    
    public List<Piece> reserve() {
    	return reserve;  
    }
    
    public List<Piece> active() { 
    	return active;  
    }
    
    public int score() { 
    	return score;   
    }
    
    public void incScore() {
    	score++;    
    }
    
    public void deactivate() {
    	this.isActive = true;
    }    
    
    public Boolean isActive() {
    	return this.isActive;
    }
    
    public void incrementFinishedPieces() {
        finishedPieceCount++;
    }

    public boolean hasFinishedAllPieces() {
        return finishedPieceCount >= 4;
    }
}
