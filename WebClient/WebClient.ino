
#include <SPI.h>
#include <Ethernet2.h>

// Enter a MAC address for your controller below.
// Newer Ethernet shields have a MAC address printed on a sticker on the shield
byte mac[] = {0x90, 0xA2, 0xDA, 0x10, 0x2D, 0xD6 };
// if you don't want to use DNS (and reduce your sketch size)
// use the numeric IP instead of the name for the server:
//IPAddress server(74,125,232,128);  // numeric IP for Google (no DNS)
//char server[] = "216.58.214.14";//"www.google.com";    // name address for Google (using DNS)
//char server[] = "23.235.33.194";//"http://warframe.wikia.com";
char server[] = "192.168.0.9";
int portPC = 80;
int portLH = 8080;
//char server[] = "127.0.0.1";
// Set the static IP address to use if the DHCP fails to assign
IPAddress ip(192, 168, 0, 177);

// Initialize the Ethernet client library
// with the IP address and port of the server
// that you want to connect to (port 80 is default for HTTP):
EthernetClient client;

boolean startReadingSignKey;
boolean startReadingSecretKey;
boolean startReadingPublicKey;
unsigned char inString[32];
int stringPos=0;

void setup() {
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  
//  while (!Serial) {
//    ; // wait for serial port to connect. Needed for Leonardo only
//  }

  // start the Ethernet connection:
  if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure Ethernet using DHCP");
    // no point in carrying on, so do nothing forevermore:
    // try to congifure using IP address instead of DHCP:
    //Ethernet.begin(mac, ip);
  }
  // give the Ethernet shield a second to initialize:
    Serial.print("IP Address        : ");
  Serial.println(Ethernet.localIP());
  Serial.print("Subnet Mask       : ");
  Serial.println(Ethernet.subnetMask());
  Serial.print("Default Gateway IP: ");
  Serial.println(Ethernet.gatewayIP());
  Serial.print("DNS Server IP     : ");
  Serial.println(Ethernet.dnsServerIP());
  
  delay(1000);
  Serial.println("connecting...");

  // if you get a connection, report back via serial:
  
  int cont = client.connect(server, portLH); //asdlkjhsadkijsandksja hdaksjdhlakjsDHBD Lskidufjdh ;,\zxkfkjdh l\ksxzj.df hbl\xkdjfgh n.dm,zjfjgp;ksjdhbhgf;iusdf
  //its vvvvvvvvvv dodgy
  Serial.println(cont);
  if ( cont) {
    Serial.println("connected");
    // Make a HTTP request:
    client.println("GET /DueIoTPlatform/SecretKey HTTP/1.1");
    //client.println("GET /?gws_rd=ssl#q=helo HTTP/1.1");
    //client.println("GET /wiki/Clan HTTP/1.1");
    client.println("Host: 192.168.0.9");
    client.println("Connection: close");
    client.println();
  }
  else {
    // kf you didn't get a connection to the server:
    Serial.println("connection failed");
  }
}

void loop()
{
  int i;
  // if there are incoming bytes available
  // from the server, read them and print them:
  if (client.available()) {
    char c = client.read();
  //  Serial.print(c);
  
  if (c == '<' ) { //'<' is our begining character
        startReadingSignKey = true; //Ready to start reading the part 
      }else if(startReadingSignKey){
        if(c != '>'){ //'>' is our ending character
          inString[stringPos] = c;
          stringPos ++;
        }else{
          //got what we need here! We can disconnect now
          startReadingSignKey = false;
          
          for(i=0;i<64;i++){
            Serial.println(inString[i]);
          }
        }
      }
  
  Serial.println("  ");
   stringPos = 0;
  if(c == '<' ){
      startReadingPublicKey = true;
  } else if(startReadingPublicKey){
      if(c != '>'){
         inString[stringPos] = c;
         stringPos ++;
      }else{
          startReadingPublicKey = false;
           client.stop();
          client.flush();
          for(i=0;i<32;i++){
            Serial.println(inString[i]);
          }
          Serial.print("   ");
          Serial.println("disconnecting.");
      }
   } 
   
    Serial.println("  ");
   stringPos = 0;
  if(c == '<' ){
      startReadingSecretKey = true;
  } else if(startReadingSecretKey){
      if(c != '>'){
         inString[stringPos] = c;
         stringPos ++;
      }else{
          startReadingSecretKey = false;
           client.stop();
          client.flush();
          for(i=0;i<32;i++){
            Serial.println(inString[i]);
          }
          Serial.print("   ");
          Serial.println("disconnecting.");
      }
   } 
   
  }
}

