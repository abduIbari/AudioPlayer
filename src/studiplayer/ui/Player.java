package studiplayer.ui;

import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;

import java.io.File;
import java.net.URL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Player extends Application{
	
	boolean useCertPlayList = false;
	public final static String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";
	private final static String PLAYLIST_DIRECTORY = "/Users/abdulbari/Desktop/Programming 2/Task6/playlists";
	private final static String INITIAL_PLAY_TIME_LABEL = "00:00";
	private final static String NO_CURRENT_SONG = "-";
	
	private Button playButton;
	private Button pauseButton;
	private Button stopButton;
	private Button nextButton;
	private Label playListLabel = new Label(DEFAULT_PLAYLIST);
	private Label playTimeLabel = new Label(INITIAL_PLAY_TIME_LABEL);
	private Label currentSongLabel = new Label(NO_CURRENT_SONG);
	private ChoiceBox<SortCriterion> sortChoiceBox;
	private TextField searchTextField;
	private Button filterButton;
	
	private PlayList playList = new PlayList();
	SongTable table = new SongTable(playList);
	
	
	private TimerThread timerThread;
    private PlayerThread playerThread;
    private Thread runnableTimerThread;
    private Thread runnablePlayerThread;
    private boolean isSongPaused = false;
    
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@SuppressWarnings({ "deprecation" })
	@Override
	public void start(Stage primaryStage) throws Exception {
		if (useCertPlayList == true) {
			loadPlayList(DEFAULT_PLAYLIST);
			playListLabel.setText(DEFAULT_PLAYLIST);
		}
		else {
			FileChooser fileChooser = new FileChooser();		
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null) {
				String pathName = file.getPath();
				loadPlayList(pathName);
				playListLabel.setText(file.getPath());
			}else {
				loadPlayList(null);
				playListLabel.setText(DEFAULT_PLAYLIST);
			}
		}
		
		Stage window = primaryStage;
		window.setTitle("APA Player");
		
		//table
		SongTable table = new SongTable(playList);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		
// 		--------------
		
		
		//filter option on the top
		GridPane filterPane = new GridPane();
	    filterPane.setPadding(new Insets(10));
	    filterPane.setVgap(10);
	    filterPane.setHgap(10);
	    
	    
	    Label searchLabel = new Label("Search text:");
	    GridPane.setConstraints(searchLabel, 0, 0);
	    
	    
	    searchTextField = new TextField();
	    GridPane.setConstraints(searchTextField, 1, 0);
	    
	    
	    Label sortLabel = new Label("Sort by:");
	    GridPane.setConstraints(sortLabel, 0, 1);
	    
	    
	    sortChoiceBox = new ChoiceBox<SortCriterion>();
	    GridPane.setConstraints(sortChoiceBox, 1, 1);
	    sortChoiceBox.getItems().addAll(SortCriterion.values());
	    sortChoiceBox.setValue(SortCriterion.DEFAULT);
	    
	    
	    
	    filterButton = new Button("display");
	    GridPane.setConstraints(filterButton, 2, 1);
	    filterButton.setOnAction(e -> {
	    	playList.setSearch(searchTextField.getText());
	    	playList.setSortCriterion(sortChoiceBox.getValue());
	    	table.refreshSongs();
	    });
	    
	    filterPane.getChildren().addAll(searchLabel, searchTextField, sortLabel, sortChoiceBox, filterButton);
	    
	    TitledPane filter = new TitledPane("Filter", filterPane);
	    
	    
// 		--------------
	    
	    
		//gridpane for song info, this will go in the VBox
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(0,0,0,10));;
		grid.setVgap(8);
		grid.setHgap(10);
	
		
		grid.add(new Label("PlayList: "), 0, 0);
		grid.add(playListLabel, 1, 0);
		
		grid.add(new Label("Current Song: "), 0, 1);
		grid.add(currentSongLabel, 1, 1);
		
		grid.add(new Label("Playtime: "), 0, 2);
		grid.add(playTimeLabel, 1, 2);
				
		
// 		--------------
		
		
		//Hbox for buttons, this will go in the VBox
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		playButton = createButton("play.jpg");
		playButton.setOnAction(e -> playCurrentSong());
		
		pauseButton = createButton("pause.jpg");
		pauseButton.setOnAction(e -> pauseCurrentSong());
		
		nextButton = createButton("next.jpg");
		nextButton.setOnAction(e -> playNextSong());
		
		stopButton = createButton("stop.jpg");
		stopButton.setOnAction(e -> stopCurrentSong());
		
		buttons.getChildren().addAll(playButton, pauseButton, stopButton, nextButton);
		
		
// 		--------------
		
		
		//VBox for bottom of the borderpane
		VBox bottomVBox = new VBox();
		bottomVBox.setPadding(new Insets(10,10,10,10));
		
		bottomVBox.getChildren().addAll(grid, buttons);
		
		
