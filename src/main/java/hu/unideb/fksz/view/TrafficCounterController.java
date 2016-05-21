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

import hu.unideb.fksz.FileSaver;
import hu.unideb.fksz.Main;
import hu.unideb.fksz.VideoProcessor;
import hu.unideb.fksz.model.Observation;
import hu.unideb.fksz.model.ObservationDAO;
import hu.unideb.fksz.model.User;
import hu.unideb.fksz.model.UserDAO;
import hu.unideb.fksz.FileOpener;
import hu.unideb.fksz.FileNameParser;
import hu.unideb.fksz.TrafficCounterLogger;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TrafficCounterController implements Initializable {

	private VideoProcessor videoProcessor = new VideoProcessor();
	private boolean pauseVideo = false;
	private boolean startButtonClicked = false;
	private boolean otherFileSelected = false;
	private String currentlyPlaying;
	private String lastVideoName;
	private Point mousePosition = new Point();

	private Timer timer = new Timer();
	private List<String> items = new ArrayList<String>();
	private List<String> results = new ArrayList<String>();
	private List<Observation> userObservations;

	private List<String> videoDetails = new ArrayList<String>();
	private Map<String, String> itemsWithPath = new HashMap<String, String>();
	private User loggedUser;

	@FXML
	private ImageView imageView;
	@FXML
	private Button loadButton;
	@FXML
	private Button saveImageButton;
	@FXML
	private Button startButton;
	@FXML
	private Label currentVideoDetailsLabel;
	@FXML
	private ListView<String> listViewForFileNames;
	@FXML
	private ListView<String> listViewForResults;
	@FXML
	private ListView<String> listViewForVideoDetails;
	@FXML
	private AnchorPane root;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private Button observationsButton;
	@FXML
	private Button logInButton;
	@FXML
	private Button increaseTrafficCountButton;
	@FXML
	private Button commitResultsButton;
	@FXML
	private Button logOutButton;
	@FXML
	private Label trafficCounterWindowTrafficCountLabel;
	private int trafficCount;
	private boolean firstLogin = false;
	private boolean firstAdminAccess = false;
	private boolean firstMonitorAccess = false;

	private Scene logInScene;
	private Scene trafficCounterScene;
	private Scene adminAccessScene;
	private Scene monitorAccessScene;
	private Scene logOutScene;
	private Stage trafficCounterStage;

	public Scene getLogOutScene() {
		return logOutScene;
	}

	public void setLogOutScene(Scene logOutScene) {
		this.logOutScene = logOutScene;
	}

	public Scene getTrafficCounterScene() {
		return trafficCounterScene;
	}

	public void setTrafficCounterScene(Scene trafficCounterScene) {
		this.trafficCounterScene = trafficCounterScene;
	}

	public Scene getLogInScene() {
		return logInScene;
	}

	public void setLogInScene(Scene logInScene) {
		this.logInScene = logInScene;
	}

	public Scene getAdminAccessScene() {
		return adminAccessScene;
	}

	public void setAdminAccessScene(Scene adminAccessScene) {
		this.adminAccessScene = adminAccessScene;
	}

	public Scene getMonitorAccessScene() {
		return monitorAccessScene;
	}

	public void setMonitorAccessScene(Scene monitorAccessScene) {
		this.monitorAccessScene = monitorAccessScene;
	}

	public Stage getTrafficCounterStage() {
		return trafficCounterStage;
	}

	public void setTrafficCounterStage(Stage trafficCounterStage) {
		this.trafficCounterStage = trafficCounterStage;
	}

	@FXML
	private void commitResultsButtonClicked() {
		if (!userObservations.isEmpty()) {
			Alert commitConfirmation = new Alert(AlertType.CONFIRMATION);
			commitConfirmation.setTitle("Commit confirmation");
			commitConfirmation.setHeaderText("Commit your results");
			commitConfirmation.setContentText("Are you sure you want to commit your results?");

			ButtonType noButton = new ButtonType("No", ButtonData.NO);
			ButtonType yesButton = new ButtonType("Yes", ButtonData.YES);

			commitConfirmation.getButtonTypes().setAll(yesButton, noButton);
			Optional<ButtonType> dialogResult = commitConfirmation.showAndWait();

			if (dialogResult.get() == yesButton) {
				commitResults();
				TrafficCounterLogger.infoMessage("Committing results of " + getLoggedUser().getName());
			} else if (dialogResult.get() == noButton) {
				TrafficCounterLogger.infoMessage("Committing canceled");
			}
		} else {
			Alert nothingToCommitInfo = new Alert(AlertType.INFORMATION);
			nothingToCommitInfo.setTitle("Nothing to commit");
			nothingToCommitInfo.setHeaderText("You have no results to commit!");
			nothingToCommitInfo.setContentText("Try again when you'll have new observations!");
			nothingToCommitInfo.showAndWait();
		}
	}

	@FXML
	private void logOutButtonClicked() {

		Alert logOutConfirmation = new Alert(AlertType.CONFIRMATION);
		logOutConfirmation.setTitle("Log out confirmation");
		logOutConfirmation.setHeaderText("Any uncommitted result will be lost if you log out");
		logOutConfirmation.setContentText("Choose whether you want to log out or commit and log out");

		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		ButtonType logOutButton = new ButtonType("Log out");
		ButtonType commitAndLogOutButton = new ButtonType("Commit");
		if (!userObservations.isEmpty()) {
			logOutConfirmation.getButtonTypes().setAll(logOutButton, commitAndLogOutButton, cancelButton);
		} else {
			logOutConfirmation.getButtonTypes().setAll(logOutButton, cancelButton);
		}
		Optional<ButtonType> dialogResult = logOutConfirmation.showAndWait();

		if (dialogResult.get() == logOutButton) {
			onlogOutConfirmationDialogLogOutResult();
			TrafficCounterLogger.infoMessage("Logging out..");
		} else if (dialogResult.get() == commitAndLogOutButton) {
			commitResults();
			onlogOutConfirmationDialogLogOutResult();
			TrafficCounterLogger.infoMessage("Committing and logging out..");
		}
	}

	public AnchorPane getRoot() {
		return root;
	}

	public void setRoot(AnchorPane root) {
		this.root = root;
	}

	public void onlogOutConfirmationDialogLogOutResult() {
		setLoggedUser(new User());
		results = new ArrayList<String>();
		listViewForResults.setItems(FXCollections.observableList(results));
		setTitle("Traffic counter - No user");
		hideControls();
	}

	public void resetResults() {
		results = new ArrayList<String>();
		listViewForResults.setItems(FXCollections.observableList(results));
	}

	public void commitResults() {
		for (Observation o : userObservations) {
			ObservationDAO.insertObservation(o);
		}
		userObservations = new ArrayList<Observation>();
		resetResults();
	}

	public void resetTitle() {
		setTitle("Traffic counter - " + loggedUser.getName() + "-" + loggedUser.getRole());
	}

	@FXML
	private void increaseTrafficCountButtonOnAction() {
		setTrafficCount(getTrafficCount() + 1);
		trafficCounterWindowTrafficCountLabel.setText("Traffic count: " + Integer.toString(trafficCount));
	}

	public void hideControls() {
		observationsButton.setVisible(false);
		increaseTrafficCountButton.setVisible(false);
		trafficCounterWindowTrafficCountLabel.setVisible(false);
		logOutButton.setVisible(false);
		commitResultsButton.setVisible(false);
	}

	public void showControls() {
		observationsButton.setVisible(true);
		increaseTrafficCountButton.setVisible(true);
		trafficCounterWindowTrafficCountLabel.setVisible(true);
		logOutButton.setVisible(true);
		commitResultsButton.setVisible(true);
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
		setTitle("Traffic counter - " + loggedUser.getName() + "-" + loggedUser.getRole());
		showControls();
		userObservations = new ArrayList<Observation>();
	}

	@FXML
	private void trafficCounterWindowAnchorPaneOnKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.PLUS) {
			trafficCount++;
			trafficCounterWindowTrafficCountLabel.setText("Traffic count: " + Integer.toString(trafficCount));
		}
	}

	@FXML
	private void logInButtonClicked() {
		if (!firstLogin) {
			try {
				firstLogin = true;
				Stage stage;
				Parent root;
				stage = (Stage) logInButton.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LogInWindow.fxml"));
				root = loader.load();
				Scene scene = new Scene(root);
				stage.setTitle("Log in");
				stage.setScene(scene);
				loader.<LogInController> getController().setTrafficCounterController(this);
				setLogInScene(scene);
				stage.show();
			} catch (IOException e) {
				TrafficCounterLogger.errorMessage(e.toString());
			}
		} else {
			getTrafficCounterStage().setScene(getLogInScene());
			getTrafficCounterStage().setTitle("Log in");
			getTrafficCounterStage().show();
		}
		System.gc();
	}

	@FXML
	private void observationsButtonClicked() {
		if (loggedUser.getName() != null) {
			try {
				Stage stage;
				Parent root;
				Scene scene = null;
				FXMLLoader loader;
				stage = (Stage) logInButton.getScene().getWindow();
				if (getLoggedUser().getRole().equals(UserDAO.ADMIN_ROLE)) {
					if (!firstAdminAccess) {
						firstAdminAccess = true;
						loader = new FXMLLoader(getClass().getResource("/fxml/AdminAccessWindow.fxml"));
						root = loader.load();
						scene = new Scene(root);
						loader.<AdminAccessController> getController().populateUserList();
						loader.<AdminAccessController> getController().setTrafficCounterController(this);
						setAdminAccessScene(scene);
						stage.setScene(scene);
						stage.show();
					} else {
						getTrafficCounterStage().setScene(getAdminAccessScene());
						getTrafficCounterStage().show();
					}
				} else if (getLoggedUser().getRole().equals(UserDAO.MONITOR_ROLE)) {
					if (!firstMonitorAccess) {
						firstAdminAccess = true;
						loader = new FXMLLoader(getClass().getResource("/fxml/MonitorAccessWindow.fxml"));
						root = loader.load();
						scene = new Scene(root);
						setTitle("Observations of " + loggedUser.getName());
						loader.<MonitorAccessController> getController().populateObservationsTable(getLoggedUser());
						loader.<MonitorAccessController> getController().setTrafficCounterController(this);
						setMonitorAccessScene(scene);
						stage.setScene(scene);
						stage.show();
					} else {
						getTrafficCounterStage().setScene(getMonitorAccessScene());

					}
				}
			} catch (IOException e) {
				TrafficCounterLogger.errorMessage(e.toString());
			}
		}
	}

	private void setTitle(String title) {
		getTrafficCounterStage().setTitle(title);
	}

	/**
	 * Gets called when {@code listViewForFileNames} is clicked.
	 */
	@FXML
	private void listViewForFileNamesClicked() {
		if (!listViewForFileNames.getSelectionModel().getSelectedItem().equals(currentlyPlaying)) {
			startButton.setText("Start");
			otherFileSelected = true;
		} else {
			if (!pauseVideo) {
				startButton.setText("Pause");
			}
			otherFileSelected = false;
		}
	}

	/**
	 * Tries to load a video, returns whether the {@code VideoProcessor}
	 * successfully loaded the video or not.
	 *
	 * @param filename
	 *            the absolute path of the video to be loaded.
	 * @return whether the {@code VideoProcessor} successfully loaded the video
	 *         or not.
	 */
	public int loadVideo(String filename) {
		if (filename != null) {
			if (videoProcessor.initVideo(filename) == 0) {
				this.imageView.setImage(videoProcessor.getImageAtPos(1));

				TrafficCounterLogger.infoMessage("Video " + filename + " loaded!");
				if (this.saveImageButton.disableProperty().get() == true) {
					this.saveImageButton.disableProperty().set(false);
					this.startButton.disableProperty().set(false);
				}

				if (!videoDetails.isEmpty()) {
					videoDetails.clear();
				}
				videoDetails.add("Video name: " + FileNameParser.getFileName(filename));
				videoDetails.add("City: " + FileNameParser.getCity(filename));
				videoDetails.add("Street: " + FileNameParser.getStreet(filename));
				videoDetails.add("Length: " + videoProcessor.getLengthFormatted());
				videoDetails.add("FPS: " + (int) videoProcessor.getFPS());
				videoDetails.add("Frame count: " + videoProcessor.getFrameCount());
				videoDetails.add("Extension: " + FileNameParser.getExtension(filename));

				listViewForVideoDetails.setItems(FXCollections.observableList(videoDetails));

				currentlyPlaying = FileNameParser.getCity(filename) + " - " + FileNameParser.getStreet(filename);
				if (!otherFileSelected)
					videoProcessor.setDetectedCarsCount(0);
				addListViewItem(filename);
				setTrafficCount(0);
				return 0;
			}
		} else if (filename == null) {
			TrafficCounterLogger.warnMessage("No file was selected!");
			return 1;
		}
		return 0;
	}

	public int getTrafficCount() {
		return trafficCount;
	}

	public void setTrafficCount(int trafficCount) {
		this.trafficCount = trafficCount;
	}

	/**
	 * Gets called when the {@code loadButton} is clicked. Pauses the video
	 * while the file is being selected, if it was playing. Calls
	 * {@code loadVideo()} to load the selected file.
	 */
	@FXML
	private void loadButtonClicked() {
		if (!pauseVideo) {
			pauseVideo = true;
			this.startButton.setText("Start");
		}
		String filename = new FileOpener().getFileName((Stage) this.loadButton.getScene().getWindow());
		loadVideo(filename);
	}

	/**
	 * Gets called when the {@code saveImageButton} is clicked.
	 */
	@FXML
	private void saveImageClicked() {
		TrafficCounterLogger.infoMessage("Saving image..");
		if (!pauseVideo) {
			pauseVideo = true;
			startButton.setText("Start");
		}

		String filename = new FileSaver().getFileName((Stage) this.saveImageButton.getScene().getWindow());
		if (filename != null) {
			try {
				Imgcodecs.imwrite(filename, videoProcessor.getFrame());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			pauseVideo = false;
			startButton.setText("Pause");
		} else {
			TrafficCounterLogger.errorMessage("File name was not specified, file was not saved!");
			pauseVideo = false;
		}
	}

	/**
	 * Gets called when the {@code startButton} is clicked. Starts or pauses/
	 * unpauses the video, or loads a new video if other than the currently
	 * playing video is selected from the {@code listViewForFileNames}
	 */
	@FXML
	private void startButtonClicked() {
		System.gc();
		if (otherFileSelected) {
			pauseVideo = true;

			if (loadVideo(
					itemsWithPath.get(listViewForFileNames.getSelectionModel().selectedItemProperty().get())) == 0) {
				// pauseVideo = true;
				otherFileSelected = false;
				startButton.setText("Pause");
				timer.cancel();
				Observation currentObservation = new Observation();
				currentObservation.setMonitor_id(getLoggedUser().getId());
				currentObservation.setObservationDate(Timestamp.valueOf(LocalDateTime.now()));
				currentObservation.setTrafficCount(getTrafficCount());
				currentObservation.setObservedVideoTitle(lastVideoName);
				currentObservation.setComputerTrafficCount(videoProcessor.getDetectedCarsCount());
				userObservations.add(currentObservation);
				results.add(lastVideoName + ": " + videoProcessor.getDetectedCarsCount() + " cars detected, "
						+ videoProcessor.getCarsPerMinute() + " cars per minute.");
				listViewForResults.setItems(FXCollections.observableList(results));

				videoProcessor.setDetectedCarsCount(0);
				setTrafficCount(0);
				startProcessing();
			} else {
				TrafficCounterLogger.errorMessage("Failed to load "
						+ itemsWithPath.get(listViewForFileNames.getSelectionModel().selectedItemProperty().get()));
			}
		}

		if (!startButtonClicked && pauseVideo) {
			startButtonClicked = true;
			pauseVideo = false;
			this.startButton.setText("Pause");
			startProcessing();
		} else if (!pauseVideo) {
			pauseVideo = true;
			this.startButton.setText("Start");
		} else if (pauseVideo) {
			pauseVideo = false;
			this.startButton.setText("Pause");
		}
	}

	/**
	 * Gets called when {@code imageView} is clicked. Pauses or unpauses the
	 * video.
	 */
	@FXML
	private void imageViewClicked() {
		/*
		 * if (startButtonClicked) { if (!mouseDragged)
		 *
		 * if (!pauseVideo) { pauseVideo = true;
		 * this.startButton.setText("Start"); } else if (pauseVideo) {
		 * this.startButton.setText("Pause"); pauseVideo = false; } }
		 */
	}

	/**
	 * Initializes some elements of the user interface.
	 */
	private void init() {
		try {
			this.imageView.setImage(new Image(Main.class.getClass().getResource("/image/load_video.jpg").toString()));
			TrafficCounterLogger.traceMessage("Initial picture loaded successfully!");
		} catch (Exception e) {
			TrafficCounterLogger.errorMessage("Initial picture failed to load!");
		}

		this.listViewForFileNames.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		this.saveImageButton.disableProperty().set(true);
		this.startButton.disableProperty().set(true);

		EventHandler<InputEvent> eventHandler = event -> {

			if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
				KeyEvent keyevent = (KeyEvent) event.clone();
				if (keyevent.getCode().equals(KeyCode.ESCAPE)) {
					timer.cancel();
					videoProcessor.getVideoCap().release();
					((Node) (event.getSource())).getScene().getWindow().hide();
					System.exit(0);
				}
			}
		};

		root.setOnKeyPressed(eventHandler);

		imageView.setOnMousePressed(event -> {

			mousePosition.x = event.getX();
			mousePosition.y = event.getY();

			if (mousePosition.inside(videoProcessor.getImageArea())) {
				videoProcessor.setPreviousControlPointsHeight((int) videoProcessor.getHeightOfAControlPoint());
			}

		});

		imageView.setOnMouseDragged(event -> {

			if (mousePosition.inside(videoProcessor.getImageArea())) {
				Point relativeMousePosition = new Point(mousePosition.x - event.getX(), mousePosition.y - event.getY());
				videoProcessor.setHeightOfTheControlPoints(
						videoProcessor.getPreviousControlPointsHeight() - relativeMousePosition.y);
			}

			if (videoProcessor.getHeightOfAControlPoint() < 100) {
				videoProcessor.setHeightOfTheControlPoints(100);
			}

			if (videoProcessor.getHeightOfAControlPoint() > videoProcessor.getImageArea().height - 100) {
				videoProcessor.setHeightOfTheControlPoints(videoProcessor.getImageArea().height - 100);
			}

		});
		setTrafficCount(0);
		TrafficCounterLogger.infoMessage("TrafficCounterController initialized!");
		hideControls();
	}

	/**
	 * Adds an item to the {@code listViewForFileNames} {@code ListView} and to
	 * {@code items} and {@code itemsWithPath} if it's not containing it.
	 *
	 * @param filename
	 *            path to be added to the {@code itemsWithPath}.
	 */
	private void addListViewItem(String filename) {
		if (items != null) {
			if (!items.contains(currentlyPlaying) && filename != null) {
				items.add(currentlyPlaying);
				itemsWithPath.put(currentlyPlaying, filename);
			}
			listViewForFileNames.selectionModelProperty().get().select(currentlyPlaying);
			;
			listViewForFileNames.setItems(FXCollections.observableList(items));
		}
	}

	/**
	 * Main loop, separately in a {@code Timer} thread.
	 */
	private void startProcessing() {
		TimerTask frame_grabber = new TimerTask() {
			Image tmp;

			@Override
			public void run() {
				if (!pauseVideo) {
					videoProcessor.processVideo();
					videoProcessor.writeOnFrame("Detected cars count: " + videoProcessor.getDetectedCarsCount());

					tmp = videoProcessor.convertCvMatToImage();
					progressBar.setProgress(videoProcessor.getFramePos() / videoProcessor.getFrameCount());

					if (videoProcessor.isFinished()) {
						results.add(currentlyPlaying + ": " + videoProcessor.getDetectedCarsCount() + " cars detected, "
								+ videoProcessor.getCarsPerMinute() + " cars per minute.");
						listViewForResults.setItems(FXCollections.observableList(results));
						if (loggedUser != null) {
							if (loggedUser.getName() != null) {
								Observation currentObservation = new Observation();
								currentObservation.setMonitor_id(getLoggedUser().getId());
								currentObservation.setObservationDate(Timestamp.valueOf(LocalDateTime.now()));
								currentObservation.setTrafficCount(getTrafficCount());
								currentObservation.setObservedVideoTitle(lastVideoName);
								currentObservation.setComputerTrafficCount(videoProcessor.getDetectedCarsCount());
								userObservations.add(currentObservation);
							}
						}
						setTrafficCount(0);
						videoProcessor.setDetectedCarsCount(0);
						videoProcessor.setFinished(false);

						pauseVideo = true;
					} else {

					}
					Platform.runLater(() -> imageView.setImage(tmp));
				}
			}
		};
		timer = new Timer();

		Double period = 1000 / videoProcessor.getFPS() * 2;
		this.timer.schedule(frame_grabber, 0, period.longValue());

		lastVideoName = currentlyPlaying;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();
	}
}
