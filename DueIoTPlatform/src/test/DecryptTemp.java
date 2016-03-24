package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tweetNaCl.TweetNaCl;
import updateKey.SecretKey;

/**
 * Servlet implementation class DecryptTemp
 */
@WebServlet("/DecryptTemp")
public class DecryptTemp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Random prng = new Random(0);
	SecretKey sk = new SecretKey();
     
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DecryptTemp() {
        super();
        // TODO Auto-generated constructor stub
    }
    
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/html");
        PrintWriter printWriter  = response.getWriter();
        printWriter.println("<html>");
        printWriter.println("<head>");
        printWriter.println("<title>Sensor Data</title>");
        printWriter.println("</head>");
        printWriter.println("<body>");
        printWriter.println("<h1>Temperature Sensor Readings</h1>");
        printWriter.println("<table border=1 cellspacing=1 cellpadding=1>");  //work with ' instead of "?
        printWriter.println("<tr>");
        printWriter.println("<td>&nbsp;ID&nbsp;</td>");
        printWriter.println("<td>&nbsp;Timestamp&nbsp;</td>");
        printWriter.println("<td>&nbsp;Temperature (Hexadecimal)&nbsp;</td>");
        printWriter.println("<td>&nbsp;Temperature (Integer)&nbsp;</td>");
        printWriter.println("</tr>");
        
    
        
        /*straight up box2.c test*/
         
         
         printWriter.println(sk.getServerPublicKey());
        
        
        // API requires first 16 bytes to be 0
//         byte[] cipherArray = {
//        		    0,   0,   0,   0,   0,   0,   0,   0
//        		,   0,   0,   0,   0,   0,   0,   0,   0
//        		,(byte) 0xf3,(byte) 0xff,(byte) 0xc7,0x70,0x3f,(byte) 0x94,0x00,(byte) 0xe5
//        		,0x2a,0x7d,(byte) 0xfb,0x4b,0x3d,0x33,0x05,(byte) 0xd9
//        		,(byte) 0x8e,(byte) 0x99,0x3b,(byte) 0x9f,0x48,0x68,0x12,0x73
//        		,(byte) 0xc2,(byte) 0x96,0x50,(byte) 0xba,0x32,(byte) 0xfc,0x76,(byte) 0xce
//        		,0x48,0x33,0x2e,(byte) 0xa7,0x16,0x4d,(byte) 0x96,(byte) 0xa4
//        		,0x47,0x6f,(byte) 0xb8,(byte) 0xc5,0x31,(byte) 0xa1,0x18,0x6a
//        		,(byte) 0xc0,(byte) 0xdf,(byte) 0xc1,0x7c,(byte) 0x98,(byte) 0xdc,(byte) 0xe8,0x7b
//        		,0x4d,(byte) 0xa7,(byte) 0xf0,0x11,(byte) 0xec,0x48,(byte) 0xc9,0x72
//        		,0x71,(byte) 0xd2,(byte) 0xc2,0x0f,(byte) 0x9b,(byte) 0x92,(byte) 0x8f,(byte) 0xe2
//        		,0x27,0x0d,0x6f,(byte) 0xb8,0x63,(byte) 0xd5,0x17,0x38
//        		,(byte) 0xb4,(byte) 0x8e,(byte) 0xee,(byte) 0xe3,0x14,(byte) 0xa7,(byte) 0xcc,(byte) 0x8a
//        		,(byte) 0xb9,0x32,0x16,0x45,0x48,(byte) 0xe5,0x26,(byte) 0xae
//        		,(byte) 0x90,0x22,0x43,0x68,0x51,0x7a,(byte) 0xcf,(byte) 0xea
//        		,(byte) 0xbd,0x6b,(byte) 0xb3,0x73,0x2b,(byte) 0xc0,(byte) 0xe9,(byte) 0xda
//        		,(byte) 0x99,(byte) 0x83,0x2b,0x61,(byte) 0xca,0x01,(byte) 0xb6,(byte) 0xde
//        		,0x56,0x24,0x4a,(byte) 0x9e,(byte) 0x88,(byte) 0xd5,(byte) 0xf9,(byte) 0xb3
//        		,0x79,0x73,(byte) 0xf6,0x22,(byte) 0xa4,0x3d,0x14,(byte) 0xa6
//        		,0x59,(byte) 0x9b,0x1f,0x65,0x4c,(byte) 0xb4,0x5a,0x74
//        		,(byte) 0xe3,0x55,(byte) 0xa5
//        		} ;

//         
//
//         byte[] realCipher = hexStringToByteArray(cipherString);
//         printWriter.println("cipher length "+realCipher.length);
//         printWriter.println("cipher  ");
//         for(int i=0;i<realCipher.length;i++){
//        	 printWriter.println(String.format(",0x%02x ", realCipher[i]));
//         }
//         
//         long messageLength = realCipher.length;
//         byte[] message = new byte[(int) messageLength];
//         int ret=20;
//         try{
//        	  ret = TweetNaCl.crypto_box_open(message, realCipher, messageLength, nonceArray, alicepkArray, bobskArray);
//         }catch(Exception e){
//        	 e.printStackTrace(printWriter);
//         }
//         printWriter.println("suc ="+ret);
//         printWriter.println("decrypted message ");
//         for(int i=0;i<message.length;i++){
//        	 printWriter.println(String.format(",0x%02x ", message[i]));
//         }
         
         /*message signing*/
         printWriter.println("nerw tysadssa");
