package application.pages.staff;

import application.StartCSE360;
import application.User;
import application.UserRole;
import application.obj.Question;
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
 * Allows {@link User}s whose role is {@link UserRole#STAFF} to view the
 * Questions table.
 */
public class StaffViewQuestionsPage {

	private final DatabaseHelper database;
	private final User user;

	public StaffViewQuestionsPage(DatabaseHelper helper, User user) {
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
			// TODO: Make Staff landing page
			StaffHomePage target = new StaffHomePage(this.database, this.user);
			target.show(primaryStage);
		});

		// Container for the back button
		HBox backButtonBox = new HBox(backButton);
		backButtonBox.setAlignment(Pos.CENTER_LEFT);
		backButtonBox.setPadding(new Insets(10, 10, 0, 10));
		backButtonBox.getStyleClass().add("background");

		// A basic header
		Label header1 = new Label("Questions Table View");
		header1.getStyleClass().add("header-main");

		// Sub header
		Label header2 = new Label("Data can only be viewed, not modified.");
		header2.getStyleClass().add("header-sub");

		// Create tableView
		TableView<Question> tableView = new TableView<>();

		// Set up column for Question ID
		TableColumn<Question, Integer> idCol = new TableColumn<>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

		// Set up column for Question username
		TableColumn<Question, Integer> userNameCol = new TableColumn<>("Username");
		userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));

		// Set up column for Question creation date
		TableColumn<Question, String> creationDateCol = new TableColumn<>("Creation date");
		creationDateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

		// Set up column for Question title
		TableColumn<Question, Integer> titleCol = new TableColumn<>("Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

		// Set up column for Question content
		TableColumn<Question, Integer> contentCol = new TableColumn<>("Content");
		contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));

		// Set up column for Question answers
		TableColumn<Question, Integer> answersCol = new TableColumn<>("Answers");
		answersCol.setCellValueFactory(new PropertyValueFactory<>("answers"));

		// Set up column for Question tags
		TableColumn<Question, Integer> tagsCol = new TableColumn<>("Tags");
		tagsCol.setCellValueFactory(new PropertyValueFactory<>("tags"));

		// Add columns to the table
		tableView.getColumns().addAll(idCol, userNameCol, creationDateCol, titleCol, contentCol, answersCol, tagsCol);

		// Populate table
		ObservableList<Question> data = FXCollections.observableArrayList();
		for (Question q : StartCSE360.getQuestionManager().getQuestionSet()) {
			data.add(q);
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
		primaryStage.setTitle("Question/Answer System - Staff Questions Viewer");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}
}
