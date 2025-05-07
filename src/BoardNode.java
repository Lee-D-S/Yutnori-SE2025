import java.util.ArrayList;

public class BoardNode {
    private ArrayList<Piece> piecesOnNode = new ArrayList<>();
    private ArrayList<BoardNode> nextNodes = new ArrayList<>();
    private ArrayList<BoardNode> prevNodes = new ArrayList<>();
    
    public BoardNode() {
    	
    }
    
    public ArrayList<Piece> getPiecesOnNode(){
    	return this.piecesOnNode;
    }
    
    public ArrayList<BoardNode> nextNodes(){
    	return this.nextNodes;
    }
    public ArrayList<BoardNode> prevNodes(){
    	return this.prevNodes;
    }
    
    public void addNext(BoardNode next) {
    	if (next == null) throw new IllegalArgumentException("next was null");
    	this.nextNodes.add(next);
    }
    
    public void addPrev(BoardNode prev) {
    	this.prevNodes.add(prev);
    }
}
