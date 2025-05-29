package application;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.TitledPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

import java.util.*;

public class BoardPane extends TitledPane implements GameListener {
    private BoardLayout layout;
    private Map<BoardNode, Point2D> coordinates = new HashMap<>();
    private GameModel model;
    private GameManager manager;
    private final Map<Rectangle, Piece> clickablePieces = new HashMap<>();
    private Piece selectedPiece = null;
    private Canvas canvas;
    private final Pane board;

    public BoardPane(GameModel model, GameManager manager) {
        this.model = model;
        this.manager = manager;
        this.layout = model.board().layout();
        this.setText("Game Board"); 

        board = new Pane();
        board.setPrefSize(700, 500);
        board.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");

        canvas = new Canvas(700, 500);
        board.getChildren().add(canvas);
        this.setContent(board);

        assignCoordinates(layout.type());
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
            	int x = (int)e.getX();
                int y = (int)e.getY();
                for (Map.Entry<Rectangle, Piece> entry : clickablePieces.entrySet()) {
                    if (entry.getKey().contains(x,y)) {
                        selectedPiece = entry.getValue();
                    	manager.selectPiece(entry.getValue());  // <- calls GameManager
                    	reDraw();
                        break;
                    }
                }
            }
        });
        reDraw();
    }

    private void assignCoordinates(BoardType type) {
        List<BoardNode> allNodes = layout.buildNodes();
        int cx = 250, cy = 250, radius = 150;

        if (type == BoardType.SQUARE) {
            double[][] grid = {
                {5,5}, {5,4}, {5,3}, {5,2}, {5,1}, 
                {5,0}, {4,0}, {3,0}, {2,0}, {1,0}, 
                {0,0}, {0,1}, {0,2}, {0,3}, {0,4}, 
                {0,5}, {1,5}, {2,5}, {3,5}, {4,5},
                {4.2,0.8}, {3.4,1.6}, {0.8,0.8}, {1.6,1.6}, {2.5,2.5}, {1.6,3.4}, {0.8,4.2}, {3.4,3.4}, {4.2,4.2}  
            };
            int i = 0;
            for (BoardNode node : allNodes) {
                if (i >= grid.length) break;
                coordinates.put(node, new Point2D((int)(60 + 70 * grid[i][0]), (int)(40 + 70 * grid[i][1])));
                i++;
            }
   
        } else if (type == BoardType.PENTAGON) {
        	int outerSize = 25;
    	    int shortcut = 5;  
    	    int i = 0;

            for (; i < outerSize; i++) {
                double angle = -2 * Math.PI * i / outerSize;
                int x = cx + (int)(radius * Math.cos(angle));
    	        int y = cy + (int)(radius * Math.sin(angle));
                coordinates.put(allNodes.get(i), new Point2D(x, y));
            }

            for (int j = 0; j < shortcut; j++) {
                double baseAngle = - (2 * Math.PI / shortcut + 2 * Math.PI * j / shortcut);
                
                for (int k = 0; k < 2; k++) {
                    double step = (k + 1) / 3.0;
                    double x = cx + (radius * (1 - step)) * Math.cos(baseAngle);
                    double y = cy + (radius * (1 - step)) * Math.sin(baseAngle);
                    coordinates.put(allNodes.get(i++), new Point2D(x, y));
                }
            }

            // Center node
            coordinates.put(allNodes.get(i), new Point2D(cx, cy));
        } else {
            int outerSize = 30;
            int shortcut = 6;
            int i = 0;

            // 30 outer ring nodes
            for (; i < outerSize; i++) {
                double angle = - 2 * Math.PI * i / outerSize;
                double x = cx + (int)(radius * Math.cos(angle));
                double y = cy + (int)(radius * Math.sin(angle));
                coordinates.put(allNodes.get(i), new Point2D(x, y));
            }

            // 6 shortcut paths with 2 nodes each
            for (int j = 0; j < shortcut; j++) {
                double baseAngle = - (Math.PI * 2 / shortcut + 2 * Math.PI * j / shortcut);

                for (int k = 0; k < 2; k++) {
                    double step = (k + 1) / 3.0;  // 1/3 and 2/3 into the radius
                    int x = cx + (int)(radius * (1 - step) * Math.cos(baseAngle));
                    int y = cy + (int)(radius * (1 - step) * Math.sin(baseAngle));
                    coordinates.put(allNodes.get(i++), new Point2D(x, y));
                }
            }

            // 1 center node
            coordinates.put(allNodes.get(i), new Point2D(cx, cy));
        }

    }
  

    private void reDraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw paths
        gc.setStroke(Color.BLACK);
        for (BoardNode node : coordinates.keySet()) {
            Point2D p = coordinates.get(node);
            for (BoardNode next : node.nextNodes()) {
                Point2D q = coordinates.get(next);
                if (q != null) gc.strokeLine(p.getX(), p.getY(), q.getX(), q.getY());
            }
        }
        Point2D p1 = coordinates.get(layout.startNode());
        Point2D p2 = coordinates.get(layout.entryNode().nextNodes().get(0));

        if (p1 != null && p2 != null) {
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }

        for (Map.Entry<BoardNode, Point2D> entry : coordinates.entrySet()) {
            BoardNode node = entry.getKey();
            Point2D p = entry.getValue();

            if (node == layout.startNode()) {
                gc.setFill(Color.YELLOW);  // Highlight start node
            } else {
                gc.setFill(Color.WHITE);
            }

            gc.fillOval(p.getX() - 10, p.getY() - 10, 20, 20);
            gc.setFill(Color.BLACK);
            gc.strokeOval(p.getX() - 10, p.getY() - 10, 20, 20);
        }

        // Draw pieces
        clickablePieces.clear();
        drawPieces(gc);
    }

    private void drawPieces(GraphicsContext gc) {
        int pieceSize = 20;
        int spacing = 10;
        int playerSpacing = 120;
        int labelOffset = 15;
        Color[] playerColors = { Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE };

        for (int i = 0; i < model.players().size(); i++) {
            Player player = model.players().get(i);
            int playerCol = i % 2;
            int playerRow = i / 2;

            int startX = 500 + playerCol * playerSpacing;
            int startY = 60 + playerRow * playerSpacing;

            gc.setFill(Color.BLACK);
            gc.fillText(player.id(), startX, startY - labelOffset);

            int pieceCol = 0;
            int pieceRow = 0;

            for (Piece piece : player.reserve()) {
                int x = startX + pieceCol * (pieceSize + spacing);
                int y = startY + pieceRow * (pieceSize + spacing);

                Rectangle r = new Rectangle(x, y, pieceSize, pieceSize);
                clickablePieces.put(r, piece);

                gc.setFill(playerColors[i % playerColors.length]);
                gc.fillOval(x, y, pieceSize, pieceSize);
                gc.setStroke(Color.BLACK);
                gc.strokeOval(x, y, pieceSize, pieceSize);
                
                if (piece.equals(selectedPiece)) {
                	 gc.setStroke(Color.RED);
                     gc.setLineWidth(2);
                     gc.strokeOval(x - 2, y - 2, pieceSize + 4, pieceSize + 4);
                     gc.setLineWidth(1);
                }

                pieceCol++;
                if (pieceCol >= 2) {
                    pieceCol = 0;
                    pieceRow++;
                }
            }
            
            for (Player player1 : model.players()) {
            	System.out.println("Active pieces for " + player1.id() + ": " + player1.active().size());
            	for (Piece piece : player1.active()) {
            	    BoardNode pos = piece.position();
            	    if (pos == null) continue;

            	    Point2D p = coordinates.get(pos);
            	    if (p == null) continue;

            	    int x = (int)p.getX() - 8;
            	    int y = (int)p.getY() - 8;
            	    int size = 16;

            	    Rectangle r = new Rectangle(x, y, size, size);
            	    clickablePieces.put(r, piece);  

            	    gc.setFill(getPlayerColor(player1));
                    gc.fillOval(x, y, size,size);
                    gc.setStroke(Color.BLACK);
                    gc.strokeOval(x, y, size, size);

            	    //  highlight if selected
            	    if (piece.equals(selectedPiece)) {
            	    	gc.setStroke(Color.RED);
                        gc.setLineWidth(2);
                        gc.strokeOval(x - 2, y - 2, 20, 20);
                        gc.setLineWidth(1);
            	    }
            	}

            }
        }
    }
    private Color getPlayerColor(Player p) {
        int index = model.players().indexOf(p);
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE};
        return colors[index % colors.length];
    }


    @Override
    public void pieceMoved(PieceMovedEvent e) {
    	reDraw();
    }

    @Override
    public void pieceCaptured(PieceCapturedEvent e) {
    	reDraw();
    }

    @Override
    public void stackFormed(StackFormedEvent e) {
    	reDraw();
    }
}

