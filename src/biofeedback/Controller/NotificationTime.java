package biofeedback.Controller;

public class NotificationTime {
    public static int notification_time;
    public static double angle = 1000;
    public static double angle2 = 1000;
    
    public void setNotificationTime(int time){
        notification_time = time;
    }
    public int getNotificationTIme(){
        return notification_time;
    }
    
    public void setAngle(double angle_value){
        angle = angle_value;
    }
    public double getAngle(){
        return angle;
    }
    
    public void setAngle2(double angle_value){
        angle2 = angle_value;
    }
    public double getAngle2(){
        return angle2;
    }
}
