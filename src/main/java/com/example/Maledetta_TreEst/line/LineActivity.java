package com.example.Maledetta_TreEst.line;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.text.IDNA;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Maledetta_TreEst.CommunicationController;
import com.example.Maledetta_TreEst.Model;
import com.example.Maledetta_TreEst.OnRecyclerViewClickListener;
import com.example.Maledetta_TreEst.R;
import com.example.Maledetta_TreEst.Utils;
import com.example.Maledetta_TreEst.board.BoardActivity;
import com.example.Maledetta_TreEst.user.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONException;

import java.lang.reflect.Type;

public class LineActivity extends AppCompatActivity implements OnRecyclerViewClickListener {
    private LineAdapter lineAdapter;
    private CommunicationController communicationController;
    private TextView title;
    private SharedPreferences sharedPreferences;
    public static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        Log.d("LineActivity", "onCreate");

        //controllo della connessione in fase di avvio dell'app

        communicationController = new CommunicationController(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation2);
        bottomNavigationView.setSelectedItemId(R.id.linee);
        onItemSelectedLine(bottomNavigationView);

        title = findViewById(R.id.titoloLinee);

        RecyclerView recyclerView = findViewById(R.id.onRecyclerViewLine);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lineAdapter = new LineAdapter(this, this);
        recyclerView.setAdapter(lineAdapter);

