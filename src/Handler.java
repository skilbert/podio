import java.net.*;
import java.io.*;

class Handler{
    private Downloader downloader;

    public Handler(String url){
        run(url);
    }
    private void run(String url){
        this.downloader = new Downloader();
        downloader.downloadPodcast(url);   
    }
}