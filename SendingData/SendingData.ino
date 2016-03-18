
#include <Ethernet2.h>
#include <SPI.h>

byte mac[] = {
//0x90, 0xA2, 0xDA, 0x10, 0x2D,0xD6
0x90, 0xA2, 0xDA, 0x10, 0x04, 0xB5
};
//char server[]= "www.tonterias.site88.net";
char server[] = "192.168.0.3";
EthernetClient client;

String data;
int t = 0;
int h=0;

void setup() {
  Serial.begin(9600);
  Serial.println("start");
  
  if(Ethernet.begin(mac)==0){
    Serial.println("Failed to assign IP");
  }else{
     Serial.println("Assigned IP");
  }
  Serial.print("IP Address        : ");
  Serial.println(Ethernet.localIP());
  Serial.print("Subnet Mask       : ");
  Serial.println(Ethernet.subnetMask());
  Serial.print("Default Gateway IP: ");
  Serial.println(Ethernet.gatewayIP());
  Serial.print("DNS Server IP     : ");
  Serial.println(Ethernet.dnsServerIP());
  
  delay(1000);
  
  t=20;
  h=21;
  data="";
}

void loop() {
  //80 for 000webhost
  if(client.connect(server,80)){ //server address port:21
     Serial.println("Connected");
     
     client.println("POST /tempLog/add.php HTTP/1.1"); 
     client.println("HOST: 192.168.0.3"); //server address
     client.println("Content-Type: application/x-www-form-urlencoded");
     client.print("Content-Length: ");
     client.println(19);
     client.println();
     client.print("temperatureHex=");
     client.print("adsa");
     
     Serial.println("Data sent");
//     client.println("GET tonterias.site88.net/add.php?"); 
//     client.print("temperature=");
//     client.print("21");
//     client.print(" HTTP/1.1");
//     client.print("Host: ");
//     client.print(server);
//     client.print( "Connection: close");
//     client.println();
//     client.println();
     //client.stop();
   }else{
       Serial.println("Failed to Connect");
   }
   
   if(client.connected()){
      client.stop(); 
   }
   
   delay(30000);
}