//         byte[] publicSigningKey = {(byte) 0xb6,(byte) 0xa6,(byte) 0x83,(byte) 0x9a,0x41,0x03,0x25,(byte) 0xb1
//        		 ,(byte) 0xc8,0x04,0x33,(byte) 0xc1,0x19,0x50,(byte) 0xdd,0x26
//        		 ,(byte) 0x96,0x62,(byte) 0xb1,0x67,(byte) 0xe9,0x0b,(byte) 0xa5,0x46
//        		 ,(byte) 0xe7,0x48,0x5f,(byte) 0xa9,0x15,0x10,0x2e,0x51};
         //new byte[32];
//         byte[] secretSigningKey = {
//        		 0x74,(byte) 0xc5,0x5a,(byte) 0xcc,(byte) 0x8b,(byte) 0xa0,(byte) 0x8f,0x01,
//        		 (byte) 0xec,0x2b,(byte) 0xed,0x47,0x4f,0x64,0x15,0x5a,
//        		 0x4c,(byte) 0xb3,0x7a,0x61,0x71,0x66,(byte) 0xf5,0x58,
//        		 (byte) 0xb4,(byte) 0x83,(byte) 0xc7,0x29,0x47,0x68,(byte) 0xc6,0x40,
//        		  (byte) 0xb6,(byte) 0xa6,(byte) 0x83,(byte) 0x9a,0x41,0x03,0x25,(byte) 0xb1
//        		 ,(byte) 0xc8,0x04,0x33,(byte) 0xc1,0x19,0x50,(byte) 0xdd,0x26
//        		 ,(byte) 0x96,0x62,(byte) 0xb1,0x67,(byte) 0xe9,0x0b,(byte) 0xa5,0x46
//        		 ,(byte) 0xe7,0x48,0x5f,(byte) 0xa9,0x15,0x10,0x2e,0x51};
//        		 //new byte[64];
//         
//         byte[] message1 = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//        	        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//        	        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,    
//        	        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//        	        (byte) 0xde, 0x61, 0x18, 0x0c, 0x6f, 0x72, (byte) 0xb9, 0x66,
//        	        0x2a};//new byte[41];
//         
//         //int signedLength = message1.length + TweetNaCl.SIGNATURE_SIZE_BYTES;
//         
//         boolean isSeeded = false;
//         TweetNaCl.crypto_sign_keypair(publicSigningKey, secretSigningKey, isSeeded);
//         
//         //prng.nextBytes(message1);
//         
//         printWriter.println("Message Length   "+message1.length);
//         for(int i=0;i<message1.length;i++){
//        	 printWriter.println(String.format(",0x%02x ", message1[i]));
//         }
//         
//         byte[] signedMessage = TweetNaCl.crypto_sign(message1, secretSigningKey);
//         
//         printWriter.println("SM Length   "+signedMessage.length);
//         for(int i=0;i<signedMessage.length;i++){
//        	 printWriter.println(String.format(",0x%02x ", signedMessage[i]));
//         }
//         
//         
//         byte[] javaMessage = TweetNaCl.crypto_sign_open(signedMessage, publicSigningKey);
//         
//         printWriter.println("javaMessage   "+javaMessage.length);
//         for(int i=0;i<javaMessage.length;i++){
//        	 printWriter.println(String.format(",0x%02x ", javaMessage[i]));
//         }
         
         String signedCipher = "000000000000000034aec97a965682e72e7a9246d99e67a3cf1a58917b8412a8cf07623798751c9e910479d92019f96134e05c887df54d9225337bd3ab619d615760d8c5b224a85b1d0efe0eb8a7ee163abb0376529fcc0990b54d80e71e32f7f3";
         signedCipher="0000000000000000"+signedCipher;
         byte[] signedCipherArray = hexStringToByteArray(signedCipher);
         
         
         byte[] javaMessage = TweetNaCl.crypto_sign_open(signedCipherArray, SecretKey.publicFirstSigningKey);
         
         printWriter.println("javaMessage   "+javaMessage.length);
         for(int i=0;i<javaMessage.length;i++){
        	 printWriter.println(String.format(",0x%02x ", javaMessage[i]));
         }
         
          //JDBC driver name and database URL
	      //final String JDBCDRIVER="com.mysql.jdbc.Driver";  
	      final String DB_URL="jdbc:mysql://localhost/iotplatform";
	      
	      /**
	         * 3306 is the default port for MySQL in XAMPP. Note both the 
	         * MySQL server and Apache must be running. 
	         */
	        //String url = "jdbc:mysql://localhost:8080/";
	        String user = "root";
	        
	        /**
	         * Password for the above MySQL user. If no password has been 
	         * set (as is the default for the root user in XAMPP's MySQL),
	         * an empty string can be used.
	         */
	        String password = "";
	        
	     // Set response content type
	        //response.setContentType("text/html");
	        //PrintWriter out = response.getWriter();
	      /*  String title = "Database Result";
	        String docType =
	          "<!doctype html public \"-//w3c//dtd html 4.0 " +
	           "transitional//en\">\n";
	           out.println(docType +
	           "<html>\n" +
	           "<head><title>" + title + "</title></head>\n" +
	           "<body bgcolor=\"#f0f0f0\">\n" +
	           "<h1 align=\"center\">" + title + "</h1>\n");*/
	        printWriter.println("before try catch     ");
	        try
	        {
	         // Register JDBC driver
	            Class.forName("com.mysql.jdbc.Driver").newInstance();

	            // Open a connection
	            Connection conn = DriverManager.getConnection(DB_URL, user, password);

	            // Execute SQL query
	            Statement stmt = conn.createStatement();
	            String sql;
	            sql = "SELECT id, timeStamp, tempHex FROM tempLog";
	            ResultSet rs = stmt.executeQuery(sql);
	            printWriter.println("Starting to get data from SQL      ");
	            // Extract data from result set
	            while(rs.next()){
	               //Retrieve by column name
	               int id  = rs.getInt("id");
	               //int tempInt = rs.getInt("tempInt");
	               String tempHex = rs.getString("tempHex");
	               Timestamp timeStamp = rs.getTimestamp("timeStamp");
	               	
	               //String cipherString = "0000000000000000"
//      		 			 + "0000000000000000"
//      		 			 + "d915640e14670da6"
//      		 			 + "0e39944c2d46285f"
//      		 			 + "1a9e2f1c8b16e9b6"
//      		 			 + "e7";
//	            		   "0000000000000000"
//	            		 + "5622d363147820f2"
//	            		 + "6eaffa3ed20bf423"
//	            		 + "1a9e2f1c8b16eab6"
//	            		 + "b2"  
	            		   
	               tempHex="0000000000000000"+tempHex;
	               //printWriter.println("StringCipher "+tempHex);
	               //printWriter.println(" ");
	               
	               //printWriter.println("tempHexLength "+ tempHex.length());
	               
	               
	               byte[] cipher = hexStringToByteArray(tempHex);
	               long messageLength = cipher.length;
	               byte[] message = new byte[(int) messageLength];
	               int ret=20;
	               try{
	              	  ret = TweetNaCl.crypto_box_open(message, cipher, messageLength, SecretKey.nonceFirstArray, SecretKey.arduinoFirstpkArray, SecretKey.serverFirstskArray);
	               }catch(Exception e){
	              	 e.printStackTrace(printWriter);
	               }
	               
	            // Convert the data to actual temperature
	               // because the result is a 16 bit signed integer, it should
	               // be stored to an "int16_t" type, which is always 16 bits
	               // even when compiled on a 32 bit processor.
	               float celsius=0;
	               try{
	               byte[] data = new byte[9];
	               printWriter.println("temp hex ");
	               for(int i=32;i<message.length;i++){
	              	 data[i-32]=message[i];
	              	 printWriter.println(String.format(",0x%02x ", data[i-32]));
	               }
	               boolean type_s = true; //for DS18S20
	               int raw = (data[1] << 8) | data[0];
	               if (type_s) {
	                 raw = raw << 3; // 9 bit resolution default
	                 if (data[7] == 0x10) {
	                   // "count remain" gives full 12 bit resolution
	                   raw = (raw & 0xFFF0) + 12 - data[6];
	                 }
	               } else {
	                 byte cfg = (byte) (data[4] & 0x60);
	                 // at lower res, the low bits are undefined, so let's zero them
	                 if (cfg == 0x00) raw = raw & ~7;  // 9 bit resolution, 93.75 ms
	                 else if (cfg == 0x20) raw = raw & ~3; // 10 bit res, 187.5 ms
	                 else if (cfg == 0x40) raw = raw & ~1; // 11 bit res, 375 ms
	                 //// default is 12 bit resolution, 750 ms conversion time
	               }
	               celsius =  (float) (raw / 16.0);
	               
	              	 printWriter.println("temp: "+(int)celsius);
	               }catch(Exception e){
	              	 e.printStackTrace(printWriter);
	               }
	               
	               //Display values
	               printWriter.println("<tr><td>"+id+"</td>");
	               printWriter.println("<td>"+timeStamp+"</td>");
	               printWriter.println("<td>"+tempHex+"</td>");
	               printWriter.println("<td>"+celsius+"</td></tr>");
	               
	              
	            }
	            printWriter.println("</body></html>");

	            // Clean-up environment
	            rs.close();
	            stmt.close();
	            conn.close();
	        }
	        catch(Exception e){
	        	e.printStackTrace(printWriter);
	        }
		
		
		
	}

	private static byte[] nonce() {
		byte[] nonce = new byte[24];
	    prng.nextBytes(nonce);
        return nonce;
    }
	    
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	
	
	
	public static String getHello(){
		return "Hello";
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
