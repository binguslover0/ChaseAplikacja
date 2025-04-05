package com.example.chaseczycos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private final PostInterface postInterface;

    Context context;
    ArrayList<Post> postArrayList;

    public PostAdapter(Context context, ArrayList<Post> postArrayList, PostInterface postInterface) {
        this.context = context;
        this.postArrayList = postArrayList;
        this.postInterface = postInterface;
    }

    @NonNull
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);

        return new MyViewHolder(v, postInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post post = postArrayList.get(position);

        // Set the text views with data
        holder.username.setText(post.username);
        holder.title.setText(post.title);
        holder.description.setText(post.description);

        // Use Glide to load the image URL into the ImageView
    }


    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, username, description;

        public MyViewHolder(@NonNull View itemView, PostInterface postInterface) {
            super(itemView);
            title = itemView.findViewById(R.id.titleView);
            username = itemView.findViewById(R.id.usernameView);
            description = itemView.findViewById(R.id.descriptionView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (postInterface != null) {
                        int pos = getAdapterPosition();

                        if  (pos != RecyclerView.NO_POSITION) {
                            postInterface.onItemClick(pos);
                        }

                    }
                }
            });

        }
    }
}
