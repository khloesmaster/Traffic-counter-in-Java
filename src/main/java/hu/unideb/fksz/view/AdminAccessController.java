package hu.unideb.fksz.view;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import hu.unideb.fksz.model.Observation;
import hu.unideb.fksz.model.ObservationDAO;
import hu.unideb.fksz.model.User;
import hu.unideb.fksz.model.UserDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
	private TableColumn<Observation, LocalDateTime> adminAccessDateColumn;

	@FXML
	private ListView<String> adminAccessWindowListView;

	public void populateUserList() {
	    adminAccessWindowListView.setItems(UserDAO.usersString());
	}

	public void populateObservationsTable(User user) {
		List<Observation> observationsOfUser = ObservationDAO.observationsOf(user);

	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	public void onAdminAccessUsersListViewClicked() {
		User selectedUser = new User();
		selectedUser.setName(adminAccessWindowListView.getSelectionModel().getSelectedItem());

	}

}
