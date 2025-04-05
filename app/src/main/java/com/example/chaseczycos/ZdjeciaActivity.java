package com.example.chaseczycos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ZdjeciaActivity extends AppCompatActivity implements PostInterface{

    RecyclerView recyclerView;
    ArrayList<Post> postArrayList;
    PostAdapter postAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posty);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        recyclerView = findViewById(R.id.RView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        postArrayList = new ArrayList<>();
        postAdapter = new PostAdapter(ZdjeciaActivity.this, postArrayList, this);

        recyclerView.setAdapter(postAdapter);

        EventChangeListener();

        // Navigate to EventActivity when the button is clicked
        ImageButton button12 = findViewById(R.id.eventyButton);
        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZdjeciaActivity.this, EventActivity.class);
                startActivity(intent);
            }
        });

        ImageButton addPostButton = findViewById(R.id.dodajpost);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZdjeciaActivity.this, dodajPostActivity.class);
                startActivity(intent);
            }
        });

        ImageButton mapaButton = findViewById(R.id.mapaButton);
        mapaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZdjeciaActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void EventChangeListener() {
        db.collection("user_posts").orderBy("timestamp", Query.Direction.DESCENDING) // Sort by timestamp field
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        postArrayList.clear();

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Post post = dc.getDocument().toObject(Post.class);
                                postArrayList.add(post); // Add post to list
                            }
                        }

                        Log.d("ZdjeciaActivity", "Number of posts: " + postArrayList.size());

                        postAdapter.notifyDataSetChanged();

                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ZdjeciaActivity.this, PostyClickActivity.class);

        intent.putExtra("TITLE", postArrayList.get(position).getTitle());
        intent.putExtra("USERNAME", postArrayList.get(position).getUsername());
        intent.putExtra("DESCRIPTION", postArrayList.get(position).getDescription());

        startActivity(intent);
    }
}
