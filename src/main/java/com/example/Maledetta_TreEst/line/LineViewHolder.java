package com.example.Maledetta_TreEst.line;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Maledetta_TreEst.OnRecyclerViewClickListener;
import com.example.Maledetta_TreEst.R;
import com.example.Maledetta_TreEst.post.Post;

public class LineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView direction;

    private final OnRecyclerViewClickListener mRecyclerViewClickListener;

    public LineViewHolder(@NonNull View itemView, OnRecyclerViewClickListener mRecyclerViewClickListener) {
        super(itemView);

        this.mRecyclerViewClickListener = mRecyclerViewClickListener;
        direction = itemView.findViewById(R.id.direction);
        direction.setOnClickListener(this);

    }

    @SuppressLint("SetTextI18n")
    public void updateContent(Line line, int position) {
        direction.setText( line.getArrival() + " - " + line.getDeparture() + "\n"+ "direzione " + line.getDirection() );
        if (position % 2 == 0) {
            direction.setBackgroundResource(R.drawable.border_blue);
        } else {
            direction.setBackgroundResource(R.drawable.border_gray);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        mRecyclerViewClickListener.onRecyclerViewClick(view,getAdapterPosition());
    }
}
