package pl.edu.dblach.przychodnia;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

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
            //dateTime = (TextView) itemView.findViewById(R.id.textView2);
            //Log.i(LOG_TAG, "Adding Listener");
            //itemView.setOnClickListener(this);
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

    @Override public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.description_line1.setText(appointments.get(position).data()+", "+appointments.get(position).godzina_rozpoczecia());
        holder.description_line2.setText(appointments.get(position).lekarz());
        holder.description_line3.setText("");
        //holder.label.setText(mDataset.get(position).getmText1());
        //holder.dateTime.setText(mDataset.get(position).getmText2());
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