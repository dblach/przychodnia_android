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

public class NewAppointment_tabClinic_ClinicAdapter extends RecyclerView.Adapter<NewAppointment_tabClinic_ClinicAdapter.DataObjectHolder>{
    private ArrayList<Clinic> clinics;
    private static MyClickListener myClickListener;
    private Context context;


    public NewAppointment_tabClinic_ClinicAdapter(ArrayList<Clinic> myDataset,Context context){
        clinics=myDataset;
        this.context=context;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        ImageView icon;

        public DataObjectHolder(View itemView){
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            icon=(ImageView)itemView.findViewById(R.id.image);
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

    @Override public void onBindViewHolder(DataObjectHolder holder, final int position) {
        holder.name.setText(clinics.get(position).nazwa());
        Picasso.with(context).load(clinics.get(position).ikona()).into(holder.icon);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                Toast.makeText(v.getContext(),"klik="+position,Toast.LENGTH_SHORT).show();
            }
        });
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