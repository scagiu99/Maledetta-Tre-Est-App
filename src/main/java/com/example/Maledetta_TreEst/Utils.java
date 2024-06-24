package com.example.Maledetta_TreEst;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;

public class Utils {

    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 30, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_CLOSE);
    }

    public static Bitmap convertBase64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        if (bitmap == null) {
            bitmap = convertBase64ToBitmap("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");
        }
        return bitmap;
    }

    //funzione che verifica lo stato della connessione internet
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivity != null)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info != null)
            {
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }
    //fine della funzione che verifica lo stato della connessione internet

}
