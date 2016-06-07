import java.util.*;
/*
 * Class for a podcast station. Has a hashmap of programs of this station. 
 */
class Station{
    private HashMap<String, String> programs;
    private String name;
    /*
     * Constructor takes name and generates hashmap
     */
    public Station(String name){
        this.name = name;
        programs = new HashMap<String, String>();
    }
    /*
     * Adds a program to the hashmap with name and the URL to where we can download it from
     */
    public void addProgram(String name, String url){
        programs.put(name, url);
    }
    /*
     * get a program
     */
    public String get(String name){
        return programs.get(name);
    }
    /*
     * returns all programs for this station
     */
    public HashMap<String, String> getPrograms(){
        return programs;
    }
}