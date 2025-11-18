package application.pages;

import application.util.LogUtil;
import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The SetupLoginSelectionPage class allows users to choose between setting up a
 * new account or logging into an existing account. It provides two buttons for
 * navigation to the respective pages.
 */
public class SetupLoginSelectionPage {

	private final DatabaseHelper databaseHelper;

	public SetupLoginSelectionPage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

	public void show(Stage primaryStage) {
		// Back button
		Button backButton = new Button("← Exit");
		backButton.getStyleClass().add("back-button");

		// Back button logic
		backButton.setOnAction(_ -> {
			LogUtil.debug("Terminated via exit button");
			System.exit(0);
		});

		// Container for the back button
		HBox backButtonBox = new HBox(backButton);
		backButtonBox.setAlignment(Pos.CENTER_LEFT);
		backButtonBox.setPadding(new Insets(10, 10, 0, 10));
		backButtonBox.getStyleClass().add("background");

		// A basic welcome header
		Label header1 = new Label("Welcome!");
		header1.getStyleClass().add("header-main");

		// Smaller header
		Label header2 = new Label("Let's get you ready.");
		header2.getStyleClass().add("header-sub");

		// Buttons to select between login and registration, redirecting user flow into
		// correct direction
		Button loginButton = new Button("Log in");
		Button setupButton = new Button("Register new account");
		loginButton.getStyleClass().add("action-button");
		setupButton.getStyleClass().add("action-button");

		loginButton.setOnAction(_ -> {
			new UserLoginPage(databaseHelper).show(primaryStage);
		});
		setupButton.setOnAction(_ -> {
			new SetupAccountPage(databaseHelper).show(primaryStage);
		});

		// Container for center row (headers + buttons)
		VBox centerRow = new VBox(5, header1, header2, loginButton, setupButton);
		centerRow.getStyleClass().add("background");
		centerRow.setAlignment(Pos.CENTER);

		// Smaller text at the bottom
		Label disclaimer = new Label("For educational purposes only.");
		disclaimer.getStyleClass().add("disclaimer");

		// Container for bottom row (disclaimer)
		VBox bottomRow = new VBox(5, disclaimer);
		bottomRow.getStyleClass().add("background");
		bottomRow.setAlignment(Pos.CENTER);

		// Create root container
		BorderPane root = new BorderPane();
		root.setTop(backButtonBox);
		root.setCenter(centerRow);
		root.setBottom(bottomRow);
		root.getStyleClass().add("background");
		root.setPrefSize(600, 400);

		// Create scene
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Question/Answer System - Login/Register");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}
}
