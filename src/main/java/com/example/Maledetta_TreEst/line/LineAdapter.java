package com.example.Maledetta_TreEst.line;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Maledetta_TreEst.Model;
import com.example.Maledetta_TreEst.OnRecyclerViewClickListener;
import com.example.Maledetta_TreEst.R;
import com.example.Maledetta_TreEst.post.Post;

public class LineAdapter extends RecyclerView.Adapter<LineViewHolder>{
    private final LayoutInflater inflater;

    private OnRecyclerViewClickListener mRecyclerViewClickListener;

    public LineAdapter(Context context, OnRecyclerViewClickListener mRecyclerViewClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.mRecyclerViewClickListener = mRecyclerViewClickListener;
    }

    @NonNull
    @Override
    public LineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_line, parent, false);
        return new LineViewHolder(view, mRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LineViewHolder holder, int position) {
        Line line  = Model.getInstance().getPositionLine(position);
        holder.updateContent(line, position);

    }

    @Override
    public int getItemCount() {
        return Model.getInstance().getSizeLines();
    }

}
