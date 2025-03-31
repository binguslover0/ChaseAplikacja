package com.example.chaseczycos;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.example.chaseczycos.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Fetch posts and add markers
        loadPostMarkers();
    }

    private void loadPostMarkers() {
        firestore.collection("user_posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.exists()) {
                                // Get GeoPoint
                                GeoPoint geoPoint = document.getGeoPoint("location");
                                String title = document.getString("title");

                                if (geoPoint != null) {
                                    LatLng location = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(location).title(title));
                                }
                            }
                        }
                        // Move the camera to the first post (if available)
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot firstPost = queryDocumentSnapshots.getDocuments().get(0);
                            GeoPoint firstGeoPoint = firstPost.getGeoPoint("geoPoint");
                            if (firstGeoPoint != null) {
                                LatLng firstLocation = new LatLng(firstGeoPoint.getLatitude(), firstGeoPoint.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10));
                            }
                        }
                    } else {
                        Toast.makeText(MapsActivity.this, "No posts available", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("MapsActivity", "Error loading posts: ", e);
                    Toast.makeText(MapsActivity.this, "Error loading posts", Toast.LENGTH_SHORT).show();
                });
    }
}
