import java.util.ArrayList;
import java.util.List;

public class GameManager {
	// game variables
    private final Board         board;
    private final List<Player>  players;
    private       int           current = 0;
    private       Piece         selected;
    private final int           numPieces;
    // observer
    private final List<GameListener> listeners = new ArrayList<>();

    public GameManager(BoardType type, List<Player> players,int numPieces) {
    	if (players.size() < 2 || players.size() > 4) {
            throw new IllegalArgumentException("Numver of players must be between 2 and 4");
        }
    	if (numPieces < 2 || numPieces > 5) {
            throw new IllegalArgumentException("Number of pieces must be between 2 and 5");
        }
        this.board   = new Board(type);
        this.players = players;
        this.numPieces = numPieces;
        
        fireGameStarted(new GameStartedEvent(players.size(), type));
    }

    /* ������������������ public API ������������������ */
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

    /* ������������������ core move logic ������������������ */

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

        // ���� capture first ����
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

        // ���� add attacker piece ����
        there.add(p); 
        p.owner().active().add(p);

        // ���� stack detection ����
        if (there.size() > 1 && there.stream().allMatch(pc -> pc.owner() == p.owner())) {
            fireStackFormed(new StackFormedEvent(p.owner(), new ArrayList<>(there), node));
        }

        // ���� finish line check ����
        if (node == board.startNode() && there.size() == 1 && p != null) {
            // finish detection logic needed here, based on hasPassedStart plus additional validation
        	p.setFinished(true);// mark isFinished in piece obejct to True
        	p.owner().incScore();
        	isGameOver();
        }
    }

    /* ������������������ event helpers ������������������ */
    private void firePieceMoved(PieceMovedEvent e) {
        listeners.forEach(l -> l.pieceMoved(e));
    }
    private void fireStackFormed(StackFormedEvent e) {
        listeners.forEach(l -> l.stackFormed(e));
    }
    private void firePieceCaptured(PieceCapturedEvent e) {
        listeners.forEach(l -> l.pieceCaptured(e));
    }
    private void fireGameOver(GameOverEvent e) {
        listeners.forEach(l -> l.gameOver(e));
    }
    private void fireTurnChanged(TurnChangedEvent e) {
        listeners.forEach(l -> l.turnChanged(e));
    }
    private void fireGameStarted(GameStartedEvent e) {
        listeners.forEach(l -> l.gameStarted(e));
    }

    // for turn handlings
    private Player currentPlayer() { 
    	return players.get(current); 
    }
    
    private void nextTurn() { 
    	current = (current+1) % players.size(); 
    	fireTurnChanged(new TurnChangedEvent(currentPlayer()));
    }
    
    private void isGameOver() {
        for (Player player : players) {
            if (player.score() >= numPieces) { //player.active().stream().allMatch(Piece::isFinished
                fireGameOver(new GameOverEvent(player));
                break;
            }
        }
    }
}
