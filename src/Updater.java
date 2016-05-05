import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Updater {
	private Podcasts podcasts;
	private String tmpFilepath;
	private String filepath;
	private String[][] names;
	PrintWriter write;
	
	
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
	
	public void updateEverything(){	
		checkVersion();
		clean();
	}
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
	private void download(XMLParser xmlParser, String name){
		//this could be done in seperate threads, but I don't see why we should
		String tmpName = "TMP"+name;
		new Downloader(xmlParser.getMp3()[0], tmpName).start();
	}
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
							//check if it is in use right now
							//It looks like this is not needed
							//It just continiues to play, and next time it is started it
							//plays the new file we downloaded and renamed. Guess it is buffering it all at once
							
							
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
