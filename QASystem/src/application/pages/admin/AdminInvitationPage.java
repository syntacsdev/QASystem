package application.pages.admin;

import application.StartCSE360;
import application.User;
import application.obj.InviteCode;
import application.pages.WelcomeLoginPage;
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
 * InvitePage class represents the page where an admin can generate an
 * invitation code. The invitation code is displayed upon clicking a button.
 */

public class AdminInvitationPage {

	private final DatabaseHelper database;
	private final User user;

	public AdminInvitationPage(DatabaseHelper database, User user) {
		this.database = database;
		this.user = user;
	}

	/**
	 * Displays the Invite Page in the provided primary stage.
	 * 
	 * @param databaseHelper An instance of DatabaseHelper to handle database
	 *                       operations.
	 * @param primaryStage   The primary stage where the scene will be displayed.
	 */
	public void show(Stage primaryStage) {
		// Back button
		Button backButton = new Button("← Back");
		backButton.getStyleClass().add("back-button");

		// Logic for the back button
		backButton.setOnAction(_ -> {
			new WelcomeLoginPage(this.database).show(primaryStage, user);
		});

		// Container for the back button
		HBox topBar = new HBox(backButton);
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setPadding(new Insets(10, 10, 0, 10)); // top, right, bottom, left

		// Basic welcome header
		Label header1 = new Label("Generate Invitation Code");
		header1.getStyleClass().add("header-main");

		// A sub-header
		Label header2 = new Label("Each code generated can only be used once! Don't forget them.");
		header2.getStyleClass().add("header-sub");

		// Button to generate the invitation code
		Button makeCodeButton = new Button("Generate invitation code");
		makeCodeButton.getStyleClass().add("action-button");

		// Label to display the generated invitation code
		Label inviteCodeLabel = new Label("");
		inviteCodeLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");

		// Logic for the button to generate invitation codes
		makeCodeButton.setOnAction(_ -> {
			// Generate the invitation code using the databaseHelper and set it to the label
			InviteCode invCode = StartCSE360.getInviteCodeManager().createInviteCode();
			inviteCodeLabel.setText(invCode.getCode());
		});

		// Container for the main content
		VBox mainContent = new VBox(10);
		mainContent.getChildren().addAll(header1, header2, makeCodeButton, inviteCodeLabel);
		mainContent.setAlignment(Pos.CENTER);
		mainContent.setPrefSize(800, 600);

		// Organize the containers into a big container
		BorderPane root = new BorderPane();
		root.setTop(topBar);
		root.setCenter(mainContent);

		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/styles.css").toExternalForm());

		// Finalize the scene and show
		primaryStage.setTitle("Question/Answer System - Generate Invitation Code");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}