package in.theindianlifter.indianlifter.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import in.theindianlifter.indianlifter.R;

import static in.theindianlifter.indianlifter.constants.AppConstants.CITY;
import static in.theindianlifter.indianlifter.constants.AppConstants.EMIAL;
import static in.theindianlifter.indianlifter.constants.AppConstants.FULL_NAME;
import static in.theindianlifter.indianlifter.constants.AppConstants.IMAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.USERS;


public class InfoActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 1212;
    private AppCompatEditText etName, etEmail, etCity;
    private String name, email, city;
    private AppCompatTextView tvAddPhoto;
    private CircleImageView ivPhoto;
    private AppCompatButton btnSubmit;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase myDb;
    DatabaseReference mainRef, userRef;
    FirebaseStorage userStorage = FirebaseStorage.getInstance();
    StorageReference storageRef = userStorage.getReference();
    StorageReference imagesRef = storageRef.child("profile");
    private Uri userImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setUpFirebase();
        initializeViews();
        addListerners();
    }

    private void setUpFirebase() {
        mAuth = FirebaseAuth.getInstance();
        myDb = FirebaseDatabase.getInstance();
        mainRef = myDb.getReference(USERS);
    }

    private void initializeViews() {
        etName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etCity = findViewById(R.id.etCity);
        tvAddPhoto = findViewById(R.id.tvAddPhoto);
        ivPhoto = findViewById(R.id.ivPhoto);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void addListerners() {
        tvAddPhoto.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddPhoto:
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                break;
            case R.id.btnSubmit:
                validateInputs();
                break;
            default:
                break;
        }
    }

    private void validateInputs() {
        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        city = etCity.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showErrorMessage("Enter a valid name.",
                    null,
                    null,
                    "OK",
                    "");
        } else if (TextUtils.isEmpty(email)) {
            showErrorMessage("Enter a valid email.",
                    null,
                    null,
                    "OK",
                    "");
        } else if (TextUtils.isEmpty(city)) {
            showErrorMessage("Enter a valid city.",
                    null,
                    null,
                    "OK",
                    "");
        } else if (userImageUri == null) {
            showErrorMessage("Please upload a profile photo.",
                    null,
                    null,
                    "OK",
                    "");
        } else {
            showLoading(InfoActivity.this);
            updateImage();
        }
    }

    private void updateImage() {
        user = mAuth.getCurrentUser();
        StorageReference usersRef = imagesRef.child(user + "/" + userImageUri.getLastPathSegment());
        UploadTask uploadTask = usersRef.putFile(userImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                showErrorMessage("Image upload failed!.",
                        null,
                        new OnPositiveButtonClick() {
                            @Override
                            public void onPositiveButtonClick() {
                                updateImage();
                            }
                        },
                        "Retry",
                        "Cancel");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                updateDatabase(downloadUrl);
            }
        });
    }

    private void updateDatabase(Uri downloadUrl) {
        if (user != null) {
            userRef = mainRef.child(user.getUid());
            userRef.child(FULL_NAME).setValue(name);
            userRef.child(EMIAL).setValue(email);
            userRef.child(CITY).setValue(city);
            userRef.child(IMAGE).setValue(downloadUrl.toString());
            hideLoading();
            startActivity(new Intent(InfoActivity.this, ChatInfoActivity.class));
        } else {
            finishAffinity();
            showErrorMessage("Something went wrong! Please try signing in again.",
                    null,
                    new OnPositiveButtonClick() {
                        @Override
                        public void onPositiveButtonClick() {
                            startActivity(new Intent(InfoActivity.this, SignupActivity.class));
                        }
                    },
                    "OK",
                    "");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length == 2) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .setAspectRatio(1, 1)
                            .start(this);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                userImageUri = result.getUri();
                ivPhoto.setImageURI(userImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
