package com.example.mehdi.esp_wifi;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityRoom1 extends MainActivity implements OnClickListener {

	/* Define a Handler to transfer data from the thread in which send and
	   receive is done */
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case HTTPRequest.RoomHandler:

				byte[] writeBuf = (byte[]) msg.obj;
				int begin =0;
				int end =3;

				String RoomTemp = new String(writeBuf);
				String Indicator = RoomTemp.substring(begin, end);

				if (Indicator == "L1ON"){
					buttonLight1.setBackgroundColor(16711936);
				} else if (Indicator == "L1OF"){
					buttonLight1.setBackgroundColor(0);
				}else if (Indicator == "L2ON"){
					buttonLight2.setBackgroundColor(16711936);
				}else if (Indicator == "L2OF"){
					buttonLight2.setBackgroundColor(0);
				}else if (Indicator == "R1TC"){

					RoomTemp = RoomTemp.substring(4,8);
					SetTemperature(RoomTemp);
				}
				break;
			}
		}
	};

	/* IP address and Port No. should be modified based on the IP of the module */
	public final static String PREF_IP = "192.168.1.10";		// This IP Address should be assigned to wifi module by the router
    public final static String PREF_PORT = "80";
    
 	// Get the pin number
    String parameterValue 	= "";
    // Set the IP address
    String ipAddress 		= PREF_IP;
    // Set the port number
    String portNumber		= PREF_PORT;
    
    /* Declare buttons and text inputs */
    private Button 		buttonLight1,buttonLight2,buttonBack;
    private HTTPRequest httprequest;
    
    /* Shared preferences objects used to save the IP address and port so that the user doesn't have to
     type them next time he/she opens the app. */
    SharedPreferences.Editor editor;
    SharedPreferences 		 sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room1);
		
		sharedPreferences = getSharedPreferences("HTTP_HELPER_PREFS",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
 
        // Assign buttons
        buttonLight1 = (Button)findViewById(R.id.Light1);
        buttonLight2 = (Button)findViewById(R.id.Light2);
        buttonBack 	 = (Button)findViewById(R.id.Back);
 
        // Set button listener (this class)
        buttonLight1.setOnClickListener(this);
        buttonLight2.setOnClickListener(this);
        
        buttonBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				
			}
		});
 
/*      /* 	Get the IP address and port number from the last time the user used the app,
        	Put an empty string "" is this is the first time.
        editTextIPAddress.setText(sharedPreferences.getString(PREF_IP,""));
        editTextPortNumber.setText(sharedPreferences.getString(PREF_PORT,""));
 */       
        parameterValue = "INQ";
        
        httprequest = new HTTPRequest(mHandler, parameterValue, ipAddress, portNumber, "pin");
    	httprequest.start();
    	
    	
    	new Timer().schedule(new TimerTask() {          
    	    @Override
    	    public void run() {
    	    	httprequest = new HTTPRequest(mHandler, "T1", ipAddress, portNumber, "pin");
    	    	httprequest.start();     
    	    }
    	}, 60000);
    }
 
	
    @Override
    public void onClick(View view) {

        /* Save the IP address and port for the next time the app is used */
        editor.putString(PREF_IP,ipAddress); 	// Set the ip address value to save
        editor.putString(PREF_PORT,portNumber); // Set the port number to save
        editor.commit(); // Save the IP and PORT
 
        /* Get the pin number from the button that was clicked */
        if(view.getId()==buttonLight1.getId())
        {
            parameterValue = "L1";
        }
        else if(view.getId()==buttonLight2.getId())
        {
            parameterValue = "L2";
        }

		/* Execute HTTP request */
		if(ipAddress.length()>0 && portNumber.length()>0) {
			SocketClient myClient = new SocketClient(ipAddress, Integer.parseInt(portNumber), parameterValue,"pin",view.getContext());
			myClient.execute();
		}

	}

	public void SetTemperature(String temperature){

		TextView view = (TextView) this.findViewById(R.id.RoomTemperatureValue);

		DecimalFormat df = new DecimalFormat("###.##");

		String formattedTemperature = df.format(temperature);

		view.setText(formattedTemperature + " °C");

	}
    
 /*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_room1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
}
