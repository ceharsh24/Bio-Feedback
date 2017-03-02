package biofeedback;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.*;
import javafx.concurrent.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Duration;
 
/**
 * Example of displaying a splash page for a standalone JavaFX application
 */
public class StartupScreen extends Application {
 
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;
    private static final int SPLASH_WIDTH = 676;
    private static final int SPLASH_HEIGHT = 227;
 
    public static void main(String[] args) throws Exception {
        launch(args);
    }
 
    @Override
    public void init() {
        ImageView splash = new ImageView(new Image(StartupScreen.class.getResource("images/splash.jpg").toExternalForm()));
        
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH);
        progressText = new Label("");
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setStyle(
                "-fx-padding: 5; " +
                "-fx-background-color: white; " +
                "-fx-border-width:5; " +
                "-fx-border-color: " +
                    "linear-gradient(" +
                        "to top, " +
                        "black, " +
                        "derive(black, 50%)" +
                    ");"
        );
        splashLayout.setEffect(new DropShadow());
    }
 
    @Override
    public void start(final Stage initStage) throws Exception {
        final Task<ObservableList<String>> ContentTask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws InterruptedException {
                ObservableList<String> foundContents =
                        FXCollections.<String>observableArrayList();
                ObservableList<String> availableContents =
                        FXCollections.observableArrayList(
                               "Connecting Device . . .","Loading Packages . . .","Starting Application . . ."
                        );
 
                updateMessage("Initializing . . .");
                int size = availableContents.size();
                for (int i = 0; i < size; i++) {
                Thread.sleep(2000);
                    //updateProgress(i + 1, availableContents.size());
                    String nextContent = availableContents.get(i);
                    foundContents.add(nextContent);
                    updateMessage(nextContent);
                }
                Thread.sleep(1000);
                updateMessage("All Components Loaded. . .");
 
                return foundContents;
            }
        };
 
        showSplash(initStage,
                ContentTask,
                () -> {
            try {
                showMainStage(ContentTask.valueProperty());
            } catch (IOException ex) {
                Logger.getLogger(StartupScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        );
        new Thread(ContentTask).start();
    }
 
    private void showMainStage(
            ReadOnlyObjectProperty<ObservableList<String>> Contents
    ) throws IOException {
        mainStage = new Stage(StageStyle.UNDECORATED);       
        mainStage.getIcons().add(new Image(StartupScreen.class.getResource("images/icon4.png").toExternalForm()));
        mainStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Controller/Settings.fxml"))));
        mainStage.show();
    }
 
    private void showSplash(
            final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler
    ) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(0.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();
 
                initCompletionHandler.complete();
            } // todo add code to gracefully handle other task states.
        });
 
        Scene splashScene = new Scene(splashLayout);
        initStage.initStyle(StageStyle.UNDECORATED);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.getIcons().add(new Image(getClass().getResource("images/icon4.png").toExternalForm()));
       
        initStage.show();
    }
 
    public interface InitCompletionHandler {
        public void complete();
    }
}