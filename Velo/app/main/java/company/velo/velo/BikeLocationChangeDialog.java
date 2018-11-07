package company.velo.velo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jyp08 on 2017-07-23.
 */

public class BikeLocationChangeDialog extends DialogFragment {

    double latitude, longitude;
    private SharedPreferences appData;

    void setAppData(SharedPreferences mappData){
        appData = mappData;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setMessage("Change the location of the bike?")

                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), ResetMap.class);
                        startActivity(intent);
                    }
                })

                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,	int which) {
                        getDialog().cancel();

                        BikeLocationChangeDialog.AvailableTask task = new BikeLocationChangeDialog.AvailableTask();
                        task.setContext(getContext());
                        task.setString("MyBikeAvailable.php", "phptest_MyBikeAvailable");

                        GpsInfo gps = new GpsInfo(getContext(), getActivity());

                        if (gps.isGetLocation()) {
                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();

                            task.execute();
                            Toast.makeText(getActivity(), "available", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "GPS가 켜져있지 않습니다 켜주세요", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).create();
    }

    class AvailableTask extends AbstractInsertData {
        @Override
        String doSomething(MyTaskParams... params){
            String uid = appData.getString("uid", "0");
            return "uid="+uid+"&bike_x="+latitude+"&bike_y="+longitude;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "POST response  - " + result);

            if (result != "true"){
                // 예외 처리
            }
        }
    }

}
