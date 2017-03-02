/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biofeedback.Controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import static java.lang.Math.PI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.management.Notification;

public class SettingsController implements Initializable {
        

    /**
     * Initializes the controller class.
     */
    @FXML Rectangle hourhand;
    @FXML Arc clockfill;
    @FXML Arc clockfill1;
    @FXML Arc clockfill2;
    @FXML Arc clockfill3;
    @FXML Label clocktime;
    @FXML private Button close_btn;
    @FXML private Button minimize_btn;
    @FXML private ToggleButton dnd_btn;
    final DragContext drag = new DragContext();
    public Rotate count_angle;
    
   
    
    
    public Rectangle notification_area;
    public Label label;
    public Pane pane;
    public LinearGradient area_fill;
    Timeline timeline;
   NotificationTime notification = new NotificationTime();
   globalCalibration gc = new globalCalibration();
   serialPortCommunication comm = new serialPortCommunication();
   dnd DND = new dnd();
  //  public int notification_time = 2000;
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if(notification.getAngle()!= 1000){
            double angle = notification.getAngle();
            double angle2 = notification.getAngle2();
               clockfill.setLength(-angle);
               clockfill1.setLength(-angle);
               clockfill2.setLength(-angle);
               clockfill3.setLength(-angle);
               
               hourhand.getTransforms().add(new Rotate(angle2,hourhand.getBoundsInLocal().getMinX()+hourhand.getBoundsInLocal().getWidth(),
                                                            hourhand.getBoundsInLocal().getMinY()+hourhand.getBoundsInLocal().getHeight()));
               
               int notification_lable = (int) notification.getNotificationTIme()/60;
               if(notification_lable<1){
               clocktime.setText("5 Sec");
               }
               else{
                   clocktime.setText(notification_lable+" Min");
               }
               
        }
        
        if(DND.getDnd()){
            dnd_btn.setSelected(true);
        }
        else{
            dnd_btn.setSelected(false);
        }
        dnd_btn.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent ae){
                if(dnd_btn.isSelected()){
                    DND.setDnd(true);
                }
                else{
                    DND.setDnd(false);
                }
            }
        });
        pane = new Pane();
         

        
        
        hourhand.setOnMousePressed(new EventHandler<MouseEvent>(){
            public void  handle(MouseEvent me){
                drag.old_xmin = hourhand.getBoundsInLocal().getMinX();
                drag.old_xmax = hourhand.getBoundsInLocal().getMaxX();
                drag.old_ymin = hourhand.getBoundsInLocal().getMinY();
                drag.old_ymax = hourhand.getBoundsInLocal().getMaxX();
            }
        });
        
//        hourhand.setOnMouseReleased(new EventHandler<MouseEvent>(){
//
//            @Override
//            public void handle(MouseEvent me) {
//               if(timeline.getStatus()==Animation.Status.RUNNING){
//                   timeline.stop();
//               }
//               
//                change_time(notification_time);
//            }
//            
//    });
        
        hourhand.setOnMouseDragged(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent me){
                //private Rotate newAngle = new Rotate(360, pivotX, pivotY)
               // hourhand.setRotate(me.getX() - drag.old_x);
                if(hourhand.getX()<hourhand.getBoundsInLocal().getMaxX())
                {
                    hourhand.getTransforms().add(new Rotate(1,hourhand.getBoundsInLocal().getMinX()+hourhand.getBoundsInLocal().getWidth(),
                                                            hourhand.getBoundsInLocal().getMinY()+hourhand.getBoundsInLocal().getHeight()));
                }
         
                
                //hourhand.setTranslateY(drag.ty + me.getY() - drag.old_y);
                
               double xx = hourhand.getLocalToSceneTransform().getMxx();
               double xy = hourhand.getLocalToSceneTransform().getMxy();
               double xz = hourhand.getLocalToSceneTransform().getMxy();
               double angle = Math.sin(xz);
               angle = Math.toDegrees(angle);
               
               
               double angle2 = Math.atan2(-xy, xx);
               angle2 = Math.toDegrees(angle2);
           
               if (angle2<0){
                   angle2 = 180 + (180 + angle2);
               }                      
               notification.setAngle(angle);
               notification.setAngle2(angle2);
               if(angle2 < 45)
               {
                   clocktime.setText("5 Sec");
                   notification.setNotificationTime(5);
                   
               }
               else if(angle2 >= 45 && angle2 < 90 )
               {
                   clocktime.setText("1 Min");
                   notification.setNotificationTime(60);
               }
               else if(angle2 >= 90 && angle2 < 135 )
               {
                   clocktime.setText("3 Min");
                   notification.setNotificationTime(180);
               }
               else if(angle2 >= 135 && angle2 < 180 )
               {
                   clocktime.setText("5 Min");
                   notification.setNotificationTime(300);
               }
               else if(angle2 >= 180 && angle2 < 225 )
               {
                   clocktime.setText("10 Min"); 
                   notification.setNotificationTime(600);
               }
               else if(angle2 >= 225 && angle2 < 270 )
               {
                   clocktime.setText("15 Min"); 
                   notification.setNotificationTime(900);
               }
               else if(angle2 >= 270 && angle2 < 315 )
               {
                   clocktime.setText("20 Min"); 
                   notification.setNotificationTime(1200);
               }
               else if(angle2 >= 315 && angle2 < 360 )
               {
                   clocktime.setText("30 Min"); 
                   notification.setNotificationTime(1800);
               }
              
               clockfill.setLength(-angle);
               clockfill1.setLength(-angle);
               clockfill2.setLength(-angle);
               clockfill3.setLength(-angle);
               
                
            }
        });
        
        
        //close stage
        close_btn.setOnAction(new EventHandler<ActionEvent>(){
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
    
    
    public void gotoCalibration(ActionEvent event) throws IOException {
       Stage stage = new Stage();
       Pane myPane = null;
       myPane = FXMLLoader.load(getClass().getResource("calibration.fxml"));
       Scene scene = new Scene(myPane);
       stage = new Stage(StageStyle.UNDECORATED);       
       stage.getIcons().add(new Image(getClass().getResource("images/icon4.png").toExternalForm()));
       stage.setScene(scene);
       
       Stage prevStage = (Stage) close_btn.getScene().getWindow();
       prevStage.close();

       stage.show();
    }
    
//    
//    public void show_popup() {
//        if(popup_status.getpopup()){
//        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
//        Stage primaryStage = (Stage)close_btn.getScene().getWindow(); 
//        final Popup popup = new Popup();
//        popup.setAutoHide(true);
//        popup.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 360);
//        popup.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 135);
//        popup.getContent().addAll(pane);
//        
//        FadeTransition ft = new FadeTransition(Duration.millis(500), pane);
//        ft.setFromValue(0.0);
//        ft.setToValue(1.0);
//        ft.setCycleCount(1);
//        ft.setAutoReverse(false);
//        ft.play();
//        popup.show(primaryStage);
//       
//        }}
//    
//    
//    public void change_time(int value){
//        timeline = new Timeline(new KeyFrame(
//                Duration.seconds(value),
//                ae -> show_popup()));
//                   timeline.setCycleCount(Animation.INDEFINITE);
//                   timeline.play();
//    }
//    
//    
    
    
    
    private static final class DragContext{
        public double old_xmin;
        public double old_ymin;
        public double old_xmax;
        public double old_ymax;
        public double tx;
        public double ty;
        
    }
    
}
