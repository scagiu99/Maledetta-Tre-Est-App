package com.example.Maledetta_TreEst.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Maledetta_TreEst.CommunicationController;
import com.example.Maledetta_TreEst.Model;
import com.example.Maledetta_TreEst.OnRecyclerViewClickListener;
import com.example.Maledetta_TreEst.R;
import com.example.Maledetta_TreEst.Utils;
import com.example.Maledetta_TreEst.line.LineActivity;
import com.example.Maledetta_TreEst.post.AddPostActivity;
import com.example.Maledetta_TreEst.post.Author;
import com.example.Maledetta_TreEst.post.DetailOfficialPostActivity;
import com.example.Maledetta_TreEst.post.OfficialPost;
import com.example.Maledetta_TreEst.post.Post;
import com.example.Maledetta_TreEst.user.ProfileActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.Objects;

public class BoardActivity extends AppCompatActivity implements  OnMapReadyCallback, OnRecyclerViewClickListener {
    private BoardAdapter boardAdapter;
    private OfficialBoardAdapter officialBoardAdapter;
    private TextView line;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewOfficial;
    private CommunicationController communicationController;
    private GoogleMap mMap;
    private Model model;
    private String tratta;
    private Looper looper;
    private MarkerOptions positionMarker;

    //per ultima posizione nota
    private FusedLocationProviderClient fusedLocationClient;
    //per aggiornamenti di posizione
    private LocationCallback locationCallback;
    private boolean requestingLocationUpdates = true;

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

            //Inizializzo il Thread Looper
            HandlerThread handlerThread = new HandlerThread("HandlerThread");
            handlerThread.start();
            looper = handlerThread.getLooper();

            model = Model.getInstance();
            model.inizializedDB(getApplicationContext());

