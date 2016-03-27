package updateKey;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tweetNaCl.TweetNaCl;
import tempLog.DecryptTemp;


/**
 * Servlet implementation class SecretKey
 */
@WebServlet("/SecretKey")
public class SecretKey extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  	private static byte[] ServerPublicKey;
	 	private byte[] ServerSecretKey;
	 	private byte[] ArduinoPublicKey;
	 	private byte[] ArduinoSecretKey;
	 	private byte[] ArduinoSignPublicKey;
	 	private byte[] ArduinoSignSecretKey;
	 	private byte[] ServerSignPublicKey;
	 	private byte[] ServerSignSecretKey;
	 	public static int keySetNumber = 0;
	 	public static int nonceSetNumber = 0;
	 	
	 	private static Random prng = new Random(0);
	 	private PrintWriter pw = null;
	 	
	 	public int getKeySetNumber(){
	 		return keySetNumber;
	 	}
	 	
	 public static byte[] serverFirstskArray = {
    		0x5d,(byte) 0xab,0x08,0x7e,0x62,0x4a,(byte) 0x8a,0x4b
    		,0x79,(byte) 0xe1,0x7f,(byte) 0x8b,(byte) 0x83,(byte) 0x80,0x0e,(byte) 0xe6
    		,0x6f,0x3b,(byte) 0xb1,0x29,0x26,0x18,(byte) 0xb6,(byte) 0xfd
    		,0x1c,0x2f,(byte) 0x8b,0x27,(byte) 0xff,(byte) 0x88,(byte) 0xe0,(byte) 0xeb
    		};
     
	 public static byte[] arduinoFirstpkArray = {
    		 (byte) 0x85,0x20,(byte) 0xf0,0x09,(byte) 0x89,0x30,(byte) 0xa7,0x54
    		,0x74,(byte) 0x8b,0x7d,(byte) 0xdc,(byte) 0xb4,0x3e,(byte) 0xf7,0x5a
    		,0x0d,(byte) 0xbf,0x3a,0x0d,0x26,0x38,0x1a,(byte) 0xf4
    		,(byte) 0xeb,(byte) 0xa4,(byte) 0xa9,(byte) 0x8e,(byte) 0xaa,(byte) 0x9b,0x4e,0x6a
    		} ;
     
     public static byte[] nonceFirstArray = {
    		 0x69,0x69,0x6e,(byte) 0xe9,0x55,(byte) 0xb6,0x2b,0x73
    		,(byte) 0xcd,0x62,(byte) 0xbd,(byte) 0xa8,0x75,(byte) 0xfc,0x73,(byte) 0xd6
    		,(byte) 0x82,0x19,(byte) 0xe0,0x03,0x6b,0x7a,0x0b,0x37
    		} ;
     
     public static byte[] publicArduinoFirstSigningKey = {(byte) 0xb6,(byte) 0xa6,(byte) 0x83,(byte) 0x9a,0x41,0x03,0x25,(byte) 0xb1
    		 ,(byte) 0xc8,0x04,0x33,(byte) 0xc1,0x19,0x50,(byte) 0xdd,0x26
    		 ,(byte) 0x96,0x62,(byte) 0xb1,0x67,(byte) 0xe9,0x0b,(byte) 0xa5,0x46
    		 ,(byte) 0xe7,0x48,0x5f,(byte) 0xa9,0x15,0x10,0x2e,0x51};
	
     public static byte[] secretServerFirstSigningKey = {
    		 (byte) 0xfd,0x3c,0x13,(byte) 0xe0,0x1e,0x4b,0x79,0x06
    		 ,0x3a,(byte) 0xa1,(byte) 0xbe,(byte) 0xf6,(byte) 0xd0,0x3d,(byte) 0x80,(byte) 0xe3
    		 ,(byte) 0x8d,0x7c,0x3c,0x7d,(byte) 0xa2,0x76,0x65,(byte) 0xfe
    		 ,0x72,0x42,0x3f,0x5a,0x55,(byte) 0xdf,0x1f,(byte) 0xef
    		 ,(byte) 0xf2,(byte) 0xaf,(byte) 0xdb,0x38,0x2a,(byte) 0xcc,0x24,0x57
    		 ,(byte) 0x90,0x4e,(byte) 0xb8,0x77,(byte) 0xcb,0x24,0x72,0x1a
    		 ,(byte) 0xd2,(byte) 0xb2,0x2e,(byte) 0x8b,(byte) 0xe9,0x72,0x7c,(byte) 0xfb
    		 ,0x10,(byte) 0x93,(byte) 0x81,0x7e,0x5b,(byte) 0x83,0x35,(byte) 0xe0
     };
	
      byte[] serverpk 
    		    = { (byte) 0xde, (byte) 0x9e, (byte) 0xdb, 0x7d, 0x7b, 0x7d, (byte) 0xc1, (byte) 0xb4, 
    		        (byte) 0xd3, 0x5b, 0x61, (byte) 0xc2, (byte) 0xec, (byte) 0xe4, 0x35, 0x37, 
    		        0x3f, (byte) 0x83, 0x43, (byte) 0xc8, 0x5b, 0x78, 0x67, 0x4d, 
    		        (byte) 0xad, (byte) 0xfc, 0x7e, 0x14, 0x6f, (byte) 0x88, 0x2b, 0x4f };
     
     protected void getKeyPairs(){
     	ServerPublicKey = new byte[TweetNaCl.BOX_PUBLIC_KEY_BYTES];
     	ServerSecretKey = new byte[TweetNaCl.BOX_SECRET_KEY_BYTES];
    	TweetNaCl.crypto_box_keypair(ServerPublicKey, ServerSecretKey, false);
    	ArduinoPublicKey = new byte[TweetNaCl.BOX_PUBLIC_KEY_BYTES];
    	ArduinoSecretKey = new byte[TweetNaCl.BOX_SECRET_KEY_BYTES];
 	 	TweetNaCl.crypto_box_keypair(ArduinoPublicKey, ArduinoSecretKey, false);
 	 	ArduinoSignPublicKey = new byte[TweetNaCl.SIGN_PUBLIC_KEY_BYTES];
 	 	ArduinoSignSecretKey = new byte[TweetNaCl.SIGN_SECRET_KEY_BYTES];
 	 	TweetNaCl.crypto_sign_keypair(ArduinoSignPublicKey, ArduinoSignSecretKey, false);
 	 	ServerSignPublicKey = new byte[TweetNaCl.SIGN_PUBLIC_KEY_BYTES];
 	 	ServerSignSecretKey = new byte[TweetNaCl.SIGN_SECRET_KEY_BYTES];
 	 	TweetNaCl.crypto_sign_keypair(ServerSignPublicKey, ServerSignSecretKey, false);
     }
     
     protected void getNonce(){
     	
     }
   
 	public String getServerPublicKey(){
 		String key="";
 		try{
 		ServerPublicKey = new byte[TweetNaCl.BOX_PUBLIC_KEY_BYTES];
     	ServerSecretKey = new byte[TweetNaCl.BOX_SECRET_KEY_BYTES];
    	TweetNaCl.crypto_box_keypair(ServerPublicKey, ServerSecretKey, false);
    	
    	DecryptTemp.keyCollection[keySetNumber] = ServerSecretKey; //only storing the secret keys as we are only decrypting messages.   
    	keySetNumber++;
    	
    	pw.println();
    	pw.print("Java, ServerSecretKey used to decrypt");
	    for(int i=0;i<ServerSecretKey.length;i++){
	    	pw.print(String.format(",0x%02x ", ServerSecretKey[i]));
	    }
	    pw.println();
    	pw.print("Java, ServerPublicKey used to encrypt");
	    for(int i=0;i<ServerPublicKey.length;i++){
	    	pw.print(String.format(",0x%02x ", ServerPublicKey[i]));
	    }
    	
 		key = DecryptTemp.bytesToHex(ServerPublicKey);
 		
 		}catch(Exception e){
 			e.printStackTrace(pw);
 		}
 		
 		return key;
 	}
 	
