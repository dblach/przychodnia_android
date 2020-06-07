package pl.edu.dblach.przychodnia;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
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
//import com.framgia.library.calendardayview.data.IPopup;
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
import android.widget.Toast;

public class NewAppointment_tabTime extends Fragment{
    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";
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
    //private String[] weekdays=new String[8];
    private String mParam1;
    private String mParam2;

    public NewAppointment_tabTime(){

    }

    public static NewAppointment_tabTime newInstance(String param1,String param2){
        NewAppointment_tabTime fragment=new NewAppointment_tabTime();
        Bundle args=new Bundle();
        args.putString(ARG_PARAM1,param1);
        args.putString(ARG_PARAM2,param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mParam1=getArguments().getString(ARG_PARAM1);
            mParam2=getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.fragment_new_appointment_tab_time,container,false);

        Button btnPreviousDay=(Button)v.findViewById(R.id.btnPreviousDay);
        Button btnNextDay=(Button)v.findViewById(R.id.btnNextDay);
        Button btnDate=(Button)v.findViewById(R.id.btnDate);
        Button btnSelectTime=(Button)v.findViewById(R.id.btnSelectTime);
        Button btnSave=(Button)v.findViewById(R.id.btnSave);

        btnPreviousDay.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){
            date_prevDay();
        }});
        btnNextDay.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){
            date_nextDay();
        }});
        btnDate.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){
            date_openPicker();
        }});
        btnSelectTime.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){
            selectTime();
        }});
        btnSave.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){
            save();
        }});

        /*weekdays[0]="";
        weekdays[1]=getString(R.string.weekday_monday);
        weekdays[2]=getString(R.string.weekday_tuesday);
        weekdays[3]=getString(R.string.weekday_wednesday);
        weekdays[4]=getString(R.string.weekday_thursday);
        weekdays[5]=getString(R.string.weekday_friday);
        weekdays[6]=getString(R.string.weekday_saturday);
        weekdays[7]=getString(R.string.weekday_sunday);
*/
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            updateDate();
        }
    }

    public void updateDate(){
        final Context ctx=getContext();
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        final String sql_hostname=pref.getString("sql_hostname","");
        String sql_username=pref.getString("sql_username","");
        String sql_password=pref.getString("sql_password","");
        final String new_appointment_doctor_id=pref.getString("NewAppointment_doctor_id","");

        if(new_appointment_doctor_id.equals("")){
            showErrorNoDoctorSelected();
        }
        else{
            showLoading();

            OkHttpClient client=new OkHttpClient();
            String url=sql_hostname+"/get_doctor_timetable.php";
            RequestBody post_data=new FormBody.Builder().add("username",sql_username).add("password",sql_password).add("doctor_id",new_appointment_doctor_id).add("date",new SimpleDateFormat("yyyy-MM-dd").format(date)).build();
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

                            getActivity().runOnUiThread(new Runnable(){
                                public void run(){
                                    Button btnDate=(Button)getView().findViewById(R.id.btnDate);
                                    dayView=(CalendarDayView)getView().findViewById(R.id.calendar);

                                    btnDate.setText(df.format(date));
                                    dayView.setLimitTime(day_start,day_end);
                                    dayView.setEvents(terminy);
                                }
                            });
                        }catch(JSONException e){
                        }
                        ;
                    }
                }
            });
            hideLoading();
        }
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
        getActivity().runOnUiThread(new Runnable(){
            public void run(){
                final Calendar c=Calendar.getInstance();
                c.setTime(date);
                int y=c.get(Calendar.YEAR);
                int m=c.get(Calendar.MONTH);
                int d=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog picker=new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener(){@Override public void onDateSet(DatePicker v,int y,int m,int d){
                    c.set(Calendar.YEAR,y);
                    c.set(Calendar.MONTH,m);
                    c.set(Calendar.DAY_OF_MONTH,d);
                    date=c.getTime();
                    updateDate();
                }},y,m,d);
                picker.show();
            }
        });
    }

    public void selectTime(){
        final Calendar c=Calendar.getInstance();
        h=day_start;
        m=0;

        TimePickerDialog timePickerDialog=new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener(){
                    @Override public void onTimeSet(TimePicker view, int h,int m){
                        c.set(Calendar.HOUR_OF_DAY,h);
                        c.set(Calendar.MINUTE,m);
                        time=c.getTime();
                        getActivity().runOnUiThread(new Runnable(){public void run(){
                            Button btn=getActivity().findViewById(R.id.btnSelectTime);
                            btn.setText(new SimpleDateFormat("HH:mm").format(time));
                        }});
                    }
                },h,m,true);
        timePickerDialog.show();
    }

    public void save(){
        Calendar c=Calendar.getInstance();
        c.setTime(time);
        c.add(Calendar.MINUTE,30);
        Date t_stop=c.getTime();
        String s_date=new SimpleDateFormat("yyyy-MM-dd").format(date);
        String s_time_start=new SimpleDateFormat("HH:mm").format(time);
        String s_time_stop=new SimpleDateFormat("HH:mm").format(t_stop);
        final Context ctx=getContext();
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        final String sql_hostname=pref.getString("sql_hostname","");
        String sql_username=pref.getString("sql_username","");
        String sql_password=pref.getString("sql_password","");
        String new_appointment_doctor_id=pref.getString("NewAppointment_doctor_id","");
        OkHttpClient client=new OkHttpClient();
        String url=sql_hostname+"/add_appointment.php";
        RequestBody post_data=new FormBody.Builder().add("username",sql_username).add("password",sql_password).add("doctor_id",new_appointment_doctor_id).add("date",s_date).add("czas_rozpoczecia",s_time_start).add("czas_zakonczenia",s_time_stop).build();
        Request request=new Request.Builder().url(url).post(post_data).build();
        client.newCall(request).enqueue(new Callback(){
            @Override public void onFailure(Call call,IOException e){e.printStackTrace();}
            @Override public void onResponse(Call call,Response response) throws IOException{
                if(response.isSuccessful()){
                    final String r=response.body().string();
                    getActivity().runOnUiThread(new Runnable(){public void run(){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Odpowiedź");
                        alertDialogBuilder
                                .setMessage(r)
                                .setCancelable(false)
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                    }
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }});
                }
            }
        });
    }

    public void showLoading(){
        getActivity().runOnUiThread(new Runnable(){public void run(){
            TextView t=(TextView)getView().findViewById(R.id.response);
            t.setText(getResources().getString(R.string.loading));
            t.setVisibility(View.VISIBLE);
        }});
    }

    public void hideLoading(){
        getActivity().runOnUiThread(new Runnable(){public void run(){
            TextView t=(TextView)getView().findViewById(R.id.response);
            t.setVisibility(View.GONE);
        }});
    }

    public void showErrorNoDoctorSelected(){
        getActivity().runOnUiThread(new Runnable(){public void run(){
            TextView t=(TextView)getView().findViewById(R.id.response);
            t.setText(getResources().getString(R.string.new_appointment_tab_time_no_doctor_selected));
            t.setVisibility(View.VISIBLE);
        }});
    }

    public void check_for_no_admissions(){
        final TextView t=(TextView)getView().findViewById(R.id.response);
        final CalendarDayView c=(CalendarDayView)getView().findViewById(R.id.calendar);
        if(day_start==0&&day_end==1){
            getActivity().runOnUiThread(new Runnable(){public void run(){
                t.setText(getResources().getString(R.string.new_appointment_tab_time_no_admissions).replace("%nd",next_admission_day).replace("%nh",next_admission_hour));
                t.setVisibility(View.VISIBLE);
                c.setVisibility(View.GONE);
            }});
        }
        else{
            getActivity().runOnUiThread(new Runnable(){public void run(){
                t.setText("");
                t.setVisibility(View.GONE);
                c.setVisibility(View.VISIBLE);
            }});
        }
    }
}
