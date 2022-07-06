package Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicinereminderfragments.R;
import com.google.android.material.tabs.TabLayout;

import Controllers.UserController;
import Models.DataReceiver;
import Models.Medicines.Drops;
import Models.Medicines.Medicine;
import Models.Medicines.Ointment;
import Models.Medicines.Pill;
import Models.Medicines.Syrup;

public class EditMedicineFragment extends Fragment {

    private DataReceiver receiver;
    private Context context;
    private UserController controller;
    private int position;

    EditText name, initial, dosage;
    TabLayout tabLayout;
    ImageView imageView;
    TextView description, cancel, confirm;
    Switch morningSwitch, afternoonSwitch,eveningSwitch, nightSwitch;
    SeekBar morningSeekbar, afternoonSeekbar, eveningSeekbar, nightSeekbar;

    String type = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditMedicineFragment(DataReceiver receiver, Context context) {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_medicine, container, false);

        createElements(view);
        declareTabLayout(view);
        declareSeekBars(view);
        declareButtons(view);

        return view;
    }

    private void declareButtons(View view) {
        cancel.setOnClickListener(lambda -> {
            this.receiver.onEditList();
        });
        confirm.setOnClickListener(lambda -> {
            if(!name.getText().toString().isEmpty()){
                if(morningSwitch.isEnabled() || afternoonSwitch.isEnabled() || eveningSwitch.isEnabled() || nightSwitch.isEnabled()){
                        Medicine tmp;
                        switch (tabLayout.getSelectedTabPosition()){
                            case 0:
                                try{
                                    if(Integer.parseInt(dosage.getText().toString()) < Integer.parseInt(initial.getText().toString())) {
                                        tmp = new Pill(this.controller.getUser().getUsername() + "_" + position,
                                                name.getText().toString(),
                                                morningSwitch.isEnabled() ? morningSeekbar.getProgress() : -1,
                                                afternoonSwitch.isEnabled() ? afternoonSeekbar.getProgress() : -1,
                                                eveningSwitch.isEnabled() ? eveningSeekbar.getProgress() : -1,
                                                nightSwitch.isEnabled() ? nightSeekbar.getProgress() : -1,
                                                Integer.parseInt(initial.getText().toString()),
                                                Integer.parseInt(initial.getText().toString()),
                                                Integer.parseInt(dosage.getText().toString()));
                                        this.controller.getUser().getMedicines().set(position, tmp);
                                        this.controller.getMedicinesDB().setToPill((Pill)tmp, this.controller, position);
                                        this.receiver.onEditList();
                                    }else
                                        Toast.makeText(this.context, "Initial amount must be higher than dosage", Toast.LENGTH_SHORT).show();
                                } catch (Exception ex){
                                    initial.getText().clear();
                                    dosage.getText().clear();
                                    Toast.makeText(this.context, "Invalid input", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                try{
                                    tmp = new Syrup(this.controller.getUser().getUsername() + "_" + position,
                                            name.getText().toString(),
                                            morningSwitch.isEnabled() ? morningSeekbar.getProgress() : -1,
                                            afternoonSwitch.isEnabled() ? afternoonSeekbar.getProgress() : -1,
                                            eveningSwitch.isEnabled() ? afternoonSeekbar.getProgress() : -1,
                                            nightSwitch.isEnabled() ? nightSeekbar.getProgress() : -1,
                                            Double.parseDouble(dosage.getText().toString()));
                                    this.controller.getUser().getMedicines().set(position, tmp);
                                    this.controller.getMedicinesDB().setToSyrup((Syrup)tmp, this.controller, position);
                                    this.receiver.onEditList();
                                } catch (Exception ex){
                                    initial.getText().clear();
                                    dosage.getText().clear();
                                    Toast.makeText(this.context, "Invalid input", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                try{
                                    tmp = new Ointment(this.controller.getUser().getUsername() + "_" + position,
                                            name.getText().toString(),
                                            morningSwitch.isEnabled() ? morningSeekbar.getProgress() : -1,
                                            afternoonSwitch.isEnabled() ? afternoonSeekbar.getProgress() : -1,
                                            eveningSwitch.isEnabled() ? afternoonSeekbar.getProgress() : -1,
                                            nightSwitch.isEnabled() ? nightSeekbar.getProgress() : -1);
                                    this.controller.getUser().getMedicines().set(position, tmp);
                                    this.controller.getMedicinesDB().setToOintment((Ointment)tmp, this.controller, position);
                                    this.receiver.onEditList();
                                } catch (Exception ex){
                                    initial.getText().clear();
                                    dosage.getText().clear();
                                    Toast.makeText(this.context, "Invalid input", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case  3:
                                try{
                                    tmp = new Drops(this.controller.getUser().getUsername() + "_" + position,
                                            name.getText().toString(),
                                            morningSwitch.isEnabled() ? morningSeekbar.getProgress() : -1,
                                            afternoonSwitch.isEnabled() ? afternoonSeekbar.getProgress() : -1,
                                            eveningSwitch.isEnabled() ? afternoonSeekbar.getProgress() : -1,
                                            nightSwitch.isEnabled() ? nightSeekbar.getProgress() : -1,
                                            Integer.parseInt(dosage.getText().toString()));
                                    this.controller.getUser().getMedicines().set(position, tmp);
                                    this.controller.getMedicinesDB().setToDrops((Drops) tmp, this.controller, position);
                                    this.receiver.onEditList();
                                } catch (Exception ex){
                                    initial.getText().clear();
                                    dosage.getText().clear();
                                    //Toast.makeText(this.context, "Invalid input", Toast.LENGTH_SHORT).show();
                                    this.receiver.onEditList();
                                }
                                break;
                        }
                } else
                    Toast.makeText(this.context, "Must declare at least one time of taking", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this.context, "Empty name", Toast.LENGTH_SHORT).show();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void declareSeekBars(View view) {
        declareSeekBar(morningSwitch, morningSeekbar, "morning");
        declareSeekBar(afternoonSwitch, afternoonSeekbar, "afternoon");
        declareSeekBar(eveningSwitch, eveningSeekbar, "evening");
        declareSeekBar(nightSwitch, nightSeekbar, "night");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void declareSeekBar(Switch sw, SeekBar sb, String time){
        sb.setEnabled(false);
        int min ,max;
        switch (time){
            case "morning":
                min = 360;
                max = 659;
                break;
            case "afternoon":
                min = 660;
                max = 899;
                break;
            case "evening":
                min = 900;
                max = 1139;
                break;
            case "night":
                min = 1140;
                max = 1439;
                break;
            default:
                min = 0;
                max = 100;
                break;
        }
        sb.setMin(min);
        sb.setMax(max);
        sb.setProgress(sb.getMin());
        sw.setText(String.format("%s (%02d:00 - %02d:59)", time, (min / 60), (max / 60)));

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sb.setEnabled(b);
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int min, max, hour = i / 60, minute = i % 60;
                switch (time){
                    case "morning":
                        min = 360;
                        max = 659;
                        break;
                    case "afternoon":
                        min = 660;
                        max = 899;
                        break;
                    case "evening":
                        min = 900;
                        max = 1139;
                        break;
                    case "night":
                        min = 1140;
                        max = 1439;
                        break;
                    default:
                        min = 0;
                        max = 100;
                        break;
                }

                seekBar.setMin(min);
                seekBar.setMax(max);
                if(sw.isEnabled()){
                    sw.setText(String.format("%s (%02d:00 - %02d:59): %02d:%02d",
                            time, min/60, max/60, hour, minute));
                } else {
                    sw.setText(String.format("%s (%02d:00 - %02d:59)",
                            time, min/60, max/60));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Remain unused
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Remain unused
            }
        });
    }

    private void declareTabLayout(View view){
        tabLayout.addTab(tabLayout.newTab().setText("Pill"));
        tabLayout.addTab(tabLayout.newTab().setText("Syrup"));
        tabLayout.addTab(tabLayout.newTab().setText("Ointment"));
        tabLayout.addTab(tabLayout.newTab().setText("Drops"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                initial.getText().clear();
                dosage.getText().clear();

                switch (tab.getPosition()){
                    case 0:
                        imageView.setImageResource(R.drawable.pill_logo);
                        initial.setEnabled(true);
                        initial.setHint("Total amount (mg)");
                        dosage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        dosage.setEnabled(true);
                        dosage.setHint("Single dosage (mg)");
                        dosage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        type = "Pills";
                        break;
                    case 1:
                        imageView.setImageResource(R.drawable.syrup_logo);
                        initial.setEnabled(false);
                        initial.setHint("- - -");
                        initial.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        dosage.setEnabled(true);
                        dosage.setHint("Single dosage (ml)");
                        dosage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        type = "Syrup";
                        break;
                    case 2:
                        imageView.setImageResource(R.drawable.ointment_logo);
                        initial.setEnabled(false);
                        initial.setHint("- - -");
                        initial.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        dosage.setEnabled(false);
                        dosage.setHint("- - -");
                        dosage.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        type = "Ointment";
                        break;
                    case 3:
                        imageView.setImageResource(R.drawable.drops_logo);
                        initial.setEnabled(false);
                        initial.setHint("- - -");
                        initial.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        dosage.setEnabled(true);
                        dosage.setHint("Single dosage(drops)");
                        dosage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        type = "Drops";
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void createElements(View view){
        name = view.findViewById(R.id.edit_medicine_name);
        tabLayout = view.findViewById(R.id.edit_type_tab_layout);
        imageView = view.findViewById(R.id.edit_image_view);
        description = view.findViewById(R.id.edit_medicine_description);
        morningSwitch = view.findViewById(R.id.edit_morning_switch);
        morningSeekbar = view.findViewById(R.id.edit_morning_seek_bar);
        afternoonSwitch = view.findViewById(R.id.edit_afternoon_switch);
        afternoonSeekbar = view.findViewById(R.id.edit_afternoon_seek_bar);
        eveningSwitch = view.findViewById(R.id.edit_evening_switch);
        eveningSeekbar = view.findViewById(R.id.edit_evening_seek_bar);
        nightSwitch = view.findViewById(R.id.edit_night_switch);
        nightSeekbar = view.findViewById(R.id.edit_night_seek_bar);
        initial = view.findViewById(R.id.edit_initial_amount);
        dosage = view.findViewById(R.id.edit_dosage);
        cancel = view.findViewById(R.id.edit_cancel_button);
        confirm = view.findViewById(R.id.edit_confirm_button);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                description.setText(String.format("%s - %s", name.getText().toString(), type));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}