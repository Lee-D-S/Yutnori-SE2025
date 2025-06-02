package ui.javafx;
import javafx.application.Platform;

import controller.GameController; // Import GameController
import model.BoardLayout;
import model.BoardNode;
import model.BoardType;
import model.GameListener;
import model.GameModel; // Still needed for direct model interactions if any, or type hints
import model.GameOverEvent;
import model.GameStartedEvent;
import model.Piece;
import model.PieceCapturedEvent;
import model.PieceMovedEvent;
import model.Player;
import model.StackFormedEvent;
import model.TurnChangedEvent;
import model.YutThrownEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TitledPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color; // JavaFX Color
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle; // JavaFX Rectangle
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Font; // For consistency if needed, though not used in original BoardPane text

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardPane extends TitledPane implements GameListener {
    private BoardLayout layout;
    private Map<BoardNode, Point2D> coordinates = new HashMap<>();
    private GameController controller; 
    private GameModel model;           

    private final Map<Rectangle, Piece> clickablePieces = new HashMap<>();

    private Canvas canvas;
    private final Pane boardPaneContainer;

    public BoardPane(GameController controller) { 
        this.controller = controller;
        this.model = controller.getModel(); 
        this.layout = this.model.board().layout();
        this.setText("Game Board");

        boardPaneContainer = new Pane();
        boardPaneContainer.setPrefSize(700, 500);
        boardPaneContainer.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");

        canvas = new Canvas(700, 500); // Match Pane size
        boardPaneContainer.getChildren().add(canvas);
        this.setContent(boardPaneContainer);

        assignCoordinates(layout.type());

        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                double clickX = e.getX();
                double clickY = e.getY();
                for (Map.Entry<Rectangle, Piece> entry : clickablePieces.entrySet()) {
                    if (entry.getKey().contains(clickX, clickY)) {
                        controller.onPieceSelected(entry.getValue());
                        Platform.runLater(() -> {
                            reDraw();
                        });
                        break;
                    }
                }
            }
        });

        // Register as a listener to game events for repainting. red contour on selected piece  drawn

        Platform.runLater(() -> {
            reDraw();
        }); // Initial drawing 
        
    }

    private void assignCoordinates(BoardType type) {
        List<BoardNode> allNodes = layout.buildNodes();
        int cx = 350; // centre x of the canvas
        int cy = 250; // centre y of the canvas
        int radius = Math.min(cx, cy) - 50; 

        if (type == BoardType.SQUARE) {
            double[][] grid = {
                {5,5}, {5,4}, {5,3}, {5,2}, {5,1},
                {5,0}, {4,0}, {3,0}, {2,0}, {1,0},
                {0,0}, {0,1}, {0,2}, {0,3}, {0,4},
                {0,5}, {1,5}, {2,5}, {3,5}, {4,5},
                {4.2,0.8}, {3.4,1.6}, {0.8,0.8}, {1.6,1.6}, {2.5,2.5},
                {1.6,3.4}, {0.8,4.2}, {3.4,3.4}, {4.2,4.2}
            };
            
            double scale = 60;
            double offsetX = 50; 
            double offsetY = 30; 

            int i = 0;
            for (BoardNode node : allNodes) {
                if (i >= grid.length) break;
                coordinates.put(node, new Point2D(offsetX + scale * grid[i][0], offsetY + scale * grid[i][1]));
                i++;
            }

        } else if (type == BoardType.PENTAGON) {
            int outerSize = 25;
            int shortcut = 5;
            int i = 0;

            for (; i < outerSize; i++) {
                double angle = -2 * Math.PI * i / outerSize;
                double x = cx + (radius * Math.cos(angle));
                double y = cy + (radius * Math.sin(angle));
                coordinates.put(allNodes.get(i), new Point2D(x, y));
            }

            for (int j = 0; j < shortcut; j++) {
                double baseAngle = -(2 * Math.PI / shortcut + 2 * Math.PI * j / shortcut);
                for (int k = 0; k < 2; k++) {
                    double step = (k + 1) / 3.0;
                    double x = cx + (radius * (1 - step)) * Math.cos(baseAngle);
                    double y = cy + (radius * (1 - step)) * Math.sin(baseAngle);
                    if (i < allNodes.size()) coordinates.put(allNodes.get(i++), new Point2D(x, y));
                }
            }
            if (i < allNodes.size()) coordinates.put(allNodes.get(i), new Point2D(cx, cy)); // centre node

        } else { // HEXAGON 
            int outerSize = 30;
            int shortcut = 6;
            int i = 0;

            for (; i < outerSize; i++) {
                double angle = -2 * Math.PI * i / outerSize;
                double x = cx + (radius * Math.cos(angle));
                double y = cy + (radius * Math.sin(angle));
                coordinates.put(allNodes.get(i), new Point2D(x, y));
            }

            for (int j = 0; j < shortcut; j++) {
                double baseAngle = -(Math.PI * 2 / shortcut + 2 * Math.PI * j / shortcut);
                for (int k = 0; k < 2; k++) {
                    double step = (k + 1) / 3.0;
                    double x = cx + (radius * (1 - step)) * Math.cos(baseAngle);
                    double y = cy + (radius * (1 - step)) * Math.sin(baseAngle);
                     if (i < allNodes.size()) coordinates.put(allNodes.get(i++), new Point2D(x, y));
                }
            }
            if (i < allNodes.size()) coordinates.put(allNodes.get(i), new Point2D(cx, cy)); // centre node
        }
    }


    private void reDraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw paths
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        for (BoardNode node : coordinates.keySet()) {
            Point2D p = coordinates.get(node);
            if (p == null) continue;
            for (BoardNode next : node.nextNodes()) {
                Point2D q = coordinates.get(next);
                if (q != null) gc.strokeLine(p.getX(), p.getY(), q.getX(), q.getY());
            }
        }

        if (layout.startNode() != null && layout.entryNode() != null && !layout.entryNode().nextNodes().isEmpty()) {
            Point2D p1 = coordinates.get(layout.startNode());
            Point2D p2 = coordinates.get(layout.entryNode().nextNodes().get(0));
            if (p1 != null && p2 != null) {
                gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            }
        }


        // Draw nodes
        for (Map.Entry<BoardNode, Point2D> entry : coordinates.entrySet()) {
            BoardNode node = entry.getKey();
            Point2D p = entry.getValue();

            if (node == layout.startNode()) {
                gc.setFill(Color.YELLOW);
            } else {
                gc.setFill(Color.WHITE);
            }
            gc.fillOval(p.getX() - 10, p.getY() - 10, 20, 20);
            gc.setStroke(Color.BLACK); 
            gc.strokeOval(p.getX() - 10, p.getY() - 10, 20, 20);
        }

        clickablePieces.clear();
        System.out.println("BoardPane: reDraw() called. Checking piece positions:");
        drawPlayerPieces(gc);
        System.out.println("drawPlayerpicees(gc) called");
    }

    private void drawPlayerPieces(GraphicsContext gc) {
        int pieceSize = 20; // size of pieces in the reserve area
        int activePieceSize = 16; // size of pieces on the board
        int spacing = 5;    // Spacing for reserve pieces
        int playerAreaSpacing = 150; // horizontal space between player areas
        int playerAreaVerticalSpacing = 100; // vertical space between player rows
        int labelOffset = 20; // player id offset.


        Color[] playerColorsFx = { Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.CYAN };


        List<Player> players = controller.getPlayers();
        if (players == null || players.isEmpty()) return;

        // Pieces in reserve area 
        // starting at x = 500 
        double reserveAreaStartX = canvas.getWidth() - (2 * playerAreaSpacing) + 50; // Example starting X

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            int playerCol = i % 2; // 0 or 1
            int playerRow = i / 2; // 0, 1, ...

            double startX = reserveAreaStartX + playerCol * playerAreaSpacing;
            double startY = 60 + playerRow * playerAreaVerticalSpacing; // Same Y as Swing

            gc.setFill(Color.BLACK);
            gc.fillText(player.id(), startX, startY - labelOffset);

            int pieceDisplayCol = 0;
            int pieceDisplayRow = 0;
            int piecesPerRow = 2; // How many pieces to show per row in reserve

            for (Piece piece : player.reserve()) {
                double x = startX + pieceDisplayCol * (pieceSize + spacing);
                double y = startY + pieceDisplayRow * (pieceSize + spacing);

                Rectangle r = new Rectangle(x, y, pieceSize, pieceSize);
                clickablePieces.put(r, piece);

                gc.setFill(playerColorsFx[i % playerColorsFx.length]);
                gc.fillOval(x, y, pieceSize, pieceSize);
                gc.setStroke(Color.BLACK);
                gc.strokeOval(x, y, pieceSize, pieceSize);

                if (piece.equals(controller.getSelectedPiece())) { 
                    gc.setStroke(Color.DARKRED); 
                    gc.setLineWidth(2.5);
                    gc.strokeOval(x - 2, y - 2, pieceSize + 4, pieceSize + 4);
                    gc.setLineWidth(1.0); // Reset line width
                }

                pieceDisplayCol++;
                if (pieceDisplayCol >= piecesPerRow) {
                    pieceDisplayCol = 0;
                    pieceDisplayRow++;
                }
            }
        }

        // Active pieces on board nodes
        System.out.println("BoardPane: reDraw() called. Checking piece positions:");
        for (Player player : players) {
            for (Piece piece : player.active()) {
            	System.out.println("Player " + player.id() + ", Piece at " + piece.position());
                BoardNode pos = piece.position();
                if (pos == null) continue;

                Point2D p = coordinates.get(pos);
                if (p == null) continue;

                // Adjust position slightly for pieces on node to avoid overlap if multiple pieces
                // This is a simple offset; more complex stacking logic would be needed for true stacking visuals
                double x = p.getX() - activePieceSize / 2;
                double y = p.getY() - activePieceSize / 2;

                Rectangle r = new Rectangle(x, y, activePieceSize, activePieceSize);
                clickablePieces.put(r, piece);

                gc.setFill(getPlayerColor(player)); // Get JavaFX Color
                gc.fillOval(x, y, activePieceSize, activePieceSize);
                gc.setStroke(Color.BLACK);
                gc.strokeOval(x, y, activePieceSize, activePieceSize);

                if (piece.equals(controller.getSelectedPiece())) { // Use controller
                    gc.setStroke(Color.DARKRED);
                    gc.setLineWidth(2.5);
                    gc.strokeOval(x - 2, y - 2, activePieceSize + 4, activePieceSize + 4);
                    gc.setLineWidth(1.0);
                }
            }
        }
    }

    private Color getPlayerColor(Player p) { // Returns JavaFX Color
        int index = controller.getPlayers().indexOf(p); // Get players from controller
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.CYAN}; // JavaFX Colors
        return colors[index % colors.length];
    }

    // GameListener methods
    @Override
    public void pieceMoved(PieceMovedEvent e) { Platform.runLater(() -> {
        reDraw();
    }); }
