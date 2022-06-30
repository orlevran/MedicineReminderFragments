package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.medicinereminderfragments.R;

import Models.DataReceiver;
import Models.SecurityQuestion;
import Models.Sponsor;

public class SignupStepTwoFragment extends Fragment {

    private DataReceiver receiver;
    EditText sponsorFirstName, sponsorLastName, sponsorPhone, securityAnswer;
    Spinner areaCode, securityQuestion;
    TextView backButton, submitButton;

    private static String[] AREA_CODES = {"OOO", "050", "052", "053", "054", "055", "058"},
            SECURITY_QUESTIONS = {"Security question", "In what city were you born?",
                    "What is the name of your favorite pet?", "What is your mother's maiden name?",
                    "What high school did you attend?", "What is the name of your first school?",
                    "What was the make of your first car?", "What was your favorite food as a child?"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignupStepTwoFragment(DataReceiver receiver) {
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
        View view = inflater.inflate(R.layout.fragment_signup_step_two, container, false);
        sponsorFirstName = view.findViewById(R.id.signup_sponsor_first_name);
        sponsorLastName = view.findViewById(R.id.signup_sponsor_last_name);
        areaCode = view.findViewById(R.id.signup_sponsor_area_code);
        sponsorPhone = view.findViewById(R.id.signup_sponsor_phone_number);
        securityQuestion = view.findViewById(R.id.signup_security_question);
        securityAnswer = view.findViewById(R.id.signup_security_answer);
        backButton = view.findViewById(R.id.signup_step_one_back_button);
        submitButton = view.findViewById(R.id.signup_step_two_submit_button);

        ArrayAdapter phoneAdapter =
                new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, AREA_CODES);
        areaCode.setAdapter(phoneAdapter);
        ArrayAdapter securityQuestionAdapter =
                new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, SECURITY_QUESTIONS);
        securityQuestion.setAdapter(securityQuestionAdapter);

        setButtons();

        return view;
    }

    private void setButtons() {
        backButton.setOnClickListener(lambda -> {
            this.receiver.onSignup();
        });

        submitButton.setOnClickListener(lambda -> {
            Sponsor _sponsor;
            SecurityQuestion _securityQuestion;
            if (sponsorFirstName.getText().toString().isEmpty() || sponsorLastName.getText().toString().isEmpty() ||
                    !areaCode.getSelectedItem().toString().matches("[0-9]*") || !sponsorPhone.getText().toString().matches("[0-9]*") ||
                    sponsorPhone.getText().toString().length() != 7) {
                _sponsor = null;
            } else {
                _sponsor = new Sponsor(sponsorFirstName.getText().toString(), sponsorLastName.getText().toString(),
                        areaCode.getSelectedItem().toString() + sponsorPhone.getText().toString());
            }

            if (securityQuestion.getSelectedItem().toString().equals(SECURITY_QUESTIONS[0]) ||
                    securityAnswer.getText().toString().isEmpty()) {
                _securityQuestion = null;
            } else {
                _securityQuestion = new SecurityQuestion(
                        securityQuestion.getSelectedItem().toString(), securityAnswer.getText().toString());
            }

            this.receiver.onDataReceivedCreateUser(_sponsor, _securityQuestion);
        });
    }
}