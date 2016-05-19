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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import hu.unideb.fksz.TrafficCounterLogger;
import hu.unideb.fksz.model.User;
import hu.unideb.fksz.model.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

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

	@FXML
	private void logInWindowBackButtonClicked() {
		try {
			Stage stage;
			Parent root;
			stage = (Stage) logInWindowLogInButton.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/fxml/TrafficCounterWindow.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			TrafficCounterLogger.errorMessage(e.toString());
		}
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
			try {
				Stage stage;
				Parent root;
				stage = (Stage) logInWindowLogInButton.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TrafficCounterWindow.fxml"));
				root = loader.load();

				Scene scene = new Scene(root);
				stage.setScene(scene);
				loader.<TrafficCounterController> getController().setLoggedUser(user);
				stage.show();

			} catch (IOException e) {
				TrafficCounterLogger.errorMessage(e.toString());
			}
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
