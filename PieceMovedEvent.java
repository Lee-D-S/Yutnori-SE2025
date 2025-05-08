import java.util.List;

public record PieceMovedEvent(Player player,
                              List<Piece>  pieces,
                              BoardNode from,
                              BoardNode to) {

}	