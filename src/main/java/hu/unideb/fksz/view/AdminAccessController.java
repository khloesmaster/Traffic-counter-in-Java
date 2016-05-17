package hu.unideb.fksz.view;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import hu.unideb.fksz.model.Observation;
import hu.unideb.fksz.model.ObservationDAO;
import hu.unideb.fksz.model.User;
import hu.unideb.fksz.model.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminAccessController implements Initializable {

	@FXML
	private TableView<Observation> adminAccessWindowTableView;
	@FXML
	private TableColumn<Observation, Integer> adminAccessRowIdColumn;
	@FXML
	private TableColumn<Observation,Integer> adminAccessObservationIdColumn;
	@FXML
	private TableColumn<Observation, String> adminAccessVideoTitleColumn;
	@FXML
	private TableColumn<Observation, Integer> adminAccessTrafficCountColumn;
	@FXML
	private TableColumn<Observation, Timestamp> adminAccessDateColumn;
	@FXML
	private ListView<String> adminAccessWindowListView;

	public void populateUserList() {
	    adminAccessWindowListView.setItems(UserDAO.usersString());
	}

	ObservableList<Observation> tableData = FXCollections.observableArrayList();

	public void populateObservationsTable(User user) {
		List<Observation> observationsOfUser = ObservationDAO.observationsOf(user);
		tableData = FXCollections.observableList(observationsOfUser);
		adminAccessWindowTableView.setItems(tableData);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		adminAccessDateColumn.setCellValueFactory(
				new PropertyValueFactory<Observation, Timestamp>("observationDate"));
		adminAccessObservationIdColumn.setCellValueFactory(
				new PropertyValueFactory<Observation, Integer>("observationId"));
		adminAccessVideoTitleColumn.setCellValueFactory(
				new PropertyValueFactory<Observation, String>("observedVideoTitle"));
		adminAccessTrafficCountColumn.setCellValueFactory(
				new PropertyValueFactory<Observation, Integer>("trafficCount"));
	}

	@FXML
	public void onAdminAccessUsersListViewClicked() {
		User selectedUser = new User();
		selectedUser.setName(adminAccessWindowListView.getSelectionModel().getSelectedItem());
		populateObservationsTable(selectedUser);
	}

}
