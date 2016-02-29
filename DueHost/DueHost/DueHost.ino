#include <SPI.h>
#include <Ethernet2.h>

// network configuration.  
// gateway and subnet are optional.

 // the media access control (ethernet hardware) address for the shield:
byte mac[] = { 0x90, 0xA2, 0xDA, 0x10, 0x2D,0xD6 };  

//the IP address for the shield:
byte ip[] = { 10, 0, 0, 177 };    

// the router's gateway address:
byte gateway[] = { 10, 0, 0, 1 };

// the subnet:
byte subnet[] = { 255, 255, 0, 0 };

// telnet defaults to port 23
EthernetServer server = EthernetServer(23);

void setup()
{
  // initialize the ethernet device
  Ethernet.begin(mac, ip, gateway, subnet);

  // start listening for clients
  server.begin();
}

void loop()
{
  // if an incoming client connects, there will be bytes available to read:
  char incoming[100];
  EthernetClient client = server.available();
  if (client == true) {
    // read bytes from the incoming client and write them back
    // to any clients connected to the server:
    int ii = 0;
    while ((c = client.read()) != '\n') 
    {
      incoming[ii++] = c;
    }
    // the variable incoming[] now contains the most recent value sent
    // so you can do something with it  
  }
}
