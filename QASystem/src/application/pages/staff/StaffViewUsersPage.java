package application.pages.staff;

import application.StartCSE360;
import application.User;
import application.UserRole;
import application.util.LogUtil;
import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * A page allowing users with the {@link UserRole#STAFF} role to view all users
 * of the program.
 */
public class StaffViewUsersPage {

	private final DatabaseHelper database;
	private final User user;

	public StaffViewUsersPage(DatabaseHelper helper, User user) {
		this.database = helper;
		this.user = user;
	}

	@SuppressWarnings("unchecked")
	public void show(Stage primaryStage) {
		// Back button
		Button backButton = new Button("← Back");
		backButton.getStyleClass().add("back-button");

		// Back button logic
		backButton.setOnAction(_ -> {
			StaffHomePage target = new StaffHomePage(this.database, this.user);
			target.show(primaryStage);
		});

		// Container for the back button
		HBox backButtonBox = new HBox(backButton);
		backButtonBox.setAlignment(Pos.CENTER_LEFT);
		backButtonBox.setPadding(new Insets(10, 10, 0, 10));
		backButtonBox.getStyleClass().add("background");

		// A basic header
		Label header1 = new Label("Users Table View");
		header1.getStyleClass().add("header-main");

		// Sub header
		Label header2 = new Label("Data can only be viewed, not modified.");
		header2.getStyleClass().add("header-sub");

		// Create tableView
		TableView<User> tableView = new TableView<>();

		// Set up column for username
		TableColumn<User, String> userNameCol = new TableColumn<>("Username");
		userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));

		// Set up column for password
		TableColumn<User, String> passwordCol = new TableColumn<>("Password");
		passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

		// Set up column for first name
		TableColumn<User, String> firstNameCol = new TableColumn<>("First Name");
		firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

		// Set up column for last name
		TableColumn<User, String> lastNameCol = new TableColumn<>("Last Name");
		lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

		// Set up column for email
		TableColumn<User, String> emailCol = new TableColumn<>("Email Address");
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

		// Set up column for role
		TableColumn<User, String> roleCol = new TableColumn<>("Role");
		roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

		// Add the columns to the table
		tableView.getColumns().addAll(userNameCol, passwordCol, firstNameCol, lastNameCol, emailCol, roleCol);

		// Populate the table
		ObservableList<User> data = FXCollections.observableArrayList();
		for (User u : StartCSE360.getUserManager().getUserSet()) {
			data.add(u);
		}
		tableView.setItems(data);

		// Container for the center row
		VBox centerRow = new VBox(5, header1, header2, tableView);
		centerRow.getStyleClass().add("background");
		centerRow.setAlignment(Pos.CENTER);

		// Disclaimer
		Label disclaimer = new Label("For educational purposes only.");
		disclaimer.getStyleClass().add("disclaimer");

		// Container for the bottom row
		VBox bottomRow = new VBox(5, disclaimer);
		bottomRow.getStyleClass().add("background");
		bottomRow.setAlignment(Pos.CENTER);

		// Root container
		BorderPane root = new BorderPane();
		root.setTop(backButtonBox);
		root.setCenter(centerRow);
		root.setBottom(bottomRow);
		root.getStyleClass().add("background");
		root.setPrefSize(800, 600);

		// Finalize scene and stage
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Question/Answer System - Staff User Viewer");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}
}
