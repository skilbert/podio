
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.media.Format;
import javax.media.Manager;
import javax.media.Player;
import javax.media.PlugInManager;
import javax.media.format.AudioFormat;
import sun.audio.*;

/*
 * Class for playing a mp3 file. JMF is for the mp3 decode.  
 */
public class Mp3Player {
	Player player;
	AudioStream audioStream;
	
	/*
	 * Constructor receives the location and generates the stream. 
	 */
	public Mp3Player(String location){
		
		Format input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
		Format input2 = new AudioFormat(AudioFormat.MPEG);
		Format output = new AudioFormat(AudioFormat.LINEAR);
		
		PlugInManager.addPlugIn(
			"com.sun.media.codec.audio.mp3.JavaDecoder",
			new Format[]{input1, input2},
			new Format[]{output},
			PlugInManager.CODEC
		);
		
		try{
			// this solves #3 on github by using a audiostream, and not passing a URL. 
		    // open the sound file as a Java input stream
		    InputStream in = new FileInputStream(location);
		 
		    // create an audiostream from the inputstream
		    audioStream = new AudioStream(in);
		}
		catch(Exception e){
			System.out.println("Something is wrong with the mp3File: "+ e.getMessage());
			e.printStackTrace();
		}
	}
	/*
	 * starts the playing of the mp3. AudioPlayer handles this by receiving the stream we want to start
	 */
	public void start(){
	    AudioPlayer.player.start(audioStream);
	}
	/*
	 * stops (or pauses) the playing of the mp3. AudioPlayer handles this by receiving the stream we want to start
	 */
	public void stop(){
		AudioPlayer.player.stop(audioStream);
	}
	
}
