import java.util.ArrayList;
import java.util.List;

public class GameManager {
	// game variables
    private final Board         board;
    private final List<Player>  players;
    private       int           current = 0;
    private       Piece         selected;
    // observer
    private final List<GameListener> listeners = new ArrayList<>();

    public GameManager(BoardType type, List<Player> players) {
        this.board   = new Board(type);
        this.players = players;
    }

    /* 式式式式式式式式式 public API 式式式式式式式式式 */
    public void addGameListener(GameListener l) { listeners.add(l); }
    public void removeGameListener(GameListener l) { listeners.remove(l); }

    /** User clicks on a piece icon */
    public void selectPiece(Piece p) {
        if (p.owner() != currentPlayer())
            throw new IllegalStateException("Not your turn!");
        selected = p;
    }

    /** Sticks have been thrown */
    public void applyThrow(YutResult result) {
        if (selected == null) return;                // nothing selected
        movePiece(selected, result.steps());
        if (!result.extraTurn()) nextTurn();
        selected = null;
    }

    /* 式式式式式式式式式 core move logic 式式式式式式式式式 */

    private void movePiece(Piece clicked, int steps) {
    	if(!clicked.isOnBoard()) {
    		BoardNode entry = board.startNode();
    		clicked.owner().reserve().remove(clicked);
    		landPiece(clicked, entry);
    	}
    	
        BoardNode from   = clicked.position();
        List<Piece> movers = pickMovers(clicked);          // stack or single

        /* detach */
        movers.forEach(p -> from.getPiecesOnNode().remove(p));

        /* walk */
        BoardNode dest = traverse(from, steps);

        /* land */
        movers.forEach(p -> {
            p.setPosition(dest);
            landPiece(p, dest);
        });

        firePieceMoved(new PieceMovedEvent(clicked.owner(), movers, from, dest));
    }

    // handles both single, multiple pieces
    private List<Piece> pickMovers(Piece clicked) {
        BoardNode node   = clicked.position();
        List<Piece> at   = node.getPiecesOnNode();
        boolean sameOwnerStack = at.size() > 1 && at.stream().allMatch(pc -> pc.owner() == clicked.owner());

        return sameOwnerStack ? new ArrayList<>(at) : List.of(clicked);  // new arraylist when lone piece, entire list otherwise
    }

    // forward + backward traversal
    private BoardNode traverse(BoardNode start, int steps) {
        int remaining = Math.abs(steps); 
        BoardNode n   = start;

        for (int i = 0; i < remaining; i++) {
            List<BoardNode> list = (steps >= 0) ? n.nextNodes() : n.prevNodes();  // gives next node to move onto
            if (list.isEmpty())
                throw new IllegalStateException("Dead-end during traverse");
            n = list.get(0);                      // TODO: fork choice UI, path determination logic comes here
        }
        return n;
    }

    /* place a single piece; handles capture + stack detect */
    private void landPiece(Piece p, BoardNode node) {
        List<Piece> there = node.getPiecesOnNode();  // retrieve list of pieces already sitting on the destination node

        // 式式 capture first 式式
        boolean mixedOwners = !there.isEmpty() && there.stream().anyMatch(pc -> pc.owner() != p.owner());  // first detect opponent's pieces
 
        if (mixedOwners) {
            List<Piece> victims = new ArrayList<>(there);
            victims.removeIf(pc -> pc.owner() == p.owner());   // temporarily save opponent's pieces only

            // send opponents home
            for (Piece v : victims) {
            	there.remove(v);
                v.leaveBoard();                           // position = null
                v.owner().active().remove(v);             // out of board bucket
                v.owner().reserve().add(v);               // back to reserve
            }
//            there.removeAll(victims);  // remove opponent's pieces from list at destination 
            firePieceCaptured(new PieceCapturedEvent(p.owner(), victims, node));
        }

        // 式式 add attacker piece 式式
        there.add(p); 
        p.owner().active().add(p);

        // 式式 stack detection 式式
        if (there.size() > 1 && there.stream().allMatch(pc -> pc.owner() == p.owner())) {
            fireStackFormed(new StackFormedEvent(p.owner(), new ArrayList<>(there), node));
        }

        // 式式 finish line check 式式
        if (node == board.startNode() && there.size() == 1 && p != null) {
            // finish detection logic needed here, based on hasPassedStart plus additional validation
            // mark isFinished in piece obejct to True
        	p.owner().incScore();
        }
    }

    /* 式式式式式式式式式 event helpers 式式式式式式式式式 */
    private void firePieceMoved(PieceMovedEvent e) {
        listeners.forEach(l -> l.pieceMoved(e));
    }
    private void fireStackFormed(StackFormedEvent e) {
        listeners.forEach(l -> l.stackFormed(e));
    }
    private void firePieceCaptured(PieceCapturedEvent e) {
        listeners.forEach(l -> l.pieceCaptured(e));
    }

    // for turn handlings
    private Player currentPlayer() { 
    	return players.get(current); 
    }
    
    private void nextTurn() { 
    	current = (current+1) % players.size(); 
    }
}
