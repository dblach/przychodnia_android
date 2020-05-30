package pl.edu.dblach.przychodnia;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import java.util.ArrayList;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewAppointment_tabDoctor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAppointment_tabDoctor extends Fragment{
    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";

    private String mParam1;
    private String mParam2;

    private ArrayList<Doctor> doctorsList=new ArrayList<Doctor>();
    private String[] weekdays=new String[8];
    private boolean RecyclerAdapterConnected=false;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //private Context context;

    public NewAppointment_tabDoctor(){
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewAppointment_tabDoctor.
     */
    public static NewAppointment_tabDoctor newInstance(String param1,String param2){
        NewAppointment_tabDoctor fragment=new NewAppointment_tabDoctor();
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
        return inflater.inflate(R.layout.fragment_new_appointment_tab_doctor,container,false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter=new NewAppointment_tabDoctor_DoctorAdapter(doctorsList,getActivity());
        mRecyclerView.setAdapter(mAdapter);

        weekdays[0]="";
        weekdays[1]=getString(R.string.weekday_monday);
        weekdays[2]=getString(R.string.weekday_tuesday);
        weekdays[3]=getString(R.string.weekday_wednesday);
        weekdays[4]=getString(R.string.weekday_thursday);
        weekdays[5]=getString(R.string.weekday_friday);
        weekdays[6]=getString(R.string.weekday_saturday);
        weekdays[7]=getString(R.string.weekday_sunday);
    }

    @Override public void setUserVisibleHint(boolean isVisible){
        super.setUserVisibleHint(isVisible);
        if(isVisible){
            ReceiveData();
        }
    }

    private ArrayList<Doctor> ReceiveData(){
        final Context ctx=getContext();
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        final String sql_hostname=pref.getString("sql_hostname","");
        String sql_username=pref.getString("sql_username","");
        String sql_password=pref.getString("sql_password","");
        String app_clinic_id=pref.getString("NewAppointment_clinic_id","");

        if(app_clinic_id!=""){
            getActivity().runOnUiThread(new Runnable(){
                public void run(){
                    TextView loading=(TextView)getView().findViewById(R.id.loading);
                    loading.setVisibility(View.VISIBLE);
                }
            });
            doctorsList.clear();
            mAdapter.notifyDataSetChanged();

            OkHttpClient client=new OkHttpClient();
            String url=sql_hostname+"/get_doctors.php";
            RequestBody post_data=new FormBody.Builder().add("username",sql_username).add("password",sql_password).add("clinic_id",app_clinic_id).build();
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
                            JSONArray array=new JSONArray(r);
                            for(int i=0;i<array.length();i++){
                                JSONObject o=array.getJSONObject(i);
                                if(o.getString("type").equals("doctor")){
                                    Doctor d=new Doctor(o.getString("id_lekarza"),o.getString("imie"),o.getString("nazwisko"),sql_hostname+"/doctor/"+o.getString("zdjecie"));
                                    doctorsList.add(i,d);
                                }
                                if(o.getString("type").equals("admission")){
                                    for(int j=0;j<doctorsList.size();j++){
                                        if(doctorsList.get(j).id().equals(o.getString("id_lekarza"))) doctorsList.get(j).addAdmission(weekdays[Integer.parseInt(o.getString("dzien_tygodnia"))],o.getString("godzina_rozpoczecia"),o.getString("godzina_zakonczenia"),o.getString("pomieszczenie"));
                                    }
                                }
                            }
                        }catch(JSONException e){};
                        getActivity().runOnUiThread(new Runnable(){
                            public void run(){
                                mRecyclerView.setAdapter(mAdapter);
                                RecyclerAdapterConnected=true;
                                mAdapter.notifyDataSetChanged();
                                TextView loading=(TextView)getView().findViewById(R.id.loading);
                                loading.setVisibility(View.INVISIBLE);
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            });
        }return doctorsList;
    }
}
