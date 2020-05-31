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
import android.widget.DatePicker;
import android.app.DatePickerDialog;

public class NewAppointment_tabTime extends Fragment{
    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";
    private Date date=new Date();
    private DateFormat df=new SimpleDateFormat("dd.MM.yyyy");;

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

        btnPreviousDay.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){
            date_prevDay();
        }});
        btnNextDay.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){
            date_nextDay();
        }});
        btnDate.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){
            date_openPicker();
        }});

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            updateDate();


            Toast.makeText(getActivity().getApplicationContext(),"TabTime visible",Toast.LENGTH_SHORT).show();
        }
        //Log.i("my_fragment","setUserVisibleHint: "+isVisibleToUser);
    }

    public void updateDate(){
        getActivity().runOnUiThread(new Runnable(){
            public void run(){
                Button btnDate=(Button)getView().findViewById(R.id.btnDate);
                btnDate.setText(df.format(date));
            }
        });
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
                    //eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }},y,m,d);
                picker.show();
            }
        });
    }
}
