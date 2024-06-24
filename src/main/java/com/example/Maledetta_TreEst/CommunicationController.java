package com.example.Maledetta_TreEst;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CommunicationController {

    private static final String BASE_URL = "https://ewserver.di.unimi.it/mobicomp/treest/";

    private RequestQueue queue = null;

    public CommunicationController(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public void getRegister(Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "register.php";
        Log.d("CommunicationController", "getRegister");
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                responseListener,
                errorListener
        );
        queue.add(request);
    }

    public void getProfile(String sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "getProfile.php";
        Log.d("CommunicationController", "getProfile");
        JSONObject requestParam = new JSONObject();
        try {
            requestParam.put("sid", sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestParam,
                responseListener,
                errorListener
        );
        queue.add(request);
    }

    public void setProfile(String sid, String name, String picture, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "setProfile.php";
        Log.d("CommunicationController", "setProfile");
        JSONObject requestParam = new JSONObject();

        try {
            requestParam.put("sid", sid);
            requestParam.put("name", name);
            requestParam.put("picture", picture);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestParam,
                responseListener,
                errorListener
        );
        queue.add(request);
    }

    public void getLines(String sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "getLines.php";
        JSONObject requestParam = new JSONObject();
        Log.d("CommunicationController", "getLines");

        try {
            requestParam.put("sid", sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestParam,
                responseListener,
                errorListener
        );
        queue.add(request);
    }

    public void getStations(String sid, String did, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "getStations.php";
        JSONObject requestParam = new JSONObject();
        Log.d("CommunicationController", "getStations");

        try {
            requestParam.put("sid", sid);
            requestParam.put("did", did);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestParam,
                responseListener,
                errorListener
        );
        queue.add(request);
    }

    public void getPosts(String sid, String did, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "getPosts.php";
        JSONObject requestParam = new JSONObject();
        Log.d("CommunicationController", "getPosts");
        try {
            requestParam.put("sid", sid);
            requestParam.put("did", did);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestParam,
                responseListener,
                errorListener
        );
        queue.add(request);
    }

    public void addPost(String sid, String did, Integer delay, Integer status, String comment, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "addPost.php";
        Log.d("CommunicationController", "addPost");
        JSONObject requestParam = new JSONObject();
        try {
            requestParam.put("sid", sid);
            requestParam.put("did", did);
            requestParam.put("delay", delay);
            requestParam.put("status", status);
            requestParam.put("comment", comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestParam,
                responseListener,
                errorListener
        );
        queue.add(request);
    }

    public void getUserPicture(String sid, String uid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "getUserPicture.php";
        Log.d("CommunicationController", "getUserPicture");
        JSONObject requestParam = new JSONObject();
        try {
            requestParam.put("sid", sid);
            requestParam.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestParam,
                responseListener,
                errorListener
        );
        queue.add(request);
    }

    public void follow(String sid, String uid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "follow.php";
        Log.d("CommunicationController", "follow");

        JSONObject requestParam = new JSONObject();
        try {
            requestParam.put("sid", sid);
            requestParam.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestParam,
                responseListener,
                errorListener
        );
        queue.add(request);
    }

    public void unfollow(String sid, String uid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "unfollow.php";
        Log.d("CommunicationController", "unfollow");

        JSONObject requestParam = new JSONObject();
        try {
            requestParam.put("sid", sid);
            requestParam.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestParam,
                responseListener,
                errorListener
        );
        queue.add(request);
    }
}
