import java.net.*;
import java.io.*;
/*
 * Class for downloading Mp3 from a URL
 */
class Downloader{
	private String url;
	private String filename;
	
	/*
	 * Constructor. Pass URL and filename to it so we are ready to start
	 */
    public Downloader(String url, String filename){
    	this.url = url;
    	this.filename = filename;
    }
    /*
     * Starts the download. Opens the connection, creates output file (to preset destination)
     * Then creates buffer (4gb) and downloads it. File is then closed. 
     */
    public void start(){
    	System.out.println("Downloading "+filename);
    	try{
            URLConnection conn = new URL(url).openConnection();
            InputStream is = conn.getInputStream();

            OutputStream outstream = new FileOutputStream(new File("src/file/"+filename+".mp3"));
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
    	System.out.println("Done with "+filename);
    }
}