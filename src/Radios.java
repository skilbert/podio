import java.util.HashMap;

/*
 * Class for all radio stations. Holds them in a hashmap
 */
public class Radios {
	HashMap<String, String> stations;
	/*
	 * Constructor generates the hashmap
	 */
	public Radios(){
		stations = new HashMap<String, String>();
	}
	/*
	 * Adds a station to the hashmap. URL is where we can stream this from
	 */
	public void addStation(String name, String url){
		stations.put(name, url);
	}
	/*
	 * Get a station
	 */
	public String get(String name){
		return stations.get(name);
	}
	/*
	 * returns the hashmap
	 */
	public HashMap<String, String> getStations(){
		return stations;
	}
}