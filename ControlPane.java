package application;

import javafx.scene.layout.VBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

import java.util.List;
import javafx.scene.control.Button;
import javafx.geometry.Insets;

public class ControlPane extends VBox  {
	private final GameManager manager;
    private final GameModel model;
    private final GridPane throwListPane;
    private final TitledPane throwTitledPane;
    private final TitledPane actionTitledPane;
    
	public ControlPane(GameManager manager,GameModel model) {
		 this.manager = manager;
	     this.model = model;
	     this.throwListPane = new GridPane();
	    

	        // ������ Button to throw Yut ������
	        Button throwButton = new Button("Throw Yut");
	        throwButton.setOnAction(e -> {
	            if (manager.getPhase() != GameManager.Phase.WAITING_FOR_THROW) {
	                model.fireLog("You must use all previous Yut results first.");
	                return;
	            }

	            YutResult result = Yut.throwRandom();
	            manager.addYutResult(result);
//	            model.yutThrown(new YutThrownEvent(model.currentPlayer(), result));
	            refreshThrowButtons();
	        });
	        
	        GridPane actionPane = new GridPane();
	        actionPane.setHgap(10);
	        actionPane.setVgap(10);
	        actionPane.setPadding(new Insets(20));
	        actionPane.add(throwButton, 0, 0);
	        actionTitledPane = new TitledPane("Actions", actionPane);
	        actionTitledPane.setCollapsible(false);
	        
	        // ������ List of Yut throws to apply ������
	        throwListPane.setPadding(new Insets(20));
	        throwTitledPane = new TitledPane("Available Throws", throwListPane);
	        throwTitledPane.setCollapsible(false);

	        // ������ Final layout ������
	        this.getChildren().addAll(actionTitledPane, throwTitledPane);

	
	}
	
	private void refreshThrowButtons() {
		int row = 0;
		throwListPane.getChildren().clear();

        List<YutResult> available = manager.getAvailableThrows();
        for (YutResult result : available) {
            Button resultButton = new Button(result.toString());
            resultButton.setOnAction(e -> {
                try {
                    manager.applyThrow(result);
                    refreshThrowButtons();  // update UI after applying
                } catch (Exception ex) {
                    model.fireLog("Error: " + ex.getMessage());
                }
            });
            throwListPane.add(resultButton, 0, row++);
        }
	}
}