            communicationController = new CommunicationController(this);
            line = findViewById(R.id.line);
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation1);
            bottomNavigationView.setSelectedItemId(R.id.bacheca);
            onItemSelectedHome(bottomNavigationView);

            if (getIntent().hasExtra("TrattaSelezionata") && getIntent().hasExtra("LineaSelezionata")) {
                Log.d("TrattaSelezionata", getIntent().getStringExtra("TrattaSelezionata"));
                tratta = getIntent().getStringExtra("TrattaSelezionata");
                line.setText(getIntent().getStringExtra("LineaSelezionata"));
            } else {
                throw new IllegalArgumentException("Activity cannot find extras");
            }

            recyclerView = findViewById(R.id.onRecyclerViewBoard);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            boardAdapter = new BoardAdapter(this);
            recyclerView.setAdapter(boardAdapter);

        /* ----------------------------------- ESAME GENNAIO --------------------------------------- */

            recyclerViewOfficial = findViewById(R.id.recyclerViewOfficial);
            recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(this));

            officialBoardAdapter = new OfficialBoardAdapter(this, this);
            recyclerViewOfficial.setAdapter(officialBoardAdapter);

            /* -------------------------------------------------------------------------- */

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            //callback per gli aggiornamenti di posizione
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {
                        Log.d("Location", "New Location received: " + location.toString());
                        model.addPosition(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()));
                    }
                    //Aggiungo il puntatore alla mia posizione sulla mappa
                    if (positionMarker == null) {
                        Log.d("MarkerPosition", new LatLng(model.getPosition().getLatitudine(), model.getPosition().getLongitudine()).toString());
                        positionMarker = new MarkerOptions().position(new LatLng(model.getPosition().getLatitudine(),
                                model.getPosition().getLongitudine())).title("Tu Sei Qui");
                        Objects.requireNonNull(mMap.addMarker(positionMarker)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    } else {
                        Log.d("NewMarkerPosition", new LatLng(model.getPosition().getLatitudine(), model.getPosition().getLongitudine()).toString());
                        mMap.clear();
                        updateMap();
                        positionMarker = new MarkerOptions().position(new LatLng(model.getPosition().getLatitudine(),
                                model.getPosition().getLongitudine())).title("Tu Sei Qui");
                        Objects.requireNonNull(mMap.addMarker(positionMarker)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    }
                }

                ;
            };

            getOfficialPost(tratta);
            getDetailsPosts(tratta);

            //Fragment della Mappa
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            assert mapFragment != null;
            mapFragment.getMapAsync(this);

            //Aggiungo un nuovo post in quella bacheca
            FloatingActionButton addPost = findViewById(R.id.floating_action_button);
            addPost.setOnClickListener(view -> {
                Intent intent = new Intent(this, AddPostActivity.class);
                Log.d("Intent", tratta);
                intent.putExtra("Tratta", tratta);
                startActivity(intent);
            });

            //Cambio tratta opposta
            ImageButton direction = findViewById(R.id.changeDirection);
            direction.setOnClickListener(view -> {

                tratta = model.getLineOppositeDirection(tratta);
                Model.getInstance().addLineaSelezionata(model.getLine(tratta));
                Intent intent = new Intent(this, LineActivity.class);
                Log.d("IntentImageButton", tratta);
                startActivity(intent);

            });

    }
    /*------------------------------------------ ESAME GENNAIO ---------------------------------------------*/

    public void getOfficialPost(String tratta) {
        Log.d("getOfficialPost", "OfficialPost");

        model.clearOfficialPosts();
        communicationController.statoLineaTreEst(
                tratta,
                response -> {
                    model.officialPostResponse(response);
                    Log.d("esamegennaio", "did: [" + tratta + "] numero di post ufficiali: ["+ model.getSizeOfficialPosts()+ "]");
                    OfficialPost avviso;
                    officialBoardAdapter.notifyDataSetChanged();
                }, error -> {
                    Log.e("Error", error.getLocalizedMessage());
                    Toast.makeText(BoardActivity.this, "Errore di caricamento", Toast.LENGTH_SHORT).show();
                }
        );
    }

    @Override
    public void onRecyclerViewClick(View v, int position) {
        Log.d("ListaOfficialPost", "Click sul post " + position);
        Intent intent = new Intent(this, DetailOfficialPostActivity.class);
        intent.putExtra("Title", Model.getInstance().getPositionOfficialPost(position).getTitle());
        intent.putExtra("Description", Model.getInstance().getPositionOfficialPost(position).getDescription());
        intent.putExtra("Timestamp", Model.getInstance().getPositionOfficialPost(position).getTimestamp());
        intent.putExtra("TrattaSelezionata", tratta);
        startActivity(intent);
    }

    /*------------------------------------------ POST ---------------------------------------------*/

    public void getDetailsPosts(String tratta) {
        Log.d("getDataPosts", "Posts");

        model.clearPosts();
        communicationController.getPosts(
                model.getSid(),
                tratta,
                response -> {
                   model.postResponse(response);
                    Log.d("Response", "Model size posts: " + model.getSizePosts());

                    //Effettuo la chiamata per ottenere le informazioni degli autori dei post
                    for (int i = 0; i < model.getSizePosts(); i++) {
                        getDetailsPicture(model.getPositionPost(i), i);
                    }
                }, error -> {
                    Log.e("Error", error.getLocalizedMessage());
                    Toast.makeText(BoardActivity.this, "Errore di caricamento", Toast.LENGTH_SHORT).show();
                }
        );

    }

    public void getDetailsPicture(Post post, int position) {
        Log.d("getUserPicture", "Picture");

        //Thread secondario per fare la chiamata per ottenere le informazioni sugli autori
        new Handler(looper).post(() -> {

            //Cerco l'immagine dell'autore del post nel DB, altrimenti effettuo la chiamata di rete
            if (model.pictureVersionCheck(post.getAuthor().getUid(), post.getAuthor().getPversion())) {
                model.getAuthorsPictureFromDB(post.getAuthor().getUid());
                Log.d("DB", "Presente nel DB: " + post.getAuthor().getUid());
                //Main Thread per aggiornare la GUI
                new Handler(this.getMainLooper()).post(() -> {
                    boardAdapter.notifyDataSetChanged();
                });

            } else {
                communicationController.getUserPicture(
                        model.getSid(),
                        post.getAuthor().getUid(),
                        response -> {
                            Log.d("Response", "Image");
                            model.pictureResponse(response);

                            //Notifico l'adapter in modo che inizi già ad aggiornare la vista con le immagini
                            boardAdapter.notifyDataSetChanged();

                            //Aggiungo gli Autori dei post al Database attraverso un thread secondario
                            new Handler(looper).post(() -> {

                                Author user = new Author(post.getAuthor().getName(), post.getAuthor().getUid(),
                                        post.getAuthor().getPversion(), model.getAuthorsPicture(position));
                                Log.d("DB", user.getName() + " " + user.getUid() + " " + model.getAuthorsPicture(position));

                                model.addUser(user);
                            });

                        }, error -> {
                            Log.e("Error", error.getLocalizedMessage());
                            Toast.makeText(BoardActivity.this, "Errore di caricamento", Toast.LENGTH_SHORT).show();
                        }
                );
            }

        });

    }


    /*--------------------------------------- STATIONS -------------------------------------------*/

    public void getDetailsStations(String tratta) {
        Log.d("getDataStations", "Stations");

        //Vado nel Thread Secondario per fare la chiamata di rete per riempire il model dalle stazioni
        new Handler(looper).post(() -> {
            Log.d("Thread", "Secondary");
            model.clearStations();
            communicationController.getStations(
                    model.getSid(),
                    tratta,
                    response -> {
                        model.stationResponse(response);
                        Log.d("Response", "Model size stations: " + model.getSizeStations());

                        //Vado nel Main Thread per aggiornare la mappa
                        new Handler(this.getMainLooper()).post(() -> {
                            Log.d("Thread", "Main");
                            updateMap();
                        });

                    }, error -> {
                        Log.e("Error", error.getLocalizedMessage());
                        Toast.makeText(BoardActivity.this, "Errore di caricamento", Toast.LENGTH_SHORT).show();
                    }
            );
        });

    }

    /*------------------------------------------ LOCATION -------------------------------------------*/

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Location", "onResume");
        //controllo i permessi di posizione
        if(checkLocationPermission()){
            Log.d("Location", "Permessi OK");
            //richiedo l'ultima posizione
            getLastKnownLocation();
            //richiedo aggiornamenti sulla posizione
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        requestingLocationUpdates = false;
    }

    //metodo per verificare i permessi sulla posizione
    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //ho i permessi, scrivo nella casella di testo che va tutto bene
            Log.d("Location", "The permission is granted");
            return true;
        }
        return false;
    }

    //per chiedere l'ultima posizione nota
    public void getLastKnownLocation(){
        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(@NonNull Location location) {
                            Log.d("Location", "Last known location:" + location.toString());
                            model.addPosition(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()));
                        }
                    });
        }
    }

    //per chiedere la posizione aggiornata
    private void startLocationUpdates() {
        if(checkLocationPermission()) {
            //inizio a chiedere gli aggiornamenti di posizione
            requestingLocationUpdates = true;
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(1000); //in ms.
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }
    }

    /*------------------------------------------ MAP -------------------------------------------*/

    //Metodo che aggiorna i marker della mappa in base alla tratta
    public void updateMap(){
        Log.d("Thread", model.getSizeStations().toString());
        LatLng[] fermate = new LatLng[model.getSizeStations()];
        LatLng fermata = null;

        for (int i = model.getSizeStations() - 1; i >= 0; i--) {
            Log.d("Marker", String.valueOf(fermate.length));
            fermata = new LatLng(model.getPositionStation(i).getLatitudine(), model.getPositionStation(i).getLongitudine());
            Log.d("Marker", fermata.toString());
            fermate[i] = fermata;

            mMap.addMarker(new MarkerOptions().position(fermata)
                    .title(model.getPositionStation(i).getSname()));
        }
        mMap.addPolyline(new PolylineOptions().add(fermate).width(5).color(Color.BLUE));

        if (fermata != null) {
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(fermata, 11);
            mMap.animateCamera(yourLocation);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("onMapReady", model.getSizeStations().toString());
        getDetailsStations(tratta);

    }


    /*--------------------------------------- TAB BAR -----------------------------------------*/

    @SuppressLint("NonConstantResourceId")
    public void onItemSelectedHome(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            // Handle item selection
            Intent intent;
            switch (item.getItemId()) {
                case R.id.bacheca:
                    return true;
                case R.id.linee:
                    model.addSelectedBacheca(true);
                    intent = new Intent(this, LineActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
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