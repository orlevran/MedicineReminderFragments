package Adapters;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinereminderfragments.R;

import Controllers.UserController;
import Models.DataReceiver;
import Models.Medicines.Drops;
import Models.Medicines.Pill;
import Models.Medicines.Syrup;
import Models.Medicines.Type;

public class MedicinesEditListAdapter extends RecyclerView.Adapter<MedicinesEditListAdapter.MedicineHolder> {

    private UserController controller;
    private DataReceiver receiver;

    public MedicinesEditListAdapter(DataReceiver receiver){
        this.controller = UserController.getInstance();
        this.receiver = receiver;
    }

    @NonNull
    @Override
    public MedicineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MedicineHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_edit_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MedicinesEditListAdapter.MedicineHolder holder, int position) {
        if(this.controller.getUser().getMedicines().get(position).getType().equals(Type.EMPTY)){
            holder.addMedicine.setText(
                    String.format("Cell %d is empty. Click here to define new medicine", (position+1)));
            holder.addMedicine.setTextSize(20);
            holder.name.setTextSize(0);
            holder.amount.setTextSize(0);
            holder.options.setTextSize(0);
            holder.imageView.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
            holder.linearLayout.setBackgroundColor(Color.rgb(119, 153,170));
        } else {
            holder.addMedicine.setTextSize(0);
            holder.name.setText(controller.getUser().getMedicines().get(position).getName());
            holder.name.setTextSize(25);
            if(!controller.getUser().getMedicines().get(position).getType().equals(Type.OINTMENT))
                holder.amount.setTextSize(20);
            holder.options.setTextSize(60);

            switch (this.controller.getUser().getMedicines().get(position).getType()){
                case PILL:
                    Pill pill = (Pill) this.controller.getUser().getMedicines().get(position);
                    holder.amount.setText(String.format("%02d Pills left out of %02d",
                            pill.getCurrentAmount(), pill.getInitialAmount()));
                    holder.imageView.setImageResource(R.drawable.pill_logo);
                    break;
                case SYRUP:
                    Syrup syrup = (Syrup) this.controller.getUser().getMedicines().get(position);
                    holder.amount.setText(String.format("Dosage: %.02f ml",syrup.getDosage()));
                    holder.imageView.setImageResource(R.drawable.syrup_logo);
                    break;
                case OINTMENT:
                    holder.amount.setText("");
                    holder.imageView.setImageResource(R.drawable.ointment_logo);
                    break;
                case DROPS:
                    Drops drops = (Drops) this.controller.getUser().getMedicines().get(position);
                    holder.amount.setText(String.format("Dosage: %d drops", drops.getDosage()));
                    holder.imageView.setImageResource(R.drawable.drops_logo);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.controller.getUser().getMedicines().size();
    }

    public void goToEditMedicine(int position){
        this.receiver.onEditMedicine(position);
    }

    public class MedicineHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView imageView;
        TextView name, addMedicine, amount, options;
        public MedicineHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.medicine_edit_linear_layout);
            imageView = itemView.findViewById(R.id.medicine_edit_image_view);
            name = itemView.findViewById(R.id.medicine_edit_name);
            addMedicine = itemView.findViewById(R.id.medicine_edit_add_medicine);
            amount = itemView.findViewById(R.id.medicine_edit_amount);
            options = itemView.findViewById(R.id.medicine_edit_options);

            linearLayout.setOnClickListener(lambda -> {
                if(controller.getUser().getMedicines().get(getAdapterPosition()).getType().equals(Type.EMPTY)){
                    goToEditMedicine(getAdapterPosition());
                }
            });

            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!controller.getUser().getMedicines().get(getAdapterPosition()).getType().equals(Type.EMPTY)){
                        PopupMenu popupMenu = new PopupMenu(view.getContext(), options);
                        popupMenu.inflate(R.menu.options_menu);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                switch (menuItem.getItemId()){
                                    case R.id.menu_details:
                                        receiver.onDetails(getAdapterPosition());
                                        break;
                                    case R.id.menu_remove:
                                        defineRemoveAlert(view, getAdapterPosition());
                                        break;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                }
            });
        }
    }

    public void defineRemoveAlert(View view, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Are you sure you want to remove this medicine from your list?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                controller.getMedicinesDB().setToEmpty(controller, position);
                controller.getUser().getMedicines().get(position).setToEmpty();
                notifyItemChanged(position);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}
