package updateKey;

import java.io.IOException;
import java.io.PrintWriter;

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
     
     public static byte[] publicFirstSigningKey = {(byte) 0xb6,(byte) 0xa6,(byte) 0x83,(byte) 0x9a,0x41,0x03,0x25,(byte) 0xb1
    		 ,(byte) 0xc8,0x04,0x33,(byte) 0xc1,0x19,0x50,(byte) 0xdd,0x26
    		 ,(byte) 0x96,0x62,(byte) 0xb1,0x67,(byte) 0xe9,0x0b,(byte) 0xa5,0x46
    		 ,(byte) 0xe7,0x48,0x5f,(byte) 0xa9,0x15,0x10,0x2e,0x51};
	
	
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
 		String ServerPublicKeyString="";
 		for(int i=0;i<ServerPublicKey.length;i++){
 			ServerPublicKeyString = ServerPublicKeyString + ServerPublicKey[i];
 		}
 		return ServerPublicKeyString;
 	}
 	public static void getArduinoSecretKey(){
 		
 	}
 	public void getArduinoSignSecretKey(){
 	
 	}
     
     
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SecretKey() {
        super();
        // TODO Auto-generated constructor stub
        
        /*Arduino calls this method when it wants to transmit asking what keys if can use
         * and these keys are encrypted and signed using the predefined keys
         */
        /*
         * If getting the kay pairs fails 
         */
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		getKeyPairs(); 
		
		response.setContentType("text/html");
        PrintWriter printWriter  = response.getWriter();
        printWriter.println("hello");
        try{
        printWriter.println("<"+getServerPublicKey()+">");
        }catch(Exception e){
        	printWriter.println(e);
        }
        
        
        //printWriter.println("<"+DecryptTemp.getHello()+">");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
