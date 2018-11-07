package company.velo.velo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Saelyne on 2017. 7. 23..
 */

public class ResetMap extends FragmentActivity implements OnMapReadyCallback{
    GoogleMap mMap;

    double latitude = 0.0;
    double longitude = 0.0;
    private SharedPreferences appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_location);

        appData = getSharedPreferences("appData", MODE_PRIVATE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.resetMap);
        mapFragment.getMapAsync(this); //getMapAsync must be called on the main thread.

        Button ResetBtn = (Button)findViewById(R.id.reset);

        ResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latitude == 0.0 && longitude == 0.0){
                    Toast.makeText(ResetMap.this, "위치를 선택하세요", Toast.LENGTH_SHORT).show();
                } else {

                    ResetMap.AvailableTask task = new ResetMap.AvailableTask();
                    task.setContext(ResetMap.this);
                    task.setString("MyBikeAvailable.php", "phptest_MyBikeAvailable");
                    task.execute();

                    Toast.makeText(ResetMap.this, "Reset location", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent (ResetMap.this, BikeListPage.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void onMapReady(final GoogleMap googleMap) {

        LatLng latLng = new LatLng(36.3712, 127.3625);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override

            public void onMapClick(LatLng latLng) {

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place));
                markerOptions.position(latLng);

                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.addMarker(markerOptions);

                latitude = latLng.latitude;
                longitude = latLng.longitude;
            }
        });

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

            //progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);

            if (result != "true"){
                // 예외 처리
            }
        }
    }
}

