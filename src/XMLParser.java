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

    public XMLParser(String url){
        try{
            this.url = new URL(url);
        }catch(MalformedURLException e){
            System.out.println("Something is wrong with the URL");
        }
        buildParser();
        parse();
        System.out.println("XMLParser created");
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
    private void parse(){ 
        NodeList list = doc.getElementsByTagName("item");
        title = new String[list.getLength()];
        description = new String[list.getLength()];
        mp3 = new String[list.getLength()];

        for(int i = 0; i < list.getLength(); i++){
            Node node = list.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                title[i] = element.getElementsByTagName("title").item(0).getTextContent();
                description[i] = element.getElementsByTagName("description").item(0).getTextContent();
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

}