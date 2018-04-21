package in.theindianlifter.indianlifter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static in.theindianlifter.indianlifter.AppConstants.CONTACT_NUMBER;
import static in.theindianlifter.indianlifter.AppConstants.COUNTRY_CODE;
import static in.theindianlifter.indianlifter.AppConstants.USERS;

/**
 * Created by rajatdhamija on 21/04/18.
 */

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private DatabaseReference mainRef;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static boolean isNetworkAvailable(final Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) (context.getApplicationContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase myDb = FirebaseDatabase.getInstance();
        mainRef = myDb.getReference(USERS);
    }

    public void savePhoneNumberToDatabase(FirebaseUser firebaseUser, String contactNumber, String countryCode) {
        DatabaseReference authRef = mainRef.child(firebaseUser.getUid());
        authRef.child(CONTACT_NUMBER).setValue(contactNumber);
        authRef.child(COUNTRY_CODE).setValue(countryCode);
    }
}
