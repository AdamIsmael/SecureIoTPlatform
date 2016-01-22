/*#include <OneWire.h>

// DS18S20 Temperature chip i/o
OneWire ds(10);  // on pin 10

void setup(void) {
  // initialize inputs/outputs
  // start serial port
  Serial.begin(9600);
}

void loop(void) {
  byte i;
  byte present = 0;
  byte data[12];
  byte addr[8];

  ds.reset_search();
  if ( !ds.search(addr)) {
      Serial.print("No more addresses.\n");
      ds.reset_search();
      return;
  }

  Serial.print("R=");
  for( i = 0; i < 8; i++) {
    Serial.print(addr[i], HEX);
    Serial.print(" ");
  }

  if ( OneWire::crc8( addr, 7) != addr[7]) {
      Serial.print("CRC is not valid!\n");
      return;
  }

  if ( addr[0] == 0x10) {
      Serial.print("Device is a DS18S20 family device.\n");
  }
  else if ( addr[0] == 0x28) {
      Serial.print("Device is a DS18B20 family device.\n");
  }
  else {
      Serial.print("Device family is not recognized: 0x");
      Serial.println(addr[0],HEX);
      return;
  }

  ds.reset();
  ds.select(addr);
  ds.write(0x44,1);         // start conversion, with parasite power on at the end

  delay(1000);     // maybe 750ms is enough, maybe not
  // we might do a ds.depower() here, but the reset will take care of it.

  present = ds.reset();
  ds.select(addr);    
  ds.write(0xBE);         // Read Scratchpad

  Serial.print("P=");
  Serial.print(present,HEX);
  Serial.print(" ");
  for ( i = 0; i < 9; i++) {           // we need 9 bytes
    data[i] = ds.read();
    Serial.print(data[i], HEX);
    Serial.print(" ");
  }
  Serial.print(" CRC=");
  Serial.print( OneWire::crc8( data, 8), HEX);
  Serial.println();
}
*/

#include <OneWire.h>
//#include <LiquidCrystal.h>
// LCD=======================================================
//initialize the library with the numbers of the interface pins
//LiquidCrystal lcd(12, 11, 5, 4, 3, 2);
//#define LCD_WIDTH 20
//#define LCD_HEIGHT 2

// DS18S20 Temperature chip i/o 

OneWire  ds(10);  // on pin 9
#define MAX_DS1820_SENSORS 1
byte addr[MAX_DS1820_SENSORS][8];
void setup(void) 
{
  //lcd.begin(LCD_WIDTH, LCD_HEIGHT,1);
  //lcd.setCursor(0,0);
  Serial.begin(9600);
  Serial.print("DS1820 Test");
  Serial.println();
  if (!ds.search(addr[0])) 
  {
    //lcd.setCursor(0,0);
    Serial.print("No more addresses.");
    Serial.println();
    ds.reset_search();
    delay(250);
    return;
  }
  if ( !ds.search(addr[1])) 
  {
    //lcd.setCursor(0,0);
    Serial.print("No more addresses.");
    Serial.println();
    ds.reset_search();
    delay(250);
    return;
  }
}
int HighByte, LowByte, TReading, SignBit, Tc_100, Whole, Fract;
char buf[20];

void loop(void) 
{
  byte i, sensor;
  byte present = 0;
  byte data[12];

  for (sensor=0;sensor<MAX_DS1820_SENSORS;sensor++)
  {
    if ( OneWire::crc8( addr[sensor], 7) != addr[sensor][7]) 
    {
      //lcd.setCursor(0,0);
      Serial.print("CRC is not valid");
      Serial.println();
      return;
    }

    if ( addr[sensor][0] != 0x10) 
    {
      //lcd.setCursor(0,0);
      Serial.print("Device is not a DS18S20 family device.");
      Serial.println();
      return;
    }

    ds.reset();
    ds.select(addr[sensor]);
    ds.write(0x44,1);         // start conversion, with parasite power on at the end

    delay(1000);     // maybe 750ms is enough, maybe not
    // we might do a ds.depower() here, but the reset will take care of it.

    present = ds.reset();
    ds.select(addr[sensor]);    
    ds.write(0xBE);         // Read Scratchpad

    for ( i = 0; i < 9; i++) 
    {           // we need 9 bytes
      data[i] = ds.read();
      Serial.print(data[i], HEX);
      Serial.print(" ");
    }

    LowByte = data[0];
    HighByte = data[1];
    TReading = (HighByte << 8) + LowByte;
    //Serial.print(TReading, HEX);
    //Serial.println();
    SignBit = TReading & 0x8000;  // test most sig bit
    if (SignBit) // negative
    {
      TReading = (TReading ^ 0xffff) + 1; // 2's comp
    }
    Tc_100 = (TReading*100/2);    

    //Serial.print(Tc_100);
    //Serial.println();
    Whole = Tc_100 / 100;  // separate off the whole and fractional portions
    Fract = Tc_100 % 100;
    
    //sprintf(buf, "%d:%c%d.%d\337C     ",sensor,SignBit ? '-' : '+', Whole, Fract < 10 ? 0 : Fract);
    sprintf(buf, "%c%d.%d\262C     ",SignBit ? '-' : '+', Whole, Fract < 10 ? 0 : Fract);

    //lcd.setCursor(0,sensor%LCD_HEIGHT);
    Serial.print("    ");
    Serial.print(buf);
    Serial.println();
  }
}
 


