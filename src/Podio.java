class Podio{
    private static Handler handler;
    private static String url = "http://podkast.nrk.no/fil/hallo_p3/hallo_p3_2016-4-4_156_1002.MP3?stat=1";
    
    public static void main(String args[]){
        handler = new Handler(url);
    }
}