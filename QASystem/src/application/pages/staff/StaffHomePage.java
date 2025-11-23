package application.pages.staff;

import application.User;
import application.UserRole;
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

/**
 * Serves as the landing page for {@link User}s whose role is
 * {@link UserRole#STAFF}.
 */
public class StaffHomePage {

	private final DatabaseHelper database;
	private final User user;

	public StaffHomePage(DatabaseHelper database, User user) {
		this.database = database;
		this.user = user;
	}

	public void show(Stage primaryStage) {
		// --- START OF TOP ROW
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
		Label header2 = new Label("Select which dataset you'd like to view.");
		header2.getStyleClass().add("header-sub");

		// Container for top row
		VBox topRow = new VBox(5, backButtonBox, header1, header2);
		topRow.getStyleClass().add("background");
		topRow.setAlignment(Pos.CENTER);

		// Navigation buttons
		Button usersPageBtn = new Button("Users");
		usersPageBtn.getStyleClass().add("action-button");

		Button questionsPageBtn = new Button("Questions");
		questionsPageBtn.getStyleClass().add("action-button");

		Button answersPageBtn = new Button("Answers");
		answersPageBtn.getStyleClass().add("action-button");

		Button reviewsPageBtn = new Button("Reviews");
		reviewsPageBtn.getStyleClass().add("action-button");

		Button reviewReqsPageBtn = new Button("Reviewer Requests");
		reviewReqsPageBtn.getStyleClass().add("action-button");

		Button messagesPageBtn = new Button("Messages");
		messagesPageBtn.getStyleClass().add("action-button");

		Button inviteCodesPageBtn = new Button("Invitation Codes");
		inviteCodesPageBtn.getStyleClass().add("action-button");

		// Logic for users page button
		usersPageBtn.setOnAction(_ -> {
			StaffViewUsersPage target = new StaffViewUsersPage(this.database, this.user);
			target.show(primaryStage);
		});

		// Logic for questions page button
		questionsPageBtn.setOnAction(_ -> {
			StaffViewQuestionsPage target = new StaffViewQuestionsPage(this.database, this.user);
			target.show(primaryStage);
		});

		// Logic for answers page button
		answersPageBtn.setOnAction(_ -> {
			StaffViewAnswersPage target = new StaffViewAnswersPage(this.database, this.user);
			target.show(primaryStage);
		});

		// Logic for reviews page button
		reviewsPageBtn.setOnAction(_ -> {
			StaffViewReviewsPage target = new StaffViewReviewsPage(this.database, this.user);
			target.show(primaryStage);
		});

		// Logic for reviewer requests page button
		reviewReqsPageBtn.setOnAction(_ -> {
			StaffViewReviewRequestsPage target = new StaffViewReviewRequestsPage(this.database, this.user);
			target.show(primaryStage);
		});

		// Logic for invitation codes button
		inviteCodesPageBtn.setOnAction(_ -> {
			StaffViewInviteCodesPage target = new StaffViewInviteCodesPage(this.database, this.user);
			target.show(primaryStage);
		});

		// Container for center row
		VBox centerRow = new VBox(5, usersPageBtn, questionsPageBtn, answersPageBtn, reviewsPageBtn, reviewReqsPageBtn,
				inviteCodesPageBtn);
		centerRow.getStyleClass().add("background");
		centerRow.setAlignment(Pos.CENTER);

		// --- START OF BOTTOM ROW

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
		primaryStage.setTitle("Question/Answer System - Staff Home Page");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}
}
