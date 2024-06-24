package com.example.Maledetta_TreEst.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.Maledetta_TreEst.CommunicationController;
import com.example.Maledetta_TreEst.Model;
import com.example.Maledetta_TreEst.R;
import com.example.Maledetta_TreEst.Utils;
import com.example.Maledetta_TreEst.line.LineActivity;
import com.example.Maledetta_TreEst.post.Author;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    private static final int RESULT_IMAGE = 0;
    private CommunicationController communicationController;
    private ImageView profileImage;
    private EditText name;
    private Author userProfile;
    private Uri imageUri;

    @SuppressLint({"IntentReset", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        communicationController = new CommunicationController(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profilo);
        onItemSelectedProfile(bottomNavigationView);

        profileImage = findViewById(R.id.imageProfile);
        name = findViewById(R.id.editProfileName);

        getDetailProfile();

        FloatingActionButton changeImage = findViewById(R.id.changeImage);
        changeImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_IMAGE);
                }
        );

        Button save = findViewById(R.id.save);
        save.setOnClickListener(view -> {
            setImageNameProfile();
        });

    }

    /*--------------------------------------- GET PROFILE --------------------------------------------*/

    private void getDetailProfile() {
        Log.d("getDetailProfile", "Profile");

        communicationController.getProfile(
                Model.getInstance().getSid(),
                response -> {
                    Model.getInstance().profileResponse(response);

                    Log.d("getDetailProfile", Model.getInstance().getUserProfile().getName());
                    userProfile = Model.getInstance().getUserProfile();
                    name.setText(userProfile.getName());

                    Bitmap imageBitmap = Utils.convertBase64ToBitmap(userProfile.getPicture());
                    profileImage.setImageBitmap(imageBitmap);
                    Log.d("Response", "Model profile: " + Model.getInstance().getUserProfile().getName());
                }, error -> {
                    Log.e("Error", error.getLocalizedMessage());
                    Toast.makeText(ProfileActivity.this, "Errore di caricamento", Toast.LENGTH_SHORT).show();
                }
        );
    }

    /*--------------------------------------- GALLERY --------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == RESULT_IMAGE){
            imageUri = data.getData();
            Bitmap fixImg = readImageFromFileAndFix(imageUri);
            //Visualizzo l'immagine tagliata
            profileImage.setImageBitmap(fixImg);

        } else {
            Toast.makeText(getApplicationContext(), "Nessuna nuova immagine", Toast.LENGTH_SHORT).show();
        }
    }

    /*--------------------------------------- SET PROFILE --------------------------------------------*/

    public void setImageNameProfile() {
        /* Thread per eseguire le operazioni di accesso al file immagine e modifica delle dimensioni per renderla quadrata */
        new Thread(() -> {
            Bitmap fixImg;
            String encoded;

            if (imageUri != null) {
                fixImg = readImageFromFileAndFix(imageUri);
                encoded = Utils.convertBitmapToBase64(fixImg);
                Log.d("Image", encoded);
                imageUri = null;
            } else {
                encoded = "";
                Log.d("ImageNull", encoded);
            }
                profileImage.post(() -> {
                    //Ritorno al Main thread per poter modificare l'interfaccia grafica
                    if (encoded.length() >= 137000) {
                        Toast.makeText(getApplicationContext(), "Immagine troppo grande", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (String.valueOf(name.getText()).length() > 20) {
                        Toast.makeText(getApplicationContext(), "Il nome è troppo lungo", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    communicationController.setProfile(
                            Model.getInstance().getSid(),
                            String.valueOf(name.getText()),
                            encoded,
                            response -> {
                                Log.d("Response", "Image");
                                Log.d("Response", name.getText().toString());
                                getDetailProfile();
                                Toast.makeText(getApplicationContext(), "Caricamento avvenuto con successo", Toast.LENGTH_SHORT).show();
                            }, error -> {
                                Log.e("Error", error.getLocalizedMessage());
                                Toast.makeText(getApplicationContext(), "Errore di caricamento", Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            });
                });
        }
        ).start();
    }

    /*--------------------------------------- FIX IMAGE --------------------------------------------*/

    private Bitmap readImageFromFileAndFix(Uri imageUri) {
        Bitmap image = null;
        try {
            image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert image != null;
        return cropCenter(image);

    }

    public static Bitmap cropCenter(Bitmap bmp) {
        int dimension = Math.min(bmp.getWidth(), bmp.getHeight());
        return ThumbnailUtils.extractThumbnail(bmp, dimension, dimension);
    }

    /*--------------------------------------- TAB BAR --------------------------------------------*/
    @SuppressLint("NonConstantResourceId")
    public void onItemSelectedProfile(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            // Handle item selection
            Intent intent;
            switch (item.getItemId()) {
                case R.id.bacheca:
                    intent = new Intent(this, LineActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.linee:
                    Model.getInstance().addSelectedBacheca(true);
                    intent = new Intent(this, LineActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.profilo:
                    return true;
                default:
                    break;
            }
            return false;
        });
    }


}