package huge.aldajo.com.currencyconverter.apiConection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIFixioFeedTask extends AsyncTask<Void, Void, String> {

    private OnAsyncAPIFixioResponse delegate;
    private ProgressDialog dialog;
    private Rates rates;
    private Context context;

    public APIFixioFeedTask(Context cxt) {
        context = cxt;
        dialog = new ProgressDialog(context);
        rates = new Rates();
    }

    protected void onPreExecute() {
        dialog.setTitle("Please wait");
        dialog.show();
    }

    public void setDelegate(OnAsyncAPIFixioResponse delegate) {
        this.delegate = delegate;
    }

    protected String doInBackground(Void... urls) {
        /**
         * Validate connection to http://api.fixer.io/latest?base=USD
         */
        try {
            URL url = new URL("http://api.fixer.io/latest?base=USD");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param response: JSON package obtained
     */
    protected void onPostExecute(String response) {
        /**
         * Hide progress Dialog
         */
        dialog.dismiss();

        /**
         * Response works if there are permissions in AndroidManifest.xml
         */
        if (response == null) {
            response = "THERE WAS AN ERROR";
        }

        /**
         * Check response in log
         */
        Log.i("INFO", response);

        try {
            rates = new Rates();
            JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
            //String requestID = object.getString("base");
            JSONObject array = object.getJSONObject("rates");

            rates.setGBP(Double.parseDouble(array.getString("GBP")));
            rates.setEUR(Double.parseDouble(array.getString("EUR")));
            rates.setJPY(Double.parseDouble(array.getString("JPY")));
            rates.setBRL(Double.parseDouble(array.getString("BRL")));

            delegate.processFinishWithRates(rates);

        } catch (Exception e) {
            //e.printStackTrace();
            Toast.makeText(context,"Error:Please Check your Network Connection!!",Toast.LENGTH_LONG).show();
        }
    }
}