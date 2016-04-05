import java.net.*;
import java.io.*;

class DownloadPodcast{
    public static void main(String args[]){
        try{
            URLConnection conn = new URL("http://podkast.nrk.no/fil/hallo_p3/hallo_p3_2016-3-31_1545_3696.MP3?stat=1").openConnection();
            InputStream is = conn.getInputStream();

            OutputStream outstream = new FileOutputStream(new File("file/file.mp3"));
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