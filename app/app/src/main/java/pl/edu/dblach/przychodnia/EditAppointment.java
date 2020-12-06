package pl.edu.dblach.przychodnia;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.app.Activity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Button;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import com.framgia.library.calendardayview.CalendarDayView;
import com.framgia.library.calendardayview.EventView;
import com.framgia.library.calendardayview.PopupView;
import com.framgia.library.calendardayview.data.IEvent;
import com.framgia.library.calendardayview.decoration.CdvDecorationDefault;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import java.text.ParseException;

public class EditAppointment extends AppCompatActivity{
    private Date date=new Date();
    private Date time=new Date();
    private DateFormat df=new SimpleDateFormat("dd.MM.yyyy");
    private CalendarDayView dayView;
    private ArrayList<IEvent> terminy;
    private int day_start;
    private int day_end;
    private int h;
    private int m;
    private String next_admission_day="";
    private String next_admission_hour="";
    private String s_date;
    private String s_time_start="";
    private String s_time_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);

        Button btnPreviousDay=(Button)findViewById(R.id.btnPreviousDay);
        Button btnNextDay=(Button)findViewById(R.id.btnNextDay);
        Button btnDate=(Button)findViewById(R.id.btnDate);
        Button btnSelectTime=(Button)findViewById(R.id.btnSelectTime);
        Button btnSave=(Button)findViewById(R.id.btnSave);
        Button btnCancelAppointment=(Button)findViewById(R.id.btnCancelAppointment);

        btnPreviousDay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                date_prevDay();
            }
        });
        btnNextDay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                date_nextDay();
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                date_openPicker();
            }
        });
        btnSelectTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                selectTime();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                save();
            }
        });
        btnCancelAppointment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                cancel_appointment();
            }
        });

        Intent in=getIntent();
        try{date=new SimpleDateFormat("yyyy-MM-dd").parse(in.getStringExtra("data"));} catch(ParseException e){}

        updateDate();
    }

    public void updateDate(){
    //    showLoading();
        final Context ctx=EditAppointment.this;
        Intent in=getIntent();
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        final String sql_hostname=pref.getString("sql_hostname","");
        String sql_username=pref.getString("sql_username","");
        String sql_password=pref.getString("sql_password","");

        OkHttpClient client=new OkHttpClient();
        String url=sql_hostname+"/get_doctor_timetable.php";
        RequestBody post_data=new FormBody.Builder().add("username",sql_username).add("password",sql_password).add("doctor_id",in.getStringExtra("idl")).add("date",new SimpleDateFormat("yyyy-MM-dd").format(date)).build();
        Request request=new Request.Builder().url(url).post(post_data).build();
        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call,IOException e){
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call,Response response) throws IOException{
                if(response.isSuccessful()){
                    final String r=response.body().string();
                    try{
                        terminy=new ArrayList<>();
                        JSONArray array=new JSONArray(r);
                        for(int i=0;i<array.length();i++){
                            JSONObject o=array.getJSONObject(i);
                            if(o.getString("type").equals("admission")){
                                String tp=o.getString("godzina_rozpoczecia");
                                String tk=o.getString("godzina_zakonczenia");
                                int gp=Integer.parseInt(tp.substring(0,2));
                                int mp=Integer.parseInt(tp.substring(3,5));
                                int gk=Integer.parseInt(tk.substring(0,2));
                                int mk=Integer.parseInt(tk.substring(3,5));
                                Calendar p=Calendar.getInstance();
                                p.set(Calendar.HOUR_OF_DAY,gp);
                                p.set(Calendar.MINUTE,mp);
                                Calendar k=Calendar.getInstance();
                                k.set(Calendar.HOUR_OF_DAY,gk);
                                k.set(Calendar.MINUTE,mk);
                                NewAppointment_tabTime_Event a=new NewAppointment_tabTime_Event(1,p,k,"","",Color.GREEN);
                                terminy.add(a);
                            }
                            if(o.getString("type").equals("appointment")){
                                String tp=o.getString("godzina_rozpoczecia");
                                String tk=o.getString("godzina_zakonczenia");
                                int gp=Integer.parseInt(tp.substring(0,2));
                                int mp=Integer.parseInt(tp.substring(3,5));
                                int gk=Integer.parseInt(tk.substring(0,2));
                                int mk=Integer.parseInt(tk.substring(3,5));
                                Calendar p=Calendar.getInstance();
                                p.set(Calendar.HOUR_OF_DAY,gp);
                                p.set(Calendar.MINUTE,mp);
                                Calendar k=Calendar.getInstance();
                                k.set(Calendar.HOUR_OF_DAY,gk);
                                k.set(Calendar.MINUTE,mk);
                                NewAppointment_tabTime_Event a=new NewAppointment_tabTime_Event(1,p,k,getString(R.string.new_appointment_tab_time_appointment_label),"",Color.RED);
                                terminy.add(a);
                            }
                            if(o.getString("type").equals("start")){
                                if(!o.getString("d").equals("null"))
                                    day_start=Integer.parseInt(o.getString("d").substring(0,2));
                                else day_start=0;
                            }
                            if(o.getString("type").equals("stop")){
                                if(!o.getString("d").equals("null"))
                                    day_end=Integer.parseInt(o.getString("d").substring(0,2))+1;
                                else day_end=1;
                            }
                            //TODO: dodać wyświetlanie najbliższej daty
                            //if(day_start==0&&day_end==1){
                            //next_admission_day=o.getString("next_admission_day");
                            //next_admission_day=weekdays[Integer.parseInt(o.getString("next_admission_day"))];
                            //next_admission_hour=o.getString("next_admission_hour");
                            //}
                        }

                        check_for_no_admissions();

                        EditAppointment.this.runOnUiThread(new Runnable(){@Override public void run(){
                            Button btnDate=(Button)findViewById(R.id.btnDate);
                            dayView=(CalendarDayView)findViewById(R.id.calendar);
                            btnDate.setText(df.format(date));
                            dayView.setLimitTime(day_start,day_end);
                            dayView.setEvents(terminy);
                        }});
                    }catch(JSONException e){
                    }
                    ;
                }
            }
        });
        //hideLoading();
    }

    public void btnSaveClick(View v){
        Intent ret=new Intent();
        setResult(Activity.RESULT_OK,ret);
        finish();
    }

    public void date_prevDay(){
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH,-1);
        date=c.getTime();
        updateDate();
    }

    public void date_nextDay(){
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH,1);
        date=c.getTime();
        updateDate();
    }

    public void date_openPicker(){
        final Calendar c=Calendar.getInstance();
        c.setTime(date);
        int y=c.get(Calendar.YEAR);
        int m=c.get(Calendar.MONTH);
        int d=c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog picker=new DatePickerDialog(EditAppointment.this,new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker v,int y,int m,int d){
                c.set(Calendar.YEAR,y);
                c.set(Calendar.MONTH,m);
                c.set(Calendar.DAY_OF_MONTH,d);
                date=c.getTime();
                updateDate();
            }
        },y,m,d);
        picker.show();
    }

    public void selectTime(){
        final Calendar c=Calendar.getInstance();
        h=day_start;
        m=0;

        TimePickerDialog timePickerDialog=new TimePickerDialog(EditAppointment.this,
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view,int h,int m){
                        c.set(Calendar.HOUR_OF_DAY,h);
                        c.set(Calendar.MINUTE,m);
                        time=c.getTime();
                        Button btn=(Button)findViewById(R.id.btnSelectTime);
                        btn.setText(new SimpleDateFormat("HH:mm").format(time));
                    }
                },h,m,true);
        timePickerDialog.show();
    }

    public void save(){
        Button btn=(Button)findViewById(R.id.btnSelectTime);
        if(btn.getText().equals(getResources().getString(R.string.new_appointment_tab_time_select_hour))){
            show_dialog_msg(getResources().getString(R.string.new_appointment_tab_time_no_time_selected));
        }else{
            Intent in=getIntent();
            Calendar c=Calendar.getInstance();
            c.setTime(time);
            c.add(Calendar.MINUTE,30);
            Date t_stop=c.getTime();
            s_date=new SimpleDateFormat("yyyy-MM-dd").format(date);
            s_time_start=new SimpleDateFormat("HH:mm").format(time);
            s_time_stop=new SimpleDateFormat("HH:mm").format(t_stop);
            final Context ctx=EditAppointment.this;
            SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
            final String sql_hostname=pref.getString("sql_hostname","");
            String sql_username=pref.getString("sql_username","");
            String sql_password=pref.getString("sql_password","");
            OkHttpClient client=new OkHttpClient();
            String url=sql_hostname+"/update_appointment.php";
            RequestBody post_data=new FormBody.Builder().add("username",sql_username).add("password",sql_password).add("doctor_id",in.getStringExtra("idl")).add("date",s_date).add("czas_rozpoczecia",s_time_start).add("czas_zakonczenia",s_time_stop).add("idw",in.getStringExtra("idw")).build();
            Request request=new Request.Builder().url(url).post(post_data).build();
            client.newCall(request).enqueue(new Callback(){
                @Override
                public void onFailure(Call call,IOException e){
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call,Response response) throws IOException{
                    if(response.isSuccessful()){
                        final String r=response.body().string();
                        if(r.equals("{\"error_no_appointment\":\"1\"}"))
                            show_dialog_msg(getResources().getString(R.string.new_appointment_tab_time_error_no_appointment));
                        if(r.equals("{\"error_busy\":\"1\"}"))
                            show_dialog_msg(getResources().getString(R.string.new_appointment_tab_time_error_busy));
                        if(r.equals("{\"result\":1}")){
                            success();
                        }
                    }
                }
            });
        }
    }

    public void show_dialog_msg(final String t){
        EditAppointment.this.runOnUiThread(new Runnable(){public void run(){
            AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(EditAppointment.this);
            alertDialogBuilder.setTitle("");
            alertDialogBuilder
                    .setMessage(t)
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog,int id){
                        }
                    });
            AlertDialog alertDialog=alertDialogBuilder.create();
            alertDialog.show();
        }});
    }

    public void success(){
        Context ctx=EditAppointment.this;
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        String calendar_integration=pref.getString("calendar_integration","");
        if(calendar_integration.equals("yes")) savetocalendar();
        if(calendar_integration.equals("ask")) savetocalendar_showdialog();
        if(calendar_integration.equals("no")) show_success_msg();
    }

    public void savetocalendar(){
        Calendar cal=Calendar.getInstance();
        Calendar end=Calendar.getInstance();
        try{
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(s_date+' '+s_time_start));
        }catch(Exception e){
        }
        try{
            end.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(s_date+' '+s_time_stop));
        }catch(Exception e){
        }
        Intent intent=new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime",cal.getTimeInMillis());
        intent.putExtra("allDay",false);
        intent.putExtra("endTime",end.getTimeInMillis());
        intent.putExtra("title","Wizyta lekarska");
        startActivity(intent);
        form_close("saved");
    }

    public void savetocalendar_showdialog(){
        EditAppointment.this.runOnUiThread(new Runnable(){public void run(){
            AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(EditAppointment.this);
            alertDialogBuilder.setTitle("");
            alertDialogBuilder
                .setMessage(getResources().getString(R.string.new_appointment_tab_time_addtocalendar_question))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        savetocalendar();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        form_close("saved");
                    }
                });
            AlertDialog alertDialog=alertDialogBuilder.create();
            alertDialog.show();
        }});
    }

    public void show_success_msg(){
        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(EditAppointment.this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.new_appointment_tab_time_success))
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        form_close("success");
                    }
                });
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }

    public void check_for_no_admissions(){
        final TextView t=(TextView)findViewById(R.id.response);
        final CalendarDayView c=(CalendarDayView)findViewById(R.id.calendar);
        if(day_start==0&&day_end==1){
            EditAppointment.this.runOnUiThread(new Runnable(){public void run(){
                t.setText(getResources().getString(R.string.new_appointment_tab_time_no_admissions).replace("%nd",next_admission_day).replace("%nh",next_admission_hour));
                t.setVisibility(View.VISIBLE);
                c.setVisibility(View.GONE);
            }});
        }else{
            EditAppointment.this.runOnUiThread(new Runnable(){
                public void run(){
                    t.setText("");
                    t.setVisibility(View.GONE);
                    c.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void cancel_appointment(){
        EditAppointment.this.runOnUiThread(new Runnable(){public void run(){
            AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(EditAppointment.this);
            alertDialogBuilder.setTitle("");
            alertDialogBuilder
                    .setMessage(getResources().getString(R.string.edit_appointment_confirm_cancel))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog,int id){
                            Intent in=getIntent();
                            final Context ctx=EditAppointment.this;
                            SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
                            final String sql_hostname=pref.getString("sql_hostname","");
                            String sql_username=pref.getString("sql_username","");
                            String sql_password=pref.getString("sql_password","");
                            OkHttpClient client=new OkHttpClient();
                            String url=sql_hostname+"/cancel_appointment.php";
                            RequestBody post_data=new FormBody.Builder().add("username",sql_username).add("password",sql_password).add("idw",in.getStringExtra("idw")).build();
                            Request request=new Request.Builder().url(url).post(post_data).build();
                            client.newCall(request).enqueue(new Callback(){
                                @Override public void onFailure(Call call,IOException e){
                                    e.printStackTrace();
                                }
                                @Override public void onResponse(Call call,Response response) throws IOException{
                                    if(response.isSuccessful()){
                                        final String r=response.body().string();
                                        if(r.equals("{\"result\":1}")){
                                            form_close("deleted");
                                        }
                                        else{
                                            show_dialog_msg(getResources().getString(R.string.edit_appointment_cancel_error));
                                        }
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog,int id){}
                    });
            AlertDialog alertDialog=alertDialogBuilder.create();
            alertDialog.show();
        }});
    }

    public void form_close(String state){
        Intent ret=new Intent();
        ret.putExtra("state",state);
        setResult(Activity.RESULT_OK,ret);
        finish();
    }
}
