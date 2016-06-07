import java.util.HashMap;
import java.util.Map;

/*
 * Class for all Background activities. This includes setting up mp3Player and Radio so we are ready to play at once.
 * First we read the config file and process it, then run priority() method to set up. After priority is completed we notify to tell that we now are ready for use
 * After this is completed we start checking for updates to our podcasts, and downloads a new version if it exsists. 
 * All this is done in a separate thread, so we do not stop the usage of other functions. 
 */

public class BackgroundActivities implements Runnable{
	Handler handler;
	public BackgroundActivities(Handler handler){
		this.handler = handler;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * 
	 * We implement Runnable and need a run() method for this tread. It reads the config and runds priority before it runs .notify().
	 * After notify we call updater and .updateEverything() to check for latest versions of the podcasts
	 */
	public void run(){
		handler.reader = new Reader(handler.filePod, handler.fileRad);
		
        handler.reader.readPodcasts();
        handler.reader.readRadio();
		
		handler.podcasts = handler.reader.getPodcasts();
		handler.radios = handler.reader.getRadios();
		
		priority();
				
		synchronized(Handler.startupLOCK){
			Handler.startupLOCK.notify();
		}
		Updater updater = new Updater(handler.podcasts, handler.currentVersion);
        updater.updateEverything();
		        
	}       
	/*
	 * all the stuff that is important to get up and running at once. Can not run before the config is done and global variables handler.podcast (and so on) are set.
	 */
	private void priority(){
		int i = 0;
		handler.mp3Arr = new Mp3Player[handler.max];
		handler.radioArr = new Thread[handler.max];
		try{
			//iterates the results of the reader for podcasts.
			HashMap<String, Station> stationList = handler.podcasts.getStations();
			for(Map.Entry<String,Station> stationEntry : stationList.entrySet()){
				HashMap<String, String> programList = stationEntry.getValue().getPrograms();
				for(Map.Entry<String, String> programEntry : programList.entrySet()){
					handler.mp3Arr[i] = new Mp3Player("src/file/"+programEntry.getKey()+".mp3");
					i++;
				}
			}
			//adds "spansk" as this is not a podcast
			handler.mp3Arr[i] = new Mp3Player("src/file/spansk.mp3");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		try{
			//iterates the results of the reader for radioes.
			i = 0;
			HashMap<String, String> stationList = handler.radios.getStations();
			for(Map.Entry<String,String> stationEntry : stationList.entrySet()){
				LiveRadio tmp = new LiveRadio(stationEntry.getValue());
				handler.radioArr[i] = new Thread(tmp);
				i++;
			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		System.out.println("Priority setup completed");
	}
}
