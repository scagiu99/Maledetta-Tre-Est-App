package com.example.Maledetta_TreEst;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.room.Room;

import com.example.Maledetta_TreEst.line.Line;
import com.example.Maledetta_TreEst.pictures.UserDB;
import com.example.Maledetta_TreEst.post.Author;
import com.example.Maledetta_TreEst.post.Post;
import com.example.Maledetta_TreEst.station.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {
    private static Model instance = null;

    private final List<Line> lines;
    private final List<Post> posts;
    private final List<Station> stations;
    private Station posizione;
    private final List<Author> users;
    private Author profile;
    private String sid;
    private Line lineaSelezionata;
    private boolean selectedBacheca;

    private Model() {
        lines = new ArrayList<>();
        posts = new ArrayList<>();
        stations = new ArrayList<>();
        users = new ArrayList<>();
    }

    public static synchronized Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public void addSid(String sid) {
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

/* --------------------------------------------- LINE ------------------------------------------ */

    public Line getPositionLine(int position) {
        return lines.get(position);
    }

    public Integer getSizeLines() {
        return lines.size();
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public void lineResponse(JSONObject networkResponse) {
        try {
            JSONArray arrLines = networkResponse.getJSONArray("lines");
            Log.d("JSON", arrLines.toString());

            for (int i = 0; i < arrLines.length(); i++) {
                JSONObject terminus1 = arrLines.getJSONObject(i).getJSONObject("terminus1");
                JSONObject terminus2 = arrLines.getJSONObject(i).getJSONObject("terminus2");
                Log.d("JSON", terminus1.toString());
                Line line1 = new Line(terminus1.getString("sname"), terminus2.getString("sname"), terminus1.getString("did"), terminus1.getString("sname"));
                lines.add(line1);
                Line line2 = new Line(terminus1.getString("sname"), terminus2.getString("sname"), terminus2.getString("did"), terminus2.getString("sname"));
                lines.add(line2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error", "JSON sid Error");
        }
    }

    public String getLineDirection(String tratta) {
        for (Line line: lines) {
            if (line.getDidArrival().equals(tratta)) {
                return  line.getArrival() + " - " + line.getDeparture() + "\n"+ "direzione " + line.getDirection();
            }
        }
        return "";
    }

    public Line getLine(String tratta) {
        for (Line line: lines) {
            if (line.getDidArrival().equals(tratta)) {
                return  line;
            }
        }
        return null;
    }

    public String getLineOppositeDirection(String tratta) {
        Line linea = null;
        String did = "";
        for (Line line: lines) {
            if (line.getDidArrival().equals(tratta)) {
                linea = line;
            }
        }
        for (Line line: lines) {
            assert linea != null;
            if (!(line.getDidArrival().equals(linea.getDidArrival())) && (line.getDeparture().equals(linea.getArrival()) || line.getArrival().equals(linea.getArrival()))) {
                Log.d("OppositeDirection", line.getDidArrival());
                did = line.getDidArrival();
            }
        }
        return did;
    }

    public void clearLines() {
        lines.clear();
    }


/* ------------------------------------------- BACHECA ---------------------------------------- */

    public void addLineaSelezionata(Line lineaSelezionata) {
        this.lineaSelezionata = lineaSelezionata;
    }

    public void addSelectedBacheca(boolean selectedBacheca) {
        this.selectedBacheca = selectedBacheca;
    }

    public Line getLineaSelezionata() {
        return lineaSelezionata;
    }

    public boolean getSelectedBacheca() { return selectedBacheca; }


/* ------------------------------------------- STATIONS ---------------------------------------- */

    public Station getPositionStation(int position) {
        return stations.get(position);
    }

    public Integer getSizeStations() {
        return stations.size();
    }

    public void stationResponse(JSONObject networkResponse) {
        try {
            JSONArray arrStations = networkResponse.getJSONArray("stations");
            Log.d("JSON", arrStations.toString());

            for (int i = 0; i < arrStations.length(); i++) {
                Log.d("JSON", String.valueOf(arrStations));
                Station station = new Station(arrStations.getJSONObject(i).getString("sname"), arrStations.getJSONObject(i).getString("lat"), arrStations.getJSONObject(i).getString("lon") );
                stations.add(station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error", "JSON sid Error");
        }
    }

    public void addPosition(String longitude, String latitudine) {
        posizione = new Station("Posizione Attuale", latitudine, longitude );
        Log.d("Position", posizione.getLatitudine() + " " + posizione.getLongitudine());
    }

    public Station getPosition() {
        return posizione;
    }

    public void clearStations() {
        stations.clear();
    }


/* --------------------------------------------- POST ------------------------------------------ */

    public Post getPositionPost(int position) {
        return posts.get(position);
    }

    public Integer getSizePosts() {
        return posts.size();
    }

    public void postResponse(JSONObject networkResponse) {
        try {
            JSONArray arrPosts = networkResponse.getJSONArray("posts");
            Log.d("JSON", arrPosts.toString());

            for (int i = 0; i < arrPosts.length(); i++) {
                Log.d("JSON", String.valueOf(arrPosts.length()));
                String[] checkPost = checkPost(arrPosts.getJSONObject(i));
                Post post = new Post(checkPost[0], checkPost[1], checkPost[2],
                        new Author(arrPosts.getJSONObject(i).getString("authorName"),
                            arrPosts.getJSONObject(i).getString("author"), arrPosts.getJSONObject(i).getString("pversion"), null),
                        arrPosts.getJSONObject(i).getBoolean("followingAuthor"), arrPosts.getJSONObject(i).getString("datetime"));

                users.add(new Author(arrPosts.getJSONObject(i).getString("authorName"),
                        arrPosts.getJSONObject(i).getString("author"), arrPosts.getJSONObject(i).getString("pversion"), ""));
                posts.add(post);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error", "JSON sid Error");
        }
    }

    public String[] checkPost(JSONObject post) {
        return new String[] {post.optString("delay"), post.optString("comment"), post.optString("status")};
    }

    public void setFollowResponse(Post post, boolean follow) {
        for (Post p: posts) {
            if (p.getAuthor().getUid().equals(post.getAuthor().getUid())) {
                p.setFollowingAuthor(follow);
            }
        }
    }

    public void clearPosts() {
        posts.clear();
        users.clear();
    }

    /* --------------------------------------------- PROFILE ------------------------------------------ */

    public void profileResponse(JSONObject networkResponse) {
        try {
            profile = new Author(networkResponse.getString("name"),  networkResponse.getString("uid"), networkResponse.getString("pversion"), networkResponse.getString("picture"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error", "JSON sid Error");
        }
    }

    public Author getUserProfile() {
        return profile;
    }

/* --------------------------------------------- PICTURES ------------------------------------------ */

    public void pictureResponse(JSONObject networkResponse) {
        try {
            for (Author author: users) {
                if (networkResponse.getString("uid").equals(author.getUid())) {
                    author.setPicture(networkResponse.getString("picture"));
                    Log.d("ModelPicture", "Author: " + author.getPicture());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error", "JSON sid Error");
        }
    }

    public String getAuthorsPicture(int position) {
        return users.get(position).getPicture();
    }

/* --------------------------------------------- DATABASE ------------------------------------------ */

    private static UserDB userDB;

    public void inizializedDB(Context context) {
        userDB = Room.databaseBuilder(context, UserDB.class, "userDB").allowMainThreadQueries().build();
    }

    public void addUser(Author user) {
        userDB.userDao().insert(user);
    }

    public boolean pictureVersionCheck(String uid, String pversion) {
        if (userDB.userDao().checkPictureVersion(uid, pversion) > 0) {
            Log.d("DB", String.valueOf(userDB.userDao().checkPictureVersion(uid, pversion)));
            return true;
        }
        return false;
    }

    public void getAuthorsPictureFromDB(String uid) {
        Author user = userDB.userDao().getPictureFromDB(uid);
        instance.setAuthor(user.getName(), user.getUid(), user.getPicture(), user.getPversion());

    }

    private void setAuthor(String name, String uid, String picture, String pversion) {
        for (Author author: users) {
            if (author.getUid().equals(uid)) {
                author.setName(name);
                author.setPicture(picture);
                author.setPversion(pversion);
            }
        }

    }


}

