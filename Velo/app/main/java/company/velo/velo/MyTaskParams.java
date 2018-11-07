package company.velo.velo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by jyp08 on 2017-07-20.
 */

public class MyTaskParams {

    LayoutInflater inflater;
    ViewGroup container;
    Activity activity;
    View view;
    String string;
    BikeListAdapter.BikeListAdapterOnClickHandler bikeListAdapterOnClickHandler;
    SharedPreferences sharedPreferences;
    Button button;

    double latitude;
    double longitude;

    MyTaskParams(LayoutInflater i, ViewGroup c, Activity a, View v, String s, BikeListAdapter.BikeListAdapterOnClickHandler b, SharedPreferences sh, Button bu) {
        inflater = i;
        container = c;
        activity = a;
        view = v;
        string = s;
        bikeListAdapterOnClickHandler = b;
        sharedPreferences = sh;
        button = bu;
    }

    void MyTaskParamsforLocation(double lat, double lng){
        latitude = lat;
        longitude = lng;
    }
}
