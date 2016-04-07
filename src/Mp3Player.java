import java.io.File;
import java.net.URL;

import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.PlugInManager;
import javax.media.format.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class Mp3Player {
	Player player;
	
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
			this.player = Manager.createPlayer(new MediaLocator(new File(location).toURI().toURL()));
		}
		catch(Exception e){
			System.out.println("Something is wrong with the mp3File: "+ e.getMessage());
			
		}
	}
	public void start(){
		player.start();
	}
	public void stop(){
		player.stop();
	}
	//we need more methods.. what time is it stoped at? how long is this file? etc.
	
}
