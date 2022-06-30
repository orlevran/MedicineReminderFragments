package Services;

import android.Manifest;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.medicinereminderfragments.R;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Models.Medicines.Medicine;
import Models.Sponsor;

public class TimerService extends IntentService {

    private ArrayList<Medicine> medicines;
    private Sponsor sponsor;
    private String userFullName;

    public TimerService() {
        super("TimerService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        this.medicines = (ArrayList<Medicine>) intent.getSerializableExtra("medicines");
        this.sponsor = (Sponsor) intent.getSerializableExtra("sponsor");
        this.userFullName = intent.getStringExtra("fullName");

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Log.v("Timer", String.format("%02d:%02d", LocalTime.now().getHour(), LocalTime.now().getMinute()));

                for(Medicine medicine : medicines){
                    if(checkTakingTime("Night", medicine.getNightTaking()) || checkTakingTime("Morning", medicine.getMorningTaking()) ||
                            checkTakingTime("Afternoon", medicine.getAfternoonTaking()) || checkTakingTime("Evening", medicine.getEveningTaking())){
                        medicine.setTimeToTake(true);
                        showNotification(medicine);
                    }
                    else if(check30MinutesPassed("Night", medicine.getNightTaking()) || check30MinutesPassed("Morning", medicine.getMorningTaking()) ||
                            check30MinutesPassed("Afternoon", medicine.getAfternoonTaking()) || check30MinutesPassed("Evening", medicine.getEveningTaking()) && medicine.isTimeToTake()){
                        medicine.setTimeToTake(false);
                        sendTextMessage(medicine, sponsor, userFullName);
                    }
                }
            }
        };
        timer.schedule(task, 0, 60000);
    }

    private void sendTextMessage(Medicine medicine, Sponsor sponsor, String userFullName) {
        String txtMessage = String.format("Hi, %s. It's me- %s. Please make sure I didn't forget to take my %s",
                sponsor.getFirstName() + " " + sponsor.getLastName(), userFullName, medicine.getName());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                try{
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(sponsor.getPhoneNumber(), null, txtMessage, null, null);
                } catch (Exception e){
                    Toast.makeText(this, "Could not send SMS", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showNotification(Medicine medicine) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Declare the notifications system
            NotificationChannel channel = new NotificationChannel("myNotification", "My notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder
                    (TimerService.this, "myNotification").
                    setSmallIcon(R.drawable.ic_baseline_notifications_24).
                    setContentTitle("Medicine Reminder")
                    .setContentText(String.format("It's time to take %s", medicine.getName())).
                    setAutoCancel(false);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(TimerService.this);
            managerCompat.notify(1356, builder.build());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean check30MinutesPassed(String time, int takingTime) {
        switch (time){
            case "Night":
                if(takingTime < 0 || takingTime > 239)
                    return false;
                break;
            case "Morning":
                if(takingTime < 240 || takingTime > 659)
                    return false;
                break;
            case "Afternoon":
                if(takingTime < 660 || takingTime > 1079)
                    return false;
                break;
            case "Evening":
                if(takingTime < 1080 || takingTime > 1439)
                    return false;
                break;
            default:
                return false;
        }
        int currentTimeInMinutes = LocalTime.now().getHour() * 60 + LocalTime.now().getMinute();
        return currentTimeInMinutes - takingTime >= 30;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkTakingTime(String time, int takingTime) {
        switch (time){
            case "Night":
                if(takingTime < 0 || takingTime > 239)
                    return false;
                break;
            case "Morning":
                if(takingTime < 240 || takingTime > 659)
                    return false;
                break;
            case "Afternoon":
                if(takingTime < 660 || takingTime > 1079)
                    return false;
                break;
            case "Evening":
                if(takingTime < 1080 || takingTime > 1439)
                    return false;
                break;
            default:
                return false;
        }
        int currentTimeInMinutes = LocalTime.now().getHour() * 60 + LocalTime.now().getMinute();
        return currentTimeInMinutes - takingTime == 0;
    }
}