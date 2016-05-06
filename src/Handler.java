class Handler{
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
    private void run(){
        filePod = "config/podcasts.txt";
        fileRad = "config/stations.txt";
        currentVersion = "config/";
        
        Thread bgThread = new Thread(new BackgroundActivities(this));
        Thread mThread = new Thread(new Maintenance(this));
        bgThread.start();
        
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
        
        radioArr[0].start();
        /*
        try {
            Thread.sleep(5000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        radioArr[0].interrupt();
        radioArr[0] = null;
        synchronized(runntimeLOCK){
        	runntimeLOCK.notify();
        }
        
        try {
            Thread.sleep(10000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        
        radioArr[0].start();
        
        */
        //PiCom piCom = new PiCom(this);
        //piCom.initialize();

        
    }
}