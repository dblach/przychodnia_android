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

public class MyAppointmentsAdapter extends RecyclerView.Adapter<MyAppointmentsAdapter.DataObjectHolder>{
    private ArrayList<Appointment> appointments;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView description_line1;
        TextView description_line2;
        TextView description_line3;

        public DataObjectHolder(View itemView){
            super(itemView);
            description_line1=(TextView)itemView.findViewById(R.id.description_line1);
            description_line2=(TextView)itemView.findViewById(R.id.description_line2);
            description_line3=(TextView)itemView.findViewById(R.id.description_line3);
        }

        @Override public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyAppointmentsAdapter(ArrayList<Appointment> myDataset){appointments=myDataset;}

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

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                Toast.makeText(v.getContext(),"klik="+position,Toast.LENGTH_SHORT).show();
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