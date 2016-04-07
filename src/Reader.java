import java.io.*;

class Reader{
    private String filePod;
    private String fileRad;
    private Podcasts podcasts;
    private Radios radios;
    public Reader(String filePod, String fileRad){
        System.out.println("Reader created");
        this.filePod = filePod;
        this.fileRad = fileRad;
    }
    public void readPodcasts(){
        try (BufferedReader br = new BufferedReader(new FileReader(filePod))) {
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
    public void readRadio(){
        try (BufferedReader br = new BufferedReader(new FileReader(fileRad))) {
            String line;
            String station = "";
            radios = new Radios();
            while ((line = br.readLine()) != null) {
                if(line.startsWith("#")){
                    station = line.replace("#","");
                }else if(line.startsWith("http")){
                	radios.addStation(station, line);
                }
            }
        }
        catch(IOException e){
            System.out.println("Could not read config file: " +e.getMessage());
        }
    	
    }
    public Podcasts getPodcasts(){
        return podcasts;
    }
    public Radios getRadios(){
    	return radios;
    }
}