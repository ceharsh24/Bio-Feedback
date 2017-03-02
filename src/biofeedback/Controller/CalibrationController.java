package biofeedback.Controller;

import static biofeedback.Controller.compareValue.popup_status;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;


public class CalibrationController implements Initializable {

    Rotate rotation = new Rotate();
    
    
    @FXML private Button close_btn;
    @FXML private Button minimize_btn;
    @FXML private Circle outer;
    @FXML private Circle outer2;
    @FXML private Circle outer3;
    @FXML private Circle outer4;
    @FXML private Label port_identification_label;
    @FXML private Label start_calibration_label;
    
    @FXML private ToggleButton port_led;
    @FXML private ToggleButton start_led;
    @FXML private Button back_btn;
    public Rectangle rectangle1;
    public Rectangle notification_area;
    public Label warning_label;
    public Label message_label;
    public Rectangle notification_image;
    public Button notification_close;
    public Pane pane;
    public LinearGradient area_fill;
    public Rectangle warning_image;
    public static int readled = 0;
    
    public static boolean start_status = false;
    
    Timeline timeline;
    dnd DND = new dnd();
    compareValue popup_status =new compareValue();
    NotificationTime notification_time = new NotificationTime();
    
        globalCalibration gc = new globalCalibration();
    
    // trayicon
    TrayIcon trayicon;
    SystemTray tray;

