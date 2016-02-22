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

/**
 * Servlet implementation class DecryptTemp
 */
@WebServlet("/DecryptTemp")
public class DecryptTemp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Random prng = new Random(0);
	
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
        
        /*crypto_box test*/
//        final int SECRETBOX_INTERNAL_OVERHEAD_BYTES = 32;
//        int messageLength = 40;
//        byte[] message = new byte[messageLength];
//        byte[] publicBoxingKeyBob = new byte[TweetNaCl.BOX_PUBLIC_KEY_BYTES];
//        byte[] secretBoxingKeyAlice = new byte[TweetNaCl.BOX_SECRET_KEY_BYTES];
//        boolean isSeeded = true;
//        prng.nextBytes(secretBoxingKeyAlice);
//        TweetNaCl.crypto_box_keypair(publicBoxingKeyBob, secretBoxingKeyAlice, isSeeded);
//        prng.nextBytes(message);
//        printWriter.println("message ");
//        for(int i=0;i<message.length;i++){
//        	printWriter.println(String.format(", 0x%02x ",message[i]));
//        }
//        byte[] nonce = nonce();
//        byte[] cipherText = new byte[SECRETBOX_INTERNAL_OVERHEAD_BYTES + message.length];
//        byte[] paddedMessage = new byte[SECRETBOX_INTERNAL_OVERHEAD_BYTES + message.length];
//        System.arraycopy(message, 0, paddedMessage, SECRETBOX_INTERNAL_OVERHEAD_BYTES, message.length); //adds the 32 zeros the start of the message
//        byte[] javaCipherText = TweetNaCl.crypto_box(Arrays.copyOfRange(paddedMessage, SECRETBOX_INTERNAL_OVERHEAD_BYTES, paddedMessage.length),
//                nonce, publicBoxingKeyBob, secretBoxingKeyAlice); //theirpublicBoxingKey  oursecretBoxingKey
//		
//        /*crypto_box_open test*/
//        byte[] publicBoxingKeyAlice = new byte[TweetNaCl.BOX_PUBLIC_KEY_BYTES];
//        byte[] secretBoxingKeyBob = new byte[TweetNaCl.BOX_SECRET_KEY_BYTES];
//        prng.nextBytes(secretBoxingKeyBob);
//        TweetNaCl.crypto_box_keypair(publicBoxingKeyAlice, secretBoxingKeyBob, isSeeded);
//        byte[] message1 = null;
//        try{
//        	message1 = TweetNaCl.crypto_box_open(javaCipherText, nonce, publicBoxingKeyAlice, secretBoxingKeyBob);
//        }catch(Exception e){
//        	e.printStackTrace(printWriter);;
//        }
//        printWriter.println("message1 ");
//        for(int i=0;i<message.length;i++){
//        	printWriter.println(String.format(", 0x%02x ",message1[i]));
//        }/*their own goddamn code causes a invalidcipherexception*/
        
        /*straight up box2.c test*/
         byte[] bobskArray = {
        		0x5d,(byte) 0xab,0x08,0x7e,0x62,0x4a,(byte) 0x8a,0x4b
        		,0x79,(byte) 0xe1,0x7f,(byte) 0x8b,(byte) 0x83,(byte) 0x80,0x0e,(byte) 0xe6
        		,0x6f,0x3b,(byte) 0xb1,0x29,0x26,0x18,(byte) 0xb6,(byte) 0xfd
        		,0x1c,0x2f,(byte) 0x8b,0x27,(byte) 0xff,(byte) 0x88,(byte) 0xe0,(byte) 0xeb
        		} ;
         byte[] bobsk = new String(bobskArray).getBytes();
//         printWriter.println("bobsk  ");
//         for(int i=0;i<bobsk.length;i++){
//        	 printWriter.println(String.format(",0x%02x ", bobsk[i]));
//         }
         
         byte[] alicepkArray = {
        		 (byte) 0x85,0x20,(byte) 0xf0,0x09,(byte) 0x89,0x30,(byte) 0xa7,0x54
        		,0x74,(byte) 0x8b,0x7d,(byte) 0xdc,(byte) 0xb4,0x3e,(byte) 0xf7,0x5a
        		,0x0d,(byte) 0xbf,0x3a,0x0d,0x26,0x38,0x1a,(byte) 0xf4
        		,(byte) 0xeb,(byte) 0xa4,(byte) 0xa9,(byte) 0x8e,(byte) 0xaa,(byte) 0x9b,0x4e,0x6a
        		} ;
         byte[] alicepk = new String(alicepkArray).getBytes();
//         printWriter.println("alicepk   ");
//         for(int i=0;i<alicepk.length;i++){
//        	 printWriter.println(String.format(",0x%02x ", alicepk[i]));
//         }
//         
         byte[] nonceArray = {
        		 0x69,0x69,0x6e,(byte) 0xe9,0x55,(byte) 0xb6,0x2b,0x73
        		,(byte) 0xcd,0x62,(byte) 0xbd,(byte) 0xa8,0x75,(byte) 0xfc,0x73,(byte) 0xd6
        		,(byte) 0x82,0x19,(byte) 0xe0,0x03,0x6b,0x7a,0x0b,0x37
        		} ;
         byte[] nonce = new String(nonceArray).getBytes();
