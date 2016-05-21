package hu.unideb.fksz.view;

import java.io.IOException;

/*
 * #%L
 * Traffic-counter
 * %%
 * Copyright (C) 2016 FKSZSoft
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

import hu.unideb.fksz.TrafficCounterLogger;
import hu.unideb.fksz.model.Observation;
import hu.unideb.fksz.model.ObservationDAO;
import hu.unideb.fksz.model.User;
import hu.unideb.fksz.model.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminAccessController implements Initializable {

	@FXML
	private TableView<Observation> adminAccessWindowTableView;
	@FXML
	private TableColumn<Observation, Integer> adminAccessRowIdColumn;
	@FXML
	private TableColumn<Observation, Integer> adminAccessObservationIdColumn;
	@FXML
	private TableColumn<Observation, String> adminAccessVideoTitleColumn;
	@FXML
	private TableColumn<Observation, Integer> adminAccessTrafficCountColumn;
	@FXML
	private TableColumn<Observation, Timestamp> adminAccessDateColumn;
	@FXML
	private TableColumn<Observation, Integer> adminAccessComputerTrafficCountColumn;
	@FXML
	private ListView<String> adminAccessWindowListView;
	@FXML
	private Button adminAccessWindowBackButton;
	ObservableList<Observation> tableData = FXCollections.observableArrayList();
	private TrafficCounterController trafficCounterController;
	private Scene newUserScene;
	private boolean firstRegister = false;

	public Scene getNewUserScene() {
		return newUserScene;
	}

	public void setNewUserScene(Scene newUserScene) {
		this.newUserScene = newUserScene;
	}

	public TrafficCounterController getTrafficCounterController() {
		return trafficCounterController;
	}

	public void setTrafficCounterController(TrafficCounterController trafficCounterController) {
		this.trafficCounterController = trafficCounterController;
	}

	@FXML
	private void adminAccessWindowBackButtonOnAction() {
		getTrafficCounterController().getTrafficCounterStage()
				.setScene(getTrafficCounterController().getTrafficCounterScene());
		getTrafficCounterController().resetTitle();
		getTrafficCounterController().getTrafficCounterStage().show();
	}

	public void populateUserList() {
		adminAccessWindowListView.setItems(UserDAO.usersString());
		adminAccessWindowListView.getSelectionModel().select(0);
		populateTable();
	}

	private void populateTable() {
		User selectedUser = new User();
		selectedUser.setName(adminAccessWindowListView.getSelectionModel().getSelectedItem());
		populateObservationsTable(selectedUser);
	}

	public void populateObservationsTable(User user) {
		List<Observation> observationsOfUser = ObservationDAO.observationsOf(user);
		tableData = FXCollections.observableList(observationsOfUser);
		adminAccessWindowTableView.setItems(tableData);

	}

	private void onNewUserMenuItemClicked() {
		if (!firstRegister) {
			try {
				firstRegister = true;
				Stage stage;
				Parent root;
				stage = (Stage) adminAccessWindowTableView.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewUserWindow.fxml"));
				root = loader.load();
				Scene scene = new Scene(root);
				stage.setTitle("New user");
				stage.setScene(scene);
				loader.<NewUserController> getController().setAdminAccessController(this);
				setNewUserScene(scene);
				stage.show();
			} catch (IOException e) {
				TrafficCounterLogger.errorMessage(e.toString());
			}
		} else {
			getTrafficCounterController().getTrafficCounterStage().setScene(getNewUserScene());
			getTrafficCounterController().getTrafficCounterStage().setTitle("New user");
			getTrafficCounterController().getTrafficCounterStage().show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("initializing..");
		adminAccessDateColumn.setCellValueFactory(new PropertyValueFactory<Observation, Timestamp>("observationDate"));
		adminAccessObservationIdColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, Integer>("observationId"));
		adminAccessVideoTitleColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, String>("observedVideoTitle"));
		adminAccessTrafficCountColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, Integer>("trafficCount"));
		adminAccessComputerTrafficCountColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, Integer>("computerTrafficCount"));

		adminAccessComputerTrafficCountColumn.setEditable(false);
		adminAccessTrafficCountColumn.setEditable(false);
		adminAccessVideoTitleColumn.setEditable(false);
		adminAccessObservationIdColumn.setEditable(false);
		adminAccessDateColumn.setEditable(false);

		adminAccessWindowListView.setEditable(false);
		adminAccessWindowListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		ContextMenu usersListContextMenu = new ContextMenu();
		MenuItem deleteUserMenuItem = new MenuItem();
		deleteUserMenuItem.setText("Delete");
		deleteUserMenuItem.setOnAction(action -> {
			if (adminAccessWindowListView.getSelectionModel().getSelectedIndex() != -1) {
				User user = new User();
				user.setName(adminAccessWindowListView.getSelectionModel().getSelectedItem());
				UserDAO.removeUser(user);
			}
		});
		MenuItem registerUserMenuItem = new MenuItem();
		registerUserMenuItem.setText("New user");
		registerUserMenuItem.setOnAction(action -> {
			onNewUserMenuItemClicked();
		});
		usersListContextMenu.getItems().addAll(registerUserMenuItem);
		adminAccessWindowListView.setContextMenu(usersListContextMenu);
	}

	@FXML
	public void onAdminAccessUsersListViewClicked() {
		populateTable();
	}
}
