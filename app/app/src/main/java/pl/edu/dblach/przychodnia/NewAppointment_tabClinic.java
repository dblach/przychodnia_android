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


public class NewAppointment_tabClinic extends Fragment{
    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Clinic> clinicsList=new ArrayList<Clinic>();
    private boolean RecyclerAdapterConnected=false;
    private Context context;
    public NewAppointment_tabClinic(){
    }

    public static NewAppointment_tabClinic newInstance(String param1,String param2){
        NewAppointment_tabClinic fragment=new NewAppointment_tabClinic();
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
        return inflater.inflate(R.layout.fragment_new_appointment_tab_clinic,container,false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter=new NewAppointment_tabClinic_ClinicAdapter(ReceiveData(),getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<Clinic> ReceiveData(){
        final Context ctx=getContext();
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        final String sql_hostname=pref.getString("sql_hostname","");
        String sql_username=pref.getString("sql_username","");
        String sql_password=pref.getString("sql_password","");

        OkHttpClient client=new OkHttpClient();
        String url=sql_hostname+"/get_clinics.php";
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
                        for(int i=0;i<array.length();i++){
                            JSONObject o=array.getJSONObject(i);
                            Clinic c=new Clinic(o.getString("id"),o.getString("nazwa"),sql_hostname+"/clinic/"+o.getString("ikona"));
                            clinicsList.add(i,c);
                        }
                    }
                    catch(JSONException e){};
                    getActivity().runOnUiThread(new Runnable(){
                        public void run(){
                            if(!RecyclerAdapterConnected){
                                mRecyclerView.setAdapter(mAdapter);
                                RecyclerAdapterConnected=true;
                                mAdapter.notifyDataSetChanged();
                                TextView loading=(TextView)getView().findViewById(R.id.loading);
                                loading.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                }
            }
        });
        return clinicsList;
    }
}
