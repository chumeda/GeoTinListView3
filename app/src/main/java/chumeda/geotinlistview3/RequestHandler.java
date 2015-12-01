package chumeda.geotinlistview3;


import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class RequestHandler {

    public String sendPostRequest(String requestURL,
                                  HashMap<String, String> postDataParams) {

        URL url;
        StringBuilder sb = new StringBuilder();
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d("test", conn.getRequestMethod() + " 1");
            conn.setDoOutput(true);

            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            Log.d("test", conn.getRequestMethod() + " 2");
            conn.setDoInput(true);


            OutputStream os = null;
            try {
                os = conn.getOutputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("test", "test");
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            Log.d("test", conn.getRequestMethod() + " 3");
            try {
                writer.write(getPostDataString(postDataParams));
            } catch (Exception e) {
                e.printStackTrace();
            }
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            Log.d("test", "code: " + Integer.toString(responseCode) + ", " + conn.getResponseMessage());
            Log.d("test", "new: " + conn.getHeaderField("Location"));
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                while((response = br.readLine()) != null) {
                    sb.append(response);
                }
                Log.d("test", sb.toString());

            } else {
                Log.d("test", "error registering");
                response = "Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public String sendGetRequest(String requestURL) {
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(requestURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while((s=bufferedReader.readLine()) != null) {
                sb.append(s+"\n");
            }
        } catch(Exception e) {
            Log.d("test", "error getting request");
        }
        return sb.toString();
    }
    public String sendGetRequestParam(String requestURL, String id) {
        StringBuilder sb = new StringBuilder();
        id = "1";
        try {
            URL url = new URL(requestURL+id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while((s=bufferedReader.readLine())!=null) {
                sb.append(s+"\n");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "sb " +sb.toString());
        return sb.toString();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}