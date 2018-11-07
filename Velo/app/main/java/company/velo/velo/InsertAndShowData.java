package company.velo.velo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jyp08 on 2017-07-20.
 */

public class InsertAndShowData extends AbstractInsertData {
    View v;
    Activity a;
    BikeListAdapter.BikeListAdapterOnClickHandler b;

    @Override
    String doSomething(MyTaskParams... params){
        v = params[0].view;
        a = params[0].activity;
        b = params[0].bikeListAdapterOnClickHandler;
        String uid = params[0].string;

        double latitude = params[0].latitude;
        double longitude = params[0].longitude;

        return "uid="+uid+"&bike_x="+latitude+"&bike_y="+longitude;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        //progressDialog.dismiss();
        Log.d(TAG, "POST response  - " + result);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(a));
        BikeListAdapter bikeListAdapter = new BikeListAdapter(b);
        recyclerView.setAdapter(bikeListAdapter);
        bikeListAdapter.setBikeListData(new Gson().fromJson(result, String[][].class));
    }
}