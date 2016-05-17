package hu.unideb.fksz.view;

import java.io.IOException;

import hu.unideb.fksz.TrafficCounterLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInController {

	@FXML
	private Button logInWindowLogInButton;

	@FXML
	private TextField logInWindowUserNameTextField;

	@FXML
	private PasswordField logInWindowPasswordField;

	@FXML
	public void logInButtonClicked() {
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
}
