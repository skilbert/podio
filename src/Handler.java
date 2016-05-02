class Handler{
    private Downloader downloader;
    private Reader reader;

    public Handler(){
        //constructor
        run();
    }
    private void run(){
        String filePod = "config/podcasts.txt";
        String fileRad = "config/stations.txt";
        
        //reader = new Reader(filePod, fileRad);
        
        //reader.readPodcasts();
        //Podcasts podcasts = reader.getPodcasts();
        
        //Station station = podcasts.get("nrk1");
        //String urlen = station.get("20sporsmal");
        
        //XMLParser xmlParser = new XMLParser(urlen);
        //String[] mp3 = xmlParser.getMp3();
        
        //this.downloader = new Downloader();
        //downloader.downloadPodcast(mp3[0]);
        
        
  
        //reader.readRadio();
        
        //Radios radios = reader.getRadios();
        
        //LiveRadio liveRadio = new LiveRadio(radios.get("nrk1+"));
        
        PiCom piCom = new PiCom();
        piCom.initialize();

        //mp3Player.start();
        
        
        
        //liveRadio.start();
        
    }
}