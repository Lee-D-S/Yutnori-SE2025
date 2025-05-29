package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Tester extends Application {
	@Override
	public void start(Stage primaryStage) {
		LandingView landingView = new LandingView();
		landingView.show(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}