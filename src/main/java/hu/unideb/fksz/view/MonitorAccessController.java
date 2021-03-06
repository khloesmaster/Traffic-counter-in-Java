package hu.unideb.fksz.view;

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

import hu.unideb.fksz.model.Observation;
import hu.unideb.fksz.model.ObservationDAO;
import hu.unideb.fksz.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
	private TableColumn<Observation, Integer> monitorAccessComputerTrafficCountColumn;
	@FXML
	private Button monitorAccessWindowBackButton;
	private TrafficCounterController trafficCounterController;
	ObservableList<Observation> tableData = FXCollections.observableArrayList();

	public TrafficCounterController getTrafficCounterController() {
		return trafficCounterController;
	}

	public void setTrafficCounterController(TrafficCounterController trafficCounterController) {
		this.trafficCounterController = trafficCounterController;
	}

	@FXML
	private void monitorAccessWindowBackButtonOnAction() {
		getTrafficCounterController().getTrafficCounterStage()
				.setScene(getTrafficCounterController().getTrafficCounterScene());
		getTrafficCounterController().resetTitle();
		getTrafficCounterController().getTrafficCounterStage().show();
	}

	public void populateObservationsTable(User user) {
		List<Observation> observations = ObservationDAO.observationsOf(user);
		tableData = FXCollections.observableList(observations);

		monitorAccessWindowTableView.setItems(tableData);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		monitorAccessDateColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, Timestamp>("observationDate"));
		monitorAccessObservationIdColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, Integer>("observationId"));
		monitorAccessVideoTitleColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, String>("observedVideoTitle"));
		monitorAccessTrafficCountColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, Integer>("trafficCount"));
		monitorAccessComputerTrafficCountColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, Integer>("computerTrafficCount"));

		monitorAccessComputerTrafficCountColumn.setEditable(false);
		monitorAccessTrafficCountColumn.setEditable(false);
		monitorAccessVideoTitleColumn.setEditable(false);
		monitorAccessObservationIdColumn.setEditable(false);
		monitorAccessDateColumn.setEditable(false);
	}
}
