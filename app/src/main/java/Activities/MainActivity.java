package Activities;

import com.example.medicinereminderfragments.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Controllers.MedicinesDataBaseHelper;
import Controllers.UserController;
import Controllers.UserDataBaseHelper;
import Fragments.EditMedicineFragment;
import Fragments.MainFragment;
import Fragments.MedicineDetailsFragment;
import Fragments.MedicinesEditListFragment;
import Fragments.RefillFragment;
import Fragments.SignupStepOneFragment;
import Fragments.SignupStepTwoFragment;
import Fragments.UserHomeFragment;
import Models.DataReceiver;
import Models.Medicines.Medicine;
import Models.SecurityQuestion;
import Models.Sponsor;
import Models.User;
import Services.TimerService;

public class MainActivity extends AppCompatActivity implements DataReceiver {

    TextView mainTitle;
    FragmentContainerView fcv;
    FragmentManager manager;
    MainFragment main;
    SignupStepOneFragment stepOne;
    SignupStepTwoFragment stepTwo;
    UserHomeFragment home;
    //EditMedicineFragment edit;
    EditMedicineFragment[] edits;
    MedicinesEditListFragment list;
    RefillFragment refill;
    MedicineDetailsFragment details;
    UserController userController = UserController.getInstance();

    ArrayList<Medicine> medicines = new ArrayList<>();
    private static final int MEDICINES_FOR_USER = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTitle = findViewById(R.id.main_title);
        fcv = findViewById(R.id.fcv);
        manager = getSupportFragmentManager();
        defineFragments();
        manager.beginTransaction().add(R.id.fcv, main, null).commit();

        defineUserController();
    }

    private void defineFragments() {
        main = new MainFragment(this);
        stepOne = new SignupStepOneFragment(this);
        stepTwo = new SignupStepTwoFragment(this);
        home = new UserHomeFragment(this, this);
        list = new MedicinesEditListFragment(this, this);
        //edit = new EditMedicineFragment(this, this);
        edits = new EditMedicineFragment[MEDICINES_FOR_USER];
        for (int i = 0 ; i < edits.length ; i++) {
            edits[i] = new EditMedicineFragment(this, this);
        }
        refill = new RefillFragment(this, this);
        details = new MedicineDetailsFragment(this, this);
    }

    private void defineUserController() {
        userController.setUsersDB(new UserDataBaseHelper
                (this, null, null, 1));
        userController.setMedicinesDB
                (new MedicinesDataBaseHelper(this, null, null, 1));
    }

    @Override
    public void onLogin(String username, String password) {
        if (!username.isEmpty() && !password.isEmpty()) {
            userController.setUser(userController.getUsersDB().getUser(username, password));
            if (!userController.getUser().getUsername().isEmpty()) {
                userController.getUser().setMedicines(userController.getMedicinesDB().readMedicinesArrayList(username));

                manager.beginTransaction().replace(R.id.fcv, home, null).commitNow();
                startService(userController.getUser().getMedicines(), userController.getUser().getSponsor(),
                        this.userController.getUser().getFirstName() + " " + this.userController.getUser().getLastName());
                home.notifyAdapters();
                mainTitle.setText(String.format("Hello, %s", userController.getUser().getFirstName()));
            } else Toast.makeText(this, "Wrong input", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignup() {
        mainTitle.setText("Signup- Step 1\\2");
        manager.beginTransaction().replace(R.id.fcv, stepOne, null).commitNow();
    }

    @Override
    public void onDataReceivedSignup(String username, String password, String verify, String firstName, String lastName) {
        if (!username.isEmpty() && !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()) {
            if (!userController.getUsersDB().isUsernameExists(username)) {
                if (password.equals(verify)) {
                    if (nameIsValid(firstName) && nameIsValid(lastName)) {
                        userController.setUser(new User(username, password, firstName, lastName));
                        manager.beginTransaction().replace(R.id.fcv, stepTwo, null).commitNow();
                        mainTitle.setText("Signup- Step 2\\2");
                    } else
                        Toast.makeText(this, "Invalid name", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Password not equals to it's verification", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
    }

    private boolean nameIsValid(String name) {
        return name.matches("[A-Z][a-z]+");
    }

    @Override
    public void onDataReceivedCreateUser(Sponsor sponsor, SecurityQuestion question) {
        if (sponsor != null && question != null) {
            try{
                userController.getUser().setSponsor(sponsor);
                userController.getUser().setSecurityQuestion(question);
                medicines.clear();
                userController.getUsersDB().addUser(userController.getUser());
                for (int i = 0; i < MEDICINES_FOR_USER; i++){
                    medicines.add(new Medicine(userController.getUser().getUsername() + "_" + i));
                    userController.getMedicinesDB().addMedicine(medicines.get(i));
                }

                userController.getUser().setMedicines(medicines);
                home.notifyAdapters();
                manager.beginTransaction().replace(R.id.fcv, home, null).commitNow();
                mainTitle.setText(String.format("Hello, %s", userController.getUser().getFirstName()));
                startService(userController.getUser().getMedicines(), userController.getUser().getSponsor(),
                        this.userController.getUser().getFirstName() + " " + this.userController.getUser().getLastName());
            } catch (SQLiteConstraintException e){
                Toast.makeText(this, "Writing details to DB process failed", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackToMain(boolean forgetUser) {
        manager.beginTransaction().replace(R.id.fcv, main, null).commitNow();
        mainTitle.setText("Medicine Reminder");
        Intent intent = new Intent(MainActivity.this, TimerService.class);
        stopService(intent);
    }

    @Override
    public void onEditList() {
        manager.beginTransaction().replace(R.id.fcv, list, null).commitNow();
    }

    @Override
    public void onEditMedicine(int index) {
        edits[index].setPosition(index);
        manager.beginTransaction().replace(R.id.fcv, edits[index], null).commitNow();
    }

    @Override
    public void onBackToHome() {
        home.notifyAdapters();
        manager.beginTransaction().replace(R.id.fcv, home, null).commitNow();
    }

    @Override
    public void onRefill(int position) {
        refill.setPosition(position);
        manager.beginTransaction().replace(R.id.fcv, refill, null).commitNow();
    }

    @Override
    public void onDetails(int position) {
        details.setPosition(position);
        manager.beginTransaction().replace(R.id.fcv, details, null).commitNow();
    }

    public void startService(ArrayList<Medicine> medicines, Sponsor sponsor, String name){
        Intent intent = new Intent(MainActivity.this, TimerService.class);
        intent.putExtra("medicines", medicines);
        intent.putExtra("sponsor", sponsor);
        intent.putExtra("fullName", name);

        startService(intent);
    }
}