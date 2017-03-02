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
public class compareValue {
    public static boolean popup_status = false;
    
    public void setpopup(boolean popup){
        popup_status=popup;        
    }
    
    public boolean getpopup(){
        return popup_status;
    }
}
