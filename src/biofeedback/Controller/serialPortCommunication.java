/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biofeedback.Controller;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 *
 * @author jaimin
 */
public class serialPortCommunication {
    public static int led = 0;
    static SerialPort serialPort;
    globalCalibration gc = new globalCalibration();
    CalibrationController cc =new CalibrationController();
    public void getPort(){
        System.out.println("Program started");
		//System.out.println(java.library.path);
	CommPortIdentifier serialPortId;
	//static CommPortIdentifier sSerialPortId;
	Enumeration enumComm;
	//SerialPort serialPort;
        int count=0;
        String portName = null;
	enumComm = CommPortIdentifier.getPortIdentifiers();
	while (enumComm.hasMoreElements() && count <1) {
            serialPortId = (CommPortIdentifier) enumComm.nextElement();
	    if(serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                System.out.println(serialPortId.getName());
                portName = serialPortId.getName();
                
                count++;
            }
        }
                //SimpleRead(portName);
                RS232Base serial = new RS232Base(portName, 9600);
                serial.checkPorts();
                
		System.out.println("Finished successfully");
    }
    
    public  void disconnect(){
        serialPort.close();
    }
    
            public class RS232Base {

                private Enumeration portList = null;
                private CommPortIdentifier portId = null;
                private String defaultPort = null;
                private boolean portFound = false;
                private int baudRate = 0;

                /*******************************
                * Constructor for the base class
                * ******************************/
                public RS232Base(String defaultPort, int baudRate){
                    this.defaultPort = defaultPort;
                    this.baudRate = baudRate;
                }
                /********************************
                 * Methode to check the presence
                 * of ports on this system
                 *******************************/
                public void checkPorts(){

                    /***************************************
                     * Get a list of all ports on the system
                     **************************************/
                    portList = CommPortIdentifier.getPortIdentifiers();
                    System.out.println("List of all serial ports on this system:");

                    while(portList.hasMoreElements()){
                        portId = (CommPortIdentifier)portList.nextElement();
                        if(portId.getName().equals(defaultPort)){
                            portFound = true;
                            System.out.println("Port found on: " + defaultPort);

                            new SerialProgram(portId, baudRate);  // If port found, create a new class
                            gc.setCalibration(true);
                            
                            gc.setLed(true);
                        }   
                    }

                    if(!portFound){
                        System.out.println("No serial port found!!!");
                        gc.setCalibration(false);
                    }
                }


                 public class SerialProgram implements Runnable{

                private CommPortIdentifier portId = null;
               // private SerialPort serialPort = null;
                private DataInputStream is;
                private int baudRate = 0;

                private Thread readThread;



                /************************
                 * Constructor definition
                 ***********************/
                public SerialProgram(CommPortIdentifier portId, int baudRate){

                    this.portId = portId;
                    this.baudRate = baudRate;


                    /**********************
                     * Open the serial port
                     *********************/
                    try{
                        serialPort = (SerialPort)portId.open("Artificial Horizont", 2000);
                        
                    } catch (PortInUseException ex){
                        System.err.println("Port already in use!");
                    }

                    // Get input stream
                    try{
                        is = new DataInputStream(serialPort.getInputStream());
                    } catch (IOException e){
                        System.err.println("Cannot open Input Stream " + e);
                        is =null;
                    }


                    try{
                        serialPort.setSerialPortParams(baudRate,
                                                       SerialPort.DATABITS_8,
                                                       SerialPort.STOPBITS_1,
                                                       SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException ex){
                        System.err.println("Wrong settings for the serial port: " + ex.getMessage());
                    }


                    try{
                        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                    } catch (UnsupportedCommOperationException ex){
                        System.err.println("Check the flow control setting: " + ex.getMessage());
                    }

                    // Add an event Listener
                    try{
                        serialPort.addEventListener(new SerialReader(is));
                    } catch (TooManyListenersException ev){
                        System.err.println("Too many Listeners! " + ev);
                    }

                    // Advise if data available to be read on the port
                    serialPort.notifyOnDataAvailable(true);

                    // Define a Thread for reading
                    readThread = new Thread(this);
                    readThread.start();
                }



                @Override
                public void run() {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {}

                }
                

            }

                public class SerialReader implements SerialPortEventListener{

                private BufferedReader inStream;
                public int pre_x = 0;
                public int pre_y = 0;
                public int pre_z = 0;
                public int counter = 0;
                compareValue val = new compareValue();
                // Constructor
                public SerialReader(InputStream is){
                    inStream = new BufferedReader(new InputStreamReader(is));
                }



                @Override
                public void serialEvent(SerialPortEvent event) {

                    String rawInput = null;

                    switch(event.getEventType()){
                    case SerialPortEvent.BI:
                    case SerialPortEvent.CD:
                    case SerialPortEvent.CTS:
                    case SerialPortEvent.DSR:
                    case SerialPortEvent.FE:
                    case SerialPortEvent.OE:
                    case SerialPortEvent.PE:
                    case SerialPortEvent.RI:
                    case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                        break;

                    case SerialPortEvent.DATA_AVAILABLE:
                        try {
                            rawInput = inStream.readLine();
                            
                            if(rawInput == null){
                                System.out.println("No input on serial port");
                                gc.setReadLed(false);
                                System.exit(0);
                            }
                            
                           // System.out.print(rawInput+"\n");
                            
                           // System.out.println(rawInput);
                            
                        int cur_x = Integer.parseInt(rawInput.charAt(2)+""+rawInput.charAt(3)+""+rawInput.charAt(4)+rawInput.charAt(5));
                            int cur_y = Integer.parseInt(rawInput.charAt(10)+""+rawInput.charAt(11)+""+rawInput.charAt(12)+rawInput.charAt(13));
                            int cur_z = Integer.parseInt(rawInput.charAt(18)+""+rawInput.charAt(19)+""+rawInput.charAt(20)+rawInput.charAt(21));
                           // System.out.println(rawInput);
                            System.out.print("x="+cur_x+", y="+cur_y+", z="+cur_z);
                            gc.setReadLed(true);
                            if(pre_x >(cur_x + 15) || (pre_x < cur_x-15) ||pre_y >(cur_y + 15) || (pre_y < cur_y-15) || pre_z >(cur_z + 15) || (pre_z < cur_z-15)){
                                pre_x = cur_x;
                                pre_y = cur_y;
                                pre_z = cur_z;
                                counter = 0;
                                
                                val.setpopup(false);
                                
                                
                                System.out.println(" counter = "+counter);
                            }
                            else{
                                counter++;
                                if(counter>15){
                                    //compareValue val = new compareValue();
                                    val.setpopup(true);
                                }
                                System.out.println(" counter = "+counter);
                            }
                            


                        } catch (IOException e) {
                            e.printStackTrace();
                            System.exit(-1);
                        }
                        finally{
                        }
                        break;

                    default:
                        break;

                        }

                    }
                }
            }

    
    
}