//         printWriter.println("nonce  ");
//         for(int i=0;i<nonce.length;i++){
//        	 printWriter.println(String.format(",0x%02x ", nonce[i]));
//         }
         
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
//         String cipherString = "0000000000000000"
//        		 				+"0000000000000000"
//         						  	+ "f3ffc7703f9400e5"
//        		 					+ "2a7dfb4b3d3305d9"
//        		 					+ "8e993b9f48681273"
//        		 					+ "c29650ba32fc76ce"
//        		 					+ "48332ea7164d96a4"
//        		 					+ "476fb8c531a1186a"
//        		 					+ "c0dfc17c98dce87b"
//        		 					+ "4da7f011ec48c972"
//        		 					+ "71d2c20f9b928fe2"
//        		 					+ "270d6fb863d51738"
//        		 					+ "b48eeee314a7cc8a"
//        		 					+ "b932164548e526ae"
//        		 					+ "90224368517acfea"
//        		 					+ "bd6bb3732bc0e9da"
//        		 					+ "99832b61ca01b6de"
//        		 					+ "56244a9e88d5f9b3"
//        		 					+ "7973f622a43d14a6"
//        		 					+ "599b1f654cb45a74"
//        		 					+ "e355a5";
         String ciperStringFromArduino = "0000000000000000"
        		 					   + "0000000000000000"
         							   + "f3ffc7703f9400e5"
         							   + "2a7dfb4b3d3305d9"
         							   + "8e993b9f48681273"
         							   + "c29650ba32fc76ce"
         							   + "48332ea7164d96a4"
         							   + "476fb8c531a1186a"
         							   + "c0dfc17c98dce87b"
         							   + "4da7f011ec48c972"
         							   + "71d2c20f9b928fe2"
         							   + "270d6fb863d51738"
         							   + "b48eeee314a7cc8a"
         							   + "b932164548e526ae"
         							   + "90224368517acfea"
         							   + "bd6bb3732bc0e9da"
         							   + "99832b61ca01b6de"
         							   + "56244a9e88d5f9b3"
         							   + "7973f622a43d14a6"
         							   + "599b1f654cb45a74"
         							   + "e355a5";


         
//         String cipherString = "0000000000000000"
//        		 			 + "0000000000000000"
//         					 + "60594ff43e34ff1e"
//         					 + "08c63fa070df133e"
//         					 + "30d522a58be7f0b1";
         
//       byte[] cipherArray = { 0,   0,   0,   0,   0,   0,   0,   0
//			,   0,   0,   0,   0,   0,   0,   0,   0
//			,   0x60, 0x59, 0x4f, (byte) 0xf4, 0x3e, 0x34, (byte) 0xff, 0x1e
//			,   0x08, (byte) 0xc6, 0x3f, (byte) 0xa0, 0x70, (byte) 0xdf, 0x13, 0x3e
//			,   0x30, (byte) 0xd5, 0x22, (byte) 0xa5, (byte) 0x8b, (byte) 0xe7, (byte) 0xf0, (byte) 0xb1
//};
//
         String cipherString = "0000000000000000"
        		 			 + "0000000000000000"
        		 			 + "d915640e14670da6"
        		 			 + "0e39944c2d46285f"
        		 			 + "1a9e2f1c8b16e9b6"
        		 			 + "e7";

        		 
         byte[] cipherArray = {
        		 0x0 ,0x0 ,0x0 ,0x0 ,0x0 ,0x0 ,0x0 ,0x0
        		 ,0x0 ,0x0 ,0x0 ,0x0 ,0x0 ,0x0 ,0x0 ,0x0
        		 ,0x40 ,0xC ,(byte) 0x8B ,0x41 ,(byte) 0xE2 ,(byte) 0xC7 ,(byte) 0xBF ,0x5D
        		 ,(byte) 0xDA ,0x24 ,0x1E ,(byte) 0xCC ,0x76 ,(byte) 0xEE ,(byte) 0xD0 ,(byte) 0xA8
        		 ,0x30 ,(byte) 0xD5 ,0x22 ,(byte) 0xA5 ,(byte) 0x8B ,(byte) 0xE1 ,(byte) 0xF0 ,(byte) 0xCB

         };

         

         //byte[] cipher = new String(cipherArray).getBytes();
         //byte[] realCipher = CipherString.getBytes("ISO-8859-1");
         byte[] realCipher = hexStringToByteArray(cipherString);
         printWriter.println("cipher length "+realCipher.length);
         printWriter.println("cipher  ");
         for(int i=0;i<realCipher.length;i++){
        	 printWriter.println(String.format(",0x%02x ", realCipher[i]));
         }

         printWriter.println("cipher length "+cipherArray.length);
         printWriter.println("cipherArray  ");
         for(int i=0;i<cipherArray.length;i++){
        	 printWriter.println(String.format(",0x%02x ", cipherArray[i]));
         }
         if(Arrays.equals(realCipher, cipherArray)){
        	 printWriter.println("they the same");
         }else{
        	 printWriter.println("they not the same");
              }
         
         long messageLength = realCipher.length;
         byte[] message = new byte[(int) messageLength];
         int ret=20;
         try{
        	  ret = TweetNaCl.crypto_box_open(message, realCipher, messageLength, nonceArray, alicepkArray, bobskArray);
         }catch(Exception e){
        	 e.printStackTrace(printWriter);
         }
         printWriter.println("suc ="+ret);
         printWriter.println("decrypted message ");
         for(int i=0;i<message.length;i++){
        	 printWriter.println(String.format(",0x%02x ", message[i]));
         }
         
         
		// JDBC driver name and database URL
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
	               	
	               printWriter.println("StringCipher "+tempHex);
	               printWriter.println(" ");
	               
	               printWriter.println("tempHexLength "+ tempHex.length());
	               
	              
	               
	               //Display values
	               printWriter.println("<tr><td>"+id+"</td>");
	               printWriter.println("<td>"+timeStamp+"</td>");
	               printWriter.println("<td>"+tempHex+"</td>");
	               //printWriter.println("<td>"+tempInt+"</td></tr>");
	               
	              
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
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
