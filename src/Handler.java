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
        
        //Mp3Player mp3Player = new Mp3Player("src/file/file.mp3");
        //mp3Player.start();
        
        reader = new Reader(filePod, fileRad);
        reader.readRadio();
        Radios radios = reader.getRadios();
        
        LiveRadio liveRadio = new LiveRadio(radios.get("nrk1+"));
        
        liveRadio.start();
        //Podcasts podcasts = reader.getPodcasts();
        
        //Station station = podcasts.get("nrk1");
        //String urlen = station.get("dagsnytt");

        //XMLParser xmlParser = new XMLParser(urlen);
        //String[] mp3 = xmlParser.getMp3();


        //this.downloader = new Downloader();
        //downloader.downloadPodcast(mp3[0]);
    }
}