package pl.edu.dblach.przychodnia;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;
import java.util.ArrayList;
import com.squareup.picasso.Picasso;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import android.graphics.Color;

public class NewAppointment_tabClinic_ClinicAdapter extends RecyclerView.Adapter<NewAppointment_tabClinic_ClinicAdapter.DataObjectHolder>{
    private ArrayList<Clinic> clinics;
    private static MyClickListener myClickListener;
    private Context context;
    private int lastPosition = -1;
    int row_index = -1;

    public NewAppointment_tabClinic_ClinicAdapter(ArrayList<Clinic> myDataset,Context context){
        clinics=myDataset;
        this.context=context;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        ImageView icon;
        LinearLayout linearLayout;

        public DataObjectHolder(View itemView){
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            icon=(ImageView)itemView.findViewById(R.id.image);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.linearlayout);
        }

        @Override public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override public DataObjectHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_new_appointment_tab_clinic_card, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.name.setText(clinics.get(position).nazwa());
        Picasso.with(context).load(clinics.get(position).ikona()).into(holder.icon);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                SharedPreferences pref=context.getSharedPreferences(context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=pref.edit();
                edit.putString("NewAppointment_clinic_id",clinics.get(position).id());
                edit.commit();
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view){
                row_index=position;
                notifyDataSetChanged();
            }
        });

        if(row_index==position) holder.linearLayout.setBackgroundColor(Color.parseColor(context.getResources().getString(R.color.selected)));
        else holder.linearLayout.setBackgroundColor(Color.parseColor(context.getResources().getString(R.color.not_selected)));
    }

    public void addItem(Clinic c,int index){
        clinics.add(index,c);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        clinics.remove(index);
        notifyItemRemoved(index);
    }

    @Override public int getItemCount() {
        return clinics.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}