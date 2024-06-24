package com.example.Maledetta_TreEst.post;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.Maledetta_TreEst.CommunicationController;
import com.example.Maledetta_TreEst.Model;
import com.example.Maledetta_TreEst.R;
import com.example.Maledetta_TreEst.board.BoardActivity;

public class AddPostActivity extends AppCompatActivity {
    private CommunicationController communicationController;
    private String tratta;
    private EditText comment;
    private Integer delay;
    private Integer status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        communicationController = new CommunicationController(this);

        if (getIntent().hasExtra("Tratta")) {
            Log.d("Tratta",getIntent().getStringExtra("Tratta"));
            tratta = getIntent().getStringExtra("Tratta");
        } else {
            throw new IllegalArgumentException("Activity cannot find extras");
        }

        Button back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(this, BoardActivity.class);
            Log.d("Intent", tratta);
            intent.putExtra("LineaSelezionata", Model.getInstance().getLineDirection(tratta));
            intent.putExtra("TrattaSelezionata", tratta);
            startActivity(intent);
        });

        comment = findViewById(R.id.comment);
        Button add = findViewById(R.id.confirm_button);
        add.setOnClickListener(view -> {
            addNewPost();
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void onDelayRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.inOrario:
                if (checked)
                    delay = 0;
                    break;
            case R.id.pochiMinuti:
                if (checked)
                    delay = 1;
                    break;
            case R.id.totMinuti:
                if (checked)
                    delay = 2;
                    break;
            case R.id.soppressi:
                if (checked)
                    delay = 3;
                    break;
            default:
                delay = null;
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onStatusRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.ideale:
                if (checked)
                    status = 0;
                    break;
            case R.id.accettabile:
                if (checked)
                    status = 1;
                    break;
            case R.id.graviProblemi:
                if (checked)
                    status = 2;
                    break;
            default:
                status = null;
                break;
        }
    }

    public void addNewPost() {
        Log.d("addPost", "addNewPost");

        if (delay == null && status == null && String.valueOf(comment.getText()).equals("")) {
            Toast.makeText(AddPostActivity.this, "Non puoi pubblicare un post vuoto!", Toast.LENGTH_LONG).show();
        } else if (String.valueOf(comment.getText()).length() > 100) {
            Toast.makeText(AddPostActivity.this, "Il commento non può essere più lungo di 100 caratteri!", Toast.LENGTH_LONG).show();
        } else {
            communicationController.addPost(
                    Model.getInstance().getSid(),
                    tratta,
                    delay,
                    status,
                    String.valueOf(comment.getText()),
                    response -> {
                        Model.getInstance().postResponse(response);
                        Log.d("Response", "Model size posts: " + Model.getInstance().getSizePosts());
                    }, error -> {
                        Log.d("addPost", delay + " " + status + " " + comment.getText().toString());
                        Log.e("Error", error.getLocalizedMessage());
                        Toast.makeText(AddPostActivity.this, "Errore di caricamento", Toast.LENGTH_SHORT).show();
                    }
            );
            Intent intent = new Intent(this, BoardActivity.class);
            Log.d("Intent", tratta);
            intent.putExtra("LineaSelezionata", Model.getInstance().getLineDirection(tratta));
            intent.putExtra("TrattaSelezionata", tratta);
            startActivity(intent);
        }

    }

}