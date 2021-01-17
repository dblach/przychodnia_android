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
import android.view.View;
import android.widget.TextView;
import okhttp3.Callback;
import okhttp3.Request;
import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import okhttp3.Call;
import okhttp3.Callback;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    String user="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddNavigationView();
        CheckForSavedPreferences();
        CheckForInternet();
    }

    public void AddNavigationView(){
        final Context ctx=this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView nav_view = findViewById(R.id.nav_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MainPageFragment()).commit();

        nav_view.setCheckedItem(R.id.main);

        Boolean menu_active=false;
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        String h=pref.getString("sql_hostname","");
        String u=pref.getString("sql_username","");
        String p=pref.getString("sql_password","");
        if(!h.equals("")&&!u.equals("")&&!p.equals("")) menu_active=true;
        final Boolean m=menu_active;

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent i;
                switch(menuItem.getItemId()){
                    case R.id.main:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MainPageFragment()).commit();
                        break;
                    case R.id.news:
                        if(m) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NewsFragment()).commit();
                        break;
                    case R.id.appointments:
                        if(m) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MyAppointments()).commit();
                        break;
                    case R.id.new_appointment:
                        if(m){
                            i=new Intent(ctx,NewAppointment.class);
                            startActivity(i);
                        }
                        break;
                    case R.id.settings:
                        i=new Intent(ctx,Settings.class);
                        startActivity(i);
                        break;
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
        String calendar_integration=pref.getString("calendar_integration","");

        SharedPreferences.Editor edit=pref.edit();
        if(myappointments_display_count==0) edit.putInt("myappointments_display_count",10);
        if(calendar_integration.equals("")) edit.putString("calendar_integration","ask");
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
        else{
            getUserFullName();
        }

    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    public void getUserFullName(){
        Context ctx=this;
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        String sql_hostname=pref.getString("sql_hostname","");
        String sql_username=pref.getString("sql_username","");
        String sql_password=pref.getString("sql_password","");
        OkHttpClient client=new OkHttpClient();
        String url=sql_hostname+"/get_user.php";
        RequestBody post_data=new FormBody.Builder().add("username",sql_username).add("password",sql_password).build();
        Request request=new Request.Builder().url(url).post(post_data).build();
        client.newCall(request).enqueue(new Callback(){
            @Override public void onFailure(Call call,IOException e){}
            @Override public void onResponse(Call call,Response response) throws IOException{
                if(response.isSuccessful()){
                    String r=response.body().string();
                    try{
                        JSONArray array=new JSONArray(r);
                        JSONObject o=array.getJSONObject(0);
                        final String username=o.getString("user");
                        MainActivity.this.runOnUiThread(new Runnable(){public void run(){
                            NavigationView nv=(NavigationView)findViewById(R.id.nav_view);
                            View h=nv.getHeaderView(0);
                            TextView u=(TextView)h.findViewById(R.id.user);
                            u.setText(username);
                        }});
                    }catch(JSONException e){}
                }
            }
        });
    }

    public void CheckForInternet(){
        if (isNetwork(MainActivity.this)){
            Toast.makeText(MainActivity.this, "Internet connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Internet not connected", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetwork(Context context){
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}