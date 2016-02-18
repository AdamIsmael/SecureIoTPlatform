package tempLog;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
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
        printWriter.println("<h1>Temperature asdsdSensor Readings</h1>");
        printWriter.println("<table border=1 cellspacing=1 cellpadding=1>");  //work with ' instead of "?
        printWriter.println("<tr>");
        printWriter.println("<td>&nbsp;ID&nbsp;</td>");
        printWriter.println("<td>&nbsp;Timestamp&nbsp;</td>");
        //printWriter.println("<td>&nbsp;Temperature (Integer)&nbsp;</td>");
        printWriter.println("<td>&nbsp;Temperature (Hexadecimal)&nbsp;</td>");
        printWriter.println("</tr>");
        
      
		//byte[] message = new byte[40];
		//message[24] = 1;
		//printWriter.println("Message: "+bytesToHex(message));
		//printWriter.println("  ");
		boolean isSeeded = true;
        byte[] bobpk = new byte[TweetNaCl.BOX_PUBLIC_KEY_BYTES];
        byte[] alicesk = new byte[TweetNaCl.BOX_SECRET_KEY_BYTES];
        //TweetNaCl.crypto_box_keypair(bobpk, alicesk, isSeeded);
        
        byte[] alicepk = new byte[TweetNaCl.BOX_PUBLIC_KEY_BYTES];
        byte[] bobsk = new byte[TweetNaCl.BOX_SECRET_KEY_BYTES];
        TweetNaCl.crypto_box_keypair(alicepk, bobsk, isSeeded);
        
        //byte[] nonce = nonce();
        String nonceString = "69696ee955b62b73cd62bda875fc73d68219e0036b7a0b37";
        byte[] nonce = new BigInteger(nonceString,16).toByteArray();
        
        
        
		//byte[] cipher =TweetNaCl.crypto_box(message, nonce, bobpk, alicesk);
		//printWriter.println("Cipher: "+ bytesToHex(cipher));
		//printWriter.println("  ");
		
		
        
		
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
	               //byte[] cipherWithNoZeros = new BigInteger(tempHex,16).toByteArray();
	               byte[] cipher = hexStringToByteArray(tempHex);
	               
	               //byte[] cipher = tempHex.getBytes();
	               printWriter.println("ByteCipher ");
	               for(int j=0;j<cipher.length;j++){
	            	   printWriter.println(String.format(",0x%02x ", cipher[j]));
	               }
	               printWriter.println("ByteCipherLength "+cipher.length);
	               //printWriter.println("ByteCipher "+String.format(",0x%02x ", cipher[5]));
	               
	               //byte[] paddedCipher = new byte[cipher.length+16];
	               //System.arraycopy(cipher, 0, paddedCipher, 16, cipher.length);
	               byte[] ciper1 = new byte[40];
	               for(int i=0;i<16;i++){
	            	   ciper1[i]=0;
	               }
	               for(int i=8;i<cipher.length+8;i++){
	            	   ciper1[i]=cipher[i-8];
	            	   //printWriter.println("getting cipher byte..."+String.format(",0x%02x ", ciper1[i-8]));
	               }
	               printWriter.println("ByteCipher2 ");
	               for(int j=0;j<ciper1.length;j++){
	            	   printWriter.println(String.format(",0x%02x ", cipher[j]));
	               }
	               printWriter.println("ByteCipher2Length "+ciper1.length);
	               
	               byte[] cipherWithNoZeros = new byte[24];
	               for(int i=8;i<ciper1.length;i++){
	            	   cipherWithNoZeros[i-8]=ciper1[i];
	               }   
	               // es posible que crypto_box_open añada los ceros para ti
	               printWriter.println("cipherWithNoZeros ");
	               for(int j=0;j<cipherWithNoZeros.length;j++){
	            	   printWriter.println(String.format(",0x%02x ", cipherWithNoZeros[j]));
	               }
	               
	               printWriter.println("startDecrypt");
	               byte[] message1 = null;
	              try{
	                message1 = TweetNaCl.crypto_box_open(ciper1, nonce, alicepk, bobsk);
	               printWriter.println("something");
	              }catch(Exception e){
	            	  e.printStackTrace(printWriter);
	              }
	               
//	               byte[] cipher = new byte[40];
//	               for(int i=0;i<16;i++){
//	            	   cipher[i]=0;
//	               }
//	               for(int i=16;i<tempHex.length();i+=2){
//	            	   String temp =tempHex.substring(i,i+2);
//	            	   cipher 
//	               }
	               
	               printWriter.println(message1);
	       		   printWriter.println("Message: "+ bytesToHex(message1));
	       		   printWriter.println("endDecrypt");
	               
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
	        	e.printStackTrace();
	        }
		
		
		
	}

	private static byte[] nonce() {
		byte[] nonce = new byte[TweetNaCl.BOX_NONCE_BYTES];
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
