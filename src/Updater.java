import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/*
 * Class for updating the podcasts. We generate a versions.txt each time this is run, and we check it for changes. 
 * To compare we have the orginal and create a tempVersions.txt which is later renamed and to versions.txt
 */
public class Updater {
	private Podcasts podcasts;
	private String tmpFilepath;
	private String filepath;
	private String[][] names;
	PrintWriter write;
	
	/*
	 * Constructor gets a object of podcasts and the filepath for versions.txt
	 * Then generates a new printWriter
	 */
	public Updater(Podcasts podcasts, String filepath){
		this.podcasts = podcasts;
		this.tmpFilepath = filepath+"tmpVersion.txt";
		this.filepath = filepath+"version.txt";
			
		try{
			write = new PrintWriter(new FileWriter(tmpFilepath, false));
		}catch(Exception e){
			System.out.println("Can not find path.... "+ e.getMessage());
		}
		names = new String[10][10];
	}
	/*
	 * updates the podcasts if needed and cleans after it is done
	 */
	public void updateEverything(){	
		checkVersion();
		clean();
	}
	/*
	 * We start by checking the versions. It starts by reading the document and storing what we need. 
	 * We then check this information with the information online. We use the XMLparser for this. This parses each URL from the Podcasts object and checks for changes against the document. 
	 */
	private void checkVersion(){
		//first we read the document with the current versions of podcasts
		String[][] versions = new String[10][10]; // supports 10 channels with 10 programs for each. Easy to change
		try(BufferedReader br = new BufferedReader(new FileReader(filepath))){
			String line;
			int i = -1;
			int j = -1;
			while((line = br.readLine()) != null ){
				if(line.startsWith("#")){
					i++;
				}else if(line.startsWith("*")){
					j++;
				}else if(line.startsWith("time: ")){
					versions[i][j] = line.replace("time: ","");
				}
			}
			br.close();
		}catch(IOException e){
			System.out.println("Something is wrong with the version file, or it is not present");
			System.out.println(e.getMessage());
		}
		//Then read the online information
		int i = -1;
		int j = -1;
		HashMap<String, Station> stationList = podcasts.getStations();
		for(Map.Entry<String,Station> stationEntry : stationList.entrySet()){
			i++;
			HashMap<String, String> programList = stationEntry.getValue().getPrograms();
			write.println("#"+stationEntry.getKey()+"\n");
			
			for(Map.Entry<String, String> programEntry : programList.entrySet()){
				j++;
				XMLParser xmlParser = new XMLParser(programEntry.getValue(), 1);
				String pubDate = xmlParser.getPubDate()[0];
				write.println("*"+programEntry.getKey());
				if(versions[i][j] != null){
					if(pubDate.equals(versions[i][j])){
						System.out.println("we have the latest version of "+xmlParser.getTitle()[0]);
						
					}else{
						System.out.println("Downloading the latest version of "+xmlParser.getTitle()[0]);
						download(xmlParser, programEntry.getKey());
					}
					write.println("time: "+pubDate+"\n");
					names[i][j] = programEntry.getKey();
				}
			}
		}
	}
	/*
	 * Downloads the file if we do not have the newest. 
	 * Adds TMP to the title because we want to download while we listen to the last version
	 */
	private void download(XMLParser xmlParser, String name){
		//this could be done in seperate threads, but I don't see why we should
		String tmpName = "TMP"+name;
		new Downloader(xmlParser.getMp3()[0], tmpName).start();
	}
	/*
	 * This cleans after we are done. It swaps the TMPfile with the one we want so we have the newest version. It also does this for the versions.txt
	 */
	private void clean(){
		write.close();
		try{
			File oldfile = new File(filepath);
			oldfile.delete();
			File newName = new File(filepath);
			File file = new File(tmpFilepath);
			file.renameTo(newName);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		try{
			for(int i = 0; i < names.length; i++){
				for(int j = 0; j < names[0].length; j++){
					if(names[i][j] != null){
						File file = new File("src/file/TMP"+names[i][j]+".mp3");
						if(file.exists()){						
							File oldfile = new File("src/file/"+names[i][j]+".mp3");
							oldfile.delete();
							File newName = new File("src/file/"+names[i][j]+".mp3");
							file.renameTo(newName);	
						}
					}else{
						break;
					}
				}
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println("cleaning is done");
	}
}
