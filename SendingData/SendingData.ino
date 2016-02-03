
#include <Ethernet2.h>
#include <SPI.h>

byte mac[] = {
0x90, 0xA2, 0xDA, 0x10, 0x2D,0xD6
};
char server[]= "www.tonterias.site88.net";
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
  
  if(client.connect(server,80)){ //server address port:21
     Serial.println("Connected");
     
     client.println("POST /add.php HTTP/1.1"); 
     client.println("HOST: www.tonterias.site88.net"); //server address
     client.println("Content-Type: application/x-www-form-urlencoded");
     client.print("Content-Length: ");
     client.println(14);
     client.println();
     client.print("temperature=");
     client.print("22");
     
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

