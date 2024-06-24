package com.example.Maledetta_TreEst.board;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Maledetta_TreEst.CommunicationController;
import com.example.Maledetta_TreEst.Model;
import com.example.Maledetta_TreEst.OnRecyclerViewClickListener;
import com.example.Maledetta_TreEst.R;
import com.example.Maledetta_TreEst.Utils;
import com.example.Maledetta_TreEst.post.OfficialPost;
import com.example.Maledetta_TreEst.post.Post;

public class BoardAdapter extends RecyclerView.Adapter<BoardViewHolder> {
    private final LayoutInflater inflater;

    private final CommunicationController communicationController;
    private final Model model;

    public BoardAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        communicationController = new CommunicationController(context);
        model = Model.getInstance();
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_post, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        Post post = Model.getInstance().getPositionPost(position);

        Bitmap profileImage = null;
        String profileImageBase64 = Model.getInstance().getAuthorsPicture(position);

        if (profileImageBase64 != null) {
            profileImage = Utils.convertBase64ToBitmap(profileImageBase64);
        }
        holder.updateContent( post, profileImage);

        holder.follow.setOnClickListener(v -> {
            Log.d("FollowClick", String.valueOf(holder.follow.isChecked()));
            if (holder.follow.isChecked()) {
                Log.d("FollowResponse", String.valueOf(holder.follow.isChecked()));
                userFollow(post);
            } else {
                Log.d("UnFollowResponse", String.valueOf(holder.follow.isChecked()));
                userUnfollow(post);
            }
        });

    }

        private void userFollow(Post post) {
            Log.d("Follow", "follow");

            communicationController.follow(
                    model.getSid(),
                    post.getAuthor().getUid(),
                    response -> {
                        Log.d("Response", "Follow "+ response);
                        model.setFollowResponse(post, true);
                        notifyDataSetChanged();
                    }, error -> {
                        Log.e("Error", error.getLocalizedMessage());
                        //Toast.makeText(, "Errore di caricamento", Toast.LENGTH_SHORT).show();
                    }
            );
        }

        private void userUnfollow(Post post) {
            Log.d("Unfollow", "unFollow");

            communicationController.unfollow(
                    model.getSid(),
                    post.getAuthor().getUid(),
                    response -> {
                        model.setFollowResponse(post, false);
                        Log.d("Response", "Unfollow " + response);
                        notifyDataSetChanged();
                    }, error -> {
                        Log.e("Error", error.getLocalizedMessage());
                        //Toast.makeText(BoardActivity.this, "Errore di caricamento", Toast.LENGTH_SHORT).show();
                    }
            );
        }

    @Override
    public int getItemCount() {
        return Model.getInstance().getSizePosts();
    }

}
