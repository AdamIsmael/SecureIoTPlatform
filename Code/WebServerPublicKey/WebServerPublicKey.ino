
#include <SPI.h>
#include <Ethernet2.h>


byte mac[] = {
  0x90, 0xA2, 0xDA, 0x10, 0x2D, 0xD6
};
byte gateway[] = {192,168,0,1};
byte subnet[] = { 255, 255, 255, 0 };
byte serverip[] = {192, 168, 0, 50}; 



EthernetServer server(8081);


void setup() {
  Serial.begin(9600);

  Ethernet.begin(mac, serverip, gateway, subnet);
  server.begin();
  Serial.print("server is at ");
  Serial.println(Ethernet.localIP());
  Serial.print("Subnet Mask       : ");
  Serial.println(Ethernet.subnetMask());
  Serial.print("Default Gateway IP: ");
  Serial.println(Ethernet.gatewayIP());
  Serial.print("DNS Server IP     : ");
  Serial.println(Ethernet.dnsServerIP());
}


void loop() {
  // listen for incoming clients
  EthernetClient client = server.available();
  String incomingWord = " ", key = "";
  int saveNextWord = 0, takeData = 0;
  if (client) {
    Serial.println("new client");
    client.println("ACK");
    // an http request ends with a blank line
    boolean currentLineIsBlank = true;
    while (client.connected()) {
      if (client.available()) {
        char c = client.read();
        Serial.write(c);
        if(c == ' '){
          if(saveNextWord == 1){
            key = incomingWord;
          }
          incomingWord=" ";
          saveNextWord=0;
        }else{
          incomingWord = incomingWord + c;
        }
        if((incomingWord=="Content") && (takeData)){ //server has seen content header and next
           saveNextWord = 1;                         // word is content
        }
        if(incomingWord=="POST"){ //server knows to accept some content
          takeData=1;
        }
        
        if (c == '\n' && currentLineIsBlank) {
          // send a standard http response header
          client.println("HTTP/1.1 200 OK");
          client.println("Content-Type: text/html");
          client.println("Connection: close");  // the connection will be closed after completion of the response
          client.println("Refresh: 5");  // refresh the page automatically every 5 sec
          client.println();
          client.println("<!DOCTYPE HTML>");
          client.println("<html>");

          client.println("</html>");
          break;
        }
        if (c == '\n') {
          // you're starting a new line
          currentLineIsBlank = true;
        }
        else if (c != '\r') {
          // you've gotten a character on the current line
          currentLineIsBlank = false;
        }
      }
    }
    Serial.print(key);
    // give the web browser time to receive the data
    delay(1);
    // close the connection:
    client.stop();
    Serial.println("client disconnected");
  }
}

