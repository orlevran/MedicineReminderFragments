package Models;

import java.util.ArrayList;

import Models.Medicines.Medicine;

public class User {

    private String username, password, firstName, lastName;
    private Sponsor sponsor;
    private SecurityQuestion securityQuestion;
    private ArrayList<Medicine> medicines;

    public User(){
        setUsername("");
        setPassword("");
        setFirstName("");
        setLastName("");
        setSponsor(null);
        setSecurityQuestion(null);
        setMedicines(null);
    }

    public User(String username, String password, String firstName, String lastName) {
        setUsername(username);
        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
        setSponsor(null);
        setSecurityQuestion(null);
        setMedicines(null);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Sponsor getSponsor() {
        return this.sponsor;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    public SecurityQuestion getSecurityQuestion() {
        return this.securityQuestion;
    }

    public void setSecurityQuestion(SecurityQuestion securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public ArrayList<Medicine> getMedicines() {
        return this.medicines;
    }

    public void setMedicines(ArrayList<Medicine> medicines) {
        this.medicines = medicines;
    }
}
