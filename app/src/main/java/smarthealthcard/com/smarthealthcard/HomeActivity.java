package smarthealthcard.com.smarthealthcard;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import smarthealthcard.com.smarthealthcard.Adapter.RecentAdapter;
import smarthealthcard.com.smarthealthcard.Loader.ShowLoader;
import smarthealthcard.com.smarthealthcard.LocalStorage.SaveLocalData;
import smarthealthcard.com.smarthealthcard.Model.RecentModel;
import smarthealthcard.com.smarthealthcard.Model.UserModel;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUESCODE = 100;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ShowLoader showLoader;
    SaveLocalData saveLocalData;
    Button homeButton, recentButton, scanButton, profileButton;
    UserModel userModel, scanUserModel;
    RelativeLayout homeLayout, recentLayout, scanLayout, profileLayout;
    EditText mobileEdit, descriptionEdit;
    ImageView profileImage, qrImage, userImage,profileimage2;
    TextView detailText,nameview,emailview,name_on,email_on,mobile_on;
    String nameString, mobileString, emailString, profileString, userId;
    ListView recentListView;
    private int PReqCode = 10;
    Bitmap bitmap;
    String[] uploadItemsList = {"Medical Reports", "Prescriptions", "Bills", "Medical History"};
    ArrayList<RecentModel> recentModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        showLoader = new ShowLoader(HomeActivity.this);
        saveLocalData = new SaveLocalData(HomeActivity.this);
        Initializations();
    }

    public void Initializations() {
        homeButton = findViewById(R.id.home);
        recentButton = findViewById(R.id.recent);
        scanButton = findViewById(R.id.scan_qr);
        profileButton = findViewById(R.id.profile);
        homeButton.setOnClickListener(this);
        recentButton.setOnClickListener(this);
        scanButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);

        //User Info getting
