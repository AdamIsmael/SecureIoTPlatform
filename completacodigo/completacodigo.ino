#include <OneWire.h>
#include <TweetNaCl2.h>
#include <SPI.h>
#include <Ethernet2.h>


OneWire  ds(9);  // on pin 10 (a 4.7K resistor is necessary)

TweetNaCl2 tuit;

byte mac[] = { 0x90, 0xA2, 0xDA, 0x10, 0x04, 0xB5 };
//0x90, 0xA2, 0xDA, 0x10, 0x2D, 0xD6 Uno

//};
char server[] = "192.168.0.2";// do not do {192,168,0,3};
IPAddress ip(192, 168, 0, 177);
EthernetClient client;
EthernetClient client1;
int assigned = 0;



//byte = unsigned char
 const byte arduinosk[crypto_box_SECRETKEYBYTES]
    = { 0x77, 0x07, 0x6d, 0x0a, 0x73, 0x18, 0xa5, 0x7d, 
        0x3c, 0x16, 0xc1, 0x72, 0x51, 0xb2, 0x66, 0x45, 
        0xdf, 0x4c, 0x2f, 0x87, 0xeb, 0xc0, 0x99, 0x2a, 
        0xb1, 0x77, 0xfb, 0xa5, 0x1d, 0xb9, 0x2c, 0x2a };

  byte arduinosksign[crypto_sign_SECRETKEYBYTES]
    = { 0x74,0xc5,0x5a,0xcc,0x8b,0xa0,0x8f,0x01,
        0xec,0x2b,0xed,0x47,0x4f,0x64,0x15,0x5a,
        0x4c,0xb3,0x7a,0x61,0x71,0x66,0xf5,0x58,
        0xb4,0x83,0xc7,0x29,0x47,0x68,0xc6,0x40,
        0xb6,0xa6,0x83,0x9a,0x41,0x03,0x25,0xb1,
        0xc8,0x04,0x33,0xc1,0x19,0x50,0xdd,0x26,
        0x96,0x62,0xb1,0x67,0xe9,0x0b,0xa5,0x46,
        0xe7,0x48,0x5f,0xa9,0x15,0x10,0x2e,0x51 };

 const byte serverpk[crypto_box_PUBLICKEYBYTES] 
    = { 0xde, 0x9e, 0xdb, 0x7d, 0x7b, 0x7d, 0xc1, 0xb4, 
        0xd3, 0x5b, 0x61, 0xc2, 0xec, 0xe4, 0x35, 0x37, 
        0x3f, 0x83, 0x43, 0xc8, 0x5b, 0x78, 0x67, 0x4d, 
        0xad, 0xfc, 0x7e, 0x14, 0x6f, 0x88, 0x2b, 0x4f };
        
 const byte nonce[crypto_box_NONCEBYTES]
    = { 0x69, 0x69, 0x6e, 0xe9, 0x55, 0xb6, 0x2b, 0x73,
        0xcd, 0x62, 0xbd, 0xa8, 0x75, 0xfc, 0x73, 0xd6,
        0x82, 0x19, 0xe0, 0x03, 0x6b, 0x7a, 0x0b, 0x37 };
 
   int const mlen = 41;
   byte m[mlen] 
    = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,    
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        
  unsigned char smtemp[mlen+crypto_sign_BYTES]; //the message after the signature has been added but before the preceding zeros are added
  unsigned char sm[mlen+crypto_sign_BYTES+32]; //signed message with 32 zeros
  
  unsigned long long smlen=0;
 
 int connectionAttempts=0;
 int clientConnected=0;
 int Suc_Crypt=20;
 int Suc_Sign=20;
 
 
 void postRequest(String final){
  
    if(Ethernet.begin(mac)==0){
      Serial.println(F("Failed to configure Ethernet using DHCP"));
      Ethernet.begin(mac, ip);
  }
  else{
     Serial.println("Assigned IP");
     //Ethernet.begin(mac, ip);
  }
    Serial.println(F("connecting..."));
  client.stop();
  if(client1.connect(server,80)){ //server address port:21
     String title = "temperatureHex=";
     String testData = "0005e6448";
     int contentLength = title.length()+final.length();
     int testContentLength = title.length()+testData.length();
     Serial.println(F("Connected"));   
     client1.println("POST /tempLog1/add.php HTTP/1.1"); 
     client1.println("HOST: 192.168.0.9"); //server address
     client1.println("Content-Type: application/x-www-form-urlencoded");
     client1.print("Content-Length: ");
     //client.println(19);//need a client.println("Content: somnedata")
     client1.println(testContentLength);
     Serial.println(testContentLength);
     client1.println(); //should the data be after this space, does the server stop listening after this?
     client1.print(title);
     //client.print(testData);
     client1.print(final);
     client1.println();
     Serial.println(F("Data sent"));
   }else{
       Serial.println(F("Failed to Connect"));
   }
}
  
  
  
