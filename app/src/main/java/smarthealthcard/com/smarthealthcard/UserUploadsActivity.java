package smarthealthcard.com.smarthealthcard;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import smarthealthcard.com.smarthealthcard.Loader.ShowLoader;
import smarthealthcard.com.smarthealthcard.Model.UploadModel;

public class UserUploadsActivity extends AppCompatActivity {

    private static final int PICKFILE_REQUEST_CODE = 100;
    String getType;
    EditText titleEdit, descriptionEdit;
    TextView viewData;
    TextView recordtype;
    String titleString, descriptionString, uploadUrl;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ShowLoader showLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uploads);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        showLoader = new ShowLoader(UserUploadsActivity.this);
        titleEdit = findViewById(R.id.title);
        descriptionEdit = findViewById(R.id.doc_description);
        recordtype = findViewById(R.id.record_type);
        viewData = findViewById(R.id.viewData);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getType = getIntent().getStringExtra("TYPE");
        recordtype.setText(getType);
    }

    public void uploadImplementation(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri path = data.getData();
        showLoader.PresentToast(""+path);
        viewData.setText(path.getLastPathSegment());
        uploadDocumentInfo(path);
    }

    public void submitImplementation(View view) {
        titleString = titleEdit.getText().toString();
        descriptionString = descriptionEdit.getText().toString();

        if (titleString.isEmpty() || descriptionString.isEmpty() || uploadUrl.isEmpty()) {
            showLoader.PresentToast("Fields must not be empty");
        } else {
            showLoader.showProgressDialog("Please wait...");
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) + 1;
            String currentDate = calendar.get(Calendar.DAY_OF_MONTH) + "-" + month + "-" + calendar.get(Calendar.YEAR);
            UploadModel uploadModel = new UploadModel(firebaseAuth.getUid(), titleString, descriptionString, uploadUrl, currentDate);
            firebaseFirestore.collection(getType).add(uploadModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    showLoader.dismissDialog();
                    showLoader.PresentToast("Uploaded successfully");
                    titleEdit.setText("");
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

    private void uploadDocumentInfo(Uri pickedUri) {
        showLoader.showProgressDialog("Uploading image....");
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedUri.getLastPathSegment());
        imageFilePath.putFile(pickedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uploadUrl = "" + uri;
                        System.out.println("Picture: " + uploadUrl);
                        showLoader.dismissDialog();
                        showLoader.PresentToast("File uploaded successfully");
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
}
