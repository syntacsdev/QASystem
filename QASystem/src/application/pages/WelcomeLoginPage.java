package application.pages;

import application.User;
import application.UserRole;
import application.pages.admin.AdminHomePage;
import application.pages.admin.AdminInvitationPage;
import application.pages.instructor.InstructorHomePage;
import application.pages.staff.StaffHomePage;
import application.pages.student.StudentHomePage;
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
 * The WelcomeLoginPage class displays a welcome screen for authenticated users.
 * It allows users to navigate to their respective pages based on their role or
 * quit the application.
 */
public class WelcomeLoginPage {

	private final DatabaseHelper databaseHelper;

	public WelcomeLoginPage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

	public void show(Stage primaryStage, User user) {
		// Back button
		Button backButton = new Button("← Sign out");
		backButton.getStyleClass().add("back-button");

		// Back button logic
		backButton.setOnAction(_ -> {
			SetupLoginSelectionPage target = new SetupLoginSelectionPage(this.databaseHelper);
			target.show(primaryStage);
		});

		// Container for the back button
		HBox backButtonBox = new HBox(backButton);
		backButtonBox.setAlignment(Pos.CENTER_LEFT);
		backButtonBox.setPadding(new Insets(10, 10, 0, 10));
		backButtonBox.getStyleClass().add("background");

		// Basic header
		Label header1 = new Label("Success!");
		header1.getStyleClass().add("header-main");

		// Basic sub-header
		Label header2 = new Label("Click the button below to continue.");
		header2.getStyleClass().add("header-sub");

		// Continue button
		Button moveBtn = new Button("Proceed");
		moveBtn.getStyleClass().add("action-button");

		// Continue button logic
		moveBtn.setOnAction(_ -> {
			UserRole role = user.getRole();
			LogUtil.debug("User has logged in as " + role.toString().toLowerCase());
			switch (role) {
			case ADMIN:
				new AdminHomePage().show(primaryStage);
				break;
			case STUDENT:
			case REVIEWER: // reviewers go to the same home page but get extra navigation
				new StudentHomePage().show(primaryStage);
				break;
			case INSTRUCTOR:
				new InstructorHomePage().show(primaryStage);
				break;
			case STAFF:
				new StaffHomePage(databaseHelper, user).show(primaryStage);
				break;
			default:
				new StudentHomePage().show(primaryStage);
				break;
			}
		});

		// Container for center row (headers + button)
		VBox centerRow = new VBox(5, header1, header2, moveBtn);
		centerRow.getStyleClass().add("background");
		centerRow.setAlignment(Pos.CENTER);

		// Disclaimer
		Label disclaimer = new Label("For educational use only.");
		disclaimer.getStyleClass().add("disclaimer");

		// Container for bottom row (disclaimer)
		VBox bottomRow = new VBox(5, disclaimer);
		bottomRow.getStyleClass().add("background");
		bottomRow.setAlignment(Pos.CENTER);

		// TODO: Temp button for Admins to generate invite codes
		// At some point, I'd like to move this into AdminHomePage
		Button inviteBtn = new Button("ADMIN: Invite a new user");
		inviteBtn.getStyleClass().add("action-button");

		// Invite code button logic
		inviteBtn.setOnAction(_ -> {
			AdminInvitationPage target = new AdminInvitationPage(this.databaseHelper, user);
			target.show(primaryStage);
		});

		// Only show the invite button for Admins
		if (user.getRole() == UserRole.ADMIN) {
			centerRow.getChildren().add(inviteBtn);
		}

		// Root container
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
		primaryStage.setTitle("Question/Answer System - Login Successful");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}
}
