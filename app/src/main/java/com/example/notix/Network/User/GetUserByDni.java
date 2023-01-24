package com.example.notix.Network.User;

import com.example.notix.Network.NetConfiguration;
import com.example.notix.Network.Professor.beans.AuthResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetUserByDni extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "users/";
    private String dni;
    private AuthResponse userResponse = new AuthResponse();
    private String access = "";

    public GetUserByDni(String dni, String access) {
        super();
        this.dni = dni;
        this.access = access;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + dni);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + access);

            // Sending...
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 204) {
                this.userResponse = null;

            } else if (responseCode == HttpURLConnection.HTTP_OK) {
                // Response...
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                bufferedReader.close();

                // Processing the JSON...
                String theUnprocessedJSON = response.toString();



                JSONObject object = new JSONObject(theUnprocessedJSON);
                userResponse.setDni(object.getString("dni"));

                JSONArray roles = null;
                int roll = 0;
                roles = object.getJSONArray("roles");

                for (int i = 0; i < roles.length(); i++) {
                    JSONObject object2 = roles.getJSONObject(i);
                    //userResponse.setRoleId(object2.getInt("roleID"));

                    roll = object2.getInt("roleID");
                    //userResponse.setRoleId(roll);
                }
                userResponse.setRoleId(roll);
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public AuthResponse getResponse() { return userResponse; }
}
