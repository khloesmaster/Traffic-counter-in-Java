package hu.unideb.fksz.view;

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
