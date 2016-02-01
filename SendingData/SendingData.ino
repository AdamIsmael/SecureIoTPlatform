
#include <Ethernet2.h>
#include <SPI.h>

byte mac[] = {
0x90, 0xA2, 0xDA, 0x10, 0x2D,0xD6
};
byte server[]={31,170,160,73};

EthernetClient client;

String data;
int t = 0;

void setup() {
  Serial.begin(9600);
  Serial.print("start");
  
  if(Ethernet.begin(mac)==0){
    Serial.println("fail");
  }
  
  t=20;
  data="";
}

void loop() {
  
  data="temp1" + t;
  
  if(client.connect(server,80)){ //server address port:21
     client.println("POST /add.php HTTP/1.1"); 
     client.println("HOST: 31.170.160.73"); //server address
     client.println("Content-Type: application/x-www-form-urlencoded");
     client.print("Content-Length: ");
     client.println(data.length());
     client.println();
     client.print(data);
   }
   
   if(client.connected()){
      client.stop(); 
   }
   
   delay(30000);
}
