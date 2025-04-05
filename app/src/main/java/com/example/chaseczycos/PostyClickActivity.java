package com.example.chaseczycos;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class PostyClickActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_layout);

        String title = getIntent().getStringExtra("TITLE");
        String username = getIntent().getStringExtra("USERNAME");
        String description = getIntent().getStringExtra("DESCRIPTION");

        TextView usernameView = findViewById(R.id.postuser);
        TextView titleView = findViewById(R.id.posttitle);
        TextView descriptionView = findViewById(R.id.postdesc);
        //ImageView postImageView = findViewById(R.id.postimage);

        usernameView.setText(username);
        titleView.setText(title);
        descriptionView.setText(description);
    }
}
