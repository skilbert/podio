import java.util.HashMap;

public class Radios {
	HashMap<String, String> stations;
	public Radios(){
		stations = new HashMap<String, String>();
	}
	public void addStation(String name, String url){
		stations.put(name, url);
	}
	public String get(String name){
		return stations.get(name);
	}
	public HashMap<String, String> getStations(){
		return stations;
	}
}