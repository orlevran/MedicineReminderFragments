package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinereminderfragments.R;

import java.util.ArrayList;

public class TakingTimeAdapter extends RecyclerView.Adapter<TakingTimeAdapter.TimeHolder> {

    private ArrayList<Integer> list;

    public TakingTimeAdapter(ArrayList<Integer> list){
        this.list = list;
    }

    @NonNull
    @Override
    public TimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.taking_time_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeHolder holder, int position) {
        holder.takingTime.setText(String.format("%02d:%02d:00", this.list.get(position) / 60,
                this.list.get(position) % 60));
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class TimeHolder extends RecyclerView.ViewHolder {
        TextView takingTime;
        public TimeHolder(@NonNull View itemView) {
            super(itemView);
            takingTime = itemView.findViewById(R.id.taking_time);
        }
    }
}
