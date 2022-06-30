package Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.medicinereminderfragments.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import Adapters.MedicinesViewListAdapter;
import Controllers.UserController;
import Models.DataReceiver;
import Models.Medicines.Medicine;

public class UserHomeFragment extends Fragment {

    private DataReceiver receiver;
    private UserController userController;
    private Context context;

    TabLayout tabLayout;
    RecyclerView recyclerView;
    TextView showList, editUser, logout;

    ArrayList<Medicine>[] arrayLists;
    MedicinesViewListAdapter[] adapters;
    private final int NUM_OF_ADAPTERS = 4;
    private final String[] TIMES = {"Night", "Morning", "Afternoon", "Evening"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserHomeFragment(DataReceiver receiver, Context context) {
        this.receiver = receiver;
        this.userController = UserController.getInstance();
        this.context = context;
        //defineAdapters();
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
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        recyclerView = view.findViewById(R.id.user_home_rv);
        tabLayout = view.findViewById(R.id.user_home_tab_layout);
        defineTabLayout();

        defineAdapters();

        showList = view.findViewById(R.id.user_home_show_list);
        editUser = view.findViewById(R.id.user_home_edit_user);
        logout = view.findViewById(R.id.user_home_logout);
        defineButtons();

        return view;
    }

    private void defineArrayLists(ArrayList<Medicine>[] arrayLists){

        for(int i = 0 ; i < arrayLists.length ; i++){
            arrayLists[i] = new ArrayList<>();
        }

        for(Medicine medicine : this.userController.getUser().getMedicines()){
            if(medicine.getMorningTaking() != -1 && medicine.getMorningTaking() >= 360 && medicine.getMorningTaking() <= 659)
                arrayLists[0].add(medicine);
            if(medicine.getAfternoonTaking() != -1 && medicine.getAfternoonTaking() >= 660 && medicine.getAfternoonTaking() <= 899)
                arrayLists[1].add(medicine);
            if(medicine.getEveningTaking() != -1 && medicine.getEveningTaking() >= 900 && medicine.getEveningTaking() <= 1139)
                arrayLists[2].add(medicine);
            if(medicine.getNightTaking() != -1 && medicine.getNightTaking() >= 1140 && medicine.getNightTaking() <= 1439)
                arrayLists[3].add(medicine);
        }
    }

    private void defineAdapters() {
        this.arrayLists = new ArrayList[NUM_OF_ADAPTERS];
        defineArrayLists(arrayLists);
        adapters = new MedicinesViewListAdapter[NUM_OF_ADAPTERS];
        for(int i = 0 ; i < arrayLists.length ; i++){
            int index = (i+1) % 4;
            adapters[i] = new MedicinesViewListAdapter(this.receiver, this.getContext(),arrayLists[index], TIMES[index]);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapters[0]);
        notifyAdapters();
    }

    private void defineButtons() {
        showList.setOnClickListener(lambda -> {
            this.receiver.onEditList();
        });

        editUser.setOnClickListener(lambda -> {

        });

        logout.setOnClickListener(lambda -> {
            this.receiver.onBackToMain(true);
        });
    }

    private void defineTabLayout(){
        tabLayout.addTab(tabLayout.newTab().setText("Morning"));
        tabLayout.addTab(tabLayout.newTab().setText("Afternoon"));
        tabLayout.addTab(tabLayout.newTab().setText("Evening"));
        tabLayout.addTab(tabLayout.newTab().setText("Night"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                recyclerView.setAdapter(adapters[tab.getPosition()]);
                adapters[(tab.getPosition() + 1) % 4].notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Unused
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Unused
            }
        });
    }

    public void notifyAdapters(){
        for(MedicinesViewListAdapter adapter : adapters)
            adapter.notifyDataSetChanged();
    }
}