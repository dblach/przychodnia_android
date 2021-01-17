package pl.edu.dblach.przychodnia;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.graphics.Color;
import android.content.Context;
import android.view.View;
import android.content.Intent;
import android.app.Activity;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class MyAppointmentsAdapter extends RecyclerView.Adapter<MyAppointmentsAdapter.DataObjectHolder>{
    private ArrayList<Appointment> appointments;
    private static MyClickListener myClickListener;
    private Context mContext;
    private int ViewAppointmentDetailsReturnCode;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView description_line1;
        TextView description_line2;
        TextView description_line3;
        ImageView icon_doctor;
        ImageView icon_clinic;

        public DataObjectHolder(View itemView){
            super(itemView);
            description_line1=(TextView)itemView.findViewById(R.id.description_line1);
            description_line2=(TextView)itemView.findViewById(R.id.description_line2);
            description_line3=(TextView)itemView.findViewById(R.id.description_line3);
            icon_doctor=(ImageView)itemView.findViewById(R.id.icon_doctor);
            icon_clinic=(ImageView)itemView.findViewById(R.id.icon_clinic);
        }

        @Override public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyAppointmentsAdapter(ArrayList<Appointment> myDataset,Context ctx){
        appointments=myDataset;
        mContext=ctx;
    }

    @Override public DataObjectHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_appointments_card, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override public void onBindViewHolder(DataObjectHolder holder, final int position) {
        try{
            Date dw=new SimpleDateFormat("yyyy-MM-dd").parse(appointments.get(position).data());
            if(new Date().after(dw)){
                holder.description_line1.setTextColor(Color.parseColor("#adadad"));
                holder.description_line2.setTextColor(Color.parseColor("#adadad"));
                holder.description_line3.setTextColor(Color.parseColor("#adadad"));
            }
            else{
                holder.description_line1.setTextColor(Color.parseColor("#000000"));
                holder.description_line2.setTextColor(Color.parseColor("#000000"));
                holder.description_line3.setTextColor(Color.parseColor("#000000"));
            }
        }
        catch(Exception e){}


        holder.description_line1.setText(appointments.get(position).data()+", "+appointments.get(position).godzina_rozpoczecia());
        holder.description_line2.setText(appointments.get(position).lekarz());
        holder.description_line3.setText("");
        Picasso.with(mContext).load(appointments.get(position).ikona_lekarz()).into(holder.icon_doctor);
        Picasso.with(mContext).load(appointments.get(position).ikona_specjalizacja()).into(holder.icon_clinic);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                Intent i=new Intent(v.getContext(),ViewAppointmentDetails.class);
                i.putExtra("id",appointments.get(position).id());
                Activity origin=(Activity)mContext;
                origin.startActivityForResult(i,ViewAppointmentDetailsReturnCode);
            }
        });
    }

    public void addItem(Appointment app,int index){
        appointments.add(index,app);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        appointments.remove(index);
        notifyItemRemoved(index);
    }

    @Override public int getItemCount() {
        return appointments.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}