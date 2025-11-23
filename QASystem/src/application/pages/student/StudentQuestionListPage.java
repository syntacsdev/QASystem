package application.pages.student;

import java.time.format.DateTimeFormatter;
import java.util.Set;

import application.StartCSE360;
import application.User;
import application.UserRole;
import application.obj.Question;
import application.util.LogUtil;
import application.util.StringUtil;
import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Allows {@link User}s with the {@link UserRole#STUDENT} role to view the list
 * of questions asked by other Users.
 */
public class StudentQuestionListPage {

	private final DatabaseHelper database;
	private final User user;

	public StudentQuestionListPage(DatabaseHelper helper, User user) {
		this.database = helper;
		this.user = user;
	}

	public void show(Stage primaryStage) {
		// Back button
		Button backButton = new Button("← Back");
		backButton.getStyleClass().add("back-button");

		// Back button logic
		backButton.setOnAction(_ -> {
			StudentHomePageNew target = new StudentHomePageNew(this.database, this.user);
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
		Label header2 = new Label("Select a question to view its answers.");
		header2.getStyleClass().add("header-sub");

		// Container for the top section
		VBox topSection = new VBox(5, backButtonBox, header1, header2);
		topSection.getStyleClass().add("background-top");

		// New question button
		Button newQuestionBtn = new Button("Ask a new question...");
		newQuestionBtn.getStyleClass().add("action-button");

		// New question button logic // TODO
		newQuestionBtn.setOnAction(_ -> {
			// TODO 🠖 navigate to a new question page
		});

		// Set up the question list
		ListView<Question> questionList = this.buildQuestionList();

		// Container for center section
		VBox centerSection = new VBox(5, questionList, newQuestionBtn);
		centerSection.getStyleClass().add("background-center");

		Label disclaimer = new Label("For educational purposes only.");
		disclaimer.getStyleClass().add("disclaimer");

		// Container for bottom row
		VBox bottomSection = new VBox(5, disclaimer);
		bottomSection.getStyleClass().add("background-bottom");

		// Container for all the elements
		BorderPane root = new BorderPane();
		root.setTop(topSection);
		root.setCenter(centerSection);
		root.setBottom(bottomSection);
		root.getStyleClass().add("background");
		root.setPrefSize(600, 400);

		// Finalize and show
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Question/Answer System - Question List");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}

	private ListView<Question> buildQuestionList() {
		Set<Question> questionSet = StartCSE360.getQuestionManager().getQuestionSet();
		ObservableList<Question> obsList = FXCollections.observableArrayList(questionSet);
		ListView<Question> listView = new ListView<>(obsList);

		// Build custom cells for each question
		listView.setCellFactory(_ -> new ListCell<>() {
			private final Label idLabel = new Label();
			private final Label askerLabel = new Label();
			private final Label titleLabel = new Label();
			private final Label answerCountLabel = new Label();
			private final Label reviewCountLabel = new Label();
			private final Label dateLabel = new Label();

			// Build the boxes for the rows
			HBox topSection = new HBox(2, idLabel, titleLabel);
			HBox midSection = new HBox(2, answerCountLabel, reviewCountLabel);
			HBox bottomSection = new HBox(2, askerLabel, dateLabel);
			// Build the VBox for everything
			VBox root = new VBox(2, topSection, midSection, bottomSection);

			// Attach the CSS file
			{
				root.getStyleClass().add("qcell");
				idLabel.getStyleClass().add("qcell-id");
				askerLabel.getStyleClass().add("qcell-asker");
				titleLabel.getStyleClass().add("qcell-title");
				answerCountLabel.getStyleClass().add("qcell-ans-count");
				reviewCountLabel.getStyleClass().add("qcell-rev-count");
				dateLabel.getStyleClass().add("qcell-date");
			}

			@Override
			protected void updateItem(Question q, boolean empty) {
				super.updateItem(q, empty);

				// Check for empty... something?
				if (empty || q == null) {
					this.setGraphic(null);
					this.setOnMouseClicked(null); // Clear mouse clicked handler
				} else {
					// Populate labels
					idLabel.setText("ID: #" + q.getId());
					askerLabel.setText("Asker: " + q.getUserName());
					titleLabel.setText("\"" + StringUtil.truncate(q.getTitle(), 24));
					int answerCount = q.getAnswers().size();
					answerCountLabel.setText("Answers: " + answerCount);
					int reviewCount = StartCSE360.getReviewManager().getReviewsForQuestion(q.getId()).size();
					reviewCountLabel.setText("Reviews: " + reviewCount);
					dateLabel.setText(q.getCreationDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));

					// Add clicking functionality
					this.setOnMouseClicked(_ -> {
						// TODO: Create question focus page
					});

					this.setGraphic(this.root);
				}
			}
		});

		return listView;
	}

}
