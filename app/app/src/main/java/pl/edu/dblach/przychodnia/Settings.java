package pl.edu.dblach.przychodnia;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.RadioButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;

public class Settings extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SeekBar myappointments_display_SeekBar=(SeekBar)findViewById(R.id.myappointments_display_seekbar);
        myappointments_display_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override public void onStopTrackingTouch(SeekBar seekBar){}
            @Override public void onStartTrackingTouch(SeekBar seekBar){}
            @Override public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){
                TextView myappointments_display_count=(TextView)findViewById(R.id.myappointments_display_count);
                myappointments_display_count.setText(String.valueOf(progress));
            }
        });

        loadPreferences();
        getConfiguration();
    }

    // menu

    public boolean onCreateOptionsMenu(Menu m) {
        MenuInflater i=getMenuInflater();
        i.inflate(R.menu.settings_menu,m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem i){
        switch(i.getItemId()){
            case R.id.save:
                savePreferences();
                break;
            case android.R.id.home: break;
            default: break;
        }
        return true;
    }

    //==============================================================================================
    // ładowanie i zapis

    public void loadPreferences(){
        TextView Hostname=(TextView)findViewById(R.id.hostname);
        TextView Username=(TextView)findViewById(R.id.username);
        TextView Password=(TextView)findViewById(R.id.password);
        SeekBar myappointments_display_SeekBar=(SeekBar)findViewById(R.id.myappointments_display_seekbar);
        RadioButton calendar_integration_yes=(RadioButton)findViewById(R.id.calendar_integration_yes);
        RadioButton calendar_integration_ask=(RadioButton)findViewById(R.id.calendar_integration_ask);
        RadioButton calendar_integration_no=(RadioButton)findViewById(R.id.calendar_integration_no);

        final Context ctx=this;
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        String sql_hostname=pref.getString("sql_hostname","");
        String sql_username=pref.getString("sql_username","");
        String sql_password=pref.getString("sql_password","");
        int myappointments_display_value=pref.getInt("myappointments_display_count",0);
        String calendar_integration=pref.getString("calendar_integration","");

        Hostname.setText(sql_hostname);
        Username.setText(sql_username);
        Password.setText(sql_password);
        myappointments_display_SeekBar.setProgress(myappointments_display_value);
        if(calendar_integration.equals("yes")) calendar_integration_yes.setChecked(true);
        if(calendar_integration.equals("ask")) calendar_integration_ask.setChecked(true);
        if(calendar_integration.equals("no")) calendar_integration_no.setChecked(true);
    }

    public void savePreferences(){
        String Hostname=((TextView)findViewById(R.id.hostname)).getText().toString();
        String Username=((TextView)findViewById(R.id.username)).getText().toString();
        String Password=((TextView)findViewById(R.id.password)).getText().toString();
        SeekBar myappointments_display_SeekBar=(SeekBar)findViewById(R.id.myappointments_display_seekbar);
        RadioButton calendar_integration_yes=(RadioButton)findViewById(R.id.calendar_integration_yes);
        RadioButton calendar_integration_ask=(RadioButton)findViewById(R.id.calendar_integration_ask);
        RadioButton calendar_integration_no=(RadioButton)findViewById(R.id.calendar_integration_no);

        //TODO: walidacja

        final Context ctx=this;
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString("sql_hostname",Hostname);
        edit.putString("sql_username",Username);
        edit.putString("sql_password",Password);
        edit.putInt("myappointments_display_count",myappointments_display_SeekBar.getProgress());
        if(calendar_integration_yes.isChecked()) edit.putString("calendar_integration","yes");
        if(calendar_integration_ask.isChecked()) edit.putString("calendar_integration","ask");
        if(calendar_integration_no.isChecked()) edit.putString("calendar_integration","no");
        edit.commit();

        Toast.makeText(this,getResources().getString(R.string.settings_saved),Toast.LENGTH_SHORT).show();
        finish();
    }

    public void getConfiguration(){
        final Context ctx=this;
        final SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        final String sql_hostname=pref.getString("sql_hostname","");
        String sql_username=pref.getString("sql_username","");
        String sql_password=pref.getString("sql_password","");

        if(sql_hostname.equals("")) return;

        OkHttpClient client=new OkHttpClient();
        String url=sql_hostname+"/get_configuration.php";
        RequestBody post_data=new FormBody.Builder().add("username",sql_username).add("password",sql_password).build();
        Request request=new Request.Builder().url(url).post(post_data).build();
        client.newCall(request).enqueue(new Callback(){
            @Override public void onFailure(Call call,IOException e){
                e.printStackTrace();
            }
            @Override public void onResponse(Call call,Response response) throws IOException{
                if(response.isSuccessful()){
                    final String r=response.body().string();
                    try{
                        JSONArray array=new JSONArray(r);
                        JSONObject o=array.getJSONObject(0);
                        final String edit_time=o.getString("czas_na_edycje");
                        SharedPreferences.Editor edit=pref.edit();
                        edit.putString("edit_time",edit_time);
                        edit.commit();
                        runOnUiThread(new Runnable(){public void run(){
                            TextView time_to_edit=(TextView)findViewById(R.id.time_to_edit_value);
                            time_to_edit.setText(edit_time+" "+getString(R.string.days));
                        }});
                    }
                    catch(JSONException e){};
                }
            }
        });
    }
}
