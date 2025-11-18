package application.pages;

import java.sql.SQLException;

import application.StartCSE360;
import application.User;
import application.UserRole;
import application.eval.PasswordEvaluator;
import application.eval.UserNameRecognizer;
import application.util.LogUtil;
import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserLoginPage {

	private final DatabaseHelper databaseHelper;

	public UserLoginPage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

	public void show(Stage primaryStage) {
		// Back button
		Button backButton = new Button("← Back");
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

		// A basic header
		Label header1 = new Label("Entry Point");
		header1.getStyleClass().add("header-main");

		// A sub header
		Label header2 = new Label("Let's get you signed in.");
		header2.getStyleClass().add("header-sub");

		// Username field
		TextField userNameField = new TextField();
		userNameField.setPromptText("Enter your user name");
		userNameField.setMaxWidth(250);

		// Password field
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter your password");
		passwordField.setMaxWidth(250);

		// Container for user/pass input
		VBox loginRow = new VBox(5, userNameField, passwordField);
		loginRow.setAlignment(Pos.CENTER);
		loginRow.getStyleClass().add("background");

		// Checkbox to enable dev login
		CheckBox enableDevLogin = new CheckBox("Enable dev mode");
		enableDevLogin.setStyle("-fx-font-size: 11px; -fx-opacity: 0.9;");

		// Dropdown selector for roles
		ComboBox<String> roleSelector = new ComboBox<>();
		roleSelector.getItems().addAll("Student", "Reviewer", "Instructor", "Staff", "Admin");
		roleSelector.setValue("Student");

		// Disable the dropdown unless the checkbox is selected
		roleSelector.setDisable(true);
		enableDevLogin.setOnAction(_ -> {
			roleSelector.setDisable(!enableDevLogin.isSelected());
		});

		// Container for the dev login elements
		HBox devLoginRow = new HBox(5, enableDevLogin, roleSelector);
		devLoginRow.setAlignment(Pos.CENTER);

		// Container for the header elements and login containers (lol)
		VBox centerRow = new VBox(header1, header2, loginRow, devLoginRow);
		centerRow.getStyleClass().add("background");
		centerRow.setAlignment(Pos.CENTER);

		Button loginButton = new Button("Login");
		loginButton.getStyleClass().add("action-button");

		// Error label
		Label errorLabel = new Label();
		errorLabel.getStyleClass().add("error-label");

		Label disclaimer = new Label("For educational purposes only.");
		disclaimer.getStyleClass().add("disclaimer");

		// Container for the login button, error label, and disclaimer
		VBox bottomRow = new VBox(5, loginButton, errorLabel, disclaimer);
		bottomRow.setAlignment(Pos.CENTER);

		// Create the final root object that will be passed into Scene constructor
		BorderPane root = new BorderPane();
		root.setTop(backButtonBox);
		root.setCenter(centerRow);
		root.setBottom(bottomRow);
		root.getStyleClass().add("background");
		root.setPrefSize(600, 400);

		// Login button behavior
		loginButton.setOnAction(_ -> {
			String userName = userNameField.getText().trim();
			String password = passwordField.getText().trim();

			// Implement dev login
			if (enableDevLogin.isSelected()) {
				UserRole selectedRole = UserRole.valueOf(roleSelector.getValue().toUpperCase());
				User devUser = new User("dev", "dev", selectedRole);
				StartCSE360.setCurrentUser(devUser);
				new WelcomeLoginPage(databaseHelper).show(primaryStage, devUser);
				return;
			}

			// Normal login flow
			String userValidationResult = UserNameRecognizer.checkForValidUserName(userName);
			if (!userValidationResult.isEmpty()) {
				errorLabel.setText(userValidationResult);
				return;
			}

			String passValidationResult = PasswordEvaluator.evaluatePassword(password);
			if (!passValidationResult.isEmpty()) {
				errorLabel.setText(passValidationResult);
				return;
			}

			try {
				UserRole role = databaseHelper.getUserRole(userName);
				if (role == null) {
					errorLabel.setText("User account doesn't exist or is invalid");
					return;
				}

				User user = new User(userName, password, role);
				if (databaseHelper.login(user)) {
					StartCSE360.setCurrentUser(user);
					new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
				} else {
					errorLabel.setText("Error logging in: invalid credentials");
				}
			} catch (SQLException e) {
				errorLabel.setText("Database error occurred");
				e.printStackTrace();
			}
		});

		// Finalize scene and show
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Question/Answer System - Login Page");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}
}
