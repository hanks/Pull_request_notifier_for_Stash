package com.hanks;

import com.hanks.Constants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

import org.json.JSONObject;

/**
 * Utility class for hipchat message function
 *
 * @author han.zhou
 *
 */
public class Hipchat {

    /**
     * Send message to target person whose email is specified from kemonobot by hipchat.
     * 
     * @param email target user's email
     * @param message content for message
     */
    public static void send_message(String email, String message) {
        HttpURLConnection connection = null;
        OutputStreamWriter wr = null;
        BufferedReader rd = null;
        
        try {
        	String targetURL = String.format(Constants.API, email);
			URL url = new URL(targetURL);
			connection = (HttpURLConnection)url.openConnection();
			
			// set http header info
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            String auth_token = String.format("Bearer %s", Constants.TOKEN);
            connection.setRequestProperty("Authorization", auth_token);
            
            // build json data
            JSONObject jsonData = new JSONObject();
            jsonData.put("message", message);
            jsonData.put("notify", true);
            jsonData.put("message_format", "text");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            
            // write request
            connection.connect();
            wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(jsonData.toString());
            wr.flush();
            
            // send request	
            InputStream is = connection.getInputStream();
            
            // get response
            rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer(); 
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            // clean up resources
            if (wr != null) {
                try {
                    wr.close();                                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (rd != null) {
                try {
                    rd.close();                
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                connection.disconnect();                
            }
        }
    }
}
