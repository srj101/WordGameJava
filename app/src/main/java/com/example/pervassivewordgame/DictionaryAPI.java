package com.example.pervassivewordgame;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DictionaryAPI {
    private static final String API_KEY = "8537a470-9b2e-40ee-9775-6df5b5200a63";
    private static final String API_URL = "https://dictionaryapi.com/api/v3/references/learners/json/";

    public static void isValidWord(String word, ValidWordCallback callback) {
        System.out.println(word);
        new DictionaryAsyncTask(callback).execute(word);
    }

    private static class DictionaryAsyncTask extends AsyncTask<String, Void, Boolean> {
        private ValidWordCallback callback;

        DictionaryAsyncTask(ValidWordCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(String... words) {
            String word = words[0];
            try {
                URL url = new URL(API_URL + word + "?key=" + API_KEY);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();



                    // Check if the response is an array of objects and if the first object contains the key "fl"
                    JSONArray jsonArray = new JSONArray(response.toString());
                    if (jsonArray.length() > 0) {
                        JSONObject firstObject = jsonArray.getJSONObject(0);

                        return firstObject.has("fl");
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return false;
        }




        @Override
        protected void onPostExecute(Boolean isValid) {
            callback.onValidationResult(isValid);
        }
    }

    public interface ValidWordCallback {
        void onValidationResult(boolean isValid);
    }
}
