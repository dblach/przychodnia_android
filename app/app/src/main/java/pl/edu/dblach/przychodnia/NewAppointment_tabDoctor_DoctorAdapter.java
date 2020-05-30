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

public class NewAppointment_tabDoctor_DoctorAdapter extends RecyclerView.Adapter<NewAppointment_tabDoctor_DoctorAdapter.DataObjectHolder>{
    private ArrayList<Doctor> doctors;
    private static MyClickListener myClickListener;
    private Context context;

    public NewAppointment_tabDoctor_DoctorAdapter(ArrayList<Doctor> myDataset,Context context){
        doctors=myDataset;
        this.context=context;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView admissions;
        //ImageView photo;

        public DataObjectHolder(View itemView){
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            admissions=(TextView)itemView.findViewById(R.id.admissions);
            //photo=(ImageView)itemView.findViewById(R.id.image);
        }

        @Override public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override public DataObjectHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_new_appointment_tab_doctor_card, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override public void onBindViewHolder(DataObjectHolder holder, final int position) {
        holder.name.setText(doctors.get(position).nazwisko()+" "+doctors.get(position).imie());
        holder.admissions.setText(doctors.get(position).godziny_przyjec());
        //Picasso.with(context).load(doctors.get(position).zdjecie()).into(holder.photo);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                Toast.makeText(v.getContext(),"klik="+position,Toast.LENGTH_SHORT).show();
                //SharedPreferences pref=context.getSharedPreferences(context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
                //SharedPreferences.Editor edit=pref.edit();
                //edit.putString("NewAppointment_clinic_id",clinics.get(position).id());
                //edit.commit();
            }
        });
    }

    public void addItem(Doctor d,int index){
        doctors.add(index,d);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        doctors.remove(index);
        notifyItemRemoved(index);
    }

    @Override public int getItemCount() {
        return doctors.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}