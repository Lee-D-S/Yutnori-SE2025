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
		return isFinished; // piece�� �������� (������� ����, ���������� �Դ���)
	}
	public void setFinished(boolean finished) {
        this.isFinished = finished;
    }
	boolean hasPassedStart() {
		return hasPassedStart; // piece�� ������� �������� �ִ���
	}
	public void setHasPassedStart(boolean passed) {
	    this.hasPassedStart = passed;
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
	
	public Piece(Player player, BoardNode node) {
		this.owner = player;
        this.position = node;
	}
}
	