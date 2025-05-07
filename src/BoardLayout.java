import java.util.List;

public interface BoardLayout {
	List<BoardNode> buildNodes();   // create every node and wire them
	BoardNode       startNode();    // point to the first playable node
	BoardType       type();         // metadata (enum or any id)
}
