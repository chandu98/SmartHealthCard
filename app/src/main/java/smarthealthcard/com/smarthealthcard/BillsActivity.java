package smarthealthcard.com.smarthealthcard;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import smarthealthcard.com.smarthealthcard.Adapter.BillsAdapter;
import smarthealthcard.com.smarthealthcard.Adapter.MedicalReportAdapter;
import smarthealthcard.com.smarthealthcard.Loader.ShowLoader;
import smarthealthcard.com.smarthealthcard.LocalStorage.SaveLocalData;
import smarthealthcard.com.smarthealthcard.Model.UploadModel;

public class BillsActivity extends AppCompatActivity {
    String userId,getTab;
    FirebaseFirestore firebaseFirestore;
    ShowLoader showLoader;
    SaveLocalData saveLocalData;
    ListView listView;


    ArrayList<UploadModel> billsUploadModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);
        userId = getIntent().getStringExtra("USER_ID");
        firebaseFirestore = FirebaseFirestore.getInstance();
        showLoader = new ShowLoader(BillsActivity.this);
        saveLocalData = new SaveLocalData(BillsActivity.this);
        listView = findViewById(R.id.billsList);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getBillsList();
    }

    private void getBillsList() {
        billsUploadModelArrayList.clear();
        showLoader.showProgressDialog("Please wait...");
        firebaseFirestore.collection("Bills").whereEqualTo("userId",userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                showLoader.dismissDialog();
                List<DocumentChange> documents = queryDocumentSnapshots.getDocumentChanges();
                for (int i = 0; i < documents.size(); i++) {
                    UploadModel uploadModel = new UploadModel();
                    uploadModel = documents.get(i).getDocument().toObject(UploadModel.class);
                    billsUploadModelArrayList.add(uploadModel);
                }

                BillsAdapter billsAdapter = new BillsAdapter(BillsActivity.this, billsUploadModelArrayList);
                listView.setAdapter(billsAdapter);
                if (billsUploadModelArrayList.size()==0){
                    showLoader.PresentToast("No Bills found");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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
        Intent intent=new Intent(BillsActivity.this, UserUploadsActivity.class);
        intent.putExtra("TYPE","Bills");
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
