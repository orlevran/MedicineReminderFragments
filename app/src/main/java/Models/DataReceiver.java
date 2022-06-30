package Models;

public interface DataReceiver {
    void onLogin(String username, String password);
    void onSignup();
    void onDataReceivedSignup(String username, String password, String verify, String firstName, String lastName);
    void onDataReceivedCreateUser(Sponsor sponsor, SecurityQuestion question);
    void onBackToMain(boolean forgetUser);
    void onEditList();
    void onEditMedicine(int index);
    void onBackToHome();
    void onRefill(int position);
    void onDetails(int position);
}
