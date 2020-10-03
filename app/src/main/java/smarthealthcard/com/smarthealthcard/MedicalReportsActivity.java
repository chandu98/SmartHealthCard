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
import smarthealthcard.com.smarthealthcard.Loader.ShowLoader;
import smarthealthcard.com.smarthealthcard.LocalStorage.SaveLocalData;
import smarthealthcard.com.smarthealthcard.Model.UploadModel;

public class MedicalReportsActivity extends AppCompatActivity {

    String userId,getTab;
    FirebaseFirestore firebaseFirestore;
    ShowLoader showLoader;
    SaveLocalData saveLocalData;
    ListView listView;
    ArrayList<UploadModel> reUploadModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_reports);
        userId = getIntent().getStringExtra("USER_ID");
        firebaseFirestore = FirebaseFirestore.getInstance();
        showLoader = new ShowLoader(MedicalReportsActivity.this);
        saveLocalData = new SaveLocalData(MedicalReportsActivity.this);
        listView = findViewById(R.id.reportList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMedicalReportsList();
    }

    private void getMedicalReportsList() {
        reUploadModelArrayList.clear();
        showLoader.showProgressDialog("Please wait...");
        firebaseFirestore.collection("MedicalReports").whereEqualTo("userId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                showLoader.dismissDialog();
                List<DocumentChange> documents = queryDocumentSnapshots.getDocumentChanges();
                for (int i = 0; i < documents.size(); i++) {
                    UploadModel uploadModel = new UploadModel();
                    uploadModel = documents.get(i).getDocument().toObject(UploadModel.class);
                    reUploadModelArrayList.add(uploadModel);
                }

                MedicalReportAdapter reportAdapter = new MedicalReportAdapter(MedicalReportsActivity.this, reUploadModelArrayList);
                listView.setAdapter(reportAdapter);
                if (reUploadModelArrayList.size() == 0) {
                    showLoader.PresentToast("No Reports found");
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
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MedicalReportsActivity.this, UserUploadsActivity.class);
        intent.putExtra("TYPE", "MedicalReports");
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
