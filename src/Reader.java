import java.io.*;
import java.util.*;

class Reader{
    private String filename;
    private Podcasts podcasts;
    public Reader(String filename){
        System.out.println("Reader created");
        this.filename = filename;
    }
    public void read(){
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String station = "";
            String program = "";
            podcasts = new Podcasts();
            while ((line = br.readLine()) != null) {
                if(line.startsWith("#")){
                    station = line.replace("#","");
                    podcasts.addStation(station);
                }else if(line.startsWith("*")){
                    program = line.replace("*","");
                }else if(line.startsWith("http")){
                    if(podcasts.addProgram(station, program, line) == false){
                       System.out.println("Something is wrong with the provided stations list"); 
                    }
                }      
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public Podcasts getPodcasts(){
        return podcasts;
    }
}