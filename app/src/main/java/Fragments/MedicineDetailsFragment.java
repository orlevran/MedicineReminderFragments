package Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medicinereminderfragments.R;

import java.util.ArrayList;

import Adapters.TakingTimeAdapter;
import Controllers.UserController;
import Models.DataReceiver;
import Models.Medicines.Drops;
import Models.Medicines.Pill;
import Models.Medicines.Syrup;

public class MedicineDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DataReceiver receiver;
    private UserController controller;
    private Context context;
    private int position;

    TextView name, type, amount, back;
    ImageView imageView;
    RecyclerView recyclerView;

    public MedicineDetailsFragment(DataReceiver receiver, Context context) {
        this.receiver = receiver;
        this.controller = UserController.getInstance();
        this.context = context;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medicine_details, container, false);
        name = view.findViewById(R.id.details_name);
        imageView = view.findViewById(R.id.details_image_view);
        type = view.findViewById(R.id.details_type);
        amount = view.findViewById(R.id.details_amount);
        recyclerView = view.findViewById(R.id.details_rv);
        back = view.findViewById(R.id.details_back);

        setContent();

        back.setOnClickListener(lambda -> {
            this.receiver.onBackToHome();
        });

        return view;
    }

    private void setContent() {
        name.setText(this.controller.getUser().getMedicines().get(this.position).getName());
        type.setText(this.controller.getUser().getMedicines().get(position).getType().toString());
        switch (this.controller.getUser().getMedicines().get(this.position).getType()){
            case PILL:
                imageView.setImageResource(R.drawable.pill_logo);
                amount.setText(String.format("%d left out of %d. Dosage: %d",
                        ((Pill)this.controller.getUser().getMedicines().get(this.position)).getCurrentAmount(),
                        ((Pill)this.controller.getUser().getMedicines().get(this.position)).getInitialAmount(),
                        ((Pill)this.controller.getUser().getMedicines().get(this.position)).getDosage()));
                break;
            case SYRUP:
                imageView.setImageResource(R.drawable.syrup_logo);
                amount.setText(String.format("Dosage: %f",
                        ((Syrup)this.controller.getUser().getMedicines().get(this.position)).getDosage()));
                break;
            case OINTMENT:
                imageView.setImageResource(R.drawable.ointment_logo);
                amount.setText("");
                break;
            case DROPS:
                imageView.setImageResource(R.drawable.drops_logo);
                amount.setText(String.format("Dosage: %d",
                        ((Drops)this.controller.getUser().getMedicines().get(this.position)).getDosage()));
                break;
        }

        ArrayList<Integer> list = new ArrayList<>();
        if(this.controller.getUser().getMedicines().get(this.position).getMorningTaking() >= 360 &&
                this.controller.getUser().getMedicines().get(this.position).getMorningTaking() <= 659){
            list.add(this.controller.getUser().getMedicines().get(this.position).getMorningTaking());
        }
        if(this.controller.getUser().getMedicines().get(this.position).getAfternoonTaking() >= 660 &&
                this.controller.getUser().getMedicines().get(this.position).getAfternoonTaking() <= 899){
            list.add(this.controller.getUser().getMedicines().get(this.position).getAfternoonTaking());
        }
        if(this.controller.getUser().getMedicines().get(this.position).getEveningTaking() >= 900 &&
                this.controller.getUser().getMedicines().get(this.position).getEveningTaking() <= 1139){
            list.add(this.controller.getUser().getMedicines().get(this.position).getEveningTaking());
        }
        if(this.controller.getUser().getMedicines().get(this.position).getNightTaking() >= 1140 &&
                this.controller.getUser().getMedicines().get(this.position).getNightTaking() <= 1439){
            list.add(this.controller.getUser().getMedicines().get(this.position).getNightTaking());
        }

        TakingTimeAdapter adapter = new TakingTimeAdapter(list);
        adapter.notifyDataSetChanged();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
}