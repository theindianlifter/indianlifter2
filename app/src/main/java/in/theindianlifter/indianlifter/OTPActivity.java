package in.theindianlifter.indianlifter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OTPActivity extends AppCompatActivity {
    private String otp = "4444";
    private DatabaseReference mainRef;
    private DatabaseReference authRef;
    private FirebaseDatabase myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setupFirebase();
        savePhoneNumberToDatabase();
    }

    private void setupFirebase() {
        myDb = FirebaseDatabase.getInstance();
        mainRef = myDb.getReference("contact_numbers");
    }

    private void savePhoneNumberToDatabase() {
        authRef = mainRef.push();
        authRef.child("contact_number").setValue("9872226052");
        authRef.child("country_code").setValue("+91");
    }
}