    public CalibrationController() {
//        if(SystemTray.isSupported()){
//            tray = SystemTray.getSystemTray();
//            java.awt.Image image = Toolkit.getDefaultToolkit().getImage(CalibrationController.class.getResource("images/icon4.png").toExternalForm());
//          
//        }
        
    }

    
   @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        NotificationTime notification = new NotificationTime();
        int notification_time = notification.getNotificationTIme();
        serialPortCommunication comm = new serialPortCommunication();
        //port_led.setStyle("-fx-background-image:url("+led_on+")");
        
        
        Task task = new Task<Void>(){

            @Override
            protected Void call() throws Exception {
                while(true){
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            if(!gc.getCalibration())
            {
            comm.getPort();
            if(gc.getLed()){
                
                FadeTransition ft2 = new FadeTransition(Duration.millis(1000), port_led);
                ft2.setFromValue(1.0);
                ft2.setToValue(1.0);
                ft2.setCycleCount(1);
                ft2.setAutoReverse(false);
                ft2.play();
                ft2.setOnFinished(new EventHandler<ActionEvent>(){
                    public void handle(ActionEvent ae){
                        port_led.setSelected(true);
                    }
                });
                
            }
            
            
        }
                            
                            if(readled<1){
                                if(gc.getReadLed()){
                                    start_led.setSelected(true);
                                    readled++;
                                    
                                    FadeTransition ft5 = new FadeTransition(Duration.millis(1000), start_led);
                                    ft5.setFromValue(1.0);
                                    ft5.setToValue(1.0);
                                    ft5.setCycleCount(1);
                                    ft5.setAutoReverse(false);
                                    ft5.play();
                                    ft5.setOnFinished(new EventHandler<ActionEvent>(){
                                        public void handle(ActionEvent ae){
                                            FadeTransition ft6 = new FadeTransition(Duration.millis(5000), start_led);
                                            ft6.setFromValue(1.0);
                                            ft6.setToValue(1.0);
                                            ft6.setCycleCount(1);
                                            ft6.setAutoReverse(false);
                                            ft6.play();
                                            ft6.setOnFinished(new EventHandler<ActionEvent>(){
                                                public void handle(ActionEvent ae){
                                                    Stage stage=(Stage) minimize_btn.getScene().getWindow();
                    //                                Tray tray = new Tray(stage);
                    //                                tray.tray_minimize(stage);
                    //                                 
                    //                                 stage.setIconified(true);
                    //                                 stage.hide();
                    //                                SystemTrayIcon tray = new SystemTrayIcon();
                    //                                tray.setTrayIcon(stage);

//                                                    if(SystemTray.isSupported()){
//                                                        Platform.setImplicitExit(false);
//                                                       // stage.setIconified(true);
//                                                       // stage.hide();
//                                                       setTrayIcon(stage);
//                                                    }

                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    });
                    Thread.sleep(1000);
                }
            }
            
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        
        
        //gc.setCalibration(true);
        rotate();
        
        change_time(notification_time);
       
       
       
        pane = new Pane();
        
        
               
       // change_time(notification_time);
       
          
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#ffffff")), new Stop(1, Color.web("#eeeeee"))};
        area_fill = new LinearGradient(0, 10, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        notification_area = new Rectangle(350, 100);
        notification_area.setFill(new ImagePattern(new Image(getClass().getResource("images/bg.jpg").toExternalForm())));
        //notification_area.setStyle("-fx-background-image:url(\"../images/bg.jpg\")");         
        notification_area.setArcHeight(10);
        notification_area.setArcWidth(10);
        notification_area.setStrokeWidth(0.5);
        notification_area.setStroke(Color.web("#007fbc"));
        notification_image = new Rectangle(90, 90);
        notification_image.setFill(new ImagePattern(new Image(getClass().getResource("images/icon.png").toExternalForm())));
        notification_image.setX(225);
        notification_image.setY(4);
        warning_image = new Rectangle(40, 40);
        warning_image.setFill(new ImagePattern(new Image(getClass().getResource("images/warning.png").toExternalForm())));
        //warning_image.setStyle("-fx-start-margin:2000px;");
        warning_image.setX(5);
        warning_image.setY(5);
        warning_label = new Label("Warning");
        warning_label.setStyle("-fx-text-fill:red;-fx-font-weight:bolder");
        warning_label.setFont(new Font("Cambria", 25));
        warning_label.setTranslateX(60);
        warning_label.setTranslateY(10);
        message_label = new Label("Sit Properly . . .");
        message_label.setStyle("-fx-text-fill:#007bcf;-fx-font-weight:bold");
        message_label.setFont(new Font("Cambria", 18));
        message_label.setTranslateX(10);
        message_label.setTranslateY(70);
        String image = CalibrationController.class.getResource("images/x.png").toExternalForm();
        //String image_hover = CalibrationController.class.getResource("images/x_hover.png").toExternalForm();
        notification_close = new Button();
        notification_close.setStyle("-fx-background-image:url('"+image+"');-fx-background-color:transparent;-fx-background-size:100%;-fx-background-repeat:no-repeat;");
        notification_close.hoverProperty();
        notification_close.setTranslateX(325);
        notification_close.setTranslateY(7);
        pane.getChildren().addAll(notification_area,message_label,warning_image,warning_label,notification_image,notification_close);
        close_btn.setOnAction(new EventHandler<ActionEvent>(){

           @Override
            public void handle(ActionEvent ae){
                if(gc.getCalibration()){
                    comm.disconnect();
                }
                Stage stage = (Stage) close_btn.getScene().getWindow();
                stage.close();
            }
        });
        
        minimize_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Stage stage=(Stage) minimize_btn.getScene().getWindow();
                stage.setIconified(true);
            }
        });
    }    

    public void show_popup() {
        
        if(!DND.getDnd() && popup_status.getpopup()){
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        Stage primaryStage = (Stage)close_btn.getScene().getWindow(); 
        final Popup popup = new Popup();
        popup.setAutoHide(true);
        popup.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 360);
        popup.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 110);
        popup.getContent().addAll(pane);
        
        FadeTransition ft = new FadeTransition(Duration.millis(500), pane);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
        popup.show(primaryStage);
        ft.setOnFinished(new EventHandler<ActionEvent>(){
        public void handle(ActionEvent ae){
            
            FadeTransition ft2 = new FadeTransition(Duration.millis(2500), pane);
            ft2.setFromValue(1.0);
            ft2.setToValue(1.0);
            ft2.setCycleCount(1);
            ft2.setAutoReverse(false);
            ft2.play();
            
            ft2.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    FadeTransition ft3 = new FadeTransition(Duration.millis(2500), pane);
                    ft3.setFromValue(1.0);
                    ft3.setToValue(0.0);
                    ft3.setCycleCount(1);
                    ft3.setAutoReverse(false);
                    ft3.play();

                    ft3.setOnFinished(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            popup.hide();
                        }
                    });
                }
            });
            
        }
        });
     }
    }
    
    
    
    public void change_time(int value){
        timeline = new Timeline(new KeyFrame(
                Duration.seconds(value),
                ae -> show_popup()));
                   timeline.setCycleCount(Animation.INDEFINITE);
                   timeline.play();
    }
    
    

    public void rotate(){
        RotateTransition rotate_circle = new RotateTransition(Duration.millis(3000), outer);
        rotate_circle.setFromAngle(180);
        rotate_circle.setToAngle(-180);
     
        RotateTransition rotate_circle2 = new RotateTransition(Duration.millis(3000), outer2);
        rotate_circle2.setFromAngle(-360);
        rotate_circle2.setToAngle(360);

        RotateTransition rotate_circle3 = new RotateTransition(Duration.millis(3000), outer3);
        rotate_circle3.setFromAngle(180);
        rotate_circle3.setToAngle(-180);

        RotateTransition rotate_circle4 = new RotateTransition(Duration.millis(3000), outer4);
        rotate_circle4.setFromAngle(-360);
        rotate_circle4.setToAngle(360);
        
        ParallelTransition rotate_pr = new ParallelTransition(rotate_circle,rotate_circle2,rotate_circle3,rotate_circle4);
        rotate_pr.setCycleCount(Timeline.INDEFINITE);
        //rotate_pr.setAutoReverse(true);
        rotate_pr.play();
    }


    public void gotoSettings(ActionEvent event) throws IOException {
       Stage stage = new Stage();
       Pane myPane = null;
       myPane = FXMLLoader.load(getClass().getResource("Settings.fxml"));
       Scene scene = new Scene(myPane);
       stage = new Stage(StageStyle.UNDECORATED);       
       stage.getIcons().add(new Image(getClass().getResource("images/icon4.png").toExternalForm()));
       stage.setScene(scene);
       
       Stage prevStage = (Stage) back_btn.getScene().getWindow();
       prevStage.close();

       stage.show();
    } 
    
    public void changeLed(){
        port_led.setSelected(true);
    }
    
    public void changeReadLed(){
        start_led.setSelected(true);
    }
    
    
    
    //tray//
    
    
    /**
     * Sets up a system tray icon for the application.
     */
        public static void setTrayIcon(Stage stage){
            SystemTray sTray = null;
            sTray = SystemTray.getSystemTray();
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg");
            
            PopupMenu popup = new PopupMenu();
            MenuItem show = new MenuItem("show");
            MenuItem close = new MenuItem("Close");
            
            TrayIcon icon = new TrayIcon(image, "Bio Feed-Back+", popup);
            
            ActionListener listenershow = new ActionListener(){

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                   Platform.runLater(new Runnable(){

                       @Override
                       public void run() {
                           //stage.setIconified(false);
                           stage.show();
                       }
                       
                   });
                }
                
                
        };
            
            ActionListener listenerclose =new ActionListener() {

                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        System.exit(0);
                    }
                };
            
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent event) {
                   // stage.hide();
                }
            });
    //trayend//
            
            show.addActionListener(listenershow);
            close.addActionListener(listenerclose);
            
            popup.add(show);
            popup.add(close);
            
            try{
                sTray.add(icon);
            }catch(AWTException e){
                System.err.println(e);
            }
    }
}



 