// 	public String getServerPublicSignKey(){
// 		String key="";
// 		
// 		ServerSignPublicKey = new byte[TweetNaCl.SIGN_PUBLIC_KEY_BYTES];
// 	 	ServerSignSecretKey = new byte[TweetNaCl.SIGN_SECRET_KEY_BYTES];
// 	 	TweetNaCl.crypto_sign_keypair(ServerSignPublicKey, ServerSignSecretKey, false);
// 	 	DecryptTemp.keySignCollection[keySetNumber] = ServerSignPublicKey;
// 	 	key = DecryptTemp.bytesToHex(ServerSignPublicKey);
// 		
// 	 	return key;
// 	}
 	
 	private byte[] getRandomNonce() {
		byte[] randomNonce = new byte[24];
	    prng.nextBytes(randomNonce);
	    DecryptTemp.nonceCollection[nonceSetNumber] = randomNonce;
	    nonceSetNumber++;
	    pw.println();
	    pw.print("Java, Nonce used");
	    System.out.println("hello");
	    for(int i=0;i<randomNonce.length;i++){
	    	pw.print(String.format(",0x%02x ", randomNonce[i]));
	    }
	    //String nonce = DecryptTemp.bytesToHex(randomNonce);
        return randomNonce;
    }
 	
 	
 	private byte[] getSignedNonce(byte[] nonce){
 		
 		int nlen = nonce.length;
 		int snlen=nlen+TweetNaCl.SIGNATURE_SIZE_BYTES;
 		byte[] sntemp = new byte[snlen];
 									//the message we are sending
 		sntemp = TweetNaCl.crypto_sign(nonce, secretServerFirstSigningKey);
 		 		
 				
 		//put on the extra zeros
 		int snlenZeros = nlen+TweetNaCl.SIGNATURE_SIZE_BYTES+32;
 		byte[] sn = new byte[snlenZeros];
 		for(int i=0;i<32;i++){
 		    sn[i]=0;
 		  }
 		 for(int i=0;i<snlen;i++){
 		     sn[32+i]=sntemp[i]; 
 		  }
 		return sn;
 		
 	}
 	
 	private String getEncryptedNonce(byte[] sn){
 		int suc_encrypt = 20;
 		byte[] sc = new byte[24+TweetNaCl.SIGNATURE_SIZE_BYTES+32];
 		
		if(keySetNumber==1){ // new public key has just been sent but we need to use preinstalled keys to encrypt nonce
			suc_encrypt = TweetNaCl.crypto_box(sc, sn, 24+TweetNaCl.SIGNATURE_SIZE_BYTES+32 , nonceFirstArray, arduinoFirstpkArray, serverFirstskArray);
			pw.println();
		    pw.print("Java, Nonce used to encrypt the next nonce  ");
		    for(int i=0;i<nonceFirstArray.length;i++){
		    	pw.print(String.format(",0x%02x ", nonceFirstArray[i]));
		    }
		    pw.println();
		    pw.print("Java, Server secret key used to encrypt the next nonce  ");
		    for(int i=0;i<serverFirstskArray.length;i++){
		    	pw.print(String.format(",0x%02x ", serverFirstskArray[i]));
		    }
		}else{
			suc_encrypt = TweetNaCl.crypto_box(sc, sn, 24+TweetNaCl.SIGNATURE_SIZE_BYTES+32 , DecryptTemp.nonceCollection[keySetNumber-1], arduinoFirstpkArray, DecryptTemp.keyCollection[keySetNumber-1]);
			pw.println();
		    pw.print("Java, Nonce used to encrypt the next nonce  ");
		    for(int i=0;i< DecryptTemp.nonceCollection[keySetNumber-1].length;i++){
		    	pw.print(String.format(",0x%02x ",  DecryptTemp.nonceCollection[keySetNumber-1][i]));
		    }
		    pw.println();
		    pw.print("Java, Server secret key used to encrypt the next nonce  ");
		    for(int i=0;i<DecryptTemp.keyCollection[keySetNumber-1].length;i++){
		    	pw.print(String.format(",0x%02x ", DecryptTemp.keyCollection[keySetNumber-1][i]));
		    }
		}
 		
 		String scnonce = DecryptTemp.bytesToHex(sc);
 		return scnonce;
 	}
 	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		//getKeyPairs(); 
		
		response.setContentType("text/html");
        //PrintWriter printWriter = response.getWriter();
		pw = response.getWriter();
        //printWriter.println("hello");
        try{

        //printWriter.println("<"+getServerPublicSignKey(printWriter)+">");
        	pw.println("<"+getServerPublicKey()+">");
        	pw.println("KeySetNumber:"+keySetNumber);
        	pw.println("("+getEncryptedNonce(getSignedNonce(getRandomNonce()))+")");
        
        }catch(Exception e){
        	e.printStackTrace(pw);
        }
//        byte[] temp = null;
//        try{
//        	temp = DecryptTemp.keyCollection[1];// 0 has the preinstalled key
//        	printWriter.println(" keycollection size"+temp.length);
//        String temp1="";
// 		for(int i=0;i<temp.length;i++){
// 			temp1 = temp1 + temp[i];
// 		}
//        printWriter.println("key in byte form "+temp1);
//        } catch(Exception e){
//        	e.printStackTrace(printWriter);
//        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
