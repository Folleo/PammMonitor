package ru.pamm_trend.fxmonitor;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import ru.pamm_trend.fxmonitor.data.AccountsContract.AccountEntry;

/**
 * Helping Utils for parsing JSON string.
 */
public class ParseUtils {

    private final String LOG_TAG = ParseUtils.class.getSimpleName();

    public final String JSON_DATA = "data";

    /**
     * Checks whether user authorized or not
     * @param inputJSON JSON String from server
     * @return true if 'status' - true
     */
    public boolean isAuthenticationSuccessful(String inputJSON) {
        try {
            JSONObject jsonObjectJson = new JSONObject(inputJSON);
            // Check 'status' param here

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return false;
    }

    public void getUserInvestorsInfo() {
        System.out.println("I'm inside of getUserInvestorsInfo()");
        ServerRequest mServerRequestTask = new ServerRequest();
        mServerRequestTask.execute((Void) null);
    }


    public String getUserInvestorsInfoJsonString() {
        String responseJsonStr = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();

        String credentials = "folleo" + ":" + "bb95038342090b2f3626f9914e26b1c8";

        // TODO: Replace getting credentials in separate method or class (PrefUtils.java)

//        String login = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_LOGIN_USERNAME_KEY, "");
//        String apiKey = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_LOGIN_API_KEY, "");
//        String credentials = login + ":" + apiKey;

        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        try {
            HttpGet httpGet = new HttpGet("http://api.fx-trend.com/info/get_user_investors_info");
            httpGet.setHeader("Authorization", "Basic " + base64EncodedCredentials);
            httpGet.setHeader("Accept", "text/json");

            Log.d(LOG_TAG, "Executing request: " + httpGet.getRequestLine());
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();

            StringBuilder buffer = new StringBuilder();

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
            Log.d(LOG_TAG, "Response JSON String:\n" + buffer);
            responseJsonStr = buffer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return responseJsonStr;
    }

    // Parse JSON data and put it into SQLite database
    private void parseUserInvestorsInfo(String inputString) {
        try {
            JSONObject accountsJson = new JSONObject(inputString);
            JSONArray accountsArray = accountsJson.getJSONArray(JSON_DATA);

            // Inserting accounts information in the database
            Vector<ContentValues> cVVector = new Vector<>(accountsArray.length());

            for (int i = 0; i < accountsArray.length(); i++) {
                JSONObject account = accountsArray.getJSONObject(i);

                int investorId = account.getInt(AccountEntry.COLUMN_INVESTOR_ID);
                int investorType = account.getInt(AccountEntry.COLUMN_INVESTOR_TYPE);
                int partnerId = account.getInt(AccountEntry.COLUMN_PARTNER_ID);
                int userId = account.getInt(AccountEntry.COLUMN_USER_ID);
                int offerId = account.getInt(AccountEntry.COLUMN_OFFER_ID);
                int pammMtLogin = account.getInt(AccountEntry.COLUMN_PAMM_MT_LOGIN);
                int invMtLogin = account.getInt(AccountEntry.COLUMN_INV_MT_LOGIN);
                int createdAt = account.getInt(AccountEntry.COLUMN_CREATED_AT);
                int activatedAt = account.getInt(AccountEntry.COLUMN_ACTIVATED_AT);
                int closedAt = account.getInt(AccountEntry.COLUMN_CLOSED_AT);
                int status = account.getInt(AccountEntry.COLUMN_STATUS);
                int currentSum = account.getInt(AccountEntry.COLUMN_CURRENT_SUM);
                int insuredSum = account.getInt(AccountEntry.COLUMN_INSURED_SUM);
                int forIndex = account.getInt(AccountEntry.COLUMN_FOR_INDEX);
                int tradeSessionStartUnixtime = account.getInt(AccountEntry.COLUMN_TRADE_SESSION_START_UNIXTIME);
                int investorsDeposit = account.getInt(AccountEntry.COLUMN_INVESTORS_DEPOSIT);
                int investorsWithdraw = account.getInt(AccountEntry.COLUMN_INVESTORS_WITHDRAW);
                int tradeSessionPayments = account.getInt(AccountEntry.COLUMN_TRADE_SESSION_PAYMENTS);
                int periodProfit = account.getInt(AccountEntry.COLUMN_PERIOD_PROFIT);
                int availsum = account.getInt(AccountEntry.COLUMN_AVAILSUM);
                int profitPercent = account.getInt(AccountEntry.COLUMN_PROFIT_PERCENT);
                int pretermsum = account.getInt(AccountEntry.COLUMN_PRETERMSUM);
                int maxDefinedSum = account.getInt(AccountEntry.COLUMN_MAX_DEFINED_SUM);
                int actualDatetime = account.getInt(AccountEntry.COLUMN_ACTUAL_DATETIME);

                ContentValues contentValues = new ContentValues();

                contentValues.put(AccountEntry.COLUMN_INVESTOR_ID, investorId);
                contentValues.put(AccountEntry.COLUMN_INVESTOR_TYPE, investorType);
                contentValues.put(AccountEntry.COLUMN_PARTNER_ID, partnerId);
                contentValues.put(AccountEntry.COLUMN_USER_ID, userId);
                contentValues.put(AccountEntry.COLUMN_OFFER_ID, offerId);
                contentValues.put(AccountEntry.COLUMN_PAMM_MT_LOGIN, pammMtLogin);
                contentValues.put(AccountEntry.COLUMN_INV_MT_LOGIN, invMtLogin);
                contentValues.put(AccountEntry.COLUMN_CREATED_AT, createdAt);
                contentValues.put(AccountEntry.COLUMN_ACTIVATED_AT, activatedAt);
                contentValues.put(AccountEntry.COLUMN_CLOSED_AT, closedAt);
                contentValues.put(AccountEntry.COLUMN_STATUS, status);
                contentValues.put(AccountEntry.COLUMN_CURRENT_SUM, currentSum);
                contentValues.put(AccountEntry.COLUMN_INSURED_SUM, insuredSum);
                contentValues.put(AccountEntry.COLUMN_FOR_INDEX, forIndex);
                contentValues.put(AccountEntry.COLUMN_TRADE_SESSION_START_UNIXTIME, tradeSessionStartUnixtime);
                contentValues.put(AccountEntry.COLUMN_INVESTORS_DEPOSIT, investorsDeposit);
                contentValues.put(AccountEntry.COLUMN_INVESTORS_WITHDRAW, investorsWithdraw);
                contentValues.put(AccountEntry.COLUMN_TRADE_SESSION_PAYMENTS, tradeSessionPayments);
                contentValues.put(AccountEntry.COLUMN_PERIOD_PROFIT, periodProfit);
                contentValues.put(AccountEntry.COLUMN_AVAILSUM, availsum);
                contentValues.put(AccountEntry.COLUMN_PROFIT_PERCENT, profitPercent);
                contentValues.put(AccountEntry.COLUMN_PRETERMSUM, pretermsum);
                contentValues.put(AccountEntry.COLUMN_MAX_DEFINED_SUM, maxDefinedSum);
                contentValues.put(AccountEntry.COLUMN_ACTUAL_DATETIME, actualDatetime);

                cVVector.add(contentValues);
            }

            // TODO: add cVVector data into database

            int inserted = 0;

            // add to database
            /*if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = this.getContentResolver().bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, cvArray);

                // delete old data from the database
                getContext().getContentResolver().delete(WeatherContract.WeatherEntry.CONTENT_URI,
                        WeatherContract.WeatherEntry.COLUMN_DATE + " <= ?",
                        new String[]{Long.toString(dayTime.setJulianDay(julianStartDay - 1))});
            }*/
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public class ServerRequest extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            String jsonString = getUserInvestorsInfoJsonString();
            parseUserInvestorsInfo(jsonString);
            return null;
        }
    }

}
