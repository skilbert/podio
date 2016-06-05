#include <SoftwareSerial.h>

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

//INIT
void setup() {
  Serial.begin(9600);
  Serial.println("Start");

  // data rate... for seriell port
  xbee.begin(9600);
  rfid.begin(19200);
  delay(10);
  halt();
}

//MAIN
void loop() {
  read_serial();
}

void check_for_notag() {
  seek();
  delay(10);
  parse();
  set_flag();
  
  
}

void halt() {
 //Halt tag
  rfid.write((uint8_t)255);
  rfid.write((uint8_t)0);
  rfid.write((uint8_t)1);
  rfid.write((uint8_t)147);
  rfid.write((uint8_t)148);
}

void parse() {
  while(rfid.available()){
    Serial.println("hello");
    if(rfid.read() == 255){
      for(int i=1;i<11;i++){
        Str1[i]= rfid.read();
      }
    }
  }
}

void print_serial()
{
    
    RFIDserial = ""; // Resetter string for nye rfider kan bli brukt
    for(int i = 0; i < 4; i++){  
      int p = 8 - i;
      char b[16];  // setter opp buffer for nok plass
      sprintf(b, "%X", Str1[p]); //konverterer hex til string
      RFIDserial += b;  // legger neste brikke inn
    }
    Serial.println(RFIDserial);
    if(RFIDserial != last){
      delay(10);
      if(RFIDserial == "FFFFFFFFFFFFD0"){
        Serial.println("stop " + name);
      }else if(RFIDserial != "60FFD0"){
         name = RFIDserial;
         Serial.println("start "+RFIDserial);
      }
      last = RFIDserial;
    }
 // skriver ut til serial.monitor   
}

void read_serial() {
  seek();
  delay(10);
  parse();
  set_flag();
  print_serial();
  delay(100);
 
}

void seek() {
  //skal finne rfid-tag
  rfid.write((uint8_t)255);
  rfid.write((uint8_t)0);
  rfid.write((uint8_t)1);
  rfid.write((uint8_t)130);
  rfid.write((uint8_t)131);
  delay(10);
}

void set_flag()
{
  if(Str1[2] == 6){         // returnerer tag
    flag++;
  }
  if(Str1[2] == 2){           // prosesserer eller error
 
    
    if (newTag == false){ // sjekker om rfid ligger på shield
      flag = 0;
      Serial.print("The RFID ");
      Serial.print(RFIDserial);
      Serial.println(" has left the station!");
      newTag = true;            // Flipper så ikke output skriver det samme
    }
  }
}
