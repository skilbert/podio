import java.io.*;

/*
 * Class for reading the config files and creating Radios and Podcasts objects from it. 
 * Reads both files, but with seperate methods. 
 */
class Reader{
    private String filePod;
    private String fileRad;
    private Podcasts podcasts;
    private Radios radios;
    /*
     * Constructor gets the file location and name
     */
    public Reader(String filePod, String fileRad){
        System.out.println("Reader created");
        this.filePod = filePod;
        this.fileRad = fileRad;
    }
    /*
     * Reads the podcast config file. Reads until the file is done. Easy tests for chars and action after each is found. 
     */
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
    /*
     * Reads the radio config file. Reads until the file is done. Easy tests for chars and action after each is found. 
     */
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
    /*
     * Returns a object of Podcasts. Can not run before readPodcasts();
     */
    public Podcasts getPodcasts(){
        return podcasts;
    }
    /*
    *Returns a object of radios. Can not run before readRadios();
    */
    public Radios getRadios(){
    	return radios;
    }
}