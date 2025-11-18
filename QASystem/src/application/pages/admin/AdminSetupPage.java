package application.pages.admin;

import application.StartCSE360;
import application.User;
import application.UserRole;
import application.eval.EmailEvaluator;
import application.eval.NameEvaluator;
import application.eval.PasswordEvaluator;
import application.eval.UserNameRecognizer;
import application.pages.WelcomeLoginPage;
import application.util.LogUtil;
import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The SetupAdmin class handles the setup process for creating an administrator
 * account. This is intended to be used by the first user to initialize the
 * system with admin credentials.
 */
public class AdminSetupPage {

	private final DatabaseHelper database;

	public AdminSetupPage(DatabaseHelper databaseHelper) {
		this.database = databaseHelper;
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

		// A basic header
		Label header1 = new Label("Create Administrator Account");
		header1.getStyleClass().add("header-main");

		// A sub-header
		Label header2 = new Label("We'll need some details.");
		header2.getStyleClass().add("header-sub");

		// Create the top row
		VBox topRow = new VBox(5, backButtonBox, header1, header2);
		topRow.setPadding(new Insets(0, 0, 20, 0));
		topRow.setAlignment(Pos.CENTER);

		// Using a GridPane to collect details
		GridPane form = new GridPane();
		form.setHgap(15);
		form.setVgap(15);
		form.setPadding(new Insets(20));
		form.setAlignment(Pos.CENTER);

		Label usernameLabel = new Label("Username:");
		TextField usernameField = new TextField();

		Label passwordLabel = new Label("Password:");
		PasswordField passwordField = new PasswordField();

		Label firstNameLabel = new Label("First Name:");
		TextField firstNameField = new TextField();

		Label lastNameLabel = new Label("Last Name:");
		TextField lastNameField = new TextField();

		Label emailLabel = new Label("Email:");
		TextField emailField = new TextField();

		// Place controls into grid
		form.addRow(0, usernameLabel, usernameField);
		form.addRow(1, passwordLabel, passwordField);
		form.addRow(2, firstNameLabel, firstNameField);
		form.addRow(3, lastNameLabel, lastNameField);
		form.addRow(4, emailLabel, emailField);

		// Register button
		Button registerButton = new Button("Register");
		registerButton.setPrefWidth(120);

		// Error label
		Label errorLabel = new Label();
		errorLabel.getStyleClass().add("error-label");

		// Wrap form + button in a VBox
		VBox centerRow = new VBox(15, form, registerButton, errorLabel);
		centerRow.getStyleClass().add("background");
		centerRow.setAlignment(Pos.CENTER);
		// centerRow.setPadding(new Insets(30));

		// Register button logic
		registerButton.setOnAction(_ -> {
			// Retrieve input
			String userName = usernameField.getText();
			String password = passwordField.getText();
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String email = emailField.getText();

			// Validate username
			String userValidationResult = UserNameRecognizer.checkForValidUserName(userName);
			if (!userValidationResult.isEmpty()) {
				errorLabel.setText(userValidationResult);
				return;
			}

			// Validate password
			String passValidationResult = PasswordEvaluator.evaluatePassword(password);
			if (!passValidationResult.isEmpty()) {
				errorLabel.setText(passValidationResult);
				return;
			}

			// Validate the first name
			String firstNameResult = NameEvaluator.evaluateName(firstName);
			if (!firstNameResult.isEmpty()) {
				errorLabel.setText(firstNameResult);
				return;
			}

			// Validate the last name
			String lastNameResult = NameEvaluator.evaluateName(lastName);
			if (!lastNameResult.isEmpty()) {
				errorLabel.setText(lastNameResult);
				return;
			}

			// Validate the email address
			String emailResult = EmailEvaluator.evaluateEmail(email);
			if (!emailResult.isEmpty()) {
				errorLabel.setText(emailResult);
				return;
			}

			// Check if the user already exists
			if (!database.doesUserExist(userName)) {
				// Create a new user and register them in the database
				User user = this.database.createUser(userName, password, firstName, lastName, email, UserRole.ADMIN);
				StartCSE360.setCurrentUser(user);

				// Navigate user to welcome page
				new WelcomeLoginPage(database).show(primaryStage, user);
			} else
				errorLabel.setText("Unfortunately, that username is taken.");

		});

		// Create disclaimer label
		Label disclaimer = new Label("For educational purposes only.");
		disclaimer.getStyleClass().add("disclaimer");

		// Create container for bottom row
		VBox bottomRow = new VBox(5, disclaimer);
		bottomRow.setAlignment(Pos.CENTER);

		// Create the final root object that will be passed into Scene constructor
		BorderPane root = new BorderPane();
		root.setTop(topRow);
		root.setCenter(centerRow);
		root.setBottom(bottomRow);
		root.getStyleClass().add("background");
		root.setPrefSize(600, 400);

		// Finalize scene and show
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Question/Answer System - User Registration Page");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}
}
