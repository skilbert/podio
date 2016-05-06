import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class LiveRadio implements Runnable{
	private SourceDataLine line;
	private AudioInputStream din = null;
	AudioInputStream in;
	byte[] data = null;
	public LiveRadio(String url){
		try{
			in = AudioSystem.getAudioInputStream(new URL(url));
		}catch(Exception e){
			System.out.println("Something went wrong when creating radiostream: "+e.getMessage());
		}
	}
	public void run(){
		System.out.println("Radio is playing");
		start();
	}
	public void start(){
	    try {
	        AudioFormat baseFormat = in.getFormat();
	        AudioFormat decodedFormat = new AudioFormat(
	                AudioFormat.Encoding.PCM_SIGNED,
	                baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
	                baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
	                false);
	        din = AudioSystem.getAudioInputStream(decodedFormat, in);
	        DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
	        line = (SourceDataLine) AudioSystem.getLine(info);
	        if(line != null) {
	            line.open(decodedFormat);
	            data = new byte[4096];
	        }
	        line.start();
	        int nBytesRead;
	        while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
	        	if(Thread.currentThread().isInterrupted()){
	        		throw new InterruptedException();
	        	}else{
	        		line.write(data, 0, nBytesRead);
	        	}
	        }
	    }catch(LineUnavailableException e){
	    	e.printStackTrace();
	    }
	    catch(IOException e){
	    	e.printStackTrace();
	    }
	    catch(InterruptedException e) {
	    	System.out.println("Radio was stopped");
	    	return;
	    }
	    finally {
	        if(din != null) {
	            try { din.close(); } catch(IOException e) { }
	        }
	    }
	}
	public void stop(){
		try{
	        line.drain();
	        line.stop();
	        line.close();
	        din.close();
		}catch(Exception e){
			System.out.println("could not close radio: "+ e.getMessage());
		}
	}
}
