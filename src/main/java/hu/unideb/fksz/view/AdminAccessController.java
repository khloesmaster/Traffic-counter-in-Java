package hu.unideb.fksz.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import hu.unideb.fksz.model.UserDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AdminAccessController implements Initializable {

	@FXML
	private TableView adminAccessWindowTableView;

	@FXML
	private TableColumn adminAccessRowIdColumn;

	@FXML
	private TableColumn adminAccessObservationIdColumn;

	@FXML
	private TableColumn adminAccessVideoTitleColumn;

	@FXML
	private TableColumn adminAccessTrafficCountColumn;

	@FXML
	private TableColumn adminAccessDateColumn;

	@FXML
	private ListView adminAccessWindowListView;

	public void populateUserList() {
	    adminAccessWindowListView.setItems(UserDAO.usersString());
	}

	public void populateObservationsTable() {

	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateObservationsTable();
	}

}
