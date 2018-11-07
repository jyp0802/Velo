package company.velo.velo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by jyp08 on 2017-07-08.
 */

public class BikeListPage extends AppCompatActivity {

    private static SharedPreferences appData;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow_main);

        appData = getSharedPreferences("appData", MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //3줄메뉴
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.navigation_item_logout:

                        SharedPreferences.Editor editor = appData.edit();
                        editor.putBoolean("SAVE_LOGIN_DATA", false);
                        editor.apply();

                        Toast.makeText(BikeListPage.this, "Log Out", Toast.LENGTH_LONG).show();

                        Intent intent_logout = new Intent (BikeListPage.this, MainActivity.class);
                        //intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_logout);
                        finish();
                        break;

                    case R.id.navigation_item_find:
                        Intent intent_find = new Intent (BikeListPage.this, FindUser.class);
                        startActivity(intent_find);
                        break;

                    case R.id.navigation_item_developers:
                        Intent intent_dev = new Intent(BikeListPage.this, DeveloperInformation.class);
                        startActivity(intent_dev);
                }

                return true;
            }
        });

        BikeListPage.VeloMainPagerAdapter adapter = new BikeListPage.VeloMainPagerAdapter(getSupportFragmentManager(), this, this);
        BikeListPage.VeloMainPagerAdapter_bikeless adapter_bikeless = new BikeListPage.VeloMainPagerAdapter_bikeless(getSupportFragmentManager(), this, this);

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);

        InsertDataMyBikeCheck task = new InsertDataMyBikeCheck();
        task.setBikeCheck(viewPager, tabLayout, adapter, adapter_bikeless);

        task.setContext(getApplicationContext());
        task.setString("CheckBike.php", "phptest_CheckBike");

        MyTaskParams params = new MyTaskParams(null, null, null, null, null, null, appData, null);
        task.execute(params);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                // 3줄 메뉴 버튼
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Adapter for bike 있는 사람
    class VeloMainPagerAdapter extends PagerAdapter {

        public VeloMainPagerAdapter(FragmentManager fm, Activity ac, Context co) {
            super(fm);
            activity = ac;
            context = co;
        }

        @Override
        public Fragment getItem(int position) {
            return VeloFragment.newInstance(1, position, appData.getString("uid", "0"), activity, context, appData);
        }
    }

    // Adapter for bike 없는 사람
    class VeloMainPagerAdapter_bikeless extends PagerAdapter {

        public VeloMainPagerAdapter_bikeless(FragmentManager fm, Activity ac, Context co) {
            super(fm);
            activity = ac;
            context = co;
        }

        @Override
        public Fragment getItem(int position) {
            return VeloFragment.newInstance(0, position, appData.getString("uid", "0"), activity, context, appData);
        }
    }

    abstract class PagerAdapter extends FragmentStatePagerAdapter {

        Activity activity;
        Context context;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Borrow";
                case 1:
                    return "My Bike";
                case 2:
                    return "My Page";
            }
            return "Tab " + position;
        }
    }
}