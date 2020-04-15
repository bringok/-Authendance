package com.example.authendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class StudentActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    private CardView scanCard;
    private CardView moduleCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        TextView nameDisplay = findViewById(R.id.nameDisplay);
        scanCard = findViewById(R.id.scanCard);
        moduleCard = findViewById(R.id.moduleCard);

        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        getName(nameDisplay);

        scanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentActivity.this, ModulePicker.class);
                startActivity(intent);
            }
        });

        moduleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentActivity.this, StudentModules.class);
                startActivity(intent);
            }
        });
    }

    private void getName(final TextView nameDisplay) {

        String uid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        DocumentReference studentRef = db.collection("School")
                .document("0DKXnQhueh18DH7TSjsb")
                .collection("User")
                .document(uid);

        studentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                if(task.isSuccessful()) {
                    if(documentSnapshot != null) {
                        String studentName = documentSnapshot.getString("name");
                        Log.d("studentName", "Student name: " + studentName);

                        nameDisplay.setText(studentName);
                    }
                    else {
                        Toast.makeText(StudentActivity.this, "Document not found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(StudentActivity.this, "Task failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
