import java.util.*;

class Podcasts{
    HashMap<String, Station> stations;
    public Podcasts(){
        stations = new HashMap<String, Station>();
    }
    public void addStation(String name){
        stations.put(name, new Station(name));
        //System.out.println("Station added "+name);
    }
    public boolean addProgram(String name, String program, String url){
        //System.out.println("adding program to "+ name);
        if(stations.containsKey(name)){
            stations.get(name).addProgram(program, url);
            return true;
        }else{
            return false;
        }
    }
    public Station get(String name){
        return stations.get(name);
    }
    public HashMap<String, Station> getStations(){
        return stations;
    }
}