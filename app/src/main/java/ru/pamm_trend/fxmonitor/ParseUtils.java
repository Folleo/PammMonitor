package ru.pamm_trend.fxmonitor;

import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by dkolotev on 19.03.2015.
 */
public class ParseUtils {

    private final String LOG_TAG = ParseUtils.class.getSimpleName();

    /**
     * Checks whether user authorized or not
     * @param inputJSON JSON String from server
     * @return true if status - true
     */
    public boolean isAuthenticationSuccessful(String inputJSON) {
        try {
            JSONObject jsonObjectJson = new JSONObject(inputJSON);


        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return false;
    }

    public void getUserInvestorsInfo() {
        System.out.println("I'm inside of getUserInvestorsInfo()");
        parseUserInvestorsInfo(getUserInvestorsInfoJsonString());
    }


    public String getUserInvestorsInfoJsonString() {
        String responseJsonStr = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String credentials = "folleo" + ":" + "bb95038342090b2f3626f9914e26b1c8";
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        try {
            HttpGet httpGet = new HttpGet("http://api.fx-trend.com/info/get_user_investors_info");
            httpGet.setHeader("Authorization", "Basic " + base64EncodedCredentials);
            httpGet.setHeader("Accept", "text/json");

            Log.d(LOG_TAG, "Executing request: " + httpGet.getRequestLine());
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();

            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            responseJsonStr = buffer.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return responseJsonStr;
    }

    private void parseUserInvestorsInfo(String inputString) {
        try {
            JSONObject forecastJson = new JSONObject(inputString);
//            JSONArray accountsArray = forecastJson.getJSONArray(OWM_LIST);
//
//            JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
//            String cityName = cityJson.getString(OWM_CITY_NAME);
//
//            JSONObject cityCoord = cityJson.getJSONObject(OWM_COORD);
//            double cityLatitude = cityCoord.getDouble(OWM_LATITUDE);
//            double cityLongitude = cityCoord.getDouble(OWM_LONGITUDE);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

}
