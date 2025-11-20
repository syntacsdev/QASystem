package application.pages.student;

import application.User;
import application.UserRole;
import databasePart1.DatabaseHelper;
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
		
	}
	
	private VBox generateTopRow() {
		return null; // TODO
	}

}
