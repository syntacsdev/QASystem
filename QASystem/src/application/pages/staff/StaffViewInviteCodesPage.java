package application.pages.staff;

import application.StartCSE360;
import application.User;
import application.obj.InviteCode;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Allows {@link User}s whose role is {@link UserRole#STAFF} to view invitation
 * codes.
 */
public class StaffViewInviteCodesPage {

	private final DatabaseHelper database;
	private final User user;

	public StaffViewInviteCodesPage(DatabaseHelper helper, User user) {
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
			StaffHomePage target = new StaffHomePage(this.database, user);
			target.show(primaryStage);
		});

		// Container for the back button
		HBox backButtonBox = new HBox(backButton);
		backButtonBox.setAlignment(Pos.CENTER_LEFT);
		backButtonBox.setPadding(new Insets(10, 10, 0, 10));
		backButtonBox.getStyleClass().add("background");

		// A basic header
		Label header1 = new Label("Invite Codes Table View");
		header1.getStyleClass().add("header-main");

		// Sub header
		Label header2 = new Label("Data can only be viewed, not modified.");
		header2.getStyleClass().add("header-sub");

		// TODO: Center row content

		// Create table
		TableView<InviteCode> tableView = new TableView<>();

		// Column for the code itself
		TableColumn<InviteCode, String> codeCol = new TableColumn<>("Code");
		codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));

		// Column for the "used" boolean
		TableColumn<InviteCode, Boolean> usedCol = new TableColumn<>("Has been used");
		usedCol.setCellValueFactory(new PropertyValueFactory<>("used"));

		// Show the "used" column as checkboxes
		usedCol.setCellFactory(CheckBoxTableCell.forTableColumn(usedCol));

		// TODO: Populate table
		ObservableList<InviteCode> data = FXCollections.observableArrayList();
		for (InviteCode ic : StartCSE360.getInviteCodeManager().getInviteCodes()) {
			data.add(ic);
		}

		tableView.getColumns().addAll(codeCol, usedCol);
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
		primaryStage.setTitle("Question/Answer System - Staff Invite Codes Viewer");
		primaryStage.show();
		LogUtil.debug("Displayed " + this.getClass().getName());
	}

}
