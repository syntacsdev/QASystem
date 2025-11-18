package application.pages.instructor;

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

public class InstructorHomePage {

	private final DatabaseHelper database;
	private final User user;
	
	public InstructorHomePage(DatabaseHelper helper, User user) {
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
		Label header2 = new Label("Select the page you want to visit.");
		header2.getStyleClass().add("header-sub");
		
		// Container for top row
		VBox topRow = new VBox(5, backButtonBox, header1, header2);
		topRow.getStyleClass().add("background");
		topRow.setAlignment(Pos.CENTER);

		// Navigation button(s)
		Button requestsPageBtn = new Button("Reviewer Requests");
		requestsPageBtn.getStyleClass().add("action-button");
		
		// Reviewer requests button logic
		requestsPageBtn.setOnAction(_ -> {
			InstructorViewRequestsPage target = new InstructorViewRequestsPage(this.database, this.user);
			target.show(primaryStage);
		});

		// Container for the center row
		VBox centerRow = new VBox(5, requestsPageBtn);
		centerRow.getStyleClass().add("backgroud");
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
		root.setTop(topRow);
		root.setCenter(centerRow);
		root.setBottom(bottomRow);
		root.getStyleClass().add("background");
		root.setPrefSize(600, 400);

		// Finalize and show
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Question/Answer System - Instructor Home Page");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());


	}
}
