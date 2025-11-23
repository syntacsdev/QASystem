package application.pages.student;

import java.util.Set;

import application.User;
import application.pages.SetupLoginSelectionPage;
import application.util.LogUtil;
import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentHomePageNew {

	private final DatabaseHelper database;
	private final User user;

	public StudentHomePageNew(DatabaseHelper database, User user) {
		this.database = database;
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
		backButtonBox.setPadding(new Insets(10, 10, 0, 10));
		backButtonBox.getStyleClass().add("background-back-button");

		// Basic welcome header
		Label header1 = new Label("Welcome, " + this.user.getUserName() + "!");
		header1.getStyleClass().add("header-main");

		// A simple sub-header
		Label header2 = new Label("Select your destination.");
		header2.getStyleClass().add("header-sub");

		// Container for the top section
		VBox topSection = new VBox(5, backButtonBox, header1, header2);
		topSection.getStyleClass().add("background-top");

		// Buttons for navigation
		Button questionListBtn = new Button("View question list");
		questionListBtn.getStyleClass().add("big-button");
		Button messagesBtn = new Button("View messages");
		messagesBtn.getStyleClass().add("big-button");
		Button reviewReqBtn = new Button("Request reviewer role");
		reviewReqBtn.getStyleClass().add("big-button");
		Button settingsBtn = new Button("Settings");
		settingsBtn.getStyleClass().add("action-button");

		// Behavior for reviewReqBtn will be slightly different if it's already been
		// requested
		final boolean alreadyRequestedReviewer;
		Set<String> pendingNames = this.database.getPendingReviewerNames();
		if (pendingNames.contains(this.user.getUserName()))
			alreadyRequestedReviewer = true;
		else
			alreadyRequestedReviewer = false;

		// Modify reviewReqBtn if user has already requested approval
		reviewReqBtn.setText("Reviewer request pending");

		// Question list button logic
		questionListBtn.setOnAction(_ -> {
			StudentQuestionListPage target = new StudentQuestionListPage(this.database, this.user);
			target.show(primaryStage);
		});

		// Messages button logic
		messagesBtn.setOnAction(_ -> {
			StudentMessagesPage target = new StudentMessagesPage(this.database, this.user);
			target.show(primaryStage);
		});

		// Review request button logic
		reviewReqBtn.setOnAction(_ -> {
			if (!alreadyRequestedReviewer) {
				this.database.insertIntoPendingReviewers(this.user.getUserName());
				reviewReqBtn.setText("Reviewer request sent");
			} else
				return; // Do nothing if user has already requested reviewer role
		});

		// Settings button logic
		settingsBtn.setOnAction(_ -> {
			StudentSettingsPage target = new StudentSettingsPage(this.database, this.user);
			target.show(primaryStage);
		});

		// Container for center section
		VBox centerSection = new VBox(5, questionListBtn, messagesBtn, reviewReqBtn, settingsBtn);
		centerSection.getStyleClass().add("background-center");

		// Disclaimer
		Label disclaimer = new Label("For educational purposes only.");
		disclaimer.getStyleClass().add("disclaimer");

		// Container for bottom section
		VBox bottomSection = new VBox(5, disclaimer);
		bottomSection.getStyleClass().add("background-bottom");

		// Container for all the elements
		BorderPane root = new BorderPane();
		root.setTop(topSection);
		root.setCenter(bottomSection);
		root.setBottom(bottomSection);
		root.getStyleClass().add("background");
		root.setPrefSize(600, 400);

		// Finalize and show
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Question/Answer System - Student Home Page");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}

}
