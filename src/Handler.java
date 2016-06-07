/*
 * This class does the main control of the program. 
 * Starts threads, holds the public arrays 
 */

class Handler{
	//current max size for mp3Arr and radioArr, but no problem to handle more. 
	public int max = 10;
	
    public Reader reader;
    public Podcasts podcasts;
    public Radios radios;
    
    public String filePod;
    public String fileRad;
    public String currentVersion;
    
    public Mp3Player[] mp3Arr;
    public Thread[] radioArr;
    
    public static final Object startupLOCK = new Object();
    public static final Object runntimeLOCK = new Object();
    
    
    public Handler(){
        //constructor
        run();
    }
    /*
     * creates the path to the config files. Creates the background thread and maintenance thread.
     * Then starts the background thread for setup. Initiates the communication to the Arduino. And starts it
     * After this is done we enter a synchronized part on startupLOCK where we wait until priority setup is completed to start the maintenance thread. 
     */
    private void run(){
        filePod = "config/podcasts.txt";
        fileRad = "config/stations.txt";
        currentVersion = "config/";
        
        Thread bgThread = new Thread(new BackgroundActivities(this));
        Thread mThread = new Thread(new Maintenance(this));
        bgThread.start();
        
        PiCom piCom = new PiCom(this);
        piCom.initialize();
        
        synchronized(startupLOCK){
        	try{
        		System.out.println("Main thread is waiting...");
        		startupLOCK.wait();
        	    mThread.start();
        		System.out.println("Main thread is running again");
        	}catch(InterruptedException e){
        		e.printStackTrace();
        	}
        }

    }
    /*
     * this handles a runtimeLOCK. It is called by piCom and used to put maintenance thread to sleep when we do not need it. 
     * When notified it wakes up and does it job. 
     */
    public void notf(){
        synchronized(runntimeLOCK){
        	runntimeLOCK.notify();
        }
    }
}