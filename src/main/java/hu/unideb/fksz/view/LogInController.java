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
import java.util.ResourceBundle;

import hu.unideb.fksz.TrafficCounterLogger;
import hu.unideb.fksz.model.User;
import hu.unideb.fksz.model.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class LogInController implements Initializable {

	@FXML
	private Button logInWindowLogInButton;
	@FXML
	private TextField logInWindowUserNameTextField;
	@FXML
	private PasswordField logInWindowPasswordField;
	@FXML
	private CheckBox logInWindowAdminCheckBox;
	@FXML
	private Button logInWindowBackButton;
	@FXML
	private Label wrongPasswordLabel;

	private TrafficCounterController trafficCounterController;

	public TrafficCounterController getTrafficCounterController() {
		return trafficCounterController;
	}
	public void setTrafficCounterController(TrafficCounterController trafficCounterController) {
		this.trafficCounterController = trafficCounterController;
	}
	@FXML
	private void logInWindowBackButtonClicked() {
		getTrafficCounterController().getTrafficCounterStage().setScene(
				getTrafficCounterController().getTrafficCounterScene());
		getTrafficCounterController().getTrafficCounterStage().show();
	}

	@FXML
	public void logInButtonClicked() {
		User user = new User();
		user.setName(logInWindowUserNameTextField.getText());
		user.setPassword(logInWindowPasswordField.getText());
		if (logInWindowAdminCheckBox.isSelected()) {
			user.setRole("admin");
		} else {
			user.setRole("monitor");
		}
		if (UserDAO.logInUser(user)) {
			getTrafficCounterController().setLoggedUser(user);
			getTrafficCounterController().resetResults();;
			getTrafficCounterController().getTrafficCounterStage().setScene(
					getTrafficCounterController().getTrafficCounterScene());
			getTrafficCounterController().getTrafficCounterStage().show();
		} else {
			wrongPasswordLabel.setVisible(true);
			wrongPasswordLabel.setText("Wrong username/password/role");
			wrongPasswordLabel.setTextFill(Color.RED);
			TrafficCounterLogger.errorMessage("User not registered/ incorrect data");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		wrongPasswordLabel.setVisible(false);
	}
}
