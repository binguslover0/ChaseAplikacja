package com.example.chaseczycos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DodajEventActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private Button buttonAddPost;
    private FirebaseAuth mAuth;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_post);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Bind UI elements
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonAddPost = findViewById(R.id.buttonAddPost);

        // Set onClickListener for the Add Post button
        buttonAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
    }

    private void addPost() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(DodajEventActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You need to be logged in to add a post.", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = currentUser.getDisplayName();

        // Create a map to hold the post data
        Event event = new Event(title, username, description, Timestamp.now());

        // Add the post to Firestore in the 'user_posts' collection
        firestore.collection("user_posts")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(DodajEventActivity.this, "Event added successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(DodajEventActivity.this, ZdjeciaActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Optionally clear the activity stack
                    startActivity(intent);
                    finish();
                    editTextTitle.setText("");
                    editTextDescription.setText("");
                })
                .addOnFailureListener(e -> {
                    // Show error message
                    Toast.makeText(DodajEventActivity.this, "Error adding event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}