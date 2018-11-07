package company.velo.velo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class VelowingListPage extends AppCompatActivity implements VelowListAdapter.VelowListAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private VelowListAdapter mVelowListAdapter;

    private SharedPreferences appData;

    private String[][] VelowInfos;

    private String TAG = "VelowingListPage.java";
    String FindTxt;
    String uid;
    boolean isVelower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isFindUser = getIntent().getExtras().getBoolean("finduser");
        isVelower = getIntent().getExtras().getBoolean("velower");
        FindTxt = getIntent().getExtras().getString("finduser_txt");



        Log.d(TAG, "execute onCreate()");
        setContentView(R.layout.velow_page);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_velow);
        TextView title = (TextView) findViewById(R.id.velow_title);

        Log.d(TAG, "RecyclerView assign");

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        uid = appData.getString("uid", "0");

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mVelowListAdapter = new VelowListAdapter(this, appData.getString("user_id", "error"));
        mRecyclerView.setAdapter(mVelowListAdapter);

        if(isFindUser){
            title.setText("Find User");

            VelowingListPage.FindData task = new VelowingListPage.FindData();
            task.setContext(VelowingListPage.this);
            task.setString("RequestFindUserList.php", "phptest_RequestFindUserList");
            task.execute();

        } else {
            title.setText(isVelower ? "Velower" : "Velowing");

            VelowingListPage.InsertData task = new VelowingListPage.InsertData();
            task.setContext(VelowingListPage.this);
            task.setString(isVelower ? "RequestVelowerList.php" : "RequestVelowingList.php", isVelower ? "phptest_RequestVelowerList" : "phptest_RequestVelowingList");
            task.execute();
        }
    }

    private void loadVelowListData() {
        new FetchVelowListTask().execute();
    }

    @Override
    public void onClick(String[] velowData, Button button) {
        VelowingListPage.RequestInsertData task = new VelowingListPage.RequestInsertData();
        task.setContext(VelowingListPage.this);
        task.setString("RequestVelow.php", "phptest_RequestVelow");

        MyTaskParams params = new MyTaskParams(null, null, null, null, velowData[0], null, null, button);
        task.execute(params);
    }

    public class FetchVelowListTask extends AsyncTask<Void, Void, String[][]> {

        @Override
        protected String[][] doInBackground(Void... params) {
            return VelowInfos;
        }

        @Override
        protected void onPostExecute(String[][] velowData) {
            if (velowData != null) {
                mVelowListAdapter.setVelowListData(velowData);
            }
        }
    }

    class InsertData extends AbstractInsertData {

        @Override
        String doSomething(MyTaskParams... params){
            return "uid=" + uid;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "POST response  - " + result);

            VelowInfos = new Gson().fromJson(result, String[][].class);

            loadVelowListData();
        }
    }

    class RequestInsertData extends AbstractInsertData {
        private String addid;
        private Button mButton;

        @Override
        String doSomething(MyTaskParams... params){
            addid = params[0].string;
            mButton = params[0].button;
            return "uid="+uid+"&addid="+params[0].string;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "POST response  - " + result);

            if(result.equals("1")) {
                Toast.makeText(VelowingListPage.this, "Velowed", Toast.LENGTH_SHORT).show();
                mButton.setText("Velowing");
            }
            else if(result.equals("0")) {
                RequestUnvelow task = new RequestUnvelow();
                task.setContext(VelowingListPage.this);
                task.setString("RequestUnvelow.php", "phptest_RequestUnvelow");
                MyTaskParams params = new MyTaskParams(null, null, null, null, addid, null, null, mButton);
                task.execute(params);
            }
        }
    }

    class RequestUnvelow extends AbstractInsertData {
        private Button mButton;

        @Override
        String doSomething(MyTaskParams... params){
            mButton = params[0].button;
            return "uid="+uid+"&addid="+params[0].string;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("1")) {
                Toast.makeText(VelowingListPage.this, "Unvelowed", Toast.LENGTH_SHORT).show();
                mButton.setText("Velow");
                if (!isVelower) {
                    VelowingListPage.InsertData task = new VelowingListPage.InsertData();
                    task.setContext(VelowingListPage.this);
                    task.setString("RequestVelowingList.php", "phptest_RequestVelowingList");
                    task.execute();
                }
            }
            Log.d(TAG, "POST response  - " + result);
        }
    }

    class FindData extends AbstractInsertData {

        @Override
        String doSomething(MyTaskParams... params){
            return "findtxt="+FindTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "POST response  - " + result);

            VelowInfos = new Gson().fromJson(result, String[][].class);

            loadVelowListData();
        }
    }

}
