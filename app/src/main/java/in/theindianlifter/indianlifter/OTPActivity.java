package in.theindianlifter.indianlifter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static in.theindianlifter.indianlifter.AppConstants.CONTACT_NUMBER;
import static in.theindianlifter.indianlifter.AppConstants.COUNTRY_CODE;
import static in.theindianlifter.indianlifter.AppConstants.VERIFICATION_ID;

public class OTPActivity extends BaseActivity implements View.OnClickListener {
    private PhoneAuthCredential credential;
    private FirebaseAuth mAuth;
    private AppCompatEditText etOtp;
    private AppCompatButton btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        initializeViews();
        setupFirebase();
    }

    private void initializeViews() {
        etOtp = findViewById(R.id.etOtp);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            savePhoneNumberToDatabase(user, getIntent().getStringExtra(CONTACT_NUMBER), getIntent().getStringExtra(COUNTRY_CODE));
                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if (!TextUtils.isEmpty(etOtp.getText().toString().trim()) && isNetworkAvailable(this) && etOtp.getText().toString().length() == 6) {
                    credential = PhoneAuthProvider.getCredential(getIntent().getStringExtra(VERIFICATION_ID), etOtp.getText().toString().trim());
                    signInWithPhoneAuthCredential(credential);
                }
                break;
            default:
                break;
        }
    }
}
