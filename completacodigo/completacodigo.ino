#include <OneWire.h>
#include <TweetNaCl2.h>
#include <Ethernet2.h>
#include <SPI.h>

OneWire  ds(10);  // on pin 10 (a 4.7K resistor is necessary)

TweetNaCl2 tuit;

byte mac[] = {
0x90, 0xA2, 0xDA, 0x10, 0x2D,0xD6
};
char server[] = "192.168.0.6";
EthernetClient client;
int assigned = 0;

int const clen = 41;

//byte = unsigned char
 const byte alicesk[crypto_box_SECRETKEYBYTES]
    = { 0x77, 0x07, 0x6d, 0x0a, 0x73, 0x18, 0xa5, 0x7d, 
        0x3c, 0x16, 0xc1, 0x72, 0x51, 0xb2, 0x66, 0x45, 
        0xdf, 0x4c, 0x2f, 0x87, 0xeb, 0xc0, 0x99, 0x2a, 
        0xb1, 0x77, 0xfb, 0xa5, 0x1d, 0xb9, 0x2c, 0x2a };

 const byte bobpk[crypto_box_PUBLICKEYBYTES] 
    = { 0xde, 0x9e, 0xdb, 0x7d, 0x7b, 0x7d, 0xc1, 0xb4, 
        0xd3, 0x5b, 0x61, 0xc2, 0xec, 0xe4, 0x35, 0x37, 
        0x3f, 0x83, 0x43, 0xc8, 0x5b, 0x78, 0x67, 0x4d, 
        0xad, 0xfc, 0x7e, 0x14, 0x6f, 0x88, 0x2b, 0x4f };
        
 const byte nonce[crypto_box_NONCEBYTES]
    = { 0x69, 0x69, 0x6e, 0xe9, 0x55, 0xb6, 0x2b, 0x73,
        0xcd, 0x62, 0xbd, 0xa8, 0x75, 0xfc, 0x73, 0xd6,
        0x82, 0x19, 0xe0, 0x03, 0x6b, 0x7a, 0x0b, 0x37 };
 
   byte m[clen] 
    = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,    
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        
  byte c[clen];
 
 int Suc_Crypt=20;
  
void setup() {
  Serial.begin(9600);
  delay(1000);
}

void loop() {
  Serial.println("loop");
  byte i;
  byte present = 0;
  byte type_s;
  byte data[12]; //12?? not 9?
  byte addr[8];
  
  if ( !ds.search(addr)) {
    //Serial.println("No more addresses.");
    //Serial.println();
    ds.reset_search();
    delay(250);
    //return;
  }
  
  if (OneWire::crc8(addr, 7) != addr[7]) {
      Serial.println("CRC is not valid!");
      //return;
  } 
  ds.reset();
  ds.select(addr);
  ds.write(0x44, 1); // start conversion, with parasite power on at the end
  delay(1000);
  
  present = ds.reset();
  ds.select(addr);    
  ds.write(0xBE);         // Read Scratchpad

  Serial.print("  Data = ");
  //Serial.print(present, HEX);
  Serial.print(" ");
  Serial.print("message ");
  for ( i = 32; i < 41; i++) {           // we need 9 bytes
    m[i] = ds.read();
    Serial.print(m[i], HEX);
    Serial.print(" ");
  }
   Serial.println(" ");
  for ( i = 0; i < 41; i++) {
    Serial.print(" ,0x"); 
    Serial.print(m[i], HEX);
    Serial.print(" ");
    if(i%8==7){
         Serial.println();
       }
  }
  
  unsigned char testMessage[163] = {
    0,   0,   0,   0,   0,   0,   0,   0
,   0,   0,   0,   0,   0,   0,   0,   0
,   0,   0,   0,   0,   0,   0,   0,   0
,   0,   0,   0,   0,   0,   0,   0,   0
,0xbe,0x07,0x5f,0xc5,0x3c,0x81,0xf2,0xd5
,0xcf,0x14,0x13,0x16,0xeb,0xeb,0x0c,0x7b
,0x52,0x28,0xc5,0x2a,0x4c,0x62,0xcb,0xd4
,0x4b,0x66,0x84,0x9b,0x64,0x24,0x4f,0xfc
,0xe5,0xec,0xba,0xaf,0x33,0xbd,0x75,0x1a
,0x1a,0xc7,0x28,0xd4,0x5e,0x6c,0x61,0x29
,0x6c,0xdc,0x3c,0x01,0x23,0x35,0x61,0xf4
,0x1d,0xb6,0x6c,0xce,0x31,0x4a,0xdb,0x31
,0x0e,0x3b,0xe8,0x25,0x0c,0x46,0xf0,0x6d
,0xce,0xea,0x3a,0x7f,0xa1,0x34,0x80,0x57
,0xe2,0xf6,0x55,0x6a,0xd6,0xb1,0x31,0x8a
,0x02,0x4a,0x83,0x8f,0x21,0xaf,0x1f,0xde
,0x04,0x89,0x77,0xeb,0x48,0xf5,0x9f,0xfd
,0x49,0x24,0xca,0x1c,0x60,0x90,0x2e,0x52
,0xf0,0xa0,0x89,0xbc,0x76,0x89,0x70,0x40
,0xe0,0x82,0xf9,0x37,0x76,0x38,0x48,0x64
,0x5e,0x07,0x05
};
  
  
  Suc_Crypt = tuit.crypto_box(c, m, clen, nonce, bobpk, alicesk);
  Serial.print(" Successful encrypt: ");
  Serial.println(Suc_Crypt);
  
  char temp[4];
  String final;
  byte j;
  Serial.print("datatobesent ");
  for (j=0;j<clen;j++){
    Serial.print(" ,0x");
    Serial.print(c[j],HEX);
    sprintf(temp, "%x",c[j]);
    //if()
    //c[j].length();
    if(c[j]<=15&&j>15){
      final = final +0+ temp;
    }else{
    //c[j].length();
    final = final + temp;
    }
    //Serial.println(final);
     if(j%8==7){
         Serial.println();
       }
  }
  Serial.println("Hex ");
  Serial.print(final);
  Serial.println();
   //String string = c[0];  
   //data1 = c[0] + c[1];
  
  //setup has to be here otherwise the CRC from tempsen becomes in valid 
  if(Ethernet.begin(mac)==0){
      Serial.println("Failed to assign IP");
  }else{
     Serial.println("Assigned IP");
     assigned=1;
  }
  Serial.print("IP Address        : ");
  Serial.println(Ethernet.localIP());
  Serial.print("Subnet Mask       : ");
  Serial.println(Ethernet.subnetMask());
  Serial.print("Default Gateway IP: ");
  Serial.println(Ethernet.gatewayIP());
  Serial.print("DNS Server IP     : ");
  Serial.println(Ethernet.dnsServerIP());
   
   //48 individual chars + 15
  if(client.connect(server,80)){ //server address port:21
     String data = "temperatureHex=";
     int contentLength = data.length()+final.length();
     Serial.println("Connected");   
     client.println("POST /tempLog/add.php HTTP/1.1"); 
     client.println("HOST: 192.168.0.6"); //server address
     client.println("Content-Type: application/x-www-form-urlencoded");
     client.print("Content-Length: ");
     client.println(contentLength);
     Serial.println(contentLength);
     client.println();
     //client.print("temperatureInt=");
     //client.print("22");
     client.print("temperatureHex=");
     client.print(final);
     Serial.println("Data sent");
   }else{
       Serial.println("Failed to Connect");
   }
  
  Serial.println("endofloop");
  delay(300000);
  
}


