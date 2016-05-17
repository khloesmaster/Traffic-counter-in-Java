package hu.unideb.fksz.view;

import java.io.IOException;
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
import javafx.scene.control.ListView;
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
	private ListView<String> adminAccessWindowListView;
	@FXML
	private Button adminAccessWindowBackButton;

	@FXML
	private void adminAccessWindowBackButtonOnAction() {
		try {
			Stage stage;
			Parent root;
			stage = (Stage) adminAccessWindowBackButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/fxml/TrafficCounterWindow.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			TrafficCounterLogger.errorMessage(e.toString());
		}
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

	ObservableList<Observation> tableData = FXCollections.observableArrayList();

	public void populateObservationsTable(User user) {
		List<Observation> observationsOfUser = ObservationDAO.observationsOf(user);
		tableData = FXCollections.observableList(observationsOfUser);
		adminAccessWindowTableView.setItems(tableData);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		adminAccessDateColumn.setCellValueFactory(new PropertyValueFactory<Observation, Timestamp>("observationDate"));
		adminAccessObservationIdColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, Integer>("observationId"));
		adminAccessVideoTitleColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, String>("observedVideoTitle"));
		adminAccessTrafficCountColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, Integer>("trafficCount"));

	}

	@FXML
	public void onAdminAccessUsersListViewClicked() {
		populateTable();
	}

}
