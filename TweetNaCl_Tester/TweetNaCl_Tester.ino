#include <tweetnacl.h>


//m - message
//sk - secret key
//pk - public key
//n - nonce
//c - ciphertext
void setup() {
  byte PublicKey[]  = {crypto_box_PUBLICKEYBYTES};
  
  Serial.begin(9600);
  Serial.println("start");
  
  //how do we get the two secret keys?
  
  unsigned char pk[crypto_box_PUBLICKEYBYTES];
  unsigned char sk[crypto_box_SECRETKEYBYTES];
  //int v = crypto_box_keypair(pk,sk);
  int v = crypto_box_curve25519xsalsa20poly1305_tweet_keypair(pk,sk);
  Serial.println(pk[12]);

  
  if(v==0){
    Serial.println("keypair_success");
  }else if(v==-1){
     Serial.println("keypair_failure");
  }else{
      Serial.println("keypair_undefined");
  }
   Serial.println(v);
   //for(int i=0;i<crypto_box_PUBLICKEYBYTES;i++){
   // Serial.print(pk[i]); 
   //}
   //Serial.println();
   //for(int j=0;j<crypto_box_SECRETKEYBYTES;j++){
   // Serial.print(sk[j]); 
   //}
  
  //there was a const at start of next two lines
  const unsigned char n[crypto_box_NONCEBYTES]={}; //need to make this all random? i believe so, refer to py site
  const unsigned char m[crypto_box_ZEROBYTES+14] = {"0000000000000000000000000000000SecretMessage"}; //??
  unsigned long long int mlen = crypto_box_ZEROBYTES+14;
  unsigned char c[50] = {};
  
                    //unsigned char*, const unsigned char*, long long unsigned int, const unsigned char*, const unsigned char*, const unsigned char*
  int b = crypto_box (      c,                  m,                  mlen,                    n,                    pk,                      sk); 
  if(b== 0){
   Serial.print("success"); 
   //for(int i=0;i<50;i++){
   //    Serial.print(c[i]); //uncomment this for instant success
   //}
  }else if(b==-1){
     Serial.println("crypto_failure");
  }else{
      Serial.println("crypto_undefined");
  }
 Serial.println(b);
  //crypto_secretbox_xsalsa20poly1305_tweet()  //is really cryptobox
  
  unsigned char m1[crypto_box_ZEROBYTES+14] = {};
  unsigned long long int clen = crypto_box_ZEROBYTES+14;
  const unsigned char c1[50] = {};
  //strcpy(c1, c);
//c1 = reinterpret_cast<char*>(c);
                        //unsigned char*, const unsigned char*, long long unsigned int, const unsigned char*, const unsigned char*, const unsigned char*)
  int bo = crypto_box_open(m1,c,clen,n,pk,sk);
  Serial.println(bo);
  
  int k;
  //for (k = 0; k < crypto_box_ZEROBYTES+14; k = k + 1) {
  //  Serial.print(m1[k]);
  //}
  //Serial.println(m1);
  Serial.println(m1[36]);
  
}

void loop() {
  // put your main code here, to run repeatedly:

}
