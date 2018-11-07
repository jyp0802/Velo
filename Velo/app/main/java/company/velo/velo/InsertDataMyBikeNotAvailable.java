package company.velo.velo;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Switch;

/**
 * Created by jangsujin on 2017. 7. 23..
 */

public class InsertDataMyBikeNotAvailable extends AbstractInsertData {

    @Override
    String doSomething(MyTaskParams... params){
        return "uid=" + params[0].string;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.d(TAG, "POST response  - " + result);

        if(!result.equals("true")){
            // 예외 처리
        }
    }
}
