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

        final Context ctx=this;
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        String sql_hostname=pref.getString("sql_hostname","");
        String sql_username=pref.getString("sql_username","");
        String sql_password=pref.getString("sql_password","");
        int myappointments_display_value=pref.getInt("myappointments_display_count",0);

        Hostname.setText(sql_hostname);
        Username.setText(sql_username);
        Password.setText(sql_password);
        myappointments_display_SeekBar.setProgress(myappointments_display_value);
    }

    public void savePreferences(){
        String Hostname=((TextView)findViewById(R.id.hostname)).getText().toString();
        String Username=((TextView)findViewById(R.id.username)).getText().toString();
        String Password=((TextView)findViewById(R.id.password)).getText().toString();
        SeekBar myappointments_display_SeekBar=(SeekBar)findViewById(R.id.myappointments_display_seekbar);

        //TODO: walidacja

        final Context ctx=this;
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString("sql_hostname",Hostname);
        edit.putString("sql_username",Username);
        edit.putString("sql_password",Password);
        edit.putInt("myappointments_display_count",myappointments_display_SeekBar.getProgress());
        edit.commit();

        Toast.makeText(this,getResources().getString(R.string.settings_saved),Toast.LENGTH_SHORT).show();
        finish();
    }
}
