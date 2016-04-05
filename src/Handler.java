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
        //this.downloader = new Downloader();
        //downloader.downloadPodcast(url);   
    }
}