#include <OneWire.h>
#include <tweetnacl.h>



 const unsigned char alicesk[crypto_box_SECRETKEYBYTES]
    = { 0x77, 0x07, 0x6d, 0x0a, 0x73, 0x18, 0xa5, 0x7d, 0x3c, 0x16, 0xc1,
        0x72, 0x51, 0xb2, 0x66, 0x45, 0xdf, 0x4c, 0x2f, 0x87, 0xeb, 0xc0,
        0x99, 0x2a, 0xb1, 0x77, 0xfb, 0xa5, 0x1d, 0xb9, 0x2c, 0x2a };

 const unsigned char bobpk[crypto_box_PUBLICKEYBYTES]
    = { 0xde, 0x9e, 0xdb, 0x7d, 0x7b, 0x7d, 0xc1, 0xb4, 0xd3, 0x5b, 0x61,
        0xc2, 0xec, 0xe4, 0x35, 0x37, 0x3f, 0x83, 0x43, 0xc8, 0x5b, 0x78,
        0x67, 0x4d, 0xad, 0xfc, 0x7e, 0x14, 0x6f, 0x88, 0x2b, 0x4f };
        
 const unsigned char nonce[crypto_box_NONCEBYTES]
    = { 0x69, 0x69, 0x6e, 0xe9, 0x55, 0xb6, 0x2b, 0x73,
        0xcd, 0x62, 0xbd, 0xa8, 0x75, 0xfc, 0x73, 0xd6,
        0x82, 0x19, 0xe0, 0x03, 0x6b, 0x7a, 0x0b, 0x37 };
 
  unsigned char m[40] 
    = { 0,    0,    0,    0,    0,    0,    0,    0,    0,    0,        
        0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    
        0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    
        0,    0,       };
        
 unsigned char c[40];
 OneWire  ds(10);  // on pin 10 (a 4.7K resistor is necessary)
 int Suc_Crypt=1;
  
void setup() {
  Serial.begin(9600);
}

void loop() {
  
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
    return;
  }
  if (OneWire::crc8(addr, 7) != addr[7]) {
      Serial.println("CRC is not valid!");
      return;
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
  for ( i = 31; i < 40; i++) {           // we need 9 bytes
    m[i] = ds.read();
    Serial.print(m[i], HEX);
    Serial.print(" ");
  }
//  byte j;
//  Serial.print("datatobesent ");
//  for (j=0;j<40;j++){
//    // Serial.print(" ");
//    Serial.print(m[j],HEX);
//    Serial.print(" ");
//  }
  
  Suc_Crypt = crypto_box(c, m, 40, nonce, bobpk, alicesk);
  Serial.print(" Successful encrypt: ");
  Serial.println(Suc_Crypt);
  
  
  
  
  
}


