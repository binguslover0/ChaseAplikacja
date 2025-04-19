package com.example.chaseczycos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class dodajPostActivity extends AppCompatActivity {

    private static final int LOCATION_PICKER_REQUEST = 1001;

    private EditText editTextTitle, editTextDescription;
    private Button buttonAddPost;
    private ImageButton buttonPickLocation;
    private TextView textViewPickedAddress;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private GeoPoint pickedLocation = null;
    private String pickedAddress = "Nie wybrano.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_post);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonAddPost = findViewById(R.id.buttonAddPost);
        buttonPickLocation = findViewById(R.id.lokalizacjaButton);
        textViewPickedAddress = findViewById(R.id.wybranyAdres);

        textViewPickedAddress.setText(pickedAddress);

        buttonAddPost.setOnClickListener(v -> addPost(pickedLocation));

        buttonPickLocation.setOnClickListener(v -> {
            Intent intent = new Intent(dodajPostActivity.this, LokalizacjaActivity.class);
            startActivityForResult(intent, LOCATION_PICKER_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_PICKER_REQUEST && resultCode == RESULT_OK && data != null) {
            double lat = data.getDoubleExtra("lat", 0);
            double lng = data.getDoubleExtra("lng", 0);
            pickedAddress = data.getStringExtra("address");
            pickedLocation = new GeoPoint(lat, lng);

            textViewPickedAddress.setText(pickedAddress);
        }
    }

    private void addPost(GeoPoint location) {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Wypełnij oba pola.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Zaloguj się aby dodawać posty.", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = currentUser.getDisplayName();
        Post post = new Post(title, username, description, Timestamp.now(), location);

        firestore.collection("user_posts")
                .add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Dodano post!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, ZdjeciaActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Błąd przy dodawaniu: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
