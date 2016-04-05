import java.net.*;
import java.io.*;

class Handler{
    private Downloader downloader;
    private Reader reader;

    public Handler(String url){
        run(url);
    }
    private void run(String url){
        String filename = "stations.txt";
        reader = new Reader(filename);
        reader.read();
        Podcasts podcasts = reader.getPodcasts();
        
        Station station = podcasts.get("nrk1");
        String urlen = station.get("dagsnytt");

        XMLParser xmlParser = new XMLParser(urlen);
        String[] mp3 = xmlParser.getMp3();


        this.downloader = new Downloader();
        downloader.downloadPodcast(mp3[0]);   
    }
}