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

import Adapters.MedicinesEditListAdapter;
import Models.DataReceiver;

public class MedicinesEditListFragment extends Fragment {
    MedicinesEditListAdapter adapter;
    Context context;
    private DataReceiver receiver;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MedicinesEditListFragment(DataReceiver receiver, Context context) {
        adapter = new MedicinesEditListAdapter(receiver);
        this.context = context;
        this.receiver = receiver;
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
        RecyclerView recyclerView;
        TextView backButton;
        View view = inflater.inflate(R.layout.fragment_medicines_edit_list, container, false);
        recyclerView = view.findViewById(R.id.medicines_edit_list_rv);
        backButton = view.findViewById(R.id.medicines_edit_back_button);

        backButton.setOnClickListener(lambda -> {
            this.receiver.onBackToHome();
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }
}