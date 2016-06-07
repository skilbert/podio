import java.util.*;

/*
 * Class for all podcasts. Hold all podcasts hashMaps
 */
class Podcasts{
    HashMap<String, Station> stations;
    /*
     * constructor generates the hashmap for all stations
     */
    public Podcasts(){
        stations = new HashMap<String, Station>();
    }
    /*
     * Adds a station to the list of stations. Needs the name of the new station
     */
    public void addStation(String name){
        stations.put(name, new Station(name));
        //System.out.println("Station added "+name);
    }
    /*
     * Adds a program to a station. This holds the URL to where we can download this podcast from. 
     */
    public boolean addProgram(String name, String program, String url){
        //System.out.println("adding program to "+ name);
        if(stations.containsKey(name)){
            stations.get(name).addProgram(program, url);
            return true;
        }else{
            return false;
        }
    }
    /*
     * get a station by name
     */
    public Station get(String name){
        return stations.get(name);
    }
    /*
     * returns the hashmap
     */
    public HashMap<String, Station> getStations(){
        return stations;
    }
}