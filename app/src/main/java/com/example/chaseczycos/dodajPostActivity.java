package com.example.chaseczycos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class dodajPostActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private Button buttonAddPost;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FusedLocationProviderClient fusedLocationClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_post);

        // Initialize Firestore and Firebase Auth
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize location provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Bind UI elements
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonAddPost = findViewById(R.id.buttonAddPost);

        // Set onClickListener for the Add Post button
        buttonAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocationPermissionAndAddPost();
            }
        });
    }

    private void checkLocationPermissionAndAddPost() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocationAndAddPost();
        }
    }

    @SuppressWarnings("MissingPermission") // We check permission before calling this method
    private void getCurrentLocationAndAddPost() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Convert Location to GeoPoint
                    GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    addPost(geoPoint);
                } else {
                    Toast.makeText(dodajPostActivity.this, "Unable to get location, adding post without location.", Toast.LENGTH_SHORT).show();
                    addPost(null); // Proceed without location
                }
            }
        });
    }

    private void addPost(GeoPoint location) {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(dodajPostActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You need to be logged in to add a post.", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = currentUser.getDisplayName();
        Post post = new Post(title, username, description, Timestamp.now(), location);

        firestore.collection("user_posts")
                .add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(dodajPostActivity.this, "Post added successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(dodajPostActivity.this, ZdjeciaActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                    editTextTitle.setText("");
                    editTextDescription.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(dodajPostActivity.this, "Error adding post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationAndAddPost();
            } else {
                Toast.makeText(this, "Location permission denied. Post will be added without location.", Toast.LENGTH_SHORT).show();
                addPost(null);
            }
        }
    }
}
