package Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicinereminderfragments.R;

import Controllers.UserController;
import Models.DataReceiver;
import Models.Medicines.Pill;

public class RefillFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UserController controller;
    private DataReceiver receiver;
    private Context context;
    private int position;

    EditText newAmount;
    TextView approve, remove;

    public RefillFragment(DataReceiver receiver, Context context) {
        this.receiver = receiver;
        this.context = context;
        this.controller = UserController.getInstance();
    }

    public void setPosition(int position){
        this.position = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refill, container, false);
        newAmount = view.findViewById(R.id.refill_edit_text);
        approve = view.findViewById(R.id.refill_approve);
        remove = view.findViewById(R.id.refill_remove);

        setButtons();

        return view;
    }

    private void setButtons() {
        approve.setOnClickListener(lambda -> {
            if(!newAmount.getText().toString().isEmpty()){
                try{
                    int amount = Integer.parseInt(newAmount.getText().toString());
                    ((Pill)this.controller.getUser().getMedicines().get(position)).setInitialAmount(amount);
                    if(((Pill)this.controller.getUser().getMedicines().get(position)).getCurrentAmount() >= 0){
                        ((Pill)this.controller.getUser().getMedicines().get(position)).setCurrentAmount(
                                ((Pill)this.controller.getUser().getMedicines().get(position)).getCurrentAmount() + amount);
                    } else {
                        ((Pill)this.controller.getUser().getMedicines().get(position)).setCurrentAmount(amount);
                    }

                    this.controller.getMedicinesDB().setToPill((Pill)this.controller.getUser().getMedicines().get(position), this.controller, this.position);
                    receiver.onBackToHome();

                } catch (Exception e){
                    Toast.makeText(this.context, "Invalid Input", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this.context, "Empty input", Toast.LENGTH_SHORT).show();
            }
        });
        remove.setOnClickListener(lambda -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    controller.getMedicinesDB().setToEmpty(controller, position);
                    receiver.onBackToHome();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        });
    }
}