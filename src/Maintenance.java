import java.util.HashMap;
import java.util.Map;

public class Maintenance implements Runnable {
	Handler handler;
	public Maintenance(Handler handler){
		this.handler = handler;
	}
	
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
	
	private void resetRadio(){
		try{
			int i = 0;
			HashMap<String, String> stationList = handler.radios.getStations();
			for(Map.Entry<String,String> stationEntry : stationList.entrySet()){
				if(handler.radioArr[i] == null){
					LiveRadio tmp = new LiveRadio(stationEntry.getValue());
					handler.radioArr[i] = new Thread(tmp);
					System.out.println("Radiostation is reset");
				}
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
