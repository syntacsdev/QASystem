package application.pages.student;

import application.User;
import databasePart1.DatabaseHelper;
import javafx.stage.Stage;

public class StudentMessagesPage {

	private final DatabaseHelper database;
	private final User user;
	
	public StudentMessagesPage(DatabaseHelper helper, User user) {
		this.database = helper;
		this.user = user;
	}
	
	public void show(Stage primaryStage) {
		// Back button
	}
}