//        profileData = saveLocalData.getValue("USER_INFO");
//        Gson gson = new Gson();
//        userModel = gson.fromJson(profileData, UserModel.class);

        homeLayout = findViewById(R.id.home_layout);
        recentLayout = findViewById(R.id.recent_layout);
        scanLayout = findViewById(R.id.scan_layout);
        profileLayout = findViewById(R.id.profile_layout);

        //Scan
        userImage = findViewById(R.id.userImage);
        detailText = findViewById(R.id.details);
        descriptionEdit = findViewById(R.id.description);
        //profile
        profileImage = findViewById(R.id.profileImage);
        qrImage = findViewById(R.id.qrImage);
        nameview = findViewById(R.id.name);
        mobileEdit = findViewById(R.id.mobile);
        emailview = findViewById(R.id.email);
        profileimage2 = findViewById(R.id.profileImage2);
        name_on = findViewById(R.id.name_on);
        email_on = findViewById(R.id.email_on);
        mobile_on = findViewById(R.id.my_mobile);
        profileImage.setOnClickListener(this);
        profileimage2.setOnClickListener(this);


        //Recent
        recentListView = findViewById(R.id.recentListView);

        homeButton.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        recentButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
        scanButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
        profileButton.setBackgroundColor(getResources().getColor(R.color.colorGray));

        checkAndRequestForPermission();
    }

    public void updateImplementation(View view) {
        nameString = nameview.getText().toString();
        mobileString = mobileEdit.getText().toString();
        emailString = emailview.getText().toString();


        if (nameString.isEmpty() || mobileString.isEmpty() || emailString.isEmpty()) {
            showLoader.PresentToast("Fields must not be empty");
        } else {
            showLoader.showProgressDialog("Please wait...");
            userModel.setPhotoUrl(profileString);
            userModel.setMobile(mobileString);
            userModel.setName(nameString);
            userModel.setEmail(emailString);
            firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).set(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    showLoader.dismissDialog();
                    showLoader.PresentToast("Profile updated successfully");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showLoader.dismissDialog();
                    showLoader.PresentToast(e.getMessage());
                }
            });
        }
    }

    public void viewUploadsImplementation(View view) {
        // startActivity(new Intent(HomeActivity.this, UserUploadsActivity.class));
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Choose an option");
        builder.setItems(uploadItemsList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(HomeActivity.this, MedicalReportsActivity.class);
                        intent.putExtra("USER_ID", userId);
                        startActivity(intent);
                        return;
                    case 1:
                        Intent intent1 = new Intent(HomeActivity.this, PrescriptionsActivity.class);
                        intent1.putExtra("USER_ID", userId);
                        startActivity(intent1);
                        return;
                    case 2:
                        Intent intent2 = new Intent(HomeActivity.this, BillsActivity.class);
                        intent2.putExtra("USER_ID", userId);
                        startActivity(intent2);
                        return;
                    case 3:
                        Intent intent3 = new Intent(HomeActivity.this, MedicalHistoryActivity.class);
                        intent3.putExtra("USER_ID", userId);
                        startActivity(intent3);
                        return;
                }
            }
        });
        builder.show();
    }

    public void sendDetailsImplementation(View view) {
        String descriptionString = descriptionEdit.getText().toString();
        if (descriptionString.isEmpty()) {
            showLoader.PresentToast("Fields must not be empty");
        } else {
            showLoader.showProgressDialog("Please wait...");
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) + 1;
            String currentDate = calendar.get(Calendar.DAY_OF_MONTH) + "-" + month + "-" + calendar.get(Calendar.YEAR);
            RecentModel recentModel = new RecentModel(userId, scanUserModel.getName(), scanUserModel.getMobile(), userModel.getEmail(), descriptionString, currentDate);
            firebaseFirestore.collection("Recent").add(recentModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    showLoader.dismissDialog();
                    showLoader.PresentToast("Details send successfully");
                    descriptionEdit.setText("");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showLoader.dismissDialog();
                    showLoader.PresentToast(e.getMessage());
                }
            });
        }
    }

    //Recent Tab
    public void getRecentList() {
        recentModelArrayList.clear();
        showLoader.showProgressDialog("Please wait...");
        firebaseFirestore.collection("Recent").whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                showLoader.dismissDialog();
                List<DocumentChange> documents = queryDocumentSnapshots.getDocumentChanges();
                for (int i = 0; i < documents.size(); i++) {
                    RecentModel recentModel = new RecentModel();
                    recentModel = documents.get(i).getDocument().toObject(RecentModel.class);
                    recentModelArrayList.add(recentModel);
                }
                Collections.reverse(recentModelArrayList);
                RecentAdapter eventAdapter = new RecentAdapter(HomeActivity.this, recentModelArrayList);
                recentListView.setAdapter(eventAdapter);
                if (recentModelArrayList.size() == 0) {
                    showLoader.PresentToast("No events found");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showLoader.dismissDialog();
                showLoader.PresentToast(e.getMessage());
            }
        });
    }

    //Profile Tab
    public void getUserDetails() {
        showLoader.showProgressDialog("Please wait...");
        firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = new UserModel();
                userModel = documentSnapshot.toObject(UserModel.class);
                profileString = userModel.getPhotoUrl();
                Glide.with(HomeActivity.this)
                        .load(userModel.getPhotoUrl()).apply(RequestOptions.circleCropTransform())
                        .into(profileImage);
                Glide.with(HomeActivity.this)
                        .load(userModel.getPhotoUrl()).apply(RequestOptions.circleCropTransform())
                        .into(profileimage2);
                nameview.setText(userModel.getName());
                mobileEdit.setText(userModel.getMobile());
                emailview.setText(userModel.getEmail());
                name_on.setText(userModel.getName());
                email_on.setText(userModel.getEmail());
                mobile_on.setText(userModel.getMobile());

                QRGEncoder qrgEncoder = new QRGEncoder(firebaseAuth.getCurrentUser().getUid(), null, QRGContents.Type.TEXT, 100);
                try {
                    // Getting QR-Code as Bitmap
                    bitmap = qrgEncoder.encodeAsBitmap();
                    // Setting Bitmap to ImageView
                    qrImage.setImageBitmap(bitmap);
                } catch (WriterException ex) {
                    ex.printStackTrace();
                }
                showLoader.dismissDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showLoader.dismissDialog();
                showLoader.PresentToast(e.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 100, 0, "Logout").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(HomeActivity.this, SignInActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                saveLocalData.saveValue("SELECT_TAB","HOME");
                homeButton.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                recentButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                scanButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                profileButton.setBackgroundColor(getResources().getColor(R.color.colorGray));

                homeLayout.setVisibility(View.VISIBLE);
                recentLayout.setVisibility(View.GONE);
                scanLayout.setVisibility(View.GONE);
                profileLayout.setVisibility(View.GONE);

                return;
            case R.id.recent:
                saveLocalData.saveValue("SELECT_TAB","RECENT");
                homeButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                recentButton.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                scanButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                profileButton.setBackgroundColor(getResources().getColor(R.color.colorGray));

                homeLayout.setVisibility(View.GONE);
                recentLayout.setVisibility(View.VISIBLE);
                scanLayout.setVisibility(View.GONE);
                profileLayout.setVisibility(View.GONE);

                getRecentList();

                return;
            case R.id.scan_qr:
                saveLocalData.saveValue("SELECT_TAB","SCAN");
                openScanner();
                homeButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                recentButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                scanButton.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                profileButton.setBackgroundColor(getResources().getColor(R.color.colorGray));

                return;
            case R.id.profile:
                saveLocalData.saveValue("SELECT_TAB","PROFILE");
                homeButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                recentButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                scanButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                profileButton.setBackgroundColor(getResources().getColor(R.color.colorBlue));

                homeLayout.setVisibility(View.GONE);
                recentLayout.setVisibility(View.GONE);
                scanLayout.setVisibility(View.GONE);
                profileLayout.setVisibility(View.VISIBLE);

                //User Info getting
                getUserDetails();
                return;
            case R.id.profileImage:
                openGallery();
                return;
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(HomeActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
                ;
            } else {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
    }



    private void updateUserInfo(Uri pickedImgUri) {
        showLoader.showProgressDialog("Uploading image....");
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profileString = "" + uri;
                        System.out.println("Picture: " + profileString);
                        if (profileString != null) {
                            Glide.with(HomeActivity.this)
                                    .load(profileString).apply(RequestOptions.circleCropTransform())
                                    .into(profileImage);
                        } else {
                            profileImage.setBackgroundResource(R.drawable.at);
                        }
                        showLoader.dismissDialog();
                        showLoader.PresentToast("Picture uploaded successfully");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showLoader.dismissDialog();
                showLoader.PresentToast(e.getMessage());
            }
        });
    }

    public void openScanner() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            startActivityForResult(intent, 0);

            homeLayout.setVisibility(View.GONE);
            recentLayout.setVisibility(View.GONE);
            scanLayout.setVisibility(View.VISIBLE);
            profileLayout.setVisibility(View.GONE);

        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
            // Download it from Google Play
            downloadScanBarcode();
        }
    }

    private void downloadScanBarcode() {
        Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            scanLayout.setVisibility(View.VISIBLE);
            if (resultCode == RESULT_OK) {
                // showLoader.PresentToast(data.getStringExtra("SCAN_RESULT_FORMAT"));
                userId = data.getStringExtra("SCAN_RESULT");
                DocumentReference docRef = firebaseFirestore.collection("Users").document(userId);
                showLoader.showProgressDialog("Please wait...");
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        scanUserModel = documentSnapshot.toObject(UserModel.class);
                        showLoader.dismissDialog();
                        Glide.with(HomeActivity.this)
                                .load(scanUserModel.getPhotoUrl()).apply(RequestOptions.circleCropTransform())
                                .into(userImage);
                        String userDetails = "Name: " + scanUserModel.getName() + "\n\nMobile: " + scanUserModel.getMobile() + "\n\nEmail: " + scanUserModel.getEmail();
                        detailText.setText(userDetails);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showLoader.PresentToast(e.getMessage());
                        showLoader.dismissDialog();
                    }
                });
            } else if (resultCode == RESULT_CANCELED) {
                showLoader.PresentToast("Scan cancelled.");
                scanLayout.setVisibility(View.GONE);
            }
        } else if (requestCode == 100) {
            if (resultCode == RESULT_OK && data != null) {
                Uri pickedImgUri = data.getData();
                profileImage.setImageURI(pickedImgUri);
                updateUserInfo(pickedImgUri);
            }
        }
    }

    public void medicalReportsImplementation(View view) {
        Intent intent = new Intent(HomeActivity.this, MedicalReportsActivity.class);
        intent.putExtra("USER_ID", firebaseAuth.getCurrentUser().getUid());
        startActivity(intent);
    }

    public void prescriptionsImplementation(View view) {
        Intent intent = new Intent(HomeActivity.this, PrescriptionsActivity.class);
        intent.putExtra("USER_ID", firebaseAuth.getCurrentUser().getUid());
        startActivity(intent);
    }

    public void billsImplementation(View view) {
        Intent intent = new Intent(HomeActivity.this, BillsActivity.class);
        intent.putExtra("USER_ID", firebaseAuth.getCurrentUser().getUid());
        startActivity(intent);
    }

    public void medicalHistoryImplementation(View view) {
        Intent intent = new Intent(HomeActivity.this, MedicalHistoryActivity.class);
        intent.putExtra("USER_ID", firebaseAuth.getCurrentUser().getUid());
        startActivity(intent);
    }
}
