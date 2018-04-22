package in.theindianlifter.indianlifter.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.theindianlifter.indianlifter.utils.ProgressDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static in.theindianlifter.indianlifter.constants.AppConstants.CONTACT_NUMBER;
import static in.theindianlifter.indianlifter.constants.AppConstants.COUNTRY_CODE;
import static in.theindianlifter.indianlifter.constants.AppConstants.USERS;

/**
 * Created by rajatdhamija
 * 21/04/18.
 */

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private DatabaseReference mainRef;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


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

    public void showLoading(Context context) {
        ProgressDialog.showProgressDialog(context);
    }

    public void hideLoading() {
        ProgressDialog.dismissProgressDialog();
    }

    public void showErrorMessage(final String errorMessgae,
                                 final OnNegativeButtonClick onNegativeButtonClick,
                                 final OnPositiveButtonClick onPositiveButtonClick,
                                 final String positiveButtonText,
                                 final String negativeButtonText) {
        new AlertDialog.Builder(this)
                .setMessage(errorMessgae)
                .setCancelable(false)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (onPositiveButtonClick != null) {
                            onPositiveButtonClick.onPositiveButtonClick();
                        }
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (onNegativeButtonClick != null) {
                            onNegativeButtonClick.onNegativeButtonClick();
                        }
                    }
                }).show();

    }

     interface OnNegativeButtonClick {
        void onNegativeButtonClick();
    }

     interface OnPositiveButtonClick {
        void onPositiveButtonClick();
    }
}
