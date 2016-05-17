package hu.unideb.fksz.view;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import hu.unideb.fksz.model.Observation;
import hu.unideb.fksz.model.ObservationDAO;
import hu.unideb.fksz.model.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
	private TableColumn<Observation, LocalDateTime> monitorAccessDateColumn;

	@FXML
	private TableColumn<Observation, Integer> monitorAccessRowIdColumn;

	//ObservableList<Observation>
	public void populateObservationsTable(User user) {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}
