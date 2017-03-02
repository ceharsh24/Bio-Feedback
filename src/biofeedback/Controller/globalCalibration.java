/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biofeedback.Controller;

/**
 *
 * @author Harsh
 */
public class globalCalibration {
    public static boolean calibrationStart = false;
    public static boolean led = false;
    public static boolean readLed = false;
    
    public void setCalibration(boolean value){
        calibrationStart = value;
    }
    
    public boolean getCalibration(){
        return calibrationStart;
    }
    
    public void setLed(boolean value){
        led = value;
    }
    
    public boolean getLed(){
        return led;
    }
    
    public void setReadLed(boolean value){
        readLed = true;
    }
    
    public boolean getReadLed(){
        return readLed;
    }
}
