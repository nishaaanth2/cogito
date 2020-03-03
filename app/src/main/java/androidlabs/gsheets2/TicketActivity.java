//MainActivity.java

package androidlabs.gsheets2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidlabs.gsheets2.parser.JSONParser;
import androidlabs.gsheets2.util.InternetConnection;
import androidlabs.gsheets2.util.Keys;

public class ETicketActivity extends AppCompatActivity {


    private EditText user_ph_data;
    private Button fab;

    private String gname;
    private String gemail;
    private String ginstitute;
    private String gphno;
    private String gcsim;
    private String checkval = "9953072714";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eticket);


        /**
         * Just to know onClick and Printing Hello Toast in Center.
         */
        Toast toast = Toast.makeText(getApplicationContext(), "Click on FloatingActionButton to Load JSON", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();


        fab = (Button) findViewById(R.id.fab);
        user_ph_data = (EditText) findViewById(R.id.user_ph);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {

                checkval = user_ph_data.getText().toString().trim();
                /**
                 * Checking Internet Connection
                 */
                closeKeyboard();
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    new GetDataTask().execute();
                } else {
                    Snackbar.make(view, "Internet Connection Not Available", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Creating Get Data Task for Getting Data From Web
     */
    class GetDataTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        int jIndex;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */


            dialog = new ProgressDialog(ETicketActivity.this);
            dialog.setTitle("Hey Please Wait...");
            dialog.setMessage("Fetching Data");
            dialog.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    dialog.cancel();
                    Snackbar.make(findViewById(R.id.parentLayout), "No Data Found", Snackbar.LENGTH_LONG).show();
                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 15000);
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {

            /**
             * Getting JSON Object from Web Using okHttp
             */
            JSONObject jsonObject = JSONParser.getDataFromWeb();

            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {
                    /**
                     * Check Length...
                     */
                    if (jsonObject.length() > 0) {
                        /**
                         * Getting Array named "contacts" From MAIN Json Object
                         */
                        JSONArray array = jsonObject.getJSONArray(Keys.KEY_CONTACTS);

                        /**
                         * Check Length of Array...
                         */


                        int lenArray = array.length();
                        if (lenArray > 0) {
                            for (; jIndex < lenArray; jIndex++) {

                                /**
                                 * Creating Every time New Object
                                 * and
                                 * Adding into List
                                 */

                                /**
                                 * Getting Inner Object from contacts array...
                                 * and
                                 * From that We will get Name of that Contact
                                 *
                                 */
                                JSONObject innerObject = array.getJSONObject(jIndex);
                                gname = innerObject.getString(Keys.KEY_NAME);
                                gemail = innerObject.getString(Keys.KEY_MAILID);
                                ginstitute = innerObject.getString(Keys.KEY_INSTITUION);
                                gphno = innerObject.getString(Keys.KEY_PH_NO);
                                gcsim = innerObject.getString(Keys.KEY_CSIMEM);
                                if (gcsim.trim().equals("")) {
                                    gcsim = "Not Mentioned";
                                }
//                                checkval="9677057592";
                                if (gphno.trim().equals(checkval.trim())) {
                                    Snackbar.make(findViewById(R.id.parentLayout), gname + " => " + gphno + gemail + ginstitute + gcsim, Snackbar.LENGTH_LONG).show();
                                    Intent ishow = new Intent(getApplicationContext(), ShowActivity.class);
                                    ishow.putExtra("sname", gname);
                                    ishow.putExtra("semail", gemail);
                                    ishow.putExtra("sinstitute", ginstitute);
                                    ishow.putExtra("sphno", gphno);
                                    ishow.putExtra("scsim", gcsim);
                                    startActivity(ishow);
                                }

                            }
                        }
                    }
                } else {

                }
            } catch (JSONException je) {
                Log.i(JSONParser.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            /**
             * Checking if List size if more than zero then
             * Update ListView
             */
            if (gname == "") {
                Snackbar.make(findViewById(R.id.parentLayout), "No Data Found", Snackbar.LENGTH_LONG).show();
            } else {
//               Snackbar.make(findViewById(R.id.parentLayout), "Data Found", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}