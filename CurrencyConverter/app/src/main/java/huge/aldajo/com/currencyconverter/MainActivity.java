package huge.aldajo.com.currencyconverter;

//http://code.tutsplus.com/tutorials/add-charts-to-your-android-app-using-mpandroidchart--cms-23335

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import huge.aldajo.com.currencyconverter.apiConection.OnAsyncAPIFixioResponse;
import huge.aldajo.com.currencyconverter.apiConection.Rates;
import huge.aldajo.com.currencyconverter.apiConection.APIFixioFeedTask;
import huge.aldajo.com.currencyconverter.fragments.CardBackFragment;

/**
 * Main Activity
 */

public class MainActivity extends AppCompatActivity implements OnAsyncAPIFixioResponse {

    /**
     * NOTE: For add a new currency is necessary to follow the next steps:
     *
     */

    /**
     * US dollar amount input through EditText .
     */
    private EditText editText;

    /**
     * Fragment managing the presentation of the results.
     */
    private CardBackFragment cb;

    /**
     * Object for connect with fixer.io API asynchronous
     */
    private APIFixioFeedTask rf;

    /**
     * Reference for get the Context inside events listener
     */
    private AppCompatActivity thisActivity = this;

    /**
     * USD Dollar amount converted in numeric value;
     */
    private Double inputUSD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Create empty fragment without information
         */
        cb = new CardBackFragment();

        /**
         * Get the Reference objects from UI
         */
        editText = (EditText) findViewById(R.id.et);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /**
                     * This line is necessary when there are other inputs from editText.
                     */
                    //editText = (EditText) thisActivity.getCurrentFocus();

                    /**
                     * Important: Ensure that the input value is a valid number.
                     * otherwise, show a Toast Message.
                     */
                    try {
                        inputUSD = Double.parseDouble(editText.getText().toString());
                        rf = new APIFixioFeedTask(thisActivity);
                        rf.setDelegate((OnAsyncAPIFixioResponse) thisActivity);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        /**
                         *  get API information. Run asynchronous.
                         */
                        rf.execute();
                    } catch (NumberFormatException e) {
                        Toast.makeText(thisActivity, "Please insert a valid number!!", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            /**
             * If there is no saved instance state, add a fragment representing the
             * front of the card to this activity. If there is saved instance state,
             * this fragment will have already been added to the activity.
             */
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, cb)
                    .commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.designed_by, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * Inflate the menu;
         * This adds items to the action bar if it is present.
         */
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Handle action bar item clicks here. The action bar will
         * automatically handle clicks on the Home/Up button, so long
         * as you specify a parent activity in AndroidManifest.xml.
         */
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Events when API RetriveFeedTask send the complete response.
     * Rates Object for managing the conversion.
     */

    @Override
    public void processFinishWithRates(Rates output) {
        /**
         * Create new fragment for each new response.
         * Animate and then convert to Double data.
         */
        cb = new CardBackFragment();
        getFragmentManager().beginTransaction().setCustomAnimations(
                R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.container, cb).addToBackStack(null)
                .commit();
        cb.setUSD(inputUSD);
        /**
         * put Rates for manage the conversion in fragment.
         */
        cb.putRates(output);
    }
}
