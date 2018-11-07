package company.velo.velo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import static company.velo.velo.VeloFragment.appData;

/**
 * Created by jyp08 on 2017-07-21.
 */

public class InsertDataMyBikeCheck extends AbstractInsertData {

    String uid;
    SharedPreferences appData;

    ViewPager viewPager;
    TabLayout tabLayout;
    BikeListPage.VeloMainPagerAdapter adapter;
    BikeListPage.VeloMainPagerAdapter_bikeless adapter_bikeless;

    View view;

    void setBikeCheck(ViewPager mviewPager, TabLayout mtabLayout, BikeListPage.VeloMainPagerAdapter madapter, BikeListPage.VeloMainPagerAdapter_bikeless madapter_bikeless){
        viewPager = mviewPager;
        tabLayout = mtabLayout;
        adapter = madapter;
        adapter_bikeless = madapter_bikeless;
        Log.d("setup","finish");
    }

    @Override
    String doSomething(MyTaskParams... params){
        appData = params[0].sharedPreferences;
        uid = appData.getString("uid", "0");
        return "uid="+uid;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        //progressDialog.dismiss();
        Log.d(TAG, "POST response  - " + result);

        if(result.equals("1")){ // bike 있음
            viewPager.setAdapter(adapter);
            Log.d("bike","있음");
        } else { // bike 없음
            viewPager.setAdapter(adapter_bikeless);
            Log.d("bike","없음");
        }
        tabLayout.setupWithViewPager(viewPager);

    }
}