import java.util.List;

public record StackFormedEvent(Player player, List<Piece> stack, BoardNode node) {

}
