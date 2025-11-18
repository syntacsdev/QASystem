package application.pages.staff;

import application.StartCSE360;
import application.User;
import application.obj.Review;
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
 * Reviews table.
 */
public class StaffViewReviewsPage {

	private final DatabaseHelper database;
	private final User user;

	public StaffViewReviewsPage(DatabaseHelper helper, User user) {
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
		Label header1 = new Label("Reviews Table View");
		header1.getStyleClass().add("header-main");

		// Sub header
		Label header2 = new Label("Data can only be viewed, not modified.");
		header2.getStyleClass().add("header-sub");

		// Create tableView
		TableView<Review> tableView = new TableView<>();

		// Set up column for Review ID
		TableColumn<Review, Integer> idCol = new TableColumn<>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

		// Set up column for Review username
		TableColumn<Review, String> userNameCol = new TableColumn<>("Username");
		userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));

		// Set up column for Review creation date
		TableColumn<Review, Integer> creationDateCol = new TableColumn<>("Creation Date");
		creationDateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

		// Set up column for Review content
		TableColumn<Review, Integer> contentCol = new TableColumn<>("Content");
		contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));

		// Set up column for Review rating
		TableColumn<Review, Integer> ratingCol = new TableColumn<>("Rating");
		ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

		// Set up column for Review parent question ID
		TableColumn<Review, Integer> questionIdCol = new TableColumn<>("Parent Question");
		questionIdCol.setCellValueFactory(new PropertyValueFactory<>("questionId"));

		// Set up column for Review parent answer ID
		TableColumn<Review, Integer> answerIdCol = new TableColumn<>("Parent Answer");
		answerIdCol.setCellValueFactory(new PropertyValueFactory<>("answerId"));

		// Add the columns to the table
		tableView.getColumns().addAll(idCol, userNameCol, creationDateCol, contentCol, ratingCol, questionIdCol,
				answerIdCol);

		// Populate the table
		ObservableList<Review> data = FXCollections.observableArrayList();
		for (Review r : StartCSE360.getReviewManager().getReviewSet()) {
			data.add(r);
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
		primaryStage.setTitle("Question/Answer System - Staff Reviews Viewer");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}
}
