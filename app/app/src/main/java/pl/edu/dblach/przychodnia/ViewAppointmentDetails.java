package pl.edu.dblach.przychodnia;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import android.content.Context;
import android.content.SharedPreferences;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import android.widget.TableLayout;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.text.ParseException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ViewAppointmentDetails extends AppCompatActivity{
    private int EditRequestCode=0;
    private String idw;
    private String data;
    private String godzina;
    private String poradnia;
    private String lekarz;
    private String lekarz_id;
    private String pomieszczenie;
    private String odwolana;
    private String notatka;
    private Boolean HideBtnEdit=false;
    private Menu menu;

    @Override protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment_details);
        ReceiveData();
    }

    private void ReceiveData(){
        final Context ctx=getApplicationContext();
        Intent i=getIntent();
        final SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        String sql_hostname=pref.getString("sql_hostname","");
        String sql_username=pref.getString("sql_username","");
        String sql_password=pref.getString("sql_password","");
        idw=i.getStringExtra("id");
        int czas_na_edycje=Integer.valueOf(pref.getString("edit_time",""));

        OkHttpClient client=new OkHttpClient();
        String url=sql_hostname+"/get_appointment_details.php";
        RequestBody post_data=new FormBody.Builder().add("username",sql_username).add("password",sql_password).add("id",idw).build();
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
                        data=o.getString("data");
                        godzina=o.getString("czas_rozpoczecia")+" - "+o.getString("czas_zakonczenia");
                        poradnia=o.getString("poradnia");
                        lekarz=o.getString("lekarz");
                        lekarz_id=o.getString("lekarz_id");
                        pomieszczenie=o.getString("pomieszczenie");
                        odwolana=o.getString("odwolana").replace("0",getResources().getString(R.string.no)).replace("1",getResources().getString(R.string.yes));
                        notatka=o.getString("notatka");

                        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss");
                        Date data_wizyty=new Date();
                        try{data_wizyty=df.parse(data+", "+godzina);} catch(ParseException e){}
                        final long diff=(data_wizyty.getTime()-new Date().getTime())/(86400000);
                        if(diff<Integer.parseInt(pref.getString("edit_time",""))) HideBtnEdit=true;

                        runOnUiThread(new Runnable(){public void run(){
                            TextView loading=(TextView)findViewById(R.id.loading);
                            TableLayout table=(TableLayout)findViewById(R.id.table);
                            TextView date=(TextView)findViewById(R.id.date_value);
                            TextView time=(TextView)findViewById(R.id.time_value);
                            TextView clinic=(TextView)findViewById(R.id.clinic_value);
                            TextView doctor=(TextView)findViewById(R.id.doctor_value);
                            TextView room=(TextView)findViewById(R.id.room_value);
                            TextView cancelled=(TextView)findViewById(R.id.cancelled_value);
                            TextView note=(TextView)findViewById(R.id.note_value);

                            date.setText(data);
                            time.setText(godzina);
                            clinic.setText(poradnia);
                            doctor.setText(lekarz);
                            room.setText(pomieszczenie);
                            cancelled.setText(odwolana);
                            note.setText(notatka);

                            loading.setVisibility(View.GONE);
                            table.setVisibility(View.VISIBLE);

                            if(HideBtnEdit) menu.findItem(R.id.edit).setVisible(false);
                        }});
                    }
                    catch(JSONException e){};
                }
            }
        });
    }

    public void setResult(View v){
        Intent i=new Intent();
        i.putExtra("changed","0");
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.viewappointmentdetails_menu,m);
        menu=m;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem i){
        switch(i.getItemId()){
            case R.id.edit:
                Intent in=new Intent(this,EditAppointment.class);
                in.putExtra("idw",idw);
                in.putExtra("idl",lekarz_id);
                in.putExtra("data",data);
                in.putExtra("godzina",godzina);
                startActivityForResult(in,EditRequestCode);
                break;
            default: break;
        }
        return true;
    }

    @Override protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==EditRequestCode){
            if(resultCode==Activity.RESULT_OK){
                if(data.getStringExtra("state").equals("saved")) ReceiveData();
                if(data.getStringExtra("state").equals("deleted")) finish();
            }
        }
    }
}
