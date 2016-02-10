package com.srccode.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import TweetNaCl.TweetNaCl;



/**
 * Servlet implementation class HelloWorld
 */
@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Random prng = new Random(0);
	private TweetNaCl tuit;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloWorld() {
        super();
        // TODO Auto-generated constructor stub
        tuit = new TweetNaCl();
    }

    
    private static byte[] nonce() {
        byte[] nonce = new byte[TweetNaCl.BOX_NONCE_BYTES];
        prng.nextBytes(nonce);
        return nonce;
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<h1> hhshs!</h1>");
		int num1=42;
		int num2=42;
		int num3=num1+num2;
		printWriter.println("<h2>basdhas</h2>");
		//printWriter.println(num3);
		byte[] message = new byte[40];
		message[24] = 1;
		printWriter.println("Message: "+bytesToHex(message));
		printWriter.println("  ");
		boolean isSeeded = true;
        byte[] publicBoxingKey = new byte[TweetNaCl.BOX_PUBLIC_KEY_BYTES];
        byte[] secretBoxingKey = new byte[TweetNaCl.BOX_SECRET_KEY_BYTES];
        TweetNaCl.crypto_box_keypair(publicBoxingKey, secretBoxingKey, isSeeded);
        
        byte[] publicUnBoxingKey = new byte[TweetNaCl.BOX_PUBLIC_KEY_BYTES];
        byte[] secretUnBoxingKey = new byte[TweetNaCl.BOX_SECRET_KEY_BYTES];
        TweetNaCl.crypto_box_keypair(publicUnBoxingKey, secretUnBoxingKey, isSeeded);
        
        byte[] nonce = nonce();
		
		byte[] cipher =TweetNaCl.crypto_box(message, nonce, publicBoxingKey, secretBoxingKey);
		printWriter.println("Cipher: "+ bytesToHex(cipher));
		printWriter.println("  ");
		
		byte[] message1 = TweetNaCl.crypto_box_open(cipher, nonce, publicUnBoxingKey, secretUnBoxingKey);
		printWriter.println("Message: "+ bytesToHex(message1));
		printWriter.println("  ");
		
		
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
