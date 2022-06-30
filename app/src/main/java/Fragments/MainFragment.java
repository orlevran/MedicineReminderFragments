package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medicinereminderfragments.R;

import Models.DataReceiver;

public class MainFragment extends Fragment {

    private DataReceiver receiver;
    EditText username, password;
    TextView login, signup;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment(DataReceiver receiver) {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        username = view.findViewById(R.id.main_username);
        password = view.findViewById(R.id.main_password);
        login = view.findViewById(R.id.main_login);
        signup = view.findViewById(R.id.main_signup);

        setButtons();
        return view;
    }

    private void setButtons(){
        login.setOnClickListener(lambda -> {
            this.receiver.onLogin(username.getText().toString(), password.getText().toString());
        });

        signup.setOnClickListener(lambda -> {
            this.receiver.onSignup();
        });
    }
}