package application.pages.instructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.StartCSE360;
import application.User;
import application.pages.UserReviewsPage;
import application.util.LogUtil;
import databasePart1.DatabaseHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InstructorViewRequestsPage {

	private final DatabaseHelper database;
	private final User user;

	public InstructorViewRequestsPage(DatabaseHelper helper, User user) {
		this.database = helper;
		this.user = user;
	}

	public void show(Stage primaryStage) {
		// Back button
		Button backButton = new Button("← Back");
		backButton.getStyleClass().add("back-button");

		// Back button logic
		backButton.setOnAction(_ -> {
			InstructorHomePage target = new InstructorHomePage(this.database, this.user);
			target.show(primaryStage);
		});

		// Container for the back button
		HBox backButtonBox = new HBox(backButton);
		backButtonBox.setAlignment(Pos.CENTER_LEFT);
		backButtonBox.setPadding(new Insets(10, 10, 0, 10));
		backButtonBox.getStyleClass().add("background");

		// A basic header
		Label header1 = new Label("Reviewer Requests");
		header1.getStyleClass().add("header-main");

		// Sub header
		Label header2 = new Label("View and perhaps approve Reviewer requests.");
		header2.getStyleClass().add("header-sub");
		
		// Container for the top row
		VBox topRow = new VBox(5, backButtonBox, header1, header2);
		topRow.getStyleClass().add("background");
		topRow.setAlignment(Pos.CENTER);

		// Set up the user table
		TableView<User> userTable = new TableView<>();
		setupUserTable(userTable, primaryStage);

		// Container for the center row
		VBox centerRow = new VBox(5, userTable);
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
		root.setTop(topRow);
		root.setCenter(centerRow);
		root.setBottom(bottomRow);
		root.getStyleClass().add("background");
		root.setPrefSize(800, 600);

		// Finalize scene and stage
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Question/Answer System - Reviewer Requests");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}

	/**
	 * A helper method that creates the {@link TableView} that will be displayed on
	 * the page.
	 * 
	 * @param userTable    TableView to setup
	 * @param primaryStage Stage to use
	 */
	@SuppressWarnings("unchecked")
	private void setupUserTable(TableView<User> userTable, Stage primaryStage) {
		// Create user column
		TableColumn<User, String> usernameCol = createUsernameColumn();

		// Create reviews column
		TableColumn<User, Void> reviewsCol = createReviewsColumn(primaryStage);

		// Create actions column
		TableColumn<User, Void> actionCol = createActionsColumn(userTable);

		userTable.getColumns().addAll(usernameCol, reviewsCol, actionCol);
		userTable.setEditable(true);

		userTable.getItems().clear();

		String fetchPending = "SELECT * FROM PendingReviewers";
		try (Statement stmt = StartCSE360.getDatabaseHelper().getConnection().createStatement();
				ResultSet rs = stmt.executeQuery(fetchPending)) {
			while (rs.next()) {
				String userName = rs.getString("userName");

				User user = StartCSE360.getDatabaseHelper().fetchUser(userName);
				if (user != null) {
					userTable.getItems().add(user);
				} else {
					System.out.println("Skipping null user fetched for username: " + userName);
				}
			}

			System.out.println("Loaded PendingReviewers into table.");
		} catch (SQLException e) {
			System.err.println("Failed to fetch from PendingReviewers.");
			e.printStackTrace();
		}
	}

	private void removeUserFromTable(String userName, TableView<User> userTable) {
		for (User u : userTable.getItems()) {
			if (u == null)
				continue;
			if (u.getUserName().equals(userName)) {
				userTable.getItems().remove(u);
				break;
			}
		}
	}

	private TableColumn<User, String> createUsernameColumn() {
		TableColumn<User, String> usernameCol = new TableColumn<>("Username");
		usernameCol.setCellValueFactory(cellData -> {
			User u = cellData.getValue();
			if (u == null || u.getUserName() == null) {
				return new SimpleStringProperty("");
			}
			return new SimpleStringProperty(u.getUserName());
		});
		return usernameCol;
	}

	private TableColumn<User, Void> createReviewsColumn(Stage primaryStage) {
		TableColumn<User, Void> reviewsCol = new TableColumn<>("Reviews");
		reviewsCol.setCellFactory(_ -> new TableCell<User, Void>() {
			private final Button reviewsBtn = new Button("Reviews");

			{
				reviewsBtn.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white;");
				reviewsBtn.setOnAction(_ -> {
					User target = getTableView().getItems().get(getIndex());
					if (target == null)
						return;
					UserReviewsPage userReviewsPage = new UserReviewsPage(database, user);
					userReviewsPage.show(primaryStage, target);
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(reviewsBtn);
				}
			}
		});
		return reviewsCol;
	}

	private TableColumn<User, Void> createActionsColumn(TableView<User> userTable) {
		TableColumn<User, Void> actionCol = new TableColumn<>("Actions");
		actionCol.setCellFactory(_ -> new TableCell<User, Void>() {
			private final Button acceptBtn = new Button("Accept");
			private final Button rejectBtn = new Button("Reject");
			private final HBox buttonBox = new HBox(10, acceptBtn, rejectBtn);

			{
				acceptBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
				rejectBtn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

				acceptBtn.setOnAction(_ -> {
					User user = getTableView().getItems().get(getIndex());
					if (user == null)
						return;
					StartCSE360.getDatabaseHelper().approvePendingReviewer(user.getUserName());
					removeUserFromTable(user.getUserName(), userTable);
					System.out.println("Accepted: " + user.getUserName());
				});

				rejectBtn.setOnAction(_ -> {
					User user = getTableView().getItems().get(getIndex());
					if (user == null)
						return;
					StartCSE360.getDatabaseHelper().removeFromPendingReviewers(user.getUserName());
					removeUserFromTable(user.getUserName(), userTable);
					System.out.println("Rejected: " + user.getUserName());
				});

				buttonBox.setAlignment(Pos.CENTER);
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(buttonBox);
				}
			}
		});
		return actionCol;
	}
}
