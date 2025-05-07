import java.util.ArrayList;
import java.util.List;


public final class SquareLayout implements BoardLayout {
	private final List<BoardNode> nodes = new ArrayList<>();
    private BoardNode start;
	
	@Override
	public List<BoardNode> buildNodes() {
		if (!nodes.isEmpty()) return nodes;
		
		/**
		 *    
		 *   f1   n13   n12   n11   n10    f0
		 *   
		 *   n20  s10               s00    n03
		 *   
		 *   n21        s11   s01          n02
		 *                  c
		 *   n22        s20   s30          n01 
		 *   
		 *   n23  s21               s31    n00
		 *   
		 *   f2   n30   n31   n32   n33    start
		 */
		
		// refer to figure above
		// starting node
		this.start = new BoardNode();
		
		// forks and center cross
		BoardNode f0 = new BoardNode();
		BoardNode f1 = new BoardNode();
		BoardNode f2 = new BoardNode();
		BoardNode c = new BoardNode();
		
		// rest nodes
		BoardNode n00 = new BoardNode();
		BoardNode n01 = new BoardNode();
		BoardNode n02 = new BoardNode();
		BoardNode n03 = new BoardNode();
		
		BoardNode n10 = new BoardNode();
		BoardNode n11 = new BoardNode();
		BoardNode n12 = new BoardNode();
		BoardNode n13 = new BoardNode();
		
		BoardNode n20 = new BoardNode();
		BoardNode n21 = new BoardNode();
		BoardNode n22 = new BoardNode();
		BoardNode n23 = new BoardNode();
		
		BoardNode n30 = new BoardNode();
		BoardNode n31 = new BoardNode();
		BoardNode n32 = new BoardNode();
		BoardNode n33 = new BoardNode();
		
		BoardNode s00 = new BoardNode();
		BoardNode s01 = new BoardNode();
		BoardNode s10 = new BoardNode();
		BoardNode s11 = new BoardNode();
		
		BoardNode s20 = new BoardNode();
		BoardNode s21 = new BoardNode();
		BoardNode s30 = new BoardNode();
		BoardNode s31 = new BoardNode();
		
		// forward connection
	    start.addNext(n00);  n00.addNext(n01);  n01.addNext(n02);  n02.addNext(n03);   n03.addNext(f0);
	    f0.addNext(n10);  n10.addNext(n11);  n11.addNext(n12);  n12.addNext(n13);  n13.addNext(f1);
	    f0.addNext(s00);  s00.addNext(s01);  s01.addNext(c);
	    
	    f1.addNext(n20);  n20.addNext(n21);  n21.addNext(n22);  n22.addNext(n23);  n23.addNext(f2);
	    f1.addNext(s10);  s10.addNext(s11);  s11.addNext(c);
	    
	    c.addNext(s30);   s30.addNext(s31);  s31.addNext(start);
	    c.addNext(s20);   s20.addNext(s21);  s21.addNext(f2);
	    
	    f2.addNext(n30);  n30.addNext(n31);  n31.addNext(n32);  n32.addNext(n33);  n33.addNext(start);
	    
	    // backward connection to handle back-do
		n00.addPrev(start);  n01.addPrev(n00);  n02.addPrev(n01);  n03.addPrev(n02);  f0.addPrev(n03);
		n10.addPrev(f0);  n11.addPrev(n10);  n12.addPrev(n11);  n13.addPrev(n12);  f1.addPrev(n13);
		s00.addPrev(f0);  s01.addPrev(s00);  c.addPrev(s01);
		
		s10.addPrev(f1);  s11.addPrev(s10);  c.addPrev(s11);
		n20.addPrev(f1);  n21.addPrev(n20);  n22.addPrev(n21);  n23.addPrev(n22);  f2.addPrev(n23);
		
		s20.addPrev(c);  s21.addPrev(s20);  f2.addPrev(s21);
		s30.addPrev(c);  s31.addPrev(s30);  start.addPrev(s31);
		
		n30.addPrev(f2);  n31.addPrev(n30);  n32.addPrev(n31);  n33.addPrev(n32);  start.addPrev(n33);
		
		return nodes;
	}

	@Override
	public BoardNode startNode() {
		if(start == null) buildNodes();
		return this.start;
	}

	@Override
	public BoardType type() {
		return BoardType.SQUARE;
	}
    
}