// 		--------------
		
		//the entire layout
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(table);
		borderPane.setTop(filter);
		borderPane.setBottom(bottomVBox);
		
		Scene firstScene = new Scene(borderPane, 600, 400);
		primaryStage.setScene(firstScene);
		
		setButtonStates(false,true,true,false);

		window.show();
		
	}
	
	public void setUseCertPlayList(boolean value) {
		useCertPlayList = value;
	}
	
	public void setPlayList(String pathname) {
		playList = new PlayList(pathname);
		table.refreshSongs();
	}
	
	public void loadPlayList(String pathname) {
		if (pathname == null || pathname.isEmpty()) {
			playList = new PlayList(DEFAULT_PLAYLIST);
		}
		else {
			playList = new PlayList(pathname);
		}		
	}
	
	private Button createButton(String iconfile) {
		Button button = null;
		try {
			URL url = getClass().getResource("/icons/" + iconfile);
			Image icon = new Image(url.toString());
			ImageView imageView = new ImageView(icon); imageView.setFitHeight(20);
			imageView.setFitWidth(20);
			button = new Button("", imageView); 
			button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			button.setStyle("-fx-background-color: #fff;");
		} 
		catch (Exception e) 
			{ System.out.println("Image " + "icons/"
					+ iconfile + " not found!"); System.exit(-1);
			}
		return button;
	}
	
	
	
	private void playCurrentSong(){
		updateSongInfo(playList.currentAudioFile());
		startThreads(false);
		setButtonStates(true, false, false, false);
	}
	
	private void pauseCurrentSong() {
		if(isSongPaused) {
			startThreads(true);
			isSongPaused = false;
		}
		else {
			terminateThreads(true);
			isSongPaused = true;
		}
		playList.currentAudioFile().togglePause();
		setButtonStates(true, false, false, false);
		updateSongInfo(playList.currentAudioFile());
	}
	
	private void stopCurrentSong(){
		terminateThreads(false);
		playList.currentAudioFile().stop();
		setButtonStates(false, true, true, false);
		updateSongInfo(null);
	}
	
	private void playNextSong() {
		stopCurrentSong();
		setButtonStates(true, false,false,false);
		playList.nextSong();
		playCurrentSong();
		
	}


	private void setButtonStates(boolean playButtonState, boolean pauseButtonState, boolean stopButtonState, boolean nextButtonState) {
		Platform.runLater(() -> {
		this.playButton.setDisable(playButtonState);
		this.pauseButton.setDisable(pauseButtonState);
		this.stopButton.setDisable(stopButtonState);
		this.nextButton.setDisable(nextButtonState);
		});
	}
	
	protected void updateSongInfo(AudioFile af) { Platform.runLater(() -> {
		if (af == null) {
			// set currentSongLabel and playTimeLabel
			currentSongLabel.setText(NO_CURRENT_SONG);
			playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
		}
		else {
			// set currentSongLabel and playTimeLabel
			currentSongLabel.setText(af.getTitle());
			playTimeLabel.setText(af.formatPosition());
			}
		});
	}
	
	
	private class PlayerThread implements Runnable {
		private boolean stopped = false;
		
		public void terminate() {
			stopped = true;
		}

		@Override
		public void run() {
	        while (stopped == false && playList.currentAudioFile() != null) {
	            try {
	                playList.currentAudioFile().play();
	            } catch (NotPlayableException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	private class TimerThread implements Runnable{
		private boolean stopped = false;
//		Player player = new Player();
		
		public void terminate() {
			stopped = true;
		}

		@Override
		public void run() {
			while(stopped == false && playList.currentAudioFile() != null) {
				updateSongInfo(playList.currentAudioFile());
				try {
	                Thread.sleep(100); 
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
			}
		}
	}
	
	private void startThreads(boolean onlyTimer) {
	    // Check if the timer thread is already running
	    if (runnableTimerThread == null || !runnableTimerThread.isAlive()) {
	        timerThread = new TimerThread();
	        runnableTimerThread = new Thread(timerThread);
	        runnableTimerThread.start();
	    }

	    // Check if the player thread is already running and only start it if not running
	    if (!onlyTimer && (runnablePlayerThread == null || !runnablePlayerThread.isAlive())) {
	        playerThread = new PlayerThread();
	        runnablePlayerThread = new Thread(playerThread);
	        runnablePlayerThread.start();
	    }
	}

	private void terminateThreads(boolean onlyTimer) {
	    if (runnableTimerThread != null) {
	        timerThread.terminate();
	        timerThread = null;
	        runnableTimerThread = null;
	    }

	    if (!onlyTimer && runnablePlayerThread != null) {
	        playerThread.terminate();
	        playerThread = null;
	        runnablePlayerThread = null;
	    }
	}	
}

