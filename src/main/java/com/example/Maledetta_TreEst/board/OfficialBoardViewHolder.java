package com.example.Maledetta_TreEst.board;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Maledetta_TreEst.OnRecyclerViewClickListener;
import com.example.Maledetta_TreEst.R;
import com.example.Maledetta_TreEst.post.OfficialPost;

public class OfficialBoardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView title;
    private final TextView timestamp;
    private final TextView description;

    private final OnRecyclerViewClickListener mRecyclerViewClickListener;


    public OfficialBoardViewHolder( @NonNull View itemView , OnRecyclerViewClickListener mRecyclerViewClickListener) {
        super(itemView);
        this.mRecyclerViewClickListener = mRecyclerViewClickListener;

        title = itemView.findViewById(R.id.title);
        timestamp = itemView.findViewById(R.id.timestamp);
        description = itemView.findViewById(R.id.description);

        itemView.setOnClickListener(this);

    }

    @SuppressLint("SetTextI18n")
    public void updateContent(OfficialPost officialPost, int position ) {
        title.setText(officialPost.getTitle());
        timestamp.setText(officialPost.getTimestamp());
        description.setText(officialPost.getDescription());

    }


    @Override
    public void onClick(View v) {
        mRecyclerViewClickListener.onRecyclerViewClick(v,getAdapterPosition());
    }
}
