package com.example.Maledetta_TreEst.board;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Maledetta_TreEst.CommunicationController;
import com.example.Maledetta_TreEst.Model;
import com.example.Maledetta_TreEst.OnRecyclerViewClickListener;
import com.example.Maledetta_TreEst.R;
import com.example.Maledetta_TreEst.Utils;
import com.example.Maledetta_TreEst.post.OfficialPost;
import com.example.Maledetta_TreEst.post.Post;

public class BoardViewHolder extends RecyclerView.ViewHolder {
    private final TextView nPost;
    private final TextView author;
    private final TextView datetime;
    private final ImageView authorProfile;
    protected final ToggleButton follow;


    public BoardViewHolder( @NonNull View itemView) {
        super(itemView);

        nPost = itemView.findViewById(R.id.post);
        author = itemView.findViewById(R.id.author);
        datetime = itemView.findViewById(R.id.datetime);
        authorProfile = itemView.findViewById(R.id.user);
        this.follow = itemView.findViewById(R.id.follow);

    }

    @SuppressLint("SetTextI18n")
    public void updateContent(Post post, @Nullable Bitmap profileImage) {
        String newPost;
        newPost = delay(post.getDelay());
        if (!post.getDelay().equals("") && (!post.getStatus().equals("") || !post.getComment().equals(""))) {
            newPost += "\n";
        }
        newPost += status(post.getStatus());
        if (!post.getStatus().equals("") && !post.getComment().equals("")) {
            newPost += "\n";
        }
        if (!post.getComment().equals("")) {
            newPost += "Commento: " + post.getComment();
        }
        nPost.setText(newPost);
        author.setText(post.getAuthor().getName());
        datetime.setText(post.getDatetime());

        if (profileImage != null) {
            authorProfile.setImageBitmap(profileImage);
        } else {
            authorProfile.setImageResource(R.drawable.profilo);
        }

        if (post.getFollowingAuthor()) {
            nPost.setBackgroundResource(R.drawable.border_evidence);
            follow.setChecked(true);
        } else {
            nPost.setBackgroundResource(R.drawable.border);
            follow.setChecked(false);
        }
    }

    public String delay(String delay) {
        String nDelay = "Ritardo: ";
        switch (delay) {
            case "0":
                return nDelay + "In orario";
            case "1":
                return nDelay + "Ritardo di pochi minuti";
            case "2":
                return nDelay + "Ritardo oltre i 15 minuti";
            case "3":
                return nDelay + "Treni soppressi";
            default:
                return "";
        }
    }

    public String status(String status) {
        String nStatus = "Stato del viaggio: ";
        switch (status) {
            case "0":
                return nStatus + "Situazione ideale";
            case "1":
                return nStatus + "Accettabile";
            case "2":
                return nStatus + "Gravi problemi per i passeggeri";
            default:
                return "";
        }
    }


}
