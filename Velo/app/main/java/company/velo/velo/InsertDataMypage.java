package company.velo.velo;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * Created by jyp08 on 2017-07-20.
 */

public class InsertDataMypage extends AbstractInsertData {

    SharedPreferences appData;

    class Instant {
        public String user_id, user_phone, user_studentid;
    }

    @Override
    String doSomething(MyTaskParams... params){
        appData = params[0].sharedPreferences;
        return "uid=" + params[0].string;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        //progressDialog.dismiss();
        Log.d(TAG, "POST response  - " + result);

        InsertDataMypage.Instant obj = new Gson().fromJson(result, InsertDataMypage.Instant.class);

        SharedPreferences.Editor editor = appData.edit();

        editor.putBoolean("SAVE_USER_DATA", true);
        editor.putString("user_id", obj.user_id.trim());
        editor.putString("user_phone", obj.user_phone.trim());
        editor.putString("user_studentid", obj.user_studentid.trim());

        editor.apply();
    }
}