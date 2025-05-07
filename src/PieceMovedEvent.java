import java.util.List;

public record PieceMovedEvent(Player player,
                              Piece  piece,
                              BoardNode from,
                              BoardNode to) {

	public PieceMovedEvent(Player owner, List<Piece> movers, BoardNode from2, BoardNode dest) {
		// TODO Auto-generated constructor stub
	}
}