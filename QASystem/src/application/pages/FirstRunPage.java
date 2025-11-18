package application.pages;

import application.pages.admin.AdminSetupPage;
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
 * FirstPage class represents the initial screen for the first user. It prompts
 * the user to set up administrator access and navigate to the setup screen.
 */
public class FirstRunPage {

	// For database interactions
	private final DatabaseHelper database;

	public FirstRunPage(DatabaseHelper databaseHelper) {
		this.database = databaseHelper;
	}

	/**
	 * Displays the first page in the provided primary stage.
	 * 
	 * @param primaryStage The primary stage where the scene will be displayed.
	 */
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

		// A basic header
		Label header1 = new Label("First Run Setup");
		header1.getStyleClass().add("header-main");

		// A sub-header
		Label header2 = new Label("Create an admin account to complete setup.");
		header2.getStyleClass().add("header-sub");

		// Container for top row (back button + headers)
		VBox topRow = new VBox(5, backButtonBox, header1, header2);
		topRow.setPadding(new Insets(0, 0, 20, 0));
		topRow.getStyleClass().add("background");
		topRow.setAlignment(Pos.CENTER);

		// Continue button
		Button continueBtn = new Button("Proceed to admin setup");
		continueBtn.getStyleClass().add("action-button");

		// Container for center row (just a button in this case)
		VBox centerRow = new VBox(5, continueBtn);
		centerRow.getStyleClass().add("background");
		centerRow.setAlignment(Pos.CENTER);

		// Button logic
		continueBtn.setOnAction(_ -> {
			AdminSetupPage target = new AdminSetupPage(this.database);
			target.show(primaryStage);
		});

		// Disclaimer
		Label disclaimer = new Label("For educational purposes only.");
		disclaimer.getStyleClass().add("disclaimer");

		// Container for bottom row (disclaimer)
		VBox bottomRow = new VBox(5, disclaimer);
		bottomRow.getStyleClass().add("background");
		bottomRow.setAlignment(Pos.CENTER);

		// Create root container
		BorderPane root = new BorderPane();
		root.setTop(topRow);
		root.setCenter(centerRow);
		root.setBottom(bottomRow);
		root.getStyleClass().add("background");
		root.setPrefSize(600, 400);

		// Create scene
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Question/Answer System - First Run Setup");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}
}