package com.av.lenovo.sa3edny.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.av.lenovo.sa3edny.utils.Methods;
import com.av.lenovo.sa3edny.utils.Urls;
import com.av.lenovo.sa3edny.utils.Variables;
import com.av.lenovo.sa3edny.ui.items.Item;
import com.av.lenovo.sa3edny.classes.VolleySingleton;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mido on 4/3/2017.
 */

public class BackGroundService extends IntentService {
    public BackGroundService() {
        super("HI SERVICE");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
       // MainActivity.bagde_number.setText("service");
        try {
            Methods.getFavIds(getApplicationContext());

            final StringRequest request = new StringRequest(Request.Method.GET, Urls.URL_ALL_ITEM_SEARCH, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        Variables.searchList.clear();
                        JsonElement root = new JsonParser().parse(response);
                        response = root.getAsString();
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Item item = new Item();
                            item.setId(jsonObject.getString("ItemID"));
                            item.setName(Html.fromHtml(jsonObject.getString("Name_En")).toString());
                            item.setDescription(Html.fromHtml(jsonObject.getString("Description_En")).toString());
                            item.setPhone1(jsonObject.getString("Phone1"));
                            Variables.searchList.add(item);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent localIntent = new Intent("GETITEMS")
                            // Puts the status into the Intent
                            .putExtra("SEARCHITEMS", Variables.searchList);
                    // Broadcasts the Intent to receivers in this app.

                    LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(localIntent);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Methods.toast(Methods.onErrorVolley(volleyError), getApplicationContext());

                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    18000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance().addToRequestQueue(request);

        }catch (Exception e){
            Methods.toast(e.getMessage(),getApplicationContext());}

    }
}