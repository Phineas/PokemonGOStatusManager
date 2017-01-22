package io.phineas.pogostatusmanager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Phineas (phineas.io) on 24/07/2016
 */
public class APIv2 {
    //I know it's static, but it's not an Object Oriented program so it shoudln't matter too much

    public static int getHTTPResponse() {
        try {
            URL url = new URL("https://pokemongostatus.net");

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            return connection.getResponseCode();
        } catch(IOException e) {
            e.printStackTrace();
            return 500;
        }
    }

    public static String getDropletInfo(String type, String data) {
        try {
            URL website = new URL("https://internal.pokemongostatus.net/api/networking/droplets");
            URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();

            JSONObject dropletInfo = new JSONObject(response.toString());

            if(type.equals("id")) {
                if(data.equals("default")) {
                    return dropletInfo.getJSONObject("default").getString("id");
                } else {
                    return dropletInfo.getJSONObject("proxy").getString("id");
                }
            } else {
                if(data.equals("default")) {
                    return dropletInfo.getJSONObject("default").getString("ipaddr");
                } else {
                    return dropletInfo.getJSONObject("proxy").getString("ipaddr");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }
}
