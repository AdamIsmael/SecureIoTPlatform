#include <tweetnacl.h>

void setup() {
   Serial.begin(9600);
  Serial.println("start");
  
  //how do we get the two secret keys?
  
  unsigned char pk[crypto_box_PUBLICKEYBYTES];
  unsigned char sk[crypto_box_SECRETKEYBYTES];
  //int v = crypto_box_keypair(pk,sk);
  int v = crypto_box_curve25519xsalsa20poly1305_tweet_keypair(pk,sk);
  Serial.println(v);

}

void loop() {
  // put your main code here, to run repeatedly:

}