//    @Override
//    public void pieceMoved(PieceMovedEvent e) {
//        Platform.runLater(() -> {
//            System.out.println("BoardPane: pieceMoved event received. Player: " + e.player().id() + ", To: " + e.to());
//            // Check model state HERE, INSIDE Platform.runLater, right before reDraw
//            System.out.println("BoardPane (in runLater): Checking piece positions BEFORE reDraw:");
//            if (controller != null && controller.getPlayers() != null) { // Add null checks
//                for (Player player : controller.getPlayers()) {
//                    for (Piece piece : player.active()) {
//                        // Pay special attention to the piece involved in the event e.pieces()
//                        System.out.println("  Player " + player.id() + ", Piece " + piece.id() + " at " + piece.position());
//                    }
//                }
//            }
//            reDraw();
//            System.out.println("BoardPane: reDraw() called after pieceMoved (from FX thread).");
//        });
//    }
    @Override
    public void pieceCaptured(PieceCapturedEvent e) { Platform.runLater(() -> {
        reDraw();
    }); }
    @Override
    public void stackFormed(StackFormedEvent e) { Platform.runLater(() -> {
        reDraw();
    }); }

    // Add other GameListener stubs if your GameListener interface has more methods
    // (based on your Swing BoardPanel, it seems these three are the main ones triggering repaint)
//    @Override public void gameEnded(GameOverEvent e) { reDraw(); } // Example
//    @Override public void gameStarted(GameStartedEvent e) { reDraw(); } // Example
//    @Override public void turnChanged(TurnChangedEvent e) { reDraw(); } // Example
//    @Override public void yutThrown(YutThrownEvent e) { reDraw(); } // Example
}