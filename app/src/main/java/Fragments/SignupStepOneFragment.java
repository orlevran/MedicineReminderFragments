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

public class SignupStepOneFragment extends Fragment {

    private DataReceiver receiver;
    EditText username, password, verify, firstname, lastname;
    TextView cancelButton, continueButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignupStepOneFragment(DataReceiver receiver) {
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
        View view = inflater.inflate(R.layout.fragment_signup_step_one, container, false);
        // Inflate the layout for this fragment
        username = view.findViewById(R.id.signup_username);
        password = view.findViewById(R.id.signup_password);
        verify = view.findViewById(R.id.signup_verify_password);
        firstname = view.findViewById(R.id.signup_first_name);
        lastname = view.findViewById(R.id.signup_last_name);
        cancelButton = view.findViewById(R.id.signup_step_one_cancel_button);
        continueButton = view.findViewById(R.id.signup_step_one_continue_button);
        defineButtons();

        return view;
    }

    private void defineButtons() {
        cancelButton.setOnClickListener(lambda -> {
            this.receiver.onBackToMain(false);
        });

        continueButton.setOnClickListener(lambda -> {
            this.receiver.onDataReceivedSignup(username.getText().toString(),
                    password.getText().toString(), verify.getText().toString(),
                    firstname.getText().toString(), lastname.getText().toString());
        });
    }
}