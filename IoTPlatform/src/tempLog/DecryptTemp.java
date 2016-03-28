package tempLog;

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
	
											//number of key arrays, size of those key arrays
	public static byte[][] keyCollection = new byte[50][];
	public static byte[][] nonceCollection = new byte[50][];
	SecretKey sk = new SecretKey();
    private int sqlentry;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DecryptTemp() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    //Does this work when not connnect to the internet?
    
   
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
               
        byte[]	server= {(byte) 0xc9 ,(byte) 0x89 ,0x31 ,(byte) 0xc5 ,0x4d ,(byte) 0xbe ,(byte) 0xa8 ,(byte) 0xf9 
        				,0x34 ,0x43 ,(byte) 0xa2 ,0x19 ,0x61 ,0x34 ,(byte) 0xb2 ,(byte) 0xae 
        				,0x76 ,0x28 ,0x3b ,(byte) 0xee ,(byte) 0xc9 ,0x61 ,(byte) 0xd1 ,(byte) 0xf9 
        				,(byte) 0xda ,(byte) 0x8f ,(byte) 0xb5 ,(byte) 0xd4 ,0x2a ,(byte) 0x8c ,(byte) 0x8f ,0x24 

        	};
        byte[] noncee = {0x60 ,(byte) 0xb4 ,0x20 ,(byte) 0xbb ,0x38 ,0x51 ,(byte) 0xd9 ,(byte) 0xd4 
        				,0x7a ,(byte) 0xcb ,(byte) 0x93 ,0x3d ,(byte) 0xbe ,0x70 ,0x39 ,(byte) 0x9b
        				,(byte) 0xf6 ,(byte) 0xc9 ,0x2d ,(byte) 0xa3 ,0x3a ,(byte) 0xf0 ,0x1d ,0x4f
};
//        
//        //First stored temp and an  
//        printWriter.println("Start");
//        String arduinoCipher = "0000000000000000c9e1dea0627516786fdbd7793a9e1aea74041542dc3d5ce840a1ca2aade2883a5182c145228d9fe6301979ddb0d5e4506f2ec35dc0398f4a8b575a2d05980907634d6a0f142a5c40cf9ede74afa10701bab506c618e13ce777d82c3ae9d1a6f972d4160287cbfe60bf2130fc0a6ff604b00a17cc7d0b283353";
//        arduinoCipher = "0000000000000000"+arduinoCipher;
//        byte[] signedcipherA = hexStringToByteArray(arduinoCipher);
//        long signedMessageLengthA = signedcipherA.length;
//        byte[] signedMessageA = new byte[(int) signedMessageLengthA];
//        int Suc_decryptA=20;
//        try{
//     	   Suc_decryptA = TweetNaCl.crypto_box_open(signedMessageA, signedcipherA, signedMessageLengthA, SecretKey.nonceFirstArray, SecretKey.arduinoFirstpkArray, SecretKey.serverFirstskArray);
//        
//        
//        printWriter.println("javaSignedMessageA   "+signedMessageA.length);
//        for(int i=0;i<signedMessageA.length;i++){
//       	 printWriter.println(String.format(",0x%02x ", signedMessageA[i]));
//        }
//        
//        }catch(Exception e){
//          	 e.printStackTrace(printWriter);
//           }
        
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
	            //printWriter.println("Starting to get data from SQL      ");
	            // Extract data from result set
	            sqlentry=0;
	            while(rs.next()){
	               //Retrieve by column name
	              
	               int id  = rs.getInt("id");
	               String tempHex = rs.getString("tempHex");
	               Timestamp timeStamp = rs.getTimestamp("timeStamp");
	            
	               tempHex="0000000000000000"+tempHex;
	               
	               byte[] signedcipher = hexStringToByteArray(tempHex);
	               long signedMessageLength = signedcipher.length;
	               byte[] signedMessage = new byte[(int) signedMessageLength];
	               
	               int Suc_decrypt = 20;
	               //int instkey = sk.getKeySetNumber();
	                //DecryptTempsKeySetNumber = sqlentry-1;
	               //printWriter.println("keySetNumberStatic Used: "+keySetNumber);
	               //printWriter.println("keySetNumberInst Used: "+instkey);
	               printWriter.println("java keysetnumber "+sqlentry);
	                
//	                if(DecryptTempsKeySetNumber==0){
//	            	   //It's the first transmission so use the first set of keys
//	            	   try{
//		            	   Suc_decrypt = TweetNaCl.crypto_box_open(signedMessage, signedcipher, signedMessageLength, SecretKey.nonceFirstArray, SecretKey.arduinoFirstpkArray, SecretKey.serverFirstskArray);
//		            	   printWriter.println("ServerPublicKey Used:   ");
//			               for(int i=0;i<SecretKey.serverFirstskArray.length;i++){
//			            	   printWriter.println(String.format(",0x%02x ", SecretKey.serverFirstskArray[i]));
//			               } 
//	            	   }catch(Exception e){
//		              	 e.printStackTrace(printWriter);
//		               }
//	               }else 
	               //if(DecryptTempsKeySetNumber>0){
	               
	               byte[]  ServerSecretKey = keyCollection[sqlentry];
        		   byte[]  nonce = nonceCollection[sqlentry];
        		   printWriter.println("Secret Key used ");
        		   printWriter.println(" ");
        		   for(int i=0;i<ServerSecretKey.length;i++){
	            	   printWriter.println(String.format(",0x%02x ", ServerSecretKey[i]));
	               } 
        		   printWriter.println(" ");
	               printWriter.println("Nonce Used:   ");
	               for(int i=0;i<nonce.length;i++){
	            	   printWriter.println(String.format(",0x%02x ", nonce[i]));
	               } 
        		   
	            	   try{			
	            		   
		            	   Suc_decrypt = TweetNaCl.crypto_box_open(signedMessage, signedcipher, signedMessageLength, nonce, SecretKey.arduinoFirstpkArray,ServerSecretKey);
		            	   printWriter.println("ServerSecretKey Used:   ");
			             
	            	   }catch(Exception e){
		              	 e.printStackTrace(printWriter);
		               }
	               //}else{
	            	//   printWriter.println("incorrect secret key");
	               //}
	               
	               printWriter.println("decryption success "+Suc_decrypt);
	               
//	               try{
//	            	   Suc_decrypt = TweetNaCl.crypto_box_open(signedMessage, signedcipher, signedMessageLength, SecretKey.nonceFirstArray, SecretKey.arduinoFirstpkArray, SecretKey.serverFirstskArray);
//	               }catch(Exception e){
//	              	 e.printStackTrace(printWriter);
//	               }
	               
	               //printWriter.println(Suc_decrypt);
	               //printWriter.println("   ");
	               
	               //printWriter.println("Success decrypt "+Suc_decrypt);
	               
	               printWriter.println("javaSignedMessage   "+signedMessage.length);
	               for(int i=0;i<signedMessage.length;i++){
	              	 printWriter.println(String.format(",0x%02x ", signedMessage[i]));
	               }
	               
	               //the decrypted signed cipher has all the zeros attached to the front now that need gone
	               //137-32 = 105
	               byte[] signedMessageWithoutZeros = new byte[105];
	               for(int i=0;i<105;i++){
	            	   signedMessageWithoutZeros[i] = signedMessage[i+32];
	               }
	               
	               //printWriter.println("   ");
	               byte[] message = new byte[41];
	           
	               try{
	            	   message = TweetNaCl.crypto_sign_open(signedMessageWithoutZeros, SecretKey.publicArduinoFirstSigningKey);
	               }catch(Exception e){
	              	 e.printStackTrace(printWriter);
	               }
	               
	               
	               float celcius = convertHextoInt(message, printWriter);

	               //Display values
	               printWriter.println("<tr><td>"+id+"</td>");
	               printWriter.println("<td>"+timeStamp+"</td>");
	               printWriter.println("<td>"+celcius+"</td>");
	               printWriter.println("<td>"+tempHex+"</td></tr>");
	               
	               sqlentry++;
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
	
	private float convertHextoInt(byte[] message,PrintWriter printWriter){
		float celsius=0;
        try{
        byte[] data = new byte[9];
        //printWriter.println("temp hex ");
        for(int i=32;i<message.length;i++){
       	 data[i-32]=message[i];
       	 //printWriter.println(String.format(",0x%02x ", data[i-32]));
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
        
       	 //printWriter.println("temp: "+(int)celsius);
        }catch(Exception e){
       	 e.printStackTrace(printWriter);
        }
        return celsius;
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
