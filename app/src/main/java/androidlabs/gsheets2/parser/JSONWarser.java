package androidlabs.gsheets2.parser;

import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class JSONParser {

//    private static final String MAIN_URL = "https://script.googleusercontent.com/macros/echo?user_content_key=24_0RvvfZ_bH1GQm3w01D9Gi10At5PMx4RtprOAzO1lrVi8luBO-VKziTY5jRMexLRO48DrYeFwIOw4mKilde2B2HZC7jxqMOJmA1Yb3SEsKFZqtv3DaNYcMrmhZHmUMWojr9NvTBuBLhyHCd5hHa1GhPSVukpSQTydEwAEXFXgt_wltjJcH3XHUaaPC1fv5o9XyvOto09QuWI89K6KjOu0SP2F-BdwUMSp74RXjpAR4e1AZ75eaKHrngAZGpWfmpGEIHKUX4jH3G4XVdFyKdV-uvfmFyCFC5y7FLqOV0Tk27B8Rh4QJTQ&lib=MnrE7b2I2PjfH799VodkCPiQjIVyBAxva";
    private static final String MAIN_URL = "https://script.googleusercontent.com/macros/echo?user_content_key=UhFL8R2TzYv5OZMsX3og_cWyOChNqc3MJL5Obvoo2KRBlCmyOPx9cZARcg4dd23e_edr-rjBWmDifeF-RPTH6PaesOgBxYS8OJmA1Yb3SEsKFZqtv3DaNYcMrmhZHmUMWojr9NvTBuBLhyHCd5hHa1GhPSVukpSQTydEwAEXFXgt_wltjJcH3XHUaaPC1fv5o9XyvOto09QuWI89K6KjOu0SP2F-BdwUALeN7ha-_njveC9njlcrQimscFUPVXOWBTJc-wdlvg4MuBuNJFiY2LUZTzbmEAdK5tYrEq1gHrFGW8KNcrtxjWrA9YQYA_w5&lib=MnrE7b2I2PjfH799VodkCPiQjIVyBAxva";

    public static final String TAG = "TAG";

    private static final String KEY_USER_ID = "user_id";

    private static Response response;

    public static JSONObject getDataFromWeb() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(MAIN_URL)
                    .build();
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject getDataById(int userId) {

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormEncodingBuilder()
                    .add(KEY_USER_ID, Integer.toString(userId))
                    .build();

            Request request = new Request.Builder()
                    .url(MAIN_URL)
                    .post(formBody)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }
}
