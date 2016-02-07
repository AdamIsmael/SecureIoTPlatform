#include <TweetNaCl2.h>

TweetNaCl2 tuit;

void setup() {
  
  uint8_t ret = 1;
  int i=0;
  Serial.begin(9600);
  Serial.print("decrypt");
  Serial.println();
  
  unsigned char bobsk[32] = {
 0x5d,0xab,0x08,0x7e,0x62,0x4a,0x8a,0x4b
,0x79,0xe1,0x7f,0x8b,0x83,0x80,0x0e,0xe6
,0x6f,0x3b,0xb1,0x29,0x26,0x18,0xb6,0xfd
,0x1c,0x2f,0x8b,0x27,0xff,0x88,0xe0,0xeb
} ;

unsigned char alicepk[32] = {
 0x85,0x20,0xf0,0x09,0x89,0x30,0xa7,0x54
,0x74,0x8b,0x7d,0xdc,0xb4,0x3e,0xf7,0x5a
,0x0d,0xbf,0x3a,0x0d,0x26,0x38,0x1a,0xf4
,0xeb,0xa4,0xa9,0x8e,0xaa,0x9b,0x4e,0x6a
} ;

unsigned char nonce[24] = {
 0x69,0x69,0x6e,0xe9,0x55,0xb6,0x2b,0x73
,0xcd,0x62,0xbd,0xa8,0x75,0xfc,0x73,0xd6
,0x82,0x19,0xe0,0x03,0x6b,0x7a,0x0b,0x37
} ;

// API requires first 16 bytes to be 0
unsigned char c[163] = {
    0,   0,   0,   0,   0,   0,   0,   0
,   0,   0,   0,   0,   0,   0,   0,   0
,0xf3,0xff,0xc7,0x70,0x3f,0x94,0x00,0xe5
,0x2a,0x7d,0xfb,0x4b,0x3d,0x33,0x05,0xd9
,0x8e,0x99,0x3b,0x9f,0x48,0x68,0x12,0x73
,0xc2,0x96,0x50,0xba,0x32,0xfc,0x76,0xce
,0x48,0x33,0x2e,0xa7,0x16,0x4d,0x96,0xa4
,0x47,0x6f,0xb8,0xc5,0x31,0xa1,0x18,0x6a
,0xc0,0xdf,0xc1,0x7c,0x98,0xdc,0xe8,0x7b
,0x4d,0xa7,0xf0,0x11,0xec,0x48,0xc9,0x72
,0x71,0xd2,0xc2,0x0f,0x9b,0x92,0x8f,0xe2
,0x27,0x0d,0x6f,0xb8,0x63,0xd5,0x17,0x38
,0xb4,0x8e,0xee,0xe3,0x14,0xa7,0xcc,0x8a
,0xb9,0x32,0x16,0x45,0x48,0xe5,0x26,0xae
,0x90,0x22,0x43,0x68,0x51,0x7a,0xcf,0xea
,0xbd,0x6b,0xb3,0x73,0x2b,0xc0,0xe9,0xda
,0x99,0x83,0x2b,0x61,0xca,0x01,0xb6,0xde
,0x56,0x24,0x4a,0x9e,0x88,0xd5,0xf9,0xb3
,0x79,0x73,0xf6,0x22,0xa4,0x3d,0x14,0xa6
,0x59,0x9b,0x1f,0x65,0x4c,0xb4,0x5a,0x74
,0xe3,0x55,0xa5
// 0x34 ,0x7F ,0x4D ,0x3E ,0xB0 ,0x62 ,0x6F ,0x40
// ,0x87 ,0x26 ,0x33 ,0x13 ,0x89 ,0x66 ,0x4E ,0xCC
// ,0x81 ,0x50 ,0x23 ,0x3D ,0xD7 ,0x2C ,0x1E ,0xDD
// ,0x8D ,0xDA ,0xAF ,0xEC ,0xA0 ,0xDC ,0x3 ,0x3F
// ,0xD6 ,0x3F ,0x7B ,0x6D ,0xF2 ,0xE6 ,0xF9 ,0x19
// ,0xC ,0x3B ,0x65 ,0x10 ,0xB7 ,0x1C ,0x7F ,0xA4
// ,0x77 ,0x9B ,0x2A ,0xAC ,0xED ,0x34 ,0x8B ,0x2E
// ,0xC ,0x9 ,0xED ,0xB0 ,0xE5 ,0x10 ,0x77 ,0x66
// ,0x12 ,0x68 ,0xBA ,0x54 ,0x57 ,0xD7 ,0x5 ,0xE5
// ,0x98 ,0x6 ,0x7 ,0x20 ,0x0 ,0x0 ,0x0 ,0x0
// ,0x8 ,0x0 ,0x0 ,0x0 ,0x10 ,0x0 ,0x0 ,0x0
// ,0x18 ,0x0 ,0x0 ,0x0 ,0x40 ,0x0 ,0x0 ,0x0
// ,0x50 ,0x0 ,0x0 ,0x0 ,0x60 ,0x0 ,0x0 ,0x0
// ,0x70 ,0x0 ,0x0 ,0x0 ,0x0 ,0x2 ,0x0 ,0x0
// ,0x40 ,0x2 ,0x0 ,0x0 ,0x80 ,0x2 ,0x0 ,0x0
// ,0xC0 ,0x2 ,0x0 ,0x0 ,0x10 ,0xC ,0x7 ,0x20
// ,0xCB ,0x8 ,0x8 ,0x0 ,0x10 ,0xC ,0x7 ,0x20
// ,0xF9 ,0xFF ,0xFF ,0xFF ,0x0 ,0x0 ,0x0 ,0x0
// ,0x0 ,0x0 ,0x0 ,0x0 ,0x6C ,0x9 ,0x7 ,0x20
// ,0x10 ,0xC ,0x7 ,0x20 ,0x0 ,0x5A ,0x62 ,0x2
// ,0x9D ,0x13 ,0x8
} ;

unsigned char m[163];

 ret = tuit.crypto_box_open(m,c,163,nonce,alicepk,bobsk);
 Serial.println(ret);
 
  for (i = 0;i < 163;++i) {
      Serial.print(",0x");
      Serial.print(m[i],HEX);
      if (i % 8 == 7) {
        Serial.println();
      }
  }

}

