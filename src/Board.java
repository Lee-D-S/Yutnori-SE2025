import java.util.List;


public class Board {
    private final List<BoardNode> nodes;
    private final BoardNode       startNode;
    private final BoardType       type;

    public Board(BoardType type) {
        BoardLayout layout = type.create();  
        this.nodes         = layout.buildNodes();
        this.startNode     = layout.startNode();
        this.type          = type;
    }

    public List<BoardNode> nodes()     { return nodes; }
    public BoardNode       startNode() { return startNode; }
    public BoardType       type()      { return type; }
}