        checkLocationPermission();
        saveSharedPreferences();
    }

    /*------------------------------------------ LOCATION ---------------------------------------------*/

    //Metodo per verificare i permessi sulla posizione
    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //Ho i permessi
            Log.d("Location", "The permission is granted");
            return true;
        } else {
            //Non ho i permessi, devo richiederli
            Log.d("Location", "Ask for permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            return false;
        }
    }

    //Gestiamo la risposta dell'utente
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                Log.d("Location", "Now the permission is granted");
            } else {
                // Permission was not granted
                Log.d("Location", "Permission still not granted");
            }
        }
    }


    /*------------------------------------------ USER ---------------------------------------------*/

    public void saveSharedPreferences() {

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("firstAccess", true)) {
            Log.d("LineActivity", "sharedPreferences");

            registerUser(sharedPreferences);

        } else {
            String sid = sharedPreferences.getString("userSid", null);
            Log.d("LineActivity", "UserSid: " + sid);
            Model.getInstance().addSid(sid);

            if ( sharedPreferences.getBoolean("selectedBacheca", true) && !sharedPreferences.contains("bacheca")) {
                getDataLines();
            } else if (Model.getInstance().getSelectedBacheca() && sharedPreferences.contains("bacheca")) {
                getDataLines();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                Model.getInstance().addSelectedBacheca(false);
                editor.apply();
            } else if (Model.getInstance().getLineaSelezionata() == null){
                //Deserializzo il json all'interno delle sharedPreferences
                final Gson gson = new Gson();
                String bacheca =  sharedPreferences.getString("bacheca", null);
                Line line = gson.fromJson(bacheca, Line.class);
                Log.d("SharedpPref", bacheca);
                Model.getInstance().addLineaSelezionata(line);
                getDataLines();

                Intent intent = new Intent(this, BoardActivity.class);
                Log.d("Intent",  line.getDidArrival());
                intent.putExtra("LineaSelezionata",  line.getArrival() + " - " + line.getDeparture() + "\n"+ "direzione " + line.getDirection());
                intent.putExtra("TrattaSelezionata", line.getDidArrival());
                startActivity(intent);
            } else {
                //Serializzo gli oggetti JSON all'interno delle sharedPreferences
                final Gson gson = new Gson();
                String jsonBacheca = gson.toJson(Model.getInstance().getLineaSelezionata());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("bacheca", jsonBacheca);
                Log.d("SharedpPref", jsonBacheca);
                editor.apply();

                Intent intent = new Intent(this, BoardActivity.class);
                Log.d("Intent",  Model.getInstance().getLineaSelezionata().getDidArrival());
                intent.putExtra("LineaSelezionata",  Model.getInstance().getLineaSelezionata().getArrival() + " - " + Model.getInstance().getLineaSelezionata().getDeparture() + "\n"+ "direzione " + Model.getInstance().getLineaSelezionata().getDirection());
                intent.putExtra("TrattaSelezionata", Model.getInstance().getLineaSelezionata().getDidArrival());
                startActivity(intent);
            }
        }
    }

    public void registerUser(SharedPreferences sharedPreferences) {
        Log.d("getRegister", "Sid");

        communicationController.getRegister(
                response -> {
                    Log.d("Response", response.toString());
                    try {
                        String sid = response.getString("sid");
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("userSid", sid);

                        Model.getInstance().addSid(sid);
                        editor.putBoolean("firstAccess", false);
                        editor.apply();

                        getDataLines();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Error", "JSON sid Error");
                    }

                },
                error -> {
                    Log.e("Error", error.toString());
                    Toast.makeText(LineActivity.this, "Errore di caricamento", Toast.LENGTH_SHORT).show();
                }
        );
    }


    /*------------------------------------------ LINES ---------------------------------------------*/

    @SuppressLint("SetTextI18n")
    public void getDataLines() {
        Log.d("getDataLines", "Lines");

            Model.getInstance().clearLines();
             communicationController.getLines(
                     Model.getInstance().getSid(),
                     response -> {
                         Model.getInstance().lineResponse(response);
                         title.setText("Linee Ferroviarie");
                         Log.d("Response", "Model size tratte: " + Model.getInstance().getSizeLines());
                         lineAdapter.notifyDataSetChanged();
                     }, error -> {
                         Log.e("Error", error.getLocalizedMessage());
                         title.setText("Linee Ferroviarie");
                         Toast.makeText(LineActivity.this, "Errore di caricamento", Toast.LENGTH_SHORT).show();
                     }
             );

    }

    @Override
    public void onRecyclerViewClick(View v, int position) {
        Log.d("ListaLinee", "Click sulla linea " + position);
        Line line = Model.getInstance().getPositionLine(position);
        //Serializzazione di un oggeto JSON attraverso GSON
        final Gson gson = new Gson();
        String jsonBacheca = gson.toJson(line);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("bacheca", jsonBacheca);
        Log.d("SharedpPref", jsonBacheca);
        Model.getInstance().addLineaSelezionata(line);
        editor.putBoolean("selectedBacheca", false);
        Model.getInstance().addSelectedBacheca(false);
        editor.apply();

        Intent intent = new Intent(this, BoardActivity.class);
        Log.d("Intent", line.getDidArrival());
        intent.putExtra("LineaSelezionata", Model.getInstance().getLineDirection(line.getDidArrival()));
        intent.putExtra("TrattaSelezionata", line.getDidArrival());
        Model.getInstance().addLineaSelezionata(line);
        startActivity(intent);
    }


    /*---------------------------------------- TAB BAR -------------------------------------------*/
    @SuppressLint("NonConstantResourceId")
    public void onItemSelectedLine(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.bacheca:
                    if (sharedPreferences.contains("bacheca")) {
                        final Gson gson = new Gson();
                        String bacheca =  sharedPreferences.getString("bacheca", null);
                        Line line = gson.fromJson(bacheca, Line.class);
                        intent = new Intent(this, BoardActivity.class);
                        Log.d("Intent",  line.getDidArrival());
                        intent.putExtra("LineaSelezionata",  line.getArrival() + " - " + line.getDeparture() + "\n"+ "direzione " + line.getDirection());
                        intent.putExtra("TrattaSelezionata", line.getDidArrival());
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    }
                case R.id.linee:
                    return true;
                case R.id.profilo:
                    intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                default:
                    break;
            }
            return false;
        });
    }

}