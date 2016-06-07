
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

/*
 * Class for speaking with the Arduino. This involves listening for serial input. 
 * This method contains methods for sending to the Arduino, as this was a part of a early plan, but is no longer in use. 
 * After it is initialized we add eventListeners and the rest is handled by serialEvent() and handleInput()
 */
public class PiCom implements SerialPortEventListener{
	SerialPort serialPort = null;
	
    private static final String PORT_NAMES[] = { 
 //           "/dev/tty.usbmodem", // Mac OS X
 //           "/dev/usbdev", // Linux
           "/dev/ttyACM0", // Linux
//            "/dev/serial", // Linux
    		//"/dev/sda1",
//            "COM3", // Windows
    };
    
    private String appName;
    private BufferedReader input;
    private OutputStream output;
    private Handler handler;
    
    private static final int TIME_OUT = 1000; // Port open timeout
    private static final int DATA_RATE = 9600; // Arduino serial port
    
    public PiCom(Handler handler){
    	appName = getClass().getName();
    	this.handler = handler;
    }
    
	public void initialize() {
        // the next line is for Raspberry Pi and 
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
	
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		
		//First we need to find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}
		
		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);
		
			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		
			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();
		
			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

    private void sendData(String data) {
        try {
            System.out.println("Sending data: '" + data +"'");
            
            // open the streams and send the "y" character
            output = serialPort.getOutputStream();
            output.write( data.getBytes() );
        }
        catch (Exception e) {
            System.err.println(e.toString());
            System.exit(0);
        }
    }

    /*
     * should be used when we close the connection
     */
    public synchronized void close() {
        if ( serialPort != null ) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }


    /*
     * (non-Javadoc)
     * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
     * 
     * Handle serial event. Reads it an passes it to handleInput()
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        //System.out.println("Event received: " + oEvent.toString());
    	if(oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE){
    	    try {
    	        String inputLine=null;
    	        if (input.ready()) {
    	            inputLine = input.readLine();
    	            System.out.println(inputLine);
    	            handleInput(inputLine);
    	        }

    	    } catch (Exception e) {
    	        System.err.println(e.toString());
    	    }
    	}
    }
    /*
     * Dirty and a non-impressive way of solving this.. Every "pod" has two rfid tags so it can be used two ways. 
     * To stop the radio streams we use a sleep(10) (also not a very good solution, but it is to sort out the threads.
     * Would make a sleeker solution if we had more time or in new version.
     */
    private void handleInput(String inputLine){
        if(inputLine.equals("start 2724C2A0")){
            handler.mp3Arr[0].start();
        }else if(inputLine.equals("start 271E3B80")){
        	handler.mp3Arr[0].start();
        }else if(inputLine.equals("start 2714DFB0")){
        	handler.mp3Arr[1].start();
        }else if(inputLine.equals("start 271C9DD0")){
        	handler.mp3Arr[1].start();
        }else if(inputLine.equals("start 271FC560")){
        	handler.radioArr[0].start();
        }else if(inputLine.equals("start 271582A0")){
        	handler.radioArr[0].start();
        }else if(inputLine.equals("start 271F2BF0")){
        	handler.radioArr[1].start();
        }else if(inputLine.equals("start 6869EE7E")){
        	handler.radioArr[1].start();
        }
        
        else if(inputLine.equals("stop 2724C2A0")){
        	handler.mp3Arr[0].stop();
        }else if(inputLine.equals("stop 271E3B80")){
        	handler.mp3Arr[0].stop();
        }else if(inputLine.equals("stop 2714DFB0")){
        	handler.mp3Arr[1].stop();
        }else if(inputLine.equals("stop 271C9DD0")){
        	handler.mp3Arr[1].stop();
        }else if(inputLine.equals("stop 271FC560")){
            handler.radioArr[0].interrupt();
            try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            handler.notf();
        }else if(inputLine.equals("stop 271582A0")){
            handler.radioArr[0].interrupt();
            try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            handler.notf();
        }else if(inputLine.equals("stop 271F2BF0")){
            handler.radioArr[1].interrupt();
            try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            handler.notf();
        }else if(inputLine.equals("stop 6869EE7E")){
            handler.radioArr[1].interrupt();
            try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            handler.notf();
        }
    }
}