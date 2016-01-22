/*
  DHCP-based IP printer

 This sketch uses the DHCP extensions to the Ethernet library
 to get an IP address via DHCP and print the address obtained.
 using an Arduino Wiznet Ethernet shield.

 Circuit:
 * Ethernet shield attached to pins 10, 11, 12, 13

 created 12 April 2011
 modified 9 Apr 2012
 by Tom Igoe

 */

#include <SPI.h>
#include <Ethernet2.h>
//#include <Ethernet.h>  -->  #include <Ethernet2.h>
//#include <EthernetUdp.h>  -->  #include <EthernetUdp2.h>

// Enter a MAC address for your controller below.
// Newer Ethernet shields have a MAC address printed on a sticker on the shield
byte mac[] = {
  //0x00, 0xAA, 0xBB, 0xCC, 0xDE, 0x02
  0x90, 0xA2, 0xDA, 0x10, 0x2D, 0xD6
};
byte ip[] = {
  192,168,0,20
};
IPAddress ipAddress(192,168,0,20); 
// Initialize the Ethernet client library
// with the IP address and port of the server
// that you want to connect to (port 80 is default for HTTP):
EthernetClient client;

// Try using this native port?


void setup() {
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  Serial.print("start");
  SPI.begin(10);
  // this check is only needed on the Leonardo:
 // while (!Serial) {
  //  ; // wait for serial port to connect. Needed for Leonardo only
 // }

  // start the Ethernet connection:
  //
 // if (Ethernet.begin(mac) == 0) {
  Ethernet.begin(mac, ip);
    //Serial.println("Failed to configure Ethernet using DHCP");
    // no point in carrying on, so do nothing forevermore:
    //for (;;)
      //;
  //}
  // print your local IP address:
  Serial.print(Ethernet.localIP());
  Serial.print("My IP address: ");
  for (byte thisByte = 0; thisByte < 4; thisByte++) {
    // print the value of each byte of the IP address:
    
    Serial.print(Ethernet.localIP()[thisByte], DEC);
    Serial.print(".");
  }
  Serial.println();
}

void loop() {

}