void setup() {
  //send data to say that we are using key numero uno
  Serial.begin(9600);
  Serial.println("start");
  
  if(Ethernet.begin(mac)==0){
      Serial.println(F("Failed to configure Ethernet using DHCP"));
      Ethernet.begin(mac, ip);
  }
  else{
     Serial.println("Assigned IP");
     //Ethernet.begin(mac, ip);
  }
//  Serial.print("IP Address        : ");
//  Serial.println(Ethernet.localIP());
//  Serial.print("Subnet Mask       : ");
//  Serial.println(Ethernet.subnetMask());
//  Serial.print("Default Gateway IP: ");
//  Serial.println(Ethernet.gatewayIP());
//  Serial.print("DNS Server IP     : ");
//  Serial.println(Ethernet.dnsServerIP());
//  delay(1000);
   
}



void loop() {
  //Serial.println("loop");
//  
  Serial.println(F("connecting..."));
  
  client.stop();
  while((clientConnected!=1) && (connectionAttempts<4)){
    clientConnected = client.connect(server, 8080);
    connectionAttempts++;
  }
  
  if(clientConnected){
     Serial.println(F("connected"));   
     client.println("GET /IoTPlatform/SecretKey HTTP/1.1"); 
     client.println("Host: www.google.com");
     client.println("Connection: close");
     client.println();
     //client.read();
     clientConnected=0;
   }else{
       Serial.println(F("connection failed"));
   }
  
  Serial.println("");
  Serial.print(F("Client took ")); 
  Serial.print(connectionAttempts); 
  Serial.print(F(" attempts to connect"));
  connectionAttempts=0;
  
  boolean nextWordIsKey = false;
  String key = "";
  int keyNumber=0;
  char key1[64]={0};
  //char key2[64]={0};
  int counter=0;
  
  while(!client.available()){
   //wait for client ro be ready 
  }
  
  if (client.available()) {
    while (client.connected()){
      char c = client.read();
      Serial.print(c);
      
      if(c=='>'){
        nextWordIsKey = false; 
        keyNumber++;
      }
      if(nextWordIsKey){
         //Serial.print(c,HEX);
         key = key+c;
         key1[counter] = c;
         counter++;
      }
      if(c =='<'){
        nextWordIsKey = true;
      }
      
    } 
  }

  Serial.println("");
  Serial.print(F("Key Recieved is "));
  Serial.print(key);
  Serial.println("");

  
  size_t count=0;
  char *pos = key1;
  //char *pos = key;
  byte val[32];
  
  for(count=0;count<sizeof(val)/sizeof(val[0]);count++){
      sscanf(pos,"%2hhx",&val[count]);
      pos +=2 ;  
    }
    
    Serial.println("");
    Serial.print(F("0x"));
     for(count = 0;count<sizeof(val)/sizeof(val[0]);count++){
       Serial.print(val[count],HEX); 
    }
    
//  Serial.println("");
//  Serial.print(F("encryptKey"));
//  Serial.println("");
//    int k;
//    for ( k = 0; k < 32; k++) {
//    Serial.print(F(" ,0x")); 
//    Serial.print(val[k], HEX);
//    Serial.print(" ");
//    if(k%8==7){
//         Serial.println("");
//       }
//  }
    
  // if the server's disconnected, stop the client:
//  if (!client.connected()) {
//    Serial.println();
//    Serial.println("disconnecting.");
//    client.stop();
//
//    // do nothing forevermore:
//    while (true);
//  }
  
  
  byte i;
  byte present = 0;
  byte type_s;
  byte data[12]; 
  byte addr[8];
  float celsius;
  
   if ( !ds.search(addr)) {
    Serial.println(F("No more addresses."));
    Serial.println();
    ds.reset_search();
    delay(250);
    return;
  }
  
//  Serial.print(F("ROM ="));
//  for( i = 0; i < 8; i++) {
//    Serial.write(' ');
//    Serial.print(addr[i], HEX);
//  }
  
//  if (OneWire::crc8(addr, 7) != addr[7]) {
//      Serial.println(F("CRC is not valid!"));
//  } 
//  Serial.println();
  
  // the first ROM byte indicates which chip
//  switch (addr[0]) {
//    case 0x10:
//      Serial.println(F("  Chip = DS18S20"));  // or old DS1820
//      type_s = 1;
//      break;
//    case 0x28:
//      Serial.println(F("  Chip = DS18B20"));
//      type_s = 0;
//      break;
//    case 0x22:
//      Serial.println(F("  Chip = DS1822"));
//      type_s = 0;
//      break;
//    default:
//      Serial.println("Device is not a DS18x20 family device.");
//      return;
//  }
  
  ds.reset();
  ds.select(addr);
  ds.write(0x44, 1); // start conversion, with parasite power on at the end
  delay(1000);
  
  present = ds.reset();
  ds.select(addr);    
  ds.write(0xBE);         // Read Scratchpad

  Serial.print(F("  Data = "));
  Serial.print(present, HEX);
  Serial.print(" ");
  Serial.print(F("message "));
  for ( i = 0; i < 9; i++) {           // we need 9 bytes
    data[i] = ds.read();
    m[i+32] = data[i];
    Serial.print(data[i], HEX);
    Serial.print(" ");
  }
//  Serial.print(F(" CRC="));
//  Serial.print(OneWire::crc8(data, 8), HEX);
//  Serial.println();

 int16_t raw = (data[1] << 8) | data[0];
  //if (type_s) {
    raw = raw << 3; // 9 bit resolution default
    if (data[7] == 0x10) {
      // "count remain" gives full 12 bit resolution
      raw = (raw & 0xFFF0) + 12 - data[6];
    }
  //} 
//  else {
//    byte cfg = (data[4] & 0x60);
//    // at lower res, the low bits are undefined, so let's zero them
//    if (cfg == 0x00) raw = raw & ~7;  // 9 bit resolution, 93.75 ms
//    else if (cfg == 0x20) raw = raw & ~3; // 10 bit res, 187.5 ms
//    else if (cfg == 0x40) raw = raw & ~1; // 11 bit res, 375 ms
//    //// default is 12 bit resolution, 750 ms conversion time
//  }
  celsius = (float)raw / 16.0;
  Serial.print(F("  Temperature = "));
  Serial.print(celsius);
  Serial.print(F(" Celsius, "));
  
  Serial.flush();
//   Serial.println(" ");
//  for ( i = 0; i < 41; i++) {
//    Serial.print(F(" ,0x")); 
//    Serial.print(m[i], HEX);
//    Serial.print(" ");
//    if(i%8==7){
//         Serial.println();
//       }
//  }


  Suc_Sign = tuit.crypto_sign(smtemp, &smlen, m, mlen, arduinosksign);
  
  Serial.println(F(" Successful Signing: "));
  Serial.print(Suc_Sign);
   
//   for(i=0;i<smlen;i++){
//     Serial.print(" ,0x");
//     Serial.print(smtemp[i], HEX);
//     if(i%8==7){
//         Serial.println();
//       }
//   }
//   Serial.println();
   byte sc[smlen+32];
   
   for(i=0;i<32;i++){
    sm[i]=0;
  }
  for(i=0;i<137;i++){
     sm[32+i]=smtemp[i]; 
  } 
  
//  Serial.println(" ");
//  Serial.print(F(" messagewithzeros"));
//  Serial.println(" ");
//  
//   for(i=0;i<137;i++){
//       Serial.print(F(" ,0x"));
//       Serial.print(sm[i],HEX);
//       if(i%8==7){
//         Serial.println();
//       }
//     } 
  
  //Suc_Crypt = tuit.crypto_box(c, m, clen, nonce, bobpk, arduinosk);
  Suc_Crypt = tuit.crypto_box(sc, sm, 137, nonce, serverpk, arduinosk);
  Serial.flush();
  Serial.print(F(" Successful encrypt: "));
  Serial.println(Suc_Crypt);
  
  
  char temp[4];
  String final;
  byte j;
  //Serial.print(F("datatobesent "));
  for (j=0;j<137;j++){
    //Serial.print(" ,0x");
    //Serial.print(sc[j],HEX);
    sprintf(temp, "%x",sc[j]);
    if(sc[j]<=15&&j>15){
      final = final +0+ temp;
    }else{
    final = final + temp;
    }
     if(j%8==7){
        // Serial.println();
       }
  }
  //Serial.println(F("Hex "));
  //Serial.print(final);
 
  //int connectionStatus = client.connect(server,80);
  //Serial.println(connectionStatus);
   //48 individual chars + 15
   client.stop();
  
   
  postRequest(final);
//   
//    while((clientConnected!=1) && (connectionAttempts<4)){
//    clientConnected = client.connect(server, 80);
//    connectionAttempts++;
//  }
//   
//  if(client.connect(server,80)){ //server address port:21
//     String title = "temperatureHex=";
//     String testData = "0005e6448";
//     int contentLength = title.length()+final.length();
//     int testContentLength = title.length()+testData.length();
//     Serial.println(F("Connected"));   
//     client.println("POST /tempLog1/add.php HTTP/1.1"); 
//     client.println("HOST: 192.168.0.9"); //server address
//     client.println("Content-Type: application/x-www-form-urlencoded");
//     client.print("Content-Length: ");
//     //client.println(19);//need a client.println("Content: somnedata")
//     client.println(testContentLength);
//     Serial.println(testContentLength);
//     client.println(); //should the data be after this space, does the server stop listening after this?
//     client.print(title);
//     //client.print(testData);
//     client.print(final);
//     client.println();
//     Serial.println(F("Data sent"));
//   }else{
//       Serial.println(F("Failed to Connect"));
//   }
//  
//  client.stop(); // Temp sensor fails if Ethernet Shield is connected
  Serial.println(F("endofloop"));
  delay(30000);
  
}


