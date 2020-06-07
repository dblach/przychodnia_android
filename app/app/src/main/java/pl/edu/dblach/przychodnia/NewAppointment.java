package pl.edu.dblach.przychodnia;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import android.view.View;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.Context;

public class NewAppointment extends AppCompatActivity{
    private ViewPager viewPager;

    @Override protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        TabLayout tabLayout=(TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.new_appointment_tab_clinic_label)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.new_appointment_tab_doctor_label)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.new_appointment_tab_time_label)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager=(ViewPager)findViewById(R.id.pager);
        final NewAppointment_PagerAdapter adapter = new NewAppointment_PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        SharedPreferences pref=this.getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        pref.edit().putString("NewAppointment_clinic_id","");
        pref.edit().putString("NewAppointment_doctor_id","");
        pref.edit().commit();
    }
}
