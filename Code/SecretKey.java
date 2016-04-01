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
	
 	public String getServerPublicKey(){
 		String key="";
 		try{
 		ServerPublicKey = new byte[TweetNaCl.BOX_PUBLIC_KEY_BYTES];
     	ServerSecretKey = new byte[TweetNaCl.BOX_SECRET_KEY_BYTES];
    	TweetNaCl.crypto_box_keypair(ServerPublicKey, ServerSecretKey, false);
    	
    	DecryptTemp.keyCollection[keySetNumber] = ServerSecretKey; //place secret key in storage 
    	keySetNumber++;
    	
 		key = DecryptTemp.bytesToHex(ServerPublicKey); //convert into string
 		
 		}catch(Exception e){
 			e.printStackTrace(pw);
 		}
 		return key;
 	}
	
 	private byte[] getRandomNonce() {
		byte[] randomNonce = new byte[24];
	    prng.nextBytes(randomNonce);	//get randomNonce
	    DecryptTemp.nonceCollection[nonceSetNumber] = randomNonce; //place nonce in storage
	    nonceSetNumber++;
        return randomNonce;
    }
 	
 	
 	private byte[] getSignedNonce(byte[] nonce){
 		int nlen = nonce.length;
 		int snlen=nlen+TweetNaCl.SIGNATURE_SIZE_BYTES;
 		byte[] sntemp = new byte[snlen];
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
		}else{
			//can use previous keys and nonce to encrypt next nonce
			byte[] nonce = DecryptTemp.nonceCollection[keySetNumber-2];
			byte[] secretKey = DecryptTemp.keyCollection[keySetNumber-2];
			suc_encrypt = TweetNaCl.crypto_box(sc, sn, 24+TweetNaCl.SIGNATURE_SIZE_BYTES+32 , nonce , arduinoFirstpkArray, secretKey);
		}
 		String scnonce = DecryptTemp.bytesToHex(sc);
 		return scnonce;
 	}
 	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		pw = response.getWriter();
        try{
        	pw.println("<"+getServerPublicKey()+">");
        	pw.println("("+getEncryptedNonce(getSignedNonce(getRandomNonce()))+")");
        	}catch(Exception e){
				e.printStackTrace(pw);
			}

		}
}
