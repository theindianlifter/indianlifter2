package in.theindianlifter.indianlifter.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import in.theindianlifter.indianlifter.R;

import static in.theindianlifter.indianlifter.constants.AppConstants.CONTACT_NUMBER;
import static in.theindianlifter.indianlifter.constants.AppConstants.COUNTRY_CODE;
import static in.theindianlifter.indianlifter.constants.AppConstants.VERIFICATION_ID;

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
        mAuth = FirebaseAuth.getInstance();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideLoading();
                            FirebaseUser user = task.getResult().getUser();
                            savePhoneNumberToDatabase(user, getIntent().getStringExtra(CONTACT_NUMBER), getIntent().getStringExtra(COUNTRY_CODE));
                            startActivity(new Intent(OTPActivity.this, InfoActivity.class));
                            // ...
                        } else {
                            hideLoading();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                showErrorMessage("Invalid Otp!",
                                        null,
                                        null,
                                        "OK",
                                        "");
                            } else {
                                showErrorMessage("Verification failed! Try again later.",
                                        null,
                                        null,
                                        "OK",
                                        "");
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                validateAndInsert();
                break;
            default:
                break;
        }
    }

    private void validateAndInsert() {
        if (!TextUtils.isEmpty(etOtp.getText().toString().trim()) && etOtp.getText().toString().length() == 6) {
            credential = PhoneAuthProvider.getCredential(getIntent().getStringExtra(VERIFICATION_ID), etOtp.getText().toString().trim());
            signInWithPhoneAuthCredential(credential);
            showLoading(OTPActivity.this);
        } else if (isNetworkAvailable(this)) {
            showErrorMessage("Please check your Internet Connection!",
                    null,
                    new OnPositiveButtonClick() {
                        @Override
                        public void onPositiveButtonClick() {
                            validateAndInsert();
                        }
                    },
                    "Retry",
                    "");
        } else {
            showErrorMessage("Please enter valid otp!",
                    null,
                    null,
                    "OK",
                    "");
        }
    }
}
