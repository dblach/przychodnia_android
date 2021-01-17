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
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
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


public class MainPageFragment extends Fragment{
    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";
    private String mParam1;
    private String mParam2;

    public MainPageFragment(){}
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<News> newsList=new ArrayList<News>();
    private boolean RecyclerAdapterConnected=false;

    public static MainPageFragment newInstance(String param1,String param2){
        MainPageFragment fragment=new MainPageFragment();
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
    public void onViewCreated(View view,Bundle savedInstanceState){
        ReceiveData();
        mRecyclerView=(RecyclerView)view.findViewById(R.id.recycler_news);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter=new NewsAdapter(ReceiveNews());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_main_page,container,false);
    }

    private void ReceiveData(){
        final Context ctx=getContext();
        final SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        final String sql_hostname=pref.getString("sql_hostname","");
        String sql_username=pref.getString("sql_username","");
        String sql_password=pref.getString("sql_password","");

        if(sql_hostname.equals("")||sql_username.equals("")||sql_password.equals("")){
            TextView text=(TextView)getView().findViewById(R.id.welcome_text);
            text.setText(getResources().getString(R.string.main_page_no_login));
            return;
        }

        OkHttpClient client=new OkHttpClient();
        String url=sql_hostname+"/get_main_page.php";
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
                        final String welcome_text=o.getString("tekst_powitalny");
                        final String image_url=sql_hostname+"/images/"+o.getString("logo");
                        getActivity().runOnUiThread(new Runnable(){public void run(){
                                TextView welcome=(TextView)getView().findViewById(R.id.welcome_text);
                                welcome.setText(welcome_text);
                                ImageView icon=(ImageView)getView().findViewById(R.id.icon);
                                Picasso.with(getActivity()).load(image_url).into(icon);
                        }});
                    }
                    catch(JSONException e){};
                }
            }
        });

        client=new OkHttpClient();
        url=sql_hostname+"/get_configuration.php";
        post_data=new FormBody.Builder().add("username",sql_username).add("password",sql_password).build();
        request=new Request.Builder().url(url).post(post_data).build();
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
                    }
                    catch(JSONException e){};
                }
            }
        });
    }

    private ArrayList<News> ReceiveNews(){
        final Context ctx=getContext();
        SharedPreferences pref=ctx.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        final String sql_hostname=pref.getString("sql_hostname","");
        String sql_username=pref.getString("sql_username","");
        String sql_password=pref.getString("sql_password","");

        if(sql_hostname.equals("")) return new ArrayList<News>();

        OkHttpClient client=new OkHttpClient();
        String url=sql_hostname+"/get_news.php";
        RequestBody post_data=new FormBody.Builder().add("username",sql_username).add("password",sql_password).add("limit","2").build();
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
                            News a=new News(o.getString("nazwa"),o.getString("tresc"),sql_hostname+"/images/"+o.getString("obraz"),o.getString("data_dodania"));
                            newsList.add(i,a);
                        }
                    }
                    catch(JSONException e){};
                    getActivity().runOnUiThread(new Runnable(){
                        public void run(){
                            if(!RecyclerAdapterConnected){
                                mRecyclerView.setAdapter(mAdapter);
                                RecyclerAdapterConnected=true;
                                TextView label_news=(TextView)getView().findViewById(R.id.label_news);
                                label_news.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }
            }
        });
        return newsList;
    }
}
