package application.pages.reviewer;

import application.User;
import application.pages.SetupLoginSelectionPage;
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

public class ReviewerHomePage {

	private final DatabaseHelper database;
	private final User user;
	
	public ReviewerHomePage(DatabaseHelper helper, User user) {
		this.database = helper;
		this.user = user;
	}
	
	public void show(Stage primaryStage) {
		// Back button
		Button backButton = new Button("← Sign out");
		backButton.getStyleClass().add("back-button");

		// Back button logic
		backButton.setOnAction(_ -> {
			SetupLoginSelectionPage target = new SetupLoginSelectionPage(this.database);
			target.show(primaryStage);
		});

		// Container for the back button
		HBox backButtonBox = new HBox(backButton);
		backButtonBox.setAlignment(Pos.CENTER_LEFT);
		backButtonBox.setPadding(new Insets(10, 10, 0, 10));
		backButtonBox.getStyleClass().add("background");

		// Basic welcome header
		Label header1 = new Label("Welcome, " + this.user.getUserName() + "!");
		header1.getStyleClass().add("header-main");

		// A simple sub-header
		Label header2 = new Label("Select your destination.");
		header2.getStyleClass().add("header-sub");
		
		// Container for top row
		VBox topRow = new VBox(5, backButtonBox, header1, header2);
		topRow.getStyleClass().add("background");
		topRow.setAlignment(Pos.CENTER);
		
		// Navigation buttons
		Button questionListBtn = new Button("View Question List");
		questionListBtn.getStyleClass().add("action-button");
		
		Button profileBtn = new Button("Your Reviewer Profile");
		profileBtn.getStyleClass().add("action-button");
		
		Button reviewsBtn = new Button("Your Reviews");
		reviewsBtn.getStyleClass().add("action-button");
		
		Button messagesBtn = new Button("Your Inbox");
		messagesBtn.getStyleClass().add("action-button");
		
		// Button logic - TODO
		
		// Center row container
		VBox centerRow = new VBox(5, questionListBtn, profileBtn, reviewsBtn, messagesBtn);
		centerRow.getStyleClass().add("background");
		centerRow.setAlignment(Pos.CENTER);
		
		// Disclaimer
		Label disclaimer = new Label("For educational purposes only.");
		disclaimer.getStyleClass().add("disclaimer");

		// Container for bottom row
		VBox bottomRow = new VBox(5, disclaimer);
		bottomRow.getStyleClass().add("background");
		bottomRow.setAlignment(Pos.CENTER);

		// Container for all the elements
		BorderPane root = new BorderPane();
		root.setTop(backButtonBox);
		root.setCenter(centerRow);
		root.setBottom(bottomRow);
		root.getStyleClass().add("background");
		root.setPrefSize(600, 400);

		// Finalize and show
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Question/Answer System - Staff Home Page");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}
}
