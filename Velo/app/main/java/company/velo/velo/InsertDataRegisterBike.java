package company.velo.velo;

import android.widget.EditText;

import static company.velo.velo.VeloFragment.appData;

/**
 * Created by jyp08 on 2017-07-21.
 */

public class InsertDataRegisterBike extends AbstractInsertData {

    int bike_type = -1;

    @Override
    String doSomething(MyTaskParams... params){

        String uid = appData.getString("uid", "0");

        return "uid=" + uid + "&type=" + bike_type + "&size="+ "0" +"&gender=" + "0" +"&pw=" + params[0].string+"&fileName=\""+VeloFragment.bikeFileName+"\"";
    }
}
