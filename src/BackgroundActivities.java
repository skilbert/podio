import java.util.HashMap;
import java.util.Map;

public class BackgroundActivities implements Runnable{
	Handler handler;
	public BackgroundActivities(Handler handler){
		this.handler = handler;
	}
	public void run(){
			handler.reader = new Reader(handler.filePod, handler.fileRad);
			
	        handler.reader.readPodcasts();
	        handler.reader.readRadio();
			
			handler.podcasts = handler.reader.getPodcasts();
			handler.radios = handler.reader.getRadios();
			
			priority();
			synchronized(Handler.LOCK){
				Handler.LOCK.notify();
			}
	        Updater updater = new Updater(handler.podcasts, handler.currentVersion);
	        updater.updateEverything();
	}
	/*
	 * all the stuff that is important to get up and running at once
	 */
	public void priority(){
		int i = 0;
		handler.mp3Arr = new Mp3Player[handler.max];
		handler.radioArr = new LiveRadio[handler.max];
		try{
			HashMap<String, Station> stationList = handler.podcasts.getStations();
			for(Map.Entry<String,Station> stationEntry : stationList.entrySet()){
				HashMap<String, String> programList = stationEntry.getValue().getPrograms();
				for(Map.Entry<String, String> programEntry : programList.entrySet()){
					handler.mp3Arr[i] = new Mp3Player("src/file/"+programEntry.getKey()+".mp3");
					i++;
				}
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		try{
			i = 0;
			HashMap<String, String> stationList = handler.radios.getStations();
			for(Map.Entry<String,String> stationEntry : stationList.entrySet()){
				handler.radioArr[i] = new LiveRadio(stationEntry.getValue());
			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		System.out.println("Priority setup completed");
	}
}
