package application.pages.student;

import application.User;
import databasePart1.DatabaseHelper;
import javafx.stage.Stage;

public class StudentSettingsPage {

	private final DatabaseHelper database;
	private final User user;
	
	public StudentSettingsPage(DatabaseHelper helper, User user) {
		this.database = helper;
		this.user = user;
	}
	
	public void show(Stage primaryStage) {
		
	}
	
}
