package com.example.Maledetta_TreEst.board;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Maledetta_TreEst.CommunicationController;
import com.example.Maledetta_TreEst.Model;
import com.example.Maledetta_TreEst.OnRecyclerViewClickListener;
import com.example.Maledetta_TreEst.R;
import com.example.Maledetta_TreEst.Utils;
import com.example.Maledetta_TreEst.post.OfficialPost;
import com.example.Maledetta_TreEst.post.Post;

public class OfficialBoardAdapter  extends RecyclerView.Adapter<OfficialBoardViewHolder> {
    private final LayoutInflater inflater;
    private OnRecyclerViewClickListener mRecyclerViewClickListener;

    public OfficialBoardAdapter(Context context, OnRecyclerViewClickListener mRecyclerViewClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.mRecyclerViewClickListener = mRecyclerViewClickListener;
    }

    @NonNull
    @Override
    public OfficialBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_officialpost, parent, false);
        return new OfficialBoardViewHolder(view, mRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialBoardViewHolder holder, int position) {
            OfficialPost officialPost = Model.getInstance().getPositionOfficialPost(position);
            holder.updateContent(officialPost, position);
    }

    @Override
    public int getItemCount() {
        return Model.getInstance().getSizeOfficialPosts();
    }

}
