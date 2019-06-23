package com.example.mehdi.esp_wifi;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @name    SocketClient
 * @brief   The class send given data to another device through wifi connection.
 *          The IP address and Port number should be specified in the caller routine.
 *          The class use socket without ant http protocol to send the data.
 * @author  Mehdi
 */

public class SocketClient extends AsyncTask<Void, Void, Void>
{

    String              dstAddress;
    int                 dstPort;
    String              response = "";
    private String      parameter;
    private String      parameterValue;
    private Context     context;
    private AlertDialog alertDialog;

    /**
     * @name    SocketClient
     * @brief   The asyncTask class constructor. Assigns the values used in its other methods.
     *
     * @param context the application context, needed to create the dialog
     * @param parameterValue the pin number to toggle
     * @param ipAddress the ip address to send the request to
     * @param portNumber the port number of the ip address
     */
    SocketClient(String ipAddress, int portNumber, String parameterValue,
                 String parameter, Context context)
    {
        dstAddress          = ipAddress;
        dstPort             = portNumber;
        this.parameterValue = parameterValue;
        this.parameter      = parameter;
        this.context        = context;

        alertDialog = new AlertDialog.Builder(this.context)
                .setTitle("HTTP Response From IP Address:")
                .setCancelable(true)
                .create();
    }

    /**
     * @name    onPreExecute
     * @brief   This function is executed before the request is sent to ip address.
     *          The function will set the dialog's message and display the dialog.
     */
    @Override
    protected void onPreExecute()
    {
        alertDialog.setMessage("Sending data to server, please wait...");
        if(!alertDialog.isShowing())
        {
            alertDialog.show();
        }
    }

    /**
     * @name    doInBackground
     * @brief   Sends the request to the ip address
     * @param   Void
     * @return  Void
     */
    @Override
    protected Void doInBackground(Void... arg0)
    {
        Socket socket = null;
        DataOutputStream outputStream = null;

        Log.d("http://" + dstAddress + ":" + dstPort + "/?" + parameter + "=" + parameterValue,"    URL");
        alertDialog.setMessage("Data Sent, Waiting For The Reply From Server...");
        if(!alertDialog.isShowing())
        {
            alertDialog.show();
        }

        try {
            socket = new Socket(dstAddress, dstPort);
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(parameter + "=" + parameterValue);

            /* Creating a buffer in memory in which all the data sent to the stream is stored. */
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();

			/* NOTICE: inputStream.read() will block if no data return */
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);  // Write data from output stream to buffer from beginning to bytesRead
                response += byteArrayOutputStream.toString("UTF-8");
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * @name    onPostExecute
     * @brief   This function gets the response and display it on the screen .
     *
     * @param   Void void parameter
     */
    @Override
    protected void onPostExecute(Void result)
    {
        alertDialog.setMessage(response);
        super.onPostExecute(result);
    }

}