
#include <Ethernet2.h>
#include <SPI.h>

byte mac{} = {};
Ethernet client client;

String data;

void setup() {
  Serial.begin(9600);
  Serial.print("start");
  
  if(Ethernet.begin(mac)==0){
    Serial.println("fail");
  }
  
  data="";

void loop() {
  
  data="hex";
  
  if(client.connect("www.")){ //server address
     client.println("POST /add.php HTTP/1.1"); 
     client.println("HOST: ") //server address
     client.print("Data: ");
     client.print(data);
   }
   
   if(client.connected()){
      client.stop(); 
   }
   
   delay(30000);
}
