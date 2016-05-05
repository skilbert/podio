import java.net.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

class XMLParser{
    private URL url;
    Document doc;
    String[] title;
    String[] description;
    String[] mp3;
    String[] pubDate;
    
    public XMLParser(String url, int length){
        try{
            this.url = new URL(url);
        }catch(MalformedURLException e){
            System.out.println("Something is wrong with the URL");
        }
        buildParser();
        parse(length);
    }
    private void buildParser(){
        try{
            InputStream stream = url.openStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(stream);
            doc.getDocumentElement().normalize();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void parse(int length){ 
        NodeList list = doc.getElementsByTagName("item");
        title = new String[list.getLength()];
        description = new String[list.getLength()];
        mp3 = new String[list.getLength()];
        pubDate = new String[list.getLength()];
        
        int parseLength = 0;
        if(length < 1){
        	parseLength = list.getLength();
        }else{
        	parseLength = length;
        }
        for(int i = 0; i < parseLength; i++){
            Node node = list.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                title[i] = element.getElementsByTagName("title").item(0).getTextContent();
                description[i] = element.getElementsByTagName("description").item(0).getTextContent();
                pubDate[i] = element.getElementsByTagName("pubDate").item(0).getTextContent();
                Element mp3Element = (Element) element.getElementsByTagName("enclosure").item(0);
                mp3[i] = mp3Element.getAttribute("url");
            }
        }
    }
    public String[] getTitle(){
        return title;
    }
    public String[] getDescription(){
        return description;
    }
    public String[] getMp3(){
        return mp3;
    }    
    public String[] getPubDate(){
        return pubDate;
    }

}