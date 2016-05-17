package hu.unideb.fksz.view;

import java.net.URL;
import java.util.ResourceBundle;

import hu.unideb.fksz.model.ObservationDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MonitorAccessController implements Initializable {

	@FXML
	private TableView monitorAccessWindowTableView;

	@FXML
	private TableColumn monitorAccessObservationIdColumn;

	@FXML
	private TableColumn monitorAccessVideoTitleColumn;

	@FXML
	private TableColumn monitorAccessTrafficCountColumn;

	@FXML
	private TableColumn monitorAccessDateColumn;

	@FXML
	private TableColumn monitorAccessRowIdColumn;

	public void populateTable() {



	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
