package hu.unideb.fksz.view;

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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
				loader.<TrafficCounterController>getController().setLoggedUser(user);
				stage.show();

			} catch (IOException e) {
				TrafficCounterLogger.errorMessage(e.toString());
			}
		} else {
			TrafficCounterLogger.errorMessage("User not registered/ incorrect data");
		}


	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
}
