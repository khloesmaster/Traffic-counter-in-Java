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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert.AlertType;

public class NewUserController implements Initializable {

	@FXML
	private Button newUserButton;
	@FXML
	private Button cancelButton;
	@FXML
	private TextField userNameTextField;
	@FXML
	private TextField userPasswordAgainTextField;
	@FXML
	private TextField userPasswordTextField;
	@FXML
	private Label userAlreadyRegisteredLabel;

	public AdminAccessController getAdminAccessController() {
		return adminAccessController;
	}

	public void setAdminAccessController(AdminAccessController adminAccessController) {
		this.adminAccessController = adminAccessController;
	}

	private AdminAccessController adminAccessController;

	@FXML
	private void newUserButtonClicked() {
		User newUser = new User();
		boolean userOk = false;
		boolean passwordOk = false;
		if (userNameTextField.getText().isEmpty()) {
			Alert userEmptyAlert = new Alert(AlertType.ERROR);
			userEmptyAlert.setTitle("User name empty!");
			userEmptyAlert.setHeaderText("User name must be specified!");
			userEmptyAlert.setContentText("Try again and enter a valid user name!");
			userEmptyAlert.showAndWait();
		} else {
			userOk = true;
		}
		if (userPasswordTextField.getText().equals(userPasswordAgainTextField.getText())) {
			passwordOk = true;
		}

		if (userOk && passwordOk) {
			newUser.setName(userNameTextField.getText());
			newUser.setPassword(userPasswordTextField.getText());
			newUser.setRole(UserDAO.MONITOR_ROLE);
			if (!UserDAO.userExists(newUser)) {
				UserDAO.registerUser(newUser);
				TrafficCounterLogger.infoMessage("Registering new user..");
				getAdminAccessController().populateUserList();
				getAdminAccessController().getTrafficCounterController().getTrafficCounterStage()
						.setScene(getAdminAccessController().getTrafficCounterController().getAdminAccessScene());
				getAdminAccessController().getTrafficCounterController().getTrafficCounterStage().show();

			} else {
				userAlreadyRegisteredLabel.setText("User already registered!");
				userAlreadyRegisteredLabel.setTextFill(Color.RED);
				userAlreadyRegisteredLabel.setVisible(true);
			}
		}
	}

	@FXML
	private void cancelButtonClicked() {
		getAdminAccessController().getTrafficCounterController().getTrafficCounterStage()
				.setScene(getAdminAccessController().getTrafficCounterController().getAdminAccessScene());
		getAdminAccessController().getTrafficCounterController().getTrafficCounterStage().show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		userAlreadyRegisteredLabel.setVisible(false);
	}

}
