#include <SoftwareSerial.h>

//Arduino code for podio.
//koden fungerer med RFID eval fra sparkfun
//kommuniserer med Raspberry pi over serial

SoftwareSerial rfid(7, 8);
SoftwareSerial xbee(10, 9);

boolean newTag = true;
char chArray[] = "";
String name = ""; 
String RFIDserial(chArray);
String last = "";
void check_for_notag(void);
void halt(void);
void parse(void);
void print_serial(void);
void read_serial(void);
void seek(void);
void set_flag(void);

// globale variable
int flag = 0;
int Str1[11];

//setup setter opp serial på riktig data rate. Både usb serial og kommunikasjon med RFID shield.
//Vi bruker delay(10) enkelte steder fordi vi opplever at Arduino fungerer bedre med dette ved å legge inn en kort delay.
void setup() {
  Serial.begin(9600);
  Serial.println("Start");

  // data rate... for seriell port
  xbee.begin(9600);
  rfid.begin(19200);
  delay(10);
  halt();
}

//loop. Programmet kjøres her i fra etter setup er ferdig. Kaller bare read_serial()
void loop() {
  read_serial();
}
//metoden brukes ikke i den siste utgaven av prototypen, men ble brukt under testing og er defor med
void check_for_notag() {
  seek();
  delay(10);
  parse();
  set_flag();
}

//Halt tag før vi begynner loop og leser fra den
void halt() {
  rfid.write((uint8_t)255);
  rfid.write((uint8_t)0);
  rfid.write((uint8_t)1);
  rfid.write((uint8_t)147);
  rfid.write((uint8_t)148);
}

//Parser rfid shield etter informasjon og leser det som ligger på den
void parse() {
  while(rfid.available()){ //leser mens noe er tilgjengelig
    if(rfid.read() == 255){
      for(int i=1;i<11;i++){
        Str1[i]= rfid.read(); //leser rfid inn i Str1
      }
    }
  }
}

//Printer resultat fra lesingen. Print = serial print. Som betyr at det sendes til Raspberry pien
void print_serial(){
    RFIDserial = ""; // Resetter string for nye rfider kan bli brukt
    for(int i = 0; i < 4; i++){  
      int p = 8 - i;
      char b[16];  // setter opp buffer for nok plass
      sprintf(b, "%X", Str1[p]); //konverterer hex til string
      RFIDserial += b;  // legger neste brikke inn
    }
    //Serial.println(RFIDserial);
    if(RFIDserial != last){ //vi ønsker bare å printe hvis det er forandringer
      delay(10);
      if(RFIDserial == "FFFFFFFFFFFFD0"){//hvis det er tilfellet er noe fjernet fra leseren
        Serial.println("stop " + name); // vi sender da at vi skal stoppe denne stasjonen eller podcasten
      }else if(RFIDserial != "60FFD0"){//default verdi vi måtte fjerne fordi det betyr at det ikke ligger noe på den
         name = RFIDserial;
         Serial.println("start "+RFIDserial); // hvis noe er lagt på betyr det at vi skal starte denne stasjonen eller podcasten
      }
      last = RFIDserial; //oppdaterer last
    }
}

//denne metoden kjører programmet og kalles av loop(). Holder orden på rekkefølgen. Bruker enkelte delays fordi vi opplever at den fungerte bedre med disse.
void read_serial() {
  seek();
  delay(10);
  parse();
  set_flag();
  print_serial();
  delay(100);
 
}

//setter RFID leseren til å søke
void seek() {
  rfid.write((uint8_t)255);
  rfid.write((uint8_t)0);
  rfid.write((uint8_t)1);
  rfid.write((uint8_t)130);
  rfid.write((uint8_t)131);
  delay(10);
}
//vi sjekker resultatet etter parsingen og flagger hvis leseren har noe å lese.
//flagget brukes ikke i siste versjon av programvaren, men har blirt brukt under testing og er derfor med fortsatt
void set_flag(){
  if(Str1[2] == 6){
    flag++;
  }
  if(Str1[2] == 2){           // prosesserer eller error
    if (newTag == false){ // sjekker om rfid ligger på shield
      flag = 0;
      newTag = true;            // Flipper så ikke output skriver det samme
    }
  }
}
