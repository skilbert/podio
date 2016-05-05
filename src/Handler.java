class Handler{
	public int max = 10;
	
    public Reader reader;
    public Podcasts podcasts;
    public Radios radios;
    
    public String filePod;
    public String fileRad;
    public String currentVersion;
    
    public Mp3Player[] mp3Arr;
    public LiveRadio[] radioArr;
    
    public static final Object LOCK = new Object();
    
    
    public Handler(){
        //constructor
        run();
    }
    private void run(){
        filePod = "config/podcasts.txt";
        fileRad = "config/stations.txt";
        currentVersion = "config/";
        
        Thread bgThread = new Thread(new BackgroundActivities(this));
        bgThread.start();
        
        synchronized(LOCK){
        	try{
        		System.out.println("waiting...");
        		LOCK.wait();
        	}catch(InterruptedException e){
        		e.printStackTrace();
        	}
        	System.out.println("Waiting done");
        }
        
        radioArr[0].start();
        System.out.println("hello");
        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        //PiCom piCom = new PiCom(this);
        //piCom.initialize();
        
        //Mp3Player mp3Player1 = new Mp3Player("src/file/20sporsmal.mp3");
        //mp3Player1.start();
        //LiveRadio liveRadio = new LiveRadio(radios.get("nrk1+"));
                
        
        //liveRadio.start();
        
    }
}