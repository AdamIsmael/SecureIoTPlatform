package tempLog;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

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
	public static byte[][] keyCollection = new byte[50][]; //Server private key storage
	public static byte[][] nonceCollection = new byte[50][]; // Nonce storage
    private int sqlentry;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DecryptTemp() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

          //JDBC driver name and database URL
	      //final String JDBCDRIVER="com.mysql.jdbc.Driver";  
	      final String DB_URL="jdbc:mysql://localhost/iotplatform";
	      String user = "root";  //SQL password and username
	      String password = "";
	        	     
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
	            sqlentry=0;
	            while(rs.next()){
	               //Retrieve by column name
	               int id  = rs.getInt("id");
	               String tempHex = rs.getString("tempHex"); //get encrypted data
	               Timestamp timeStamp = rs.getTimestamp("timeStamp");
	            
	               tempHex="0000000000000000"+tempHex;	//adding lost zeros
	               
	               byte[] signedcipher = hexStringToByteArray(tempHex);
	               long signedMessageLength = signedcipher.length;
	               byte[] signedMessage = new byte[(int) signedMessageLength];
	               
	               int Suc_decrypt = 20;
	               
	            	   try{			
	            		   byte[]  ServerSecretKey = keyCollection[sqlentry];
	            		   byte[]  nonce = nonceCollection[sqlentry];
	            		   
		            	   Suc_decrypt = TweetNaCl.crypto_box_open(signedMessage, signedcipher, signedMessageLength, nonce, SecretKey.arduinoFirstpkArray,ServerSecretKey);
		            	   printWriter.println("ServerSecretKey Used:   ");
			             
	            	   }catch(Exception e){
		              	 e.printStackTrace(printWriter);
		               }
	               
	               
	               byte[] signedMessageWithoutZeros = new byte[105];
	               for(int i=0;i<105;i++){
	            	   signedMessageWithoutZeros[i] = signedMessage[i+32];		//removing the 32 bytes worth of zeros
	               }
	               
	               byte[] message = new byte[41];
	           
	               try{
	            	   message = TweetNaCl.crypto_sign_open(signedMessageWithoutZeros, SecretKey.publicArduinoFirstSigningKey);
	               }catch(Exception e){
	              	 e.printStackTrace(printWriter);
	               }
	               
	               
	               float celcius = convertHextoInt(message, printWriter); //converting hex into more readable integers

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
        for(int i=32;i<message.length;i++){
       	 data[i-32]=message[i];
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
	

}
