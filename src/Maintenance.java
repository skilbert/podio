import java.util.HashMap;
import java.util.Map;

/*
 * Class for maintaining the radiostations. To close a live playback from the internett the thread is intrerupted an killed.
 * This class handles setting them back up
 */
public class Maintenance implements Runnable {
	Handler handler;
	public Maintenance(Handler handler){
		this.handler = handler;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * 
	 * Because it is a thread we need run(). We enter a sync on runntimeLOCK and sleep until we receive a notification.
	 * After notification we call resetRadio();
	 */
	public void run(){
	    synchronized(Handler.runntimeLOCK){
	    	while(true){
	    		try {
		    		System.out.println("Maintenance is going to sleep");
					Handler.runntimeLOCK.wait();
		    		resetRadio();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	    }
	}
	/*
	 * resetRadio() sets up the radioArr again for reasons explaned above. 
	 */
	private void resetRadio(){
		try{
			int i = 0;
			HashMap<String, String> stationList = handler.radios.getStations();
			for(Map.Entry<String,String> stationEntry : stationList.entrySet()){
				if(handler.radioArr[i] != null){
					LiveRadio tmp = new LiveRadio(stationEntry.getValue());
					handler.radioArr[i] = new Thread(tmp);
					i++;
				}
			}
			System.out.println("Radiostation is reset");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
