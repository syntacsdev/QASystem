package application.pages.student;

import java.time.format.DateTimeFormatter;

import application.User;
import application.obj.Question;
import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentQuestionFocusPageNew {

	private final DatabaseHelper database;
	private final User user;
	private final Question question;

	public StudentQuestionFocusPageNew(DatabaseHelper helper, User user, Question question) {
		this.database = helper;
		this.user = user;
		this.question = question;
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
		int questionID = this.question.getId();
		Label header1 = new Label("Question ID: " + questionID);
		header1.getStyleClass().add("header-main");

		// A simple sub-header
		String asker = this.question.getUserName();
		String date = this.question.getCreationDate().format(DateTimeFormatter.RFC_1123_DATE_TIME);
		Label header2 = new Label("Asked by " + this.question.getUserName() + " on " + date);
		header2.getStyleClass().add("header-sub");

		// Container for top section
		VBox topSection = new VBox(5, backButtonBox, header1, header2);
		topSection.getStyleClass().add("background-top");

		// Question title label and box
		Label titleLabel = new Label("Title");
		titleLabel.getStyleClass().add("header-question-field");
		TextField titleField = new TextField();
		titleField.setText(this.question.getTitle());
		titleField.setEditable(false);
		titleField.setFocusTraversable(false);
		titleField.getStyleClass().add("text-box");

		// Question body label and box
		Label bodyLabel = new Label("Body");
		bodyLabel.getStyleClass().add("header-question-field");
		TextArea bodyText = new TextArea();
		bodyText.setText(this.question.getContent());
		bodyText.setWrapText(true);
		bodyText.setEditable(false);
		bodyText.setFocusTraversable(false);
		bodyText.getStyleClass().add("text-box");
	}
}
