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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class MonitorAccessController implements Initializable {

	@FXML
	private TableView<Observation> monitorAccessWindowTableView;
	@FXML
	private TableColumn<Observation, Integer> monitorAccessObservationIdColumn;
	@FXML
	private TableColumn<Observation, String> monitorAccessVideoTitleColumn;
	@FXML
	private TableColumn<Observation, Integer> monitorAccessTrafficCountColumn;
	@FXML
	private TableColumn<Observation, Timestamp> monitorAccessDateColumn;
	@FXML
	private Button monitorAccessWindowBackButton;

	@FXML
	private void monitorAccessWindowBackButtonOnAction() {
		try {
			Stage stage;
			Parent root;
			stage = (Stage) monitorAccessWindowBackButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/fxml/TrafficCounterWindow.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			TrafficCounterLogger.errorMessage(e.toString());
		}
	}
	ObservableList<Observation> tableData = FXCollections.observableArrayList();

	public void populateObservationsTable(User user) {
		List<Observation> observations = ObservationDAO.observationsOf(user);
		tableData = FXCollections.observableList(observations);

		monitorAccessWindowTableView.setItems(tableData);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		monitorAccessDateColumn.setCellValueFactory(
				new PropertyValueFactory<Observation, Timestamp>("observationDate"));
		monitorAccessObservationIdColumn.setCellValueFactory(
				new PropertyValueFactory<Observation, Integer>("observationId"));
		monitorAccessVideoTitleColumn.setCellValueFactory(
				new PropertyValueFactory<Observation, String>("observedVideoTitle"));
		monitorAccessTrafficCountColumn.setCellValueFactory(
				new PropertyValueFactory<Observation, Integer>("trafficCount"));
	}

}
