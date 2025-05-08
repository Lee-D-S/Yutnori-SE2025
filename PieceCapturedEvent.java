import java.util.List;

public record PieceCapturedEvent(Player attacker,  List<Piece> victims, BoardNode node) {	
}
