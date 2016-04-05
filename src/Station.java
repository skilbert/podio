import java.util.*;

class Station{
    private HashMap<String, String> programs;
    private String name;
    public Station(String name){
        this.name = name;
        programs = new HashMap<String, String>();
    }
    public void addProgram(String name, String url){
        programs.put(name, url);
    }
    public HashMap<String, String> getPrograms(){
        return programs;
    }
}