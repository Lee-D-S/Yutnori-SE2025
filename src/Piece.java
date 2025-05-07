class Piece{
	private Player owner;
	private String pieceID;
	private BoardNode position;
	private boolean isFinished = false;
	private boolean hasPassedStart = false;
	
	public Player owner() {
		return owner;
	}
	public String getPieceID() {
		return pieceID;
	}
	public BoardNode position() {
		return position;
	}
	boolean isFinished() {
		return isFinished; // piece가 끝났는지 (출발점을 지나, 도착점까지 왔는지)
	}
	boolean hasPassedStart() {
		return hasPassedStart; // piece가 출발점을 지난적이 있는지
	}
	void setPosition(BoardNode dest) {
		this.position = dest;
	}
	
	public boolean isOnBoard() { 
		return position != null; 
	}
	
	public void leaveBoard() {
		position = null; 
	} 
	
	public void enterBoard(BoardNode n) {
		position = n; 
	}
}
	