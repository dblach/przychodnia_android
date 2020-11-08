package pl.edu.dblach.przychodnia;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import java.util.ArrayList;
import com.squareup.picasso.Picasso;
import android.content.Context;
import android.view.View;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.DataObjectHolder>{
    private ArrayList<News> news;
    private Context context;
    private View view;

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView date;
        TextView text;
        ImageView icon;

        public DataObjectHolder(View itemView){
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            date=(TextView)itemView.findViewById(R.id.date);
            text=(TextView)itemView.findViewById(R.id.text);
            icon=(ImageView)itemView.findViewById(R.id.image);
        }
    }

    public NewsAdapter(ArrayList<News> myDataset){
        news=myDataset;
    }

    @Override public DataObjectHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        view=LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override public void onBindViewHolder(DataObjectHolder holder, final int position) {
        holder.title.setText(news.get(position).nazwa());
        holder.date.setText(news.get(position).data_dodania());
        holder.text.setText(news.get(position).tresc());
        Picasso.with(view.getContext()).load(news.get(position).obraz()).into(holder.icon);
    }

    public void addItem(News n,int index){
        news.add(index,n);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        news.remove(index);
        notifyItemRemoved(index);
    }

    @Override public int getItemCount() {
        return news.size();
    }
}