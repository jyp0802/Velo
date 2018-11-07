package company.velo.velo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.acl.Owner;

public class BikeInformation extends FragmentActivity implements OnMapReadyCallback {

    private Button cancelBtn, rentBtn;
    private String user_name, bid, b_dist;

    double bike_lat, bike_lng;

    TextView OwnerName, BikeDist;

    BikeInformation.BikeInfoTask.Instant obj;
    MapFragment mapFragment;

    ImageView bikeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike_information);

        // 이전 Acitivity(BikeListPage)로부터 받아온 자전거의 uid, bid
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        bid = intent.getStringExtra("bid");
        b_dist = intent.getStringExtra("bike_dist");

        OwnerName = (TextView) findViewById(R.id.OwnerName);
        OwnerName.setText(user_name);

        BikeDist = (TextView) findViewById(R.id.DistInfo);
        BikeDist.setText(b_dist);

        bikeImageView = (ImageView) findViewById(R.id.imageView2);

        BikeInfo();

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        cancelBtn = (Button) findViewById(R.id.cancel);
        rentBtn = (Button) findViewById(R.id.rent);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BikeInformation.this, BikeListPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        rentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BikeInformation.this, Riding.class);
                intent.putExtra("bid", bid);
                intent.putExtra("password", obj.bike_pw);
                startActivity(intent);
            }

        });
    }

    private void BikeInfo() {
        BikeInformation.BikeInfoTask task = new BikeInformation.BikeInfoTask();
        task.setContext(BikeInformation.this);
        task.setString("BikeInfo.php", "phptest_BikeInfo");

        task.execute();
    }

    class BikeInfoTask extends AbstractInsertData {

        class Instant {
            public String bike_type, bike_size, bike_gender, bike_pw, bike_x, bike_y, bike_photo, bike_status;
        }

        @Override
        String doSomething(MyTaskParams... params){
            return "bid=" + bid;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);

            obj = new Gson().fromJson(result, BikeInformation.BikeInfoTask.Instant.class);
            mapFragment.getMapAsync(BikeInformation.this);

            final Handler handler = new Handler();

            new Thread() {
                @Override
                public void run() {
                    try {
                        Log.d("BikePhoto", "ASDF");
                        obj.bike_photo = obj.bike_photo.replaceAll("\"","");
                        URL url = new URL("http://10.0.2.2/~jaeyoon/uploads/"+obj.bike_photo);
                        URLConnection conn = url.openConnection();
                        conn.connect();
                        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                        final Bitmap bm = BitmapFactory.decodeStream(bis);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                bikeImageView.setImageBitmap(bm);
                            }
                        });
                        bis.close();
                    } catch (Exception e) {
                        Log.d("BikePhoto", "Exception");
                        Log.d("BikePhoto", e.toString());
                    }

                }
            }.start();
        }
    }

    // 지도에 자전거 위치 띄우기

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // ↑매개변수로 GoogleMap 객체가 넘어옵니다.

        bike_lat = Double.parseDouble(obj.bike_x);
        bike_lng = Double.parseDouble(obj.bike_y);

        LatLng latLng = new LatLng(bike_lat, bike_lng);

        googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker Title").snippet("Marker Description"));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
