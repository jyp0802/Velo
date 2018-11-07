package company.velo.velo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jyp08 on 2017-07-20.
 */

public class VeloFragment extends Fragment implements BikeListAdapter.BikeListAdapterOnClickHandler, OnMapReadyCallback {
    private static final String TAB_POSITION = "tab_position";
    private static final String UID = "uid_position";
    MapView mMapView;
    private GoogleMap googleMap;
    static Context mContext;
    static Activity mActivity;
    static SharedPreferences appData;
    private static final int PICK_IMG = 100;
    ImageView bikeImage;
    Uri imageUri;
    SwipeRefreshLayout mSwipeRefreshLayout;

    LayoutInflater inflater;
    ViewGroup container;
    Bundle savedInstanceState;

    private String GpsErrorMsg = "GPS를 켜주세요";

    static int HasBike = 0;
    Switch available;

    VeloFragment.BikeInfoTask.Instant obj;
    VeloFragment.BikePasswordTask.Instant obj1;

    private static final int[] RB_ID = {1000, 1001, 1002, 1003};
    private static final int RB_ID_0 = 1000;

    static String bikeFileName;

    public VeloFragment() {
    }

    public static VeloFragment newInstance(int mHasBike, int tabPosition, String uid, Activity ac, Context co, SharedPreferences sh) {
        VeloFragment fragment = new VeloFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, tabPosition);
        args.putString(UID, uid);
        fragment.setArguments(args);
        mActivity = ac;
        mContext = co;
        appData = sh;
        HasBike = mHasBike;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater minflater, ViewGroup mcontainer, Bundle msavedInstanceState) {

        inflater = minflater;
        container = mcontainer;
        savedInstanceState = msavedInstanceState;

        Bundle args = getArguments();
        int tabPosition = args.getInt(TAB_POSITION);
        final String uid = args.getString(UID);

        String[][] mData;

        if (tabPosition == 0) // Borrow Tab
        {
            final View v = inflater.inflate(R.layout.fragment_list_view, container, false);
            mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    InsertAndShowData task = new InsertAndShowData();
                    task.setContext(mContext);
                    task.setString("BikeList.php", "phptest_BikeList");
                    MyTaskParams params = new MyTaskParams(null, null, getActivity(), v, uid, VeloFragment.this, null, null);

                    GpsInfo gps = new GpsInfo(mContext, mActivity);

                    if (gps.isGetLocation()) {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        params.MyTaskParamsforLocation(latitude, longitude);
                        task.execute(params);

                    } else {
                        Toast.makeText(mActivity, GpsErrorMsg, Toast.LENGTH_SHORT).show();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });

            InsertAndShowData task = new InsertAndShowData();
            task.setContext(mContext);
            task.setString("BikeList.php", "phptest_BikeList");

            MyTaskParams params = new MyTaskParams(null, null, getActivity(), v, uid, this, null, null);

            GpsInfo gps = new GpsInfo(mContext, mActivity);

            if (gps.isGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                params.MyTaskParamsforLocation(latitude, longitude);
                task.execute(params);

            } else {
                Toast.makeText(mActivity, GpsErrorMsg, Toast.LENGTH_SHORT).show();
            }

            return v;
        }
        else if (tabPosition == 1) // My Bike Tab
        {
            if (HasBike == 1) // 등록한 자전거가 있음
            {
                return MyBikeView();

            } else { // 등록한 자전거가 없음

                return RegisterBikeView();
            }
        }
        else if (tabPosition == 2) // My Page Tab
        {
            View v = inflater.inflate(R.layout.mypage, container, false);

            InsertDataMypage task = new InsertDataMypage();

            task.setContext(mContext);
            task.setString("MyPage.php", "phptest_Mypage");

            MyTaskParams params = new MyTaskParams(null, null, null, null, uid, null, appData, null);
            task.execute(params);

            TextView name = (TextView) v.findViewById(R.id.mypage_name);
            TextView ID = (TextView)v.findViewById(R.id.mypage_ID);
            TextView email = (TextView) v.findViewById(R.id.mypage_email);
            TextView pn = (TextView) v.findViewById(R.id.mypage_pn);

            name.setText(appData.getString("user_name", "error"));
            ID.setText(appData.getString("user_id", "error"));
            email.setText(appData.getString("user_email", "error"));
            pn.setText(appData.getString("user_phone", "error"));
            if (HasBike==1)
            {
                BikePasswordTask btask = new BikePasswordTask();
                btask.setContext(mContext);
                btask.setString("BikePasswordInfo.php", "phptest_BikePasswordInfo");
                MyTaskParams params1 = new MyTaskParams(null,null,null,v,null,null,null,null);
                btask.execute(params1);
            }

            Button velowingButton = (Button) v.findViewById(R.id.velowing_button);
            Button velowerButton = (Button) v.findViewById(R.id.velower_button);

            velowingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, VelowingListPage.class);
                    intent.putExtra("velower", false);
                    startActivity(intent);
                }
            });

            velowerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, VelowingListPage.class);
                    intent.putExtra("velower", true);
                    startActivity(intent);
                }
            });

            return v;
        } else {
            mData = new String[][]{{"not", "initialized!"}};
            View v = inflater.inflate(R.layout.fragment_list_view, container, false);
            RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            BikeListAdapter bikeListAdapter = new BikeListAdapter(/*mData, */this);
            recyclerView.setAdapter(bikeListAdapter);
            bikeListAdapter.setBikeListData(mData);

            return v;
        }
    }

    // MyBikeView, RegisterBikeView: MyBike Tab을 클릭했을때 View 반환하는 method

    private View MyBikeView () {
        final View rootView = inflater.inflate(R.layout.my_bike, container, false);
        available = (Switch) rootView.findViewById(R.id.switch1);
        available.setChecked(true);

        final String uid = appData.getString("uid", "0");

        InsertDataMyBike task = new InsertDataMyBike();
        task.setContext(mContext);
        task.setString("MyBike.php", "phptest_MyBike");
        MyTaskParams params = new MyTaskParams(null, null, null, null, uid, null, null, null);
        task.execute(params);

        available.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    BikeLocationChangeDialog bd = new BikeLocationChangeDialog();
                    bd.setAppData(appData);
                    bd.show(getFragmentManager(), "Change Location");
                } else {
                    InsertDataMyBike task = new InsertDataMyBike();
                    task.setContext(mContext);
                    task.setString("MyBikeNotAvailable.php", "phptest_MyBikeNotAvailable");
                    MyTaskParams params = new MyTaskParams(null, null, null, null, uid, null, null, null);
                    task.execute(params);
                }
            }
        });

        mMapView = (MapView) rootView.findViewById(R.id.map2);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        VeloFragment.BikeInfoTask infotask = new VeloFragment.BikeInfoTask();
        infotask.setContext(mContext);
        infotask.setString("MyBikeInfo.php", "phptest_MyBikeInfo");

        infotask.execute();

        return rootView;
    }

    private View RegisterBikeView() {
        View v = inflater.inflate(R.layout.register_bicycle, container, false);

        bikeImage = (ImageView) v.findViewById(R.id.bike_image);
        bikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMG);
            }
        });

        RadioButton[] rb = new RadioButton[4];

        rb[0] = (RadioButton)v.findViewById(R.id.bike1);
        rb[1] = (RadioButton)v.findViewById(R.id.bike2);
        rb[2] = (RadioButton)v.findViewById(R.id.bike3);
        rb[3] = (RadioButton)v.findViewById(R.id.bike4);

        for (int i = 0; i < 4; i ++){
            rb[i].setId(RB_ID[i]);
        }

        Button buttonRegister = (Button) v.findViewById(R.id.button);
        final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.bike_type_list);
        final EditText Edittxt = (EditText) v.findViewById(R.id.lock_edit);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bike_type_index = radioGroup.getCheckedRadioButtonId();

                String pw = Edittxt.getText().toString();
                if (pw.equals("")){
                    Toast.makeText(mActivity, "input password", Toast.LENGTH_SHORT).show();
                    return;
                }

                int bike_type = -1;

                switch(bike_type_index) {
                    case 1000:
                        bike_type = 0; break;
                    case 1001:
                        bike_type = 1; break;
                    case 1002:
                        bike_type = 2; break;
                    case 1003:
                        bike_type = 3; break;
                    default:
                        Toast.makeText(mActivity, "select bike type", Toast.LENGTH_SHORT).show();
                        return;
                }

                InsertDataRegisterBike task = new InsertDataRegisterBike();
                task.setContext(mContext);
                task.setString("RegisterBike.php", "phptest_RegisterBike");
                MyTaskParams params = new MyTaskParams(null, null, null, null, pw, null, null, null);
                task.bike_type = bike_type;
                task.execute(params);

                Toast.makeText(mActivity, "Registration is complete", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent (mContext, BikeListPage.class);
                startActivity(intent);
            }
        });
        return v;
    }

    class InsertDataMyBike extends AbstractInsertData {

        @Override
        String doSomething(MyTaskParams... params){
            return "uid=" + params[0].string;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "POST response  - " + result);

            if(result.equals("1")){
                available.setChecked(true);
            } else {
                available.setChecked(false);
            }
        }
    }

    class BikeInfoTask extends AbstractInsertData {

        class Instant {
            public String bike_x, bike_y;
        }

        @Override
        String doSomething(MyTaskParams... params){
            String uid = appData.getString("uid", "0");
            return "uid="+uid;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);

            obj = new Gson().fromJson(result, VeloFragment.BikeInfoTask.Instant.class);
            mMapView.getMapAsync(VeloFragment.this);
        }
    }

    class BikePasswordTask extends AbstractInsertData {
        View mView;

        class Instant {
            public String bike_pw;
        }

        @Override
        String doSomething(MyTaskParams... params){
            String uid = appData.getString("uid", "0");
            mView = params[0].view;
            return "uid=" + uid;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);

            obj1 = new Gson().fromJson(result, VeloFragment.BikePasswordTask.Instant.class);
            TextView pw  = (TextView) mView.findViewById(R.id.mypage_password);
            pw.setText(obj1.bike_pw);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // ↑매개변수로 GoogleMap 객체가 넘어옵니다.

        double bike_lat, bike_lng;

        bike_lat = Double.parseDouble(obj.bike_x);
        bike_lng = Double.parseDouble(obj.bike_y);

        LatLng latLng = new LatLng(bike_lat, bike_lng);

        googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker Title").snippet("Marker Description"));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onClick(String[] bikeData) {
        Intent intent = new Intent (mContext, BikeInformation.class);
        intent.putExtra("user_name", bikeData[0]);
        intent.putExtra("bid", bikeData[1]);
        intent.putExtra("bike_dist", bikeData[5]);
        startActivity(intent);
    }

    //@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMG) {
            imageUri = data.getData();
            bikeImage.setImageURI(imageUri);
            Log.d("IMAGE", "uri : "+imageUri);
            Log.d("IMAGE", "path : "+getPathFromUri(imageUri));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Android OS Version 6.0 이상에서 동작할 내용
                 int permissionCheck = ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE);
                if(permissionCheck== PackageManager.PERMISSION_DENIED){
                    // 권한 없음
                    // Activity에서 실행하는경우
                     if (ContextCompat.checkSelfPermission(mActivity,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                         // 이 권한을 필요한 이유를 설명해야하는가?
                          if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                              // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                              // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다
                              uploadFile(imageUri);
                              bikeFileName=new File(getPathFromUri(imageUri)).getName();
                          } else {
                              ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                              // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다
                               return;
                          }
                     }
                }else{
                    // 권한 있음
                    Log.d("UPLOAD IMAGE", "ASYNCTASK1");
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            Log.d("UPLOAD IMAGE", "response code - "+uploadFile(imageUri));
                            return null;
                        }
                    }.execute();
                    bikeFileName=new File(getPathFromUri(imageUri)).getName();
                }
            } else {
                // Android OS Version 6.0 미만의 OS에서 동작할 내용
                Log.d("UPLOAD IMAGE", "ASYNCTASK2");
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Log.d("UPLOAD IMAGE", "response code - "+uploadFile(imageUri));
                        return null;
                    }
                }.execute();
                bikeFileName=new File(getPathFromUri(imageUri)).getName();
            }
        }
    }

    public int uploadFile(Uri sourceFileUri) {
        final String uploadFilePath=getPathFromUri(sourceFileUri);
        //        final String uploadFileName="";
        String upLoadServerUri="http://10.0.2.2/~jaeyoon/UploadToServer.php";
        //        String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(uploadFilePath);
        if (!sourceFile.isFile()) {
            //dialog.dismiss();
            Log.e("uploadFile", "Source File not exist :" + uploadFilePath);
            mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    //messageText.setText("Source File not exist :" +uploadFilePath + "" + uploadFileName);
                }
            });
            return 0;
        } else {
            int serverResponseCode = 0;
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", uploadFilePath);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ uploadFilePath+ "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                Log.i("uploadFile", "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseCode);
                if(serverResponseCode == 200) {
                    mActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n" +uploadFilePath;
                            Log.d("UPLOAD IMAGE", msg);
                            //messageText.setText(msg);
                            Toast.makeText(mActivity, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                    }                    });                }
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {
                // dialog.dismiss();
                ex.printStackTrace();
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        //                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(mActivity, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                //                dialog.dismiss();
                e.printStackTrace();
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        //                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(mActivity, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
            }
            //            dialog.dismiss();
            return serverResponseCode;
        }
    }

    public String getPathFromUri(Uri uri){
        Cursor cursor = mActivity.getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        cursor.close();
        return path;
    }

    public class UploadFileName extends AbstractInsertData {
        @Override
        String doSomething(MyTaskParams... params) {
            //return "uid="+appData.getString("uid", "0")+"&fileName=\""+params[0].string+"\"";
            return "uid="+appData.getString("uid", "0")+"&fileName="+params[0].string;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "POST response  - " + result);
        }
    }
}
