import java.util.ArrayList;
import java.util.List;

public final class HexagonLayout implements BoardLayout {
	private final List<BoardNode> nodes = new ArrayList<>();
    private BoardNode start;
    
	@Override
	public List<BoardNode> buildNodes() {
		if (!nodes.isEmpty()) return nodes;
		
		// starting node
		this.start = new BoardNode();
		
		// forks and center cross
		BoardNode f0 = new BoardNode();
		BoardNode f1 = new BoardNode();
		BoardNode f2 = new BoardNode();
		BoardNode f3 = new BoardNode();
		BoardNode f4 = new BoardNode();
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
		
		BoardNode n40 = new BoardNode();
		BoardNode n41 = new BoardNode();
		BoardNode n42 = new BoardNode();
		BoardNode n43 = new BoardNode();
		
		BoardNode n50 = new BoardNode();
		BoardNode n51 = new BoardNode();
		BoardNode n52 = new BoardNode();
		BoardNode n53 = new BoardNode();
		
		BoardNode s00 = new BoardNode();
		BoardNode s01 = new BoardNode();
		BoardNode s10 = new BoardNode();
		BoardNode s11 = new BoardNode();
		BoardNode s20 = new BoardNode();
		BoardNode s21 = new BoardNode();
		BoardNode s30 = new BoardNode();
		BoardNode s31 = new BoardNode();
		BoardNode s40 = new BoardNode();
		BoardNode s41 = new BoardNode();
		BoardNode s50 = new BoardNode();
		BoardNode s51 = new BoardNode();
		
		// forward connection
	    start.addNext(n00);  n00.addNext(n01);  n01.addNext(n02);  n02.addNext(n03);   n03.addNext(f0);
	    f0.addNext(n10);  n10.addNext(n11);  n11.addNext(n12);  n12.addNext(n13);  n13.addNext(f1);
	    f0.addNext(s00);  s00.addNext(s01);  s01.addNext(c);
	    
	    f1.addNext(n20);  n20.addNext(n21);  n21.addNext(n22);  n22.addNext(n23);  n23.addNext(f2);
	    f1.addNext(s10);  s10.addNext(s11);  s11.addNext(c);
	    
	    f2.addNext(s20);  s20.addNext(s21);  s21.addNext(c);
	    c.addNext(s40);   s40.addNext(s41);  s41.addNext(f4);
	    c.addNext(s50);   s50.addNext(s51);  s51.addNext(start);
	    
	    f2.addNext(n30);  n30.addNext(n31);  n31.addNext(n32);  n32.addNext(n33);  n33.addNext(f3);
	    f3.addNext(n40);  n40.addNext(n41);  n41.addNext(n42);  n42.addNext(n43);  n43.addNext(f4);
	    f3.addNext(s30);  s30.addNext(s31);  s31.addNext(c);
	    f4.addNext(n50);  n50.addNext(n51);  n51.addNext(n52);  n52.addNext(n53);  n53.addNext(start);
	    
	    // backward connection to handle back-do
		n00.addPrev(start);  n01.addPrev(n00);  n02.addPrev(n01);  n03.addPrev(n02);  f0.addPrev(n03);
		n10.addPrev(f0);  n11.addPrev(n10);  n12.addPrev(n11);  n13.addPrev(n12);  f1.addPrev(n13);
		s00.addPrev(f0);  s01.addPrev(s00);  c.addPrev(s01);
		
		s10.addPrev(f1);  s11.addPrev(s10);  c.addPrev(s11);
		n20.addPrev(f1);  n21.addPrev(n20);  n22.addPrev(n21);  n23.addPrev(n22);  f2.addPrev(n23);
		
		s20.addPrev(f2);  s21.addPrev(s20);  c.addPrev(s21);
		s30.addPrev(f3);  s31.addPrev(s30);  c.addPrev(s31);
		s40.addPrev(c);  s41.addPrev(s40);  f4.addPrev(s41);
		s50.addPrev(c);  s51.addPrev(s50);  start.addPrev(s51);
		
		n30.addPrev(f2);  n31.addPrev(n30);  n32.addPrev(n31);  n33.addPrev(n32);  f3.addPrev(n33);
		n40.addPrev(f3);  n41.addPrev(n40);  n42.addPrev(n41);  n43.addPrev(n42);  f4.addPrev(n43);
		n50.addPrev(f4);  n51.addPrev(n50);  n52.addPrev(n51);  n53.addPrev(n52);  start.addPrev(n53);
		
		return nodes;
	}

	@Override
	public BoardNode startNode() {
		if(start == null) buildNodes();
		return this.start;
	}

	@Override
	public BoardType type() {
	    return BoardType.HEXAGON;
	}

}
