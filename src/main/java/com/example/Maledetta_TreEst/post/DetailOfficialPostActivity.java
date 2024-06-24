package com.example.Maledetta_TreEst.post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.Maledetta_TreEst.Model;
import com.example.Maledetta_TreEst.R;
import com.example.Maledetta_TreEst.board.BoardActivity;

public class DetailOfficialPostActivity extends AppCompatActivity {
    private TextView title;
    private TextView timestamp;
    private TextView description;
    private Button back;
    private String tratta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_official_post);

        title = findViewById(R.id.viewTitle);
        timestamp = findViewById(R.id.viewTimeStamp);
        description = findViewById(R.id.viewDescription);
        back = findViewById(R.id.backToBacheca);

        if (getIntent().hasExtra("Title") && getIntent().hasExtra("Description") && getIntent().hasExtra("Timestamp")
        && getIntent().hasExtra("TrattaSelezionata")) {
            Log.d("PostSelezionato", getIntent().getStringExtra("Title"));
            title.setText(getIntent().getStringExtra("Title"));
            Log.d("PostSelezionato", getIntent().getStringExtra("Timestamp"));
            timestamp.setText(getIntent().getStringExtra("Timestamp"));
            Log.d("PostSelezionato", getIntent().getStringExtra("Description"));
            description.setText(getIntent().getStringExtra("Description"));
            tratta = getIntent().getStringExtra("TrattaSelezionata");
        } else {
            throw new IllegalArgumentException("Activity cannot find extras");
        }

        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, BoardActivity.class);
            Log.d("Intent", tratta);
            intent.putExtra("LineaSelezionata", Model.getInstance().getLineDirection(tratta));
            intent.putExtra("TrattaSelezionata", tratta);
            startActivity(intent);
        });

    }
}