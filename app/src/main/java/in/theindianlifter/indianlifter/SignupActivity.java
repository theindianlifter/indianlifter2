package in.theindianlifter.indianlifter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import static in.theindianlifter.indianlifter.AppConstants.CONTACT_NUMBER;
import static in.theindianlifter.indianlifter.AppConstants.COUNTRY_CODE;
import static in.theindianlifter.indianlifter.AppConstants.VERIFICATION_ID;

public class SignupActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = SignupActivity.class.getSimpleName();
    private String contactNumber = "+919872226052";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private AppCompatEditText etPhone;
    private AppCompatButton btnSignup;
    String verificationCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initializeViews();
        addListeners();
        setCallbacks();
        setupFirebase();
//        sendOTP();
    }

    private void initializeViews() {
        etPhone = findViewById(R.id.etPhone);
        btnSignup = findViewById(R.id.btnSignup);
        mAuth = FirebaseAuth.getInstance();
    }

    private void addListeners() {
        btnSignup.setOnClickListener(this);
    }

    private void setCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                verificationCode = verificationId;
                Intent mIntent = new Intent(SignupActivity.this, OTPActivity.class);
                mIntent.putExtra(VERIFICATION_ID, verificationId);
                mIntent.putExtra(CONTACT_NUMBER, contactNumber);
                mIntent.putExtra(COUNTRY_CODE, "+91");
                startActivity(mIntent);

            }
        };
    }

    private void sendOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + contactNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup:
                contactNumber = etPhone.getText().toString().trim();
                if (!TextUtils.isEmpty(contactNumber) && contactNumber.matches("^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[56789]\\d{9}$") && isNetworkAvailable(this)) {
                    sendOTP();
                }
                break;
            default:
                break;
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            savePhoneNumberToDatabase(user, contactNumber, "+91");
                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }
                });
    }
}
