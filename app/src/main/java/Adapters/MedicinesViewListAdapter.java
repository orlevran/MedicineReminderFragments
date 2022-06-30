package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinereminderfragments.R;

import java.util.ArrayList;
import Controllers.UserController;
import Models.DataReceiver;
import Models.Medicines.Medicine;
import Models.Medicines.Pill;
import Models.Medicines.Type;

public class MedicinesViewListAdapter extends RecyclerView.Adapter<MedicinesViewListAdapter.MedicineHolder> {

    private DataReceiver receiver;
    private Context context;
    private ArrayList<Medicine> medicines;
    private String time;

    public MedicinesViewListAdapter(DataReceiver receiver, Context context, ArrayList<Medicine> medicines, String time){
        this.receiver = receiver;
        this.context = context;
        this.medicines = medicines;
        this.time = time;
    }

    @NonNull
    @Override
    public MedicineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MedicineHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_view_item, parent, false));
    }

    @SuppressLint("RecyclerView")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MedicineHolder holder, int position) {
        declareLogo(holder, this.medicines.get(position).getType());
        declareButton(holder, position);
        holder.name.setText(this.medicines.get(position).getName());
        holder.countdown.setText(getTakingTimeByString(position));
    }

    private String getTakingTimeByString(int position){
        switch (this.time){
            case "Night":
                return String.format("%02d:%02d:00", this.medicines.get(position).getNightTaking() / 60,
                        this.medicines.get(position).getNightTaking() % 60);
            case "Morning":
                return String.format("%02d:%02d:00", this.medicines.get(position).getMorningTaking() / 60,
                        this.medicines.get(position).getMorningTaking() % 60);
            case "Afternoon":
                return String.format("%02d:%02d:00", this.medicines.get(position).getAfternoonTaking() / 60,
                        this.medicines.get(position).getAfternoonTaking() % 60);
            case "Evening":
                return String.format("%02d:%02d:00", this.medicines.get(position).getEveningTaking() / 60,
                        this.medicines.get(position).getEveningTaking() % 60);
            default:
                return "HH:MM:SS";
        }
    }

    @Override
    public int getItemCount() {
        return this.medicines.size();
    }

    private void declareButton(@NonNull MedicineHolder holder, int position){
        holder.button.setOnClickListener(lambda -> {
            if(medicines.get(position).isTimeToTake()){
                medicines.get(position).setTimeToTake(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
                builder.setTitle("Medicine was taken successfully").setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

                if(medicines.get(position).getType().equals(Type.PILL)){
                    ((Pill) medicines.get(position)).setCurrentAmount(
                            ((Pill) medicines.get(position)).getCurrentAmount() -
                            ((Pill) medicines.get(position)).getDosage());

                    if(((Pill)medicines.get(position)).getCurrentAmount() <
                            (((Pill) medicines.get(position)).getDosage())){
                        this.receiver.onRefill(position);
                    }
                }
            }
            else
                Toast.makeText(context, "Invalid time", Toast.LENGTH_SHORT).show();
            /*
             *  TODO:
             *   1. Check time- 0-30 minutes after the taking hour
             *   2. Check if text if "take medicine"
             *   3. Check if medicine is pill
             *   3.1. Check if current amount is greater than or equal to dosage
             *   3.2. If not, change button text to "Refill" and switch to refill mode
             *   4. If text is "Refill", click will open a window to set new initial amount
             *   4.1 while text is "Refill", user won't get notifications
             *      Don't forget to update in database
             */
        });
    }

    private void declareLogo(@NonNull MedicineHolder holder, Type type) {
        switch (type) {
            case PILL:
                holder.imageView.setImageResource(R.drawable.pill_logo);
                break;
            case SYRUP:
                holder.imageView.setImageResource(R.drawable.syrup_logo);
                break;
            case OINTMENT:
                holder.imageView.setImageResource(R.drawable.ointment_logo);
                break;
            case DROPS:
                holder.imageView.setImageResource(R.drawable.drops_logo);
                break;
            default:
                holder.imageView.setImageDrawable(null);
                break;
        }
    }

    public class MedicineHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, countdown, button;

        public MedicineHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.medicine_view_image_view);
            name = itemView.findViewById(R.id.medicine_view_name);
            countdown = itemView.findViewById(R.id.medicine_view_countdown);
            button = itemView.findViewById(R.id.medicine_view_button);
        }
    }
}
