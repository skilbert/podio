import java.net.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

/*
 * Class for reading XML document found on the link to the podcasts. 
 * It reads the document and stores the info in Arrays. Information in all arrays with the same position belong together. 
 * First we build the parser, and then parse the document. 
 */
class XMLParser{
    private URL url;
    Document doc;
    String[] title;
    String[] description;
    String[] mp3;
    String[] pubDate;
    
    /*
     * Constructor takes the url and length calles the build and parse methods. 
     */
    public XMLParser(String url, int length){
        try{
            this.url = new URL(url);
        }catch(MalformedURLException e){
            System.out.println("Something is wrong with the URL");
        }
        buildParser();
        parse(length);
    }
    /*
     * buildParser() opens the url, creates a DocumentBuilderFactory, from this factory we create a builder and from the builder a document. 
     */
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
    /*
     * We then parse the document. First we create all elements for storage and loop it until we got them all. 
     */
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
    /*
     * Returns array of titles
     */
    public String[] getTitle(){
        return title;
    }
    /*
     * Returns array of description
     */
    public String[] getDescription(){
        return description;
    }
    /*
     * Returns array of Mp3's
     */
    public String[] getMp3(){
        return mp3;
    }
    /*
     * Returns array of pubdate
     */
    public String[] getPubDate(){
        return pubDate;
    }

}