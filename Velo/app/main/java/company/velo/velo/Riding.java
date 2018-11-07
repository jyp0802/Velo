package company.velo.velo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class Riding extends AppCompatActivity {

    private Button finishBtn;
    private String bid, password;
    private boolean isAvailable = false;
    private SharedPreferences appData;

    private double latitude = 0.0, longitude = 0.0;

    FragmentManager fm = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riding);

        appData = getSharedPreferences("appData", MODE_PRIVATE);

        Intent intent = getIntent();
        bid = intent.getStringExtra("bid");
        password = intent.getStringExtra("password");

        PasswordDialog pd = PasswordDialog.newDialog(password);
        pd.show(fm, "Password Fragment");

        RentBike();

        finishBtn = (Button) findViewById(R.id.finish);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FinishBike();

                Intent intent = new Intent(Riding.this, BikeListPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    // 자전거가 대여 가능 상태인지 확인한다
    // 자전거가 '대여 가능' 상태라면: '사용중' 상태로 바꾸고 true return
    // 자전거가 '사용중', '대여 불가능' 상태라면: false return
    private void RentBike() {

        Riding.RentBikeTask task = new Riding.RentBikeTask();
        task.setContext(Riding.this);
        task.setString("RentBike.php", "phptest_RentBike");

        task.execute();

        /*

        // 이거 고쳐야되는데 귀찮으니까 스프린트 3으로 보내자: 여러 클라이언트가 한 서버에 접속했을때 자전거 빌리기에서 충돌이 일어날경우

        if (isAvailable == false) {
            Toast.makeText(this, "다른 사람에 의해 이미 대여된 자전거입니다", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Riding.this, BikeListPage.class);
            startActivity(intent);

            return;
        }
        */

        // toast 창 말고 확인버튼 눌러서 닫을 수 있는 팝업창으로 하면 좋겠군
        //Toast.makeText(this, "비밀번호: "+password, Toast.LENGTH_SHORT).show();
    }

    private void FinishBike() {
        Riding.FinishBikeTask task = new Riding.FinishBikeTask();
        task.setContext(Riding.this);
        task.setString("FinishBike.php", "phptest_FinishBike");

        GpsInfo gps = new GpsInfo(getApplicationContext(), Riding.this);

        if (gps.isGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            task.execute();
        } else {
            Toast.makeText(this, "GPS가 켜져있지 않습니다 켜주세요", Toast.LENGTH_SHORT).show();
        }
    }

    class FinishBikeTask extends AbstractInsertData {
        @Override
        String doSomething(MyTaskParams... params){
            String uid = appData.getString("uid", "0");
            return "uid="+uid+"&bid="+bid+"&bike_x="+latitude+"&bike_y="+longitude;
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

    class RentBikeTask extends AbstractInsertData {

        @Override
        String doSomething(MyTaskParams... params){
            String uid = appData.getString("uid", "0");
            return "uid="+ uid +"&bid=" + bid;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "POST response  - " + result);

            if (result == "true"){
                isAvailable = true;
            } else if (result == "false"){
                isAvailable = false;
            } else {
                // 예외 처리
            }
        }
    }
}