void loop() {
  // should produce 
//   0,   0,   0,   0,   0,   0,   0,   0
//,   0,   0,   0,   0,   0,   0,   0,   0
//,   0,   0,   0,   0,   0,   0,   0,   0
//,   0,   0,   0,   0,   0,   0,   0,   0
//,0xbe,0x07,0x5f,0xc5,0x3c,0x81,0xf2,0xd5
//,0xcf,0x14,0x13,0x16,0xeb,0xeb,0x0c,0x7b
//,0x52,0x28,0xc5,0x2a,0x4c,0x62,0xcb,0xd4
//,0x4b,0x66,0x84,0x9b,0x64,0x24,0x4f,0xfc
//,0xe5,0xec,0xba,0xaf,0x33,0xbd,0x75,0x1a
//,0x1a,0xc7,0x28,0xd4,0x5e,0x6c,0x61,0x29
//,0x6c,0xdc,0x3c,0x01,0x23,0x35,0x61,0xf4
//,0x1d,0xb6,0x6c,0xce,0x31,0x4a,0xdb,0x31
//,0x0e,0x3b,0xe8,0x25,0x0c,0x46,0xf0,0x6d
//,0xce,0xea,0x3a,0x7f,0xa1,0x34,0x80,0x57
//,0xe2,0xf6,0x55,0x6a,0xd6,0xb1,0x31,0x8a
//,0x02,0x4a,0x83,0x8f,0x21,0xaf,0x1f,0xde
//,0x04,0x89,0x77,0xeb,0x48,0xf5,0x9f,0xfd
//,0x49,0x24,0xca,0x1c,0x60,0x90,0x2e,0x52
//,0xf0,0xa0,0x89,0xbc,0x76,0x89,0x70,0x40
//,0xe0,0x82,0xf9,0x37,0x76,0x38,0x48,0x64
//,0x5e,0x07,0x05

//but outputs
//                                                                                                                                                                                                                                                                                                                                                        148
//,0x34,0x7F,0x4D,0x3E,0xB0,0x62,0x67,0x40
//,0xA7,0x26,0x33,0x13,0x89,0x66,0x46,0xCC
//,0x85,0x50,0x23,0xBD,0xD7,0x68,0x1E,0xDD
//,0x8D,0xDA,0xAF,0xC4,0x80,0xDC,0x23,0x3F
//,0xD6,0xF,0x7B,0x6D,0xF2,0xE6,0x99,0x19
//,0xC,0x3F,0x65,0x10,0xB7,0xC,0x7B,0xA4
//,0x77,0x93,0x2A,0xA8,0xED,0x76,0x8B,0x2E
//,0xC,0x9,0xED,0xB0,0xE7,0x10,0x77,0x66
//,0x12,0x28,0xBA,0x54,0x57,0xD7,0x5,0xE5
//,0x98,0x6,0x7,0x20,0x0,0x0,0x0,0x0
//,0x8,0x0,0x0,0x0,0x10,0x0,0x0,0x0
//,0x18,0x0,0x0,0x0,0x40,0x0,0x0,0x0
//,0x50,0x0,0x0,0x0,0x60,0x0,0x0,0x0
//,0x70,0x0,0x0,0x0,0x0,0x2,0x0,0x0
//,0x40,0x2,0x0,0x0,0x80,0x2,0x0,0x0
//,0xC0,0x2,0x0,0x0,0x10,0xC,0x7,0x20
//,0xB7,0x8,0x8,0x0,0x10,0xC,0x7,0x20
//,0xF9,0xFF,0xFF,0xFF,0x0,0x0,0x0,0x0
//,0x0,0x0,0x0,0x0,0x6C,0x9,0x7,0x20
//,0x10,0xC,0x7,0x20,0x0,0x5A,0x62,0x2
//,0x8D,0x13,0x8


//trying output from encrypt
//,0x34,0x7F,0x4D,0x3F,0xB0,0x62,0x6F,0x44
//,0xA7,0x26,0x33,0x13,0x89,0x66,0x46,0xCC
//,0x85,0x50,0x23,0x3D,0xD7,0x68,0x1E,0xDD
//,0x8D,0xDA,0xAF,0xEC,0xA0,0xDC,0x3,0x3F
//,0xD6,0x9F,0x7B,0x6D,0xF2,0xE6,0xB9,0x19
//,0xC,0x3F,0x65,0x10,0xB7,0x1C,0x7B,0xA4
//,0x77,0xBB,0x2A,0xAC,0xED,0x76,0x8B,0x2E
//,0xC,0x9,0xED,0xB0,0xE7,0x10,0x77,0x66
//,0x12,0x68,0xBA,0x54,0x57,0xD6,0x5,0xE5
//,0x98,0x6,0x7,0x20,0x0,0x0,0x0,0x0
//,0x8,0x0,0x0,0x0,0x10,0x0,0x0,0x0
//,0x18,0x0,0x0,0x0,0x40,0x0,0x0,0x0
//,0x50,0x0,0x0,0x0,0x60,0x0,0x0,0x0
//,0x70,0x0,0x0,0x0,0x0,0x2,0x0,0x0
//,0x40,0x2,0x0,0x0,0x80,0x2,0x0,0x0
//,0xC0,0x2,0x0,0x0,0x10,0xC,0x7,0x20
//,0xCB,0x8,0x8,0x0,0x10,0xC,0x7,0x20
//,0xF9,0xFF,0xFF,0xFF,0x1,0x0,0x0,0x0
//,0x0,0x0,0x0,0x0,0x6C,0x9,0x7,0x20
//,0x10,0xC,0x7,0x20,0x0,0x5A,0x62,0x2
//,0xA1,0x13,0x8
}
