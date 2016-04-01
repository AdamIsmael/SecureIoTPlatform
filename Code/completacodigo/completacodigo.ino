#include <OneWire.h>
#include <TweetNaCl2.h>
#include <SPI.h>
#include <Ethernet2.h>


OneWire  ds(9);  // on pin 10 (a 4.7K resistor is necessary)

TweetNaCl2 tuit;

byte mac[] = { 0x90, 0xA2, 0xDA, 0x10, 0x04, 0xB5 };
char server[] = "192.168.0.3";
char serverUno[] = "192.168.0.50";
IPAddress ip(192, 168, 0, 177);
EthernetClient client;
EthernetClient client1;
int assigned = 0;

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
  
  byte serverpksign[crypto_sign_PUBLICKEYBYTES] 
    = { 0xf2,0xaf,0xdb,0x38,0x2a,0xcc,0x24,0x57
       ,0x90,0x4e,0xb8,0x77,0xcb,0x24,0x72,0x1a
       ,0xd2,0xb2,0x2e,0x8b,0xe9,0x72,0x7c,0xfb
       ,0x10,0x93,0x81,0x7e,0x5b,0x83,0x35,0xe0};  
 
 const byte serverpk[crypto_box_PUBLICKEYBYTES] 
    = { 0xde, 0x9e, 0xdb, 0x7d, 0x7b, 0x7d, 0xc1, 0xb4, 
        0xd3, 0x5b, 0x61, 0xc2, 0xec, 0xe4, 0x35, 0x37, 
        0x3f, 0x83, 0x43, 0xc8, 0x5b, 0x78, 0x67, 0x4d, 
        0xad, 0xfc, 0x7e, 0x14, 0x6f, 0x88, 0x2b, 0x4f };
        
 const byte preinstallnonce[crypto_box_NONCEBYTES]
    = { 0x69, 0x69, 0x6e, 0xe9, 0x55, 0xb6, 0x2b, 0x73,
        0xcd, 0x62, 0xbd, 0xa8, 0x75, 0xfc, 0x73, 0xd6,
        0x82, 0x19, 0xe0, 0x03, 0x6b, 0x7a, 0x0b, 0x37 };
 
   int const mlen = 41;
   byte m[mlen] 
    = { 0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,    
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        
  unsigned char smtemp[mlen+crypto_sign_BYTES]; //the message after the signature has been added but before the preceding zeros are added
  unsigned char sm[mlen+crypto_sign_BYTES+32]; //signed message with 32 zeros
  
  unsigned long long smlen=0;
 
 byte serverpkold[32] = {NULL}; //old server key and nonce to be used for decryption of next nonce
 byte nonceold[24];
 
 int connectionAttempts=0;
 int clientConnected=0;
 int Suc_Crypt=20;
 int Suc_Sign=20;
 
 
 void postRequest(String final){
  
   client.stop();
   //needs to be done again otherwise Due hangs
   if(Ethernet.begin(mac)==0){
      Serial.println(F("Failed to configure Ethernet using DHCP"));
      Ethernet.begin(mac, ip);
   }else{
      Serial.println("Assigned IP");
      Ethernet.begin(mac, ip);
    }
    Serial.println(F("connecting..."));
  
  if(client.connect(server,80)){ 
     String title = "temperatureHex=";
     int contentLength = title.length()+final.length();
     Serial.println(F("Connected"));   
     client.println("POST /tempLog1/add.php HTTP/1.1"); 
     client.println("HOST: 192.168.0.3"); //server address
     client.println("Content-Type: application/x-www-form-urlencoded");
     client.print("Content-Length: ");
     client.println(contentLength);
     Serial.println(contentLength);
     client.println(); 
     client.print(title);
     client.print(final);
     client.println();
     Serial.println(F("Data sent"));
   }else{
       Serial.println(F("Failed to Connect"));
   }
}

void passAlongPublicKey(String final){
  
  if(Ethernet.begin(mac)==0){
      Serial.println(F("Failed to configure Ethernet using DHCP"));
      Ethernet.begin(mac, ip);
   }else{
      Serial.println("Assigned IP");
      Ethernet.begin(mac, ip);
    }
    Serial.println(F("connecting..."));
  
    if (client.connect(serverUno,8081)) {
    Serial.println("connected");
    // Make a HTTP request:
    client.println("POST arduino HTTP/1.1 ");
    client.println("Host: 192.168.0.50 ");
    client.println("User-Agent: Arduino/1.0 ");
    client.println("Content-Type: application/x-www-form-urlencoded ");
    client.print("Content-Length: ");
    client.println(final.length());
    client.print();
    client.print("Content: ");
    client.print(final);
    client.println();
  }
  else {
    // if you didn't get a connection to the server:
    Serial.println("connection failed");
  }
  
}
  
void setup() {
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
  Serial.print("IP Address        : ");
  Serial.println(Ethernet.localIP());
  Serial.print("Subnet Mask       : ");
  Serial.println(Ethernet.subnetMask());
  Serial.print("Default Gateway IP: ");
  Serial.println(Ethernet.gatewayIP());
  Serial.print("DNS Server IP     : ");
  Serial.println(Ethernet.dnsServerIP());
  delay(1000);
}

void loop() {
  
  Serial.print(F("connecting"));
  byte i=0;
  client.stop();
  //initial connection sometimes takes a couple tries
  while((clientConnected!=1) && (connectionAttempts<4)){
    clientConnected = client.connect(server, 8080);
    connectionAttempts++;
    Serial.print(".");
  }
  
  if(clientConnected){
     Serial.println(F("connected"));   
     client.println("GET /IoTPlatform/SecretKey HTTP/1.1"); 
     client.println("Host: www.google.com");
     client.println("Connection: close");
     client.println();
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
  boolean nextWordIsNonce = false;
  String key = "";
  int keyNumber=0;
  int nonceNumber=0;
  //inital arrays are sent char by char so are twice as long as normal
  //0xde is sent as d and then e
  char keynew[64]={0};
  char noncenewtemp[240] = {0};
  int counter=0;
  
  while(!client.available()){
   //wait for client to be ready 
  }
  
  if (client.available()) {
    while (client.connected()){
      char c = client.read();
      Serial.print(c);
      
      if(c=='>'){
        nextWordIsKey = false; 
        counter=0;
        keyNumber++;
      }
      if(c==')'){
         nextWordIsNonce = false;
         nonceNumber++;
         counter=0;
      }
      if(nextWordIsKey){
         Serial.print(c,HEX);
         key = key+c;
         keynew[counter] = c;
         counter++;
      }
      if(nextWordIsNonce){
          noncenewtemp[counter] = c;
          counter++;
      }
      if(c == '('){
         nextWordIsNonce = true; 
      }
      if(c =='<'){
        nextWordIsKey = true;
      }
      
    } 
  }
  
  size_t count=0;
  char *pos = keynew;
  byte serverpknew[32];
  for(count=0;count<sizeof(serverpknew)/sizeof(serverpknew[0]);count++){
      sscanf(pos,"%2hhx",&serverpknew[count]);
      pos +=2 ;  
    }
    
  char *posNonce = noncenewtemp;
  byte scnoncenewtemp[120];
  for(count=0;count<sizeof(scnoncenewtemp)/sizeof(scnoncenewtemp[0]);count++){
      sscanf(posNonce,"%2hhx",&scnoncenewtemp[count]);
      posNonce +=2 ;  
    }

   int Suc_Decrypt = 20; 
   int scnlenwz = 24+64+32; //signed cipher of nonce with zeros
   byte snoncewz[scnlenwz]; //signed nonce with zeros
   
   if(serverpkold[0]==NULL){
     //which  means it is the first iteration and we need to use the preinstalled keys to get the next set
    Suc_Decrypt = tuit.crypto_box_open(snoncewz, scnoncenewtemp, scnlenwz, preinstallnonce, serverpk, arduinosk); 
   }else{
     Suc_Decrypt = tuit.crypto_box_open(snoncewz, scnoncenewtemp, scnlenwz, nonceold, serverpkold, arduinosk);
   }
     
   //copy new into old
    for(i = 0;i<sizeof(serverpkold)/sizeof(serverpkold[0]);i++){
       serverpkold[i] = serverpknew[i];
    }

   int Suc_unsign=20;
   unsigned long long nlen;
   int snlenwoz=24+64;  //signed nonce with out zeros
   byte nonce[crypto_box_NONCEBYTES];
   byte snoncewoz[snlenwoz];
   
   for(i=0;i<snlenwoz;i++){
       snoncewoz[i] = snoncewz[i+32];
   }
   
   Suc_unsign = tuit.crypto_sign_open(nonce,&nlen,snoncewoz,snlenwoz,serverpksign);
     
     //copy new into old  
     for(i = 0;i<sizeof(nonceold)/sizeof(nonceold[0]);i++){
       nonceold[i] = nonce[i];
      }
    
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
  }
  
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

 int16_t raw = (data[1] << 8) | data[0];
 raw = raw << 3; // 9 bit resolution default
    if (data[7] == 0x10) {
      // "count remain" gives full 12 bit resolution
      raw = (raw & 0xFFF0) + 12 - data[6];
    }
  
  celsius = (float)raw / 16.0;
  Serial.print(F("  Temperature = "));
  Serial.print(celsius);
  Serial.print(F(" Celsius, "));
  
  
  Suc_Sign = tuit.crypto_sign(smtemp, &smlen, m, mlen, arduinosksign);
  
   byte sc[smlen+32]; //signed cipher
   
   //add 32 bytes of zeros
   for(i=0;i<32;i++){
    sm[i]=0;
  }
  for(i=0;i<137;i++){
     sm[32+i]=smtemp[i]; 
  } 
  
  Suc_Crypt = tuit.crypto_box(sc, sm, 137, nonce, serverpkold, arduinosk); 
  //use serverpjold here because at the moment they contain the same thing but
  //serverpknew is altered by something
  
  
  char temp[4];
  String final;
  byte j;
  for (j=0;j<137;j++){
    //adding explicit extra zero 
    //otherwise 0x05 becomes 0x50
    sprintf(temp, "%x",sc[j]);
    if(sc[j]<=15&&j>15){
      final = final +0+ temp;
    }else{
    final = final + temp;
    }
  }
   client.stop();
  
   
  postRequest(final);

  passAlongPublicKey(final);
  
  delay(60000); //minute long delay
  
}


