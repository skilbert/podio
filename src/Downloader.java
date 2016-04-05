import java.net.*;
import java.io.*;

class Downloader{
    public Downloader(){
        System.out.println("Downloader created");
    }
    public boolean downloadPodcast(String url){
        try{
            URLConnection conn = new URL(url).openConnection();
            InputStream is = conn.getInputStream();

            OutputStream outstream = new FileOutputStream(new File("file/file.mp3"));
            byte[] buffer = new byte[4096];
            int len;
            System.out.println("Downloading...");
            while ((len = is.read(buffer)) > 0) {
                outstream.write(buffer, 0, len);
            }
            outstream.close();
            return true;
        }
        catch(MalformedURLException e){
            System.out.println("Feil URL");
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    
}