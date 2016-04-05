import java.net.*;
import java.io.*;

class DownloadPodcast{
    public static void main(String args[]){
        try{
            URLConnection conn = new URL("http://nl.nrk.no/podkast/aps/19540/hallo_p3_2016-3-31_1545_3696.MP3").openConnection();
            InputStream is = conn.getInputStream();

            OutputStream outstream = new FileOutputStream(new File("file.mp3"));
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) > 0) {
                outstream.write(buffer, 0, len);
            }
            outstream.close();
        }
        catch(MalformedURLException e){
            System.out.println("Feil URL");
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}