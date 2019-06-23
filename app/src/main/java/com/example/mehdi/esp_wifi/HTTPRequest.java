package com.example.mehdi.esp_wifi;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;

public class HTTPRequest extends Thread{

	
	private Handler handler;
	private String requestReply,ipAddress, portNumber;
    private String parameter;
    private String parameterValue;
    protected static final int RoomHandler = 1;

    // The constructor takes parameters from the activity calls the class and stores in parameter defined in this class
	public HTTPRequest(Handler handler, String parameterValue, String ipAddress, String portNumber, String parameter) {

		this.handler = handler;		
        this.ipAddress = ipAddress;
        this.parameterValue = parameterValue;
        this.portNumber = portNumber;
        this.parameter = parameter;		
	}
	

	public void run() {
		
		requestReply = SendRequest(parameterValue,ipAddress,portNumber, parameter);
		handler.obtainMessage(RoomHandler,requestReply).sendToTarget();
	}

    /*
        The method fetches data from ESP module through internet
     */
	
	public String SendRequest(String parameterValue, String ipAddress, String portNumber, String parameterName) {
        String serverResponse = "ERROR";
 
        try {
 
            HttpClient httpclient = new DefaultHttpClient(); // Create an HTTP client
            
            // Define the URL e.g. http://myIpaddress:myport/?pin=13 (to toggle pin 13 for example)
            URI website = new URI("http://" + ipAddress + ":"+portNumber + "/?" + parameterName + "=" + parameterValue);
            HttpGet getRequest = new HttpGet(); // Create an HTTP GET object
            getRequest.setURI(website); // Set the URL of the GET request
            HttpResponse response = httpclient.execute(getRequest); // execute the request
            
            // Get the data back from IP address server's reply
            InputStream content = null;
            content = response.getEntity().getContent();	// Get the response body
            // Provide a buffer and put the inputstream into it
            BufferedReader in = new BufferedReader(new InputStreamReader(content));  
            
            serverResponse = in.readLine();
            // Close the connection
            content.close();
        } catch (ClientProtocolException e) {
            // HTTP error
            serverResponse = e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            // IO error
            serverResponse = e.getMessage();
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // URL syntax error
            serverResponse = e.getMessage();
            e.printStackTrace();
        }
        // Return the server's reply/response text
        return serverResponse;
    }
}
