package pl.edu.dblach.przychodnia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;

public class MainActivity extends AppCompatActivity  {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddNavigationView();
        CheckForSavedPreferences();
    }

    public void AddNavigationView(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView nav_view = findViewById(R.id.nav_view);
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SearchFragment()).commit();

        //nav_view.setCheckedItem(R.id.search);

        final Context ctx=this;
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent i;
                switch(menuItem.getItemId()){
                    case R.id.main:
                        break;
                    case R.id.appointments:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MyAppointments()).commit();
                        break;
                    case R.id.new_appointment:
                        i=new Intent(ctx,NewAppointment.class);
                        startActivity(i);
                        break;
                    case R.id.settings:
                        i=new Intent(ctx,Settings.class);
                        startActivity(i);
                        break;

                    /*
                    case R.id.test_cardview:
                        //i=new Intent(ctx,Test_CardViewActivity.class);
                        //startActivity(i);
                        break;
                    case R.id.test_cardview_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Test_CardView_Fragment()).commit();
                        break;
                    /*
                    case R.id.call:
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CallFragment()).commit();
                        break;
                    case R.id.share:
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
                        break;
                    case R.id.send:
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SendFragment()).commit();

                        break;
                    case R.id.close:
                        finish();
                        break;
                        */

                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public void CheckForSavedPreferences(){
        final Context ctx=this;
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        String sql_hostname=pref.getString("sql_hostname","");
        String sql_username=pref.getString("sql_username","");
        String sql_password=pref.getString("sql_password","");
        int myappointments_display_count=pref.getInt("myappointments_display_count",0);

        SharedPreferences.Editor edit=pref.edit();
        if(myappointments_display_count==0) edit.putInt("myappointments_display_count",10);
        edit.commit();

        if(sql_hostname==""||sql_username==""||sql_password==""){
            AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle(getResources().getString(R.string.error_no_login_data_title));
            alertDialog.setMessage(getResources().getString(R.string.error_no_login_data_message));
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.close),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.error_no_login_data_open_settings),
                    new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog,int which){
                            Intent i=new Intent(ctx,Settings.class);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}

/*
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
*/