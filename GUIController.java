/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cp.assignment.gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Jason Pang
 */
public class GUIController extends Application implements Initializable {

    public volatile static Clock clock = new Clock();
    private static double animationDuration = 1;  // Time interval for each second in simulation
    Scene mainScene;

    private static Processes processes;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.processes = new Processes();
        
        // Define a new Runnable
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    processes.startProcess();
                } catch (InterruptedException ex) {
                    Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        };
        
        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
 
        // Terminate the running thread if the application exists
        backgroundThread.setDaemon(true);
 
        // Start the thread
        backgroundThread.start();
        

        makeMainScreen(primaryStage);

        primaryStage.setMinHeight(920);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Museum");

        primaryStage.setScene(mainScene);
        primaryStage.setOnHidden(e -> {
            Platform.exit();
            System.out.println("Performing system cleanup");
        });

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void makeMainScreen(Stage primaryStage) {
        /*  MAKING THE CLOCK   */
        final Label digitalClock = new Label();
        

        // the digital clock updates once a second.
        final Timeline digitalTime = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String hourString = pad(2, '0', clock.getHOUR() + "");
                        String minuteString = pad(2, '0', clock.getMINUTE() + "");
                        //check if clock is correct or not
                        //System.out.println(hourString + ":" + minuteString);
                        digitalClock.setText(hourString + ":" + minuteString);
                        
                        clock.updateClock();
                    }
                }
                ),
                new KeyFrame(Duration.seconds(animationDuration))
        );

        //Start cycle for digital clock
        digitalTime.setCycleCount(Animation.INDEFINITE);
        digitalTime.play();
        BorderPane borderPane = new BorderPane();

        HBox clockHBox = new HBox();
        clockHBox.setAlignment(Pos.CENTER);
        Label timeString = new Label("Time : ");
        timeString.setFont(new Font("Verdana", 18));
        digitalClock.setFont(new Font("Verdana", 18));
        digitalClock.setText(processes.getTime());
        clockHBox.getChildren().addAll(timeString, digitalClock);
        

        // The processes labels on top
        Label processLabel = new Label("Processes");
        processLabel.setFont(Font.font("Verdana", 18));

        //TEXT AREA
        TextArea textArea = new TextArea();
        textArea.setMinSize(600, 500);
        textArea.setEditable(false);
        textArea.setFont(Font.font("Verdana", 17));
        //Get and print processes

        //Print Processes Here
        textArea.textProperty().bind(processes.getProcess());
        
        //GRIDPANE
        GridPane gridPane = new GridPane();
        GridPane.setHalignment(processLabel, HPos.CENTER);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setConstraints(processLabel, 0, 0);
        GridPane.setConstraints(textArea, 0, 1);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.getChildren().addAll(processLabel, textArea);

        borderPane.setTop(clockHBox);
        borderPane.setCenter(gridPane);
        mainScene = new Scene(borderPane, 920, 920, Color.TRANSPARENT);

    }

    public static void updateProcess() {
        // Things to update
    }

    // To format a string
    public static String pad(int fieldWidth, char padChar, String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i < fieldWidth; i++) {
            sb.append(padChar);
        }
        sb.append(s);
        return sb.toString();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
