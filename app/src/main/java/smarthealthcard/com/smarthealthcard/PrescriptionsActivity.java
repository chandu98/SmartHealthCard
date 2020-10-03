package smarthealthcard.com.smarthealthcard;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import smarthealthcard.com.smarthealthcard.Adapter.MedicalReportAdapter;
import smarthealthcard.com.smarthealthcard.Adapter.PrescriptionsAdapter;
import smarthealthcard.com.smarthealthcard.Loader.ShowLoader;
import smarthealthcard.com.smarthealthcard.LocalStorage.SaveLocalData;
import smarthealthcard.com.smarthealthcard.Model.UploadModel;

public class PrescriptionsActivity extends AppCompatActivity {
    String userId,getTab;
    FirebaseFirestore firebaseFirestore;
    ShowLoader showLoader;
    SaveLocalData saveLocalData;
    ListView listView;
    ArrayList<UploadModel> prescriptionsUploadModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptions);
        userId = getIntent().getStringExtra("USER_ID");
        showLoader = new ShowLoader(PrescriptionsActivity.this);
        saveLocalData = new SaveLocalData(PrescriptionsActivity.this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.prescriptionsList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPrescriptionsList();
    }

    private void getPrescriptionsList(){
        prescriptionsUploadModelArrayList.clear();
        showLoader.showProgressDialog("Please wait...");
        firebaseFirestore.collection("Prescriptions").whereEqualTo("userId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                showLoader.dismissDialog();
                List<DocumentChange> documents = queryDocumentSnapshots.getDocumentChanges();
                for (int i = 0; i < documents.size(); i++) {
                    UploadModel uploadModel = new UploadModel();
                    uploadModel = documents.get(i).getDocument().toObject(UploadModel.class);
                    prescriptionsUploadModelArrayList.add(uploadModel);
                }

                PrescriptionsAdapter prescriptionsAdapter = new PrescriptionsAdapter(PrescriptionsActivity.this, prescriptionsUploadModelArrayList);
                listView.setAdapter(prescriptionsAdapter);
                if (prescriptionsUploadModelArrayList.size() == 0) {
                    showLoader.PresentToast("No Prescriptions found");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showLoader.PresentToast(e.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getTab= saveLocalData.getValue("SELECT_TAB");
        if (getTab.equals("HOME")){
            menu.add(0, 100, 0, "Add").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(PrescriptionsActivity.this, UserUploadsActivity.class);
        intent.putExtra("TYPE","Prescriptions");
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
