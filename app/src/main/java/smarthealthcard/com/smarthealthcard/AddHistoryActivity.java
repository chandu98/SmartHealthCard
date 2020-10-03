package smarthealthcard.com.smarthealthcard;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import smarthealthcard.com.smarthealthcard.Loader.ShowLoader;
import smarthealthcard.com.smarthealthcard.Model.HistoryModel;

public class AddHistoryActivity extends AppCompatActivity {

    EditText ageEdit, weightEdit, diseaseEdit, allergyEdit;
    Spinner bloodSpinner, diseaseSpinner, allergySpinner;
    String[] bloodArray = {"A+", "A-", "AB+", "AB-", "B+", "B-", "O+", "O-"};
    String[] diseaeArray = {"Select Disease", "No Disease", "Cancer", "Chrmic Lung Disease", "Heart Stroke", "Asthama", "Diabetes", "Kidney Disease", "Other"};
    String[] allergyArray = {"Select Allergy", "No Disease", "Skin", "Drug", "Food", "Mold", "Pet", "Pollen", "Insect", "Other"};

    String ageString, weightString, diseaseString, allergyString, bloodString;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ShowLoader showLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_history);
        Initialization();
    }

    private void Initialization() {
        ageEdit = findViewById(R.id.age);
        weightEdit = findViewById(R.id.weight);
        diseaseEdit = findViewById(R.id.disease);
        allergyEdit = findViewById(R.id.allergy);
        bloodSpinner = findViewById(R.id.bloodGroupSpinner);
        diseaseSpinner = findViewById(R.id.diseaseSpinner);
        allergySpinner = findViewById(R.id.allergiesSpinner);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        showLoader = new ShowLoader(AddHistoryActivity.this);

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(AddHistoryActivity.this, android.R.layout.simple_list_item_1, bloodArray);
        bloodSpinner.setAdapter(bloodAdapter);

        ArrayAdapter<String> diseaseAdapter = new ArrayAdapter<>(AddHistoryActivity.this, android.R.layout.simple_list_item_1, diseaeArray);
        diseaseSpinner.setAdapter(diseaseAdapter);

        ArrayAdapter<String> allergyAdapter = new ArrayAdapter<>(AddHistoryActivity.this, android.R.layout.simple_list_item_1, allergyArray);
        allergySpinner.setAdapter(allergyAdapter);

        bloodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        diseaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                diseaseString = parent.getItemAtPosition(position).toString();
                if (parent.getItemAtPosition(position).toString().equals("Other")) {
                    diseaseEdit.setVisibility(View.VISIBLE);
                } else {
                    diseaseEdit.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        allergySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                allergyString = parent.getItemAtPosition(position).toString();
                if (parent.getItemAtPosition(position).toString().equals("Other")) {
                    allergyEdit.setVisibility(View.VISIBLE);
                } else {
                    allergyEdit.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addHistoryImplementation(View view) {
        ageString = ageEdit.getText().toString();
        weightString = weightEdit.getText().toString();
        if (ageString.isEmpty() || weightString.isEmpty() || bloodString.isEmpty() || diseaseString.isEmpty() || allergyString.isEmpty()) {

        } else {
            showLoader.showProgressDialog("Please wait...");
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) + 1;
            String currentDate = calendar.get(Calendar.DAY_OF_MONTH) + "-" + month + "-" + calendar.get(Calendar.YEAR);
            HistoryModel historyModel=new HistoryModel(firebaseAuth.getUid(),ageString,weightString,bloodString,diseaseString,allergyString,currentDate);
            firebaseFirestore.collection("MedicalHistory").add(historyModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    showLoader.dismissDialog();
                    showLoader.PresentToast("Medical History Added successfully");
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
}
