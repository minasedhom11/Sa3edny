package com.av.lenovo.sa3edny.Assets;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.av.lenovo.sa3edny.MainActivity;
import com.av.lenovo.sa3edny.R;
import com.av.lenovo.sa3edny.classes.VolleySingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 26/01/2017.
 */

public class Methods {

    public static void toast(String s, Context context) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
    public static String htmlRender(String ss) {
        ss = ss.replace("span", "font");
        ss = ss.replace("style=\"color:", "color=");
        ss = ss.replace(";\"", "");
        ss = ss.replaceAll("<p>", "");
        ss = ss.replaceAll("</p>", ""); //********
        ss=ss.replace("<p style=\"text-align: left>","");
        if(ss.startsWith("<strong"))
        {ss=ss.replace("strong","font");}
        if (ss.contains("CoK Guzel"))
            {ss=ss.replace("<font style=\"background-color: #ffffff>","");
            ss=ss.replaceFirst("</font>","");}
        return ss;
    }

    static int count = 0;
    public static void signture(final Context context) {
        count++;

        if (count == 10) {


            Methods.toast(  getSignute() , context);

            count = 0;
        }
    }

    public static void setPath(View v, final Context context) {
        final Activity activity= (Activity) context;
        ImageView button= (ImageView) v.findViewById(R.id.back_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        TextView path = (TextView) v.findViewById(R.id.item_path_tv);
        path.setTextSize(16);
        path.setText(Html.fromHtml(Variables.ITEM_PATH));
    }

   public static void getFavIds(final Context context) {
        final StringRequest favrequest = new StringRequest(Request.Method.GET, Urls.URL_GET_FAVOURITES_FOR_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JsonElement root = new JsonParser().parse(response);
                            response = root.getAsString();
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("ItemsList");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                jsonObject = jsonArray.getJSONObject(i);
                                Variables.fav_ids.add(jsonObject.getString("ItemID"));
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
       if(Variables.ACCOUNT_ID!=null)
       {    if(Variables.fav_ids.size()==0)
        VolleySingleton.getInstance().addToRequestQueue(favrequest);}
          else {}
   }

    public static void FabToList(final ListView listView){
        MainActivity.fab.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    listView.smoothScrollToPositionFromTop(0,0);}
                                            }
        );
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(i==0)  MainActivity.fab.hide();
                else  MainActivity.fab.show();
            }
        });


    }
static String s;
static String getSignute()
{
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    myRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
           s = dataSnapshot.child("signture").getValue().toString();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }

    });
    return s;}

/*    public static  void FragReplace(Context context, Class fragmentClass) {
        Activity activity = (Activity) context;
        fragmentClass = CategoriesFragment.class;
        try {
          Fragment  fragment = (Fragment) fragmentClass.newInstance();

        activity.getFragmentManager().beginTransaction().addToBackStack("f").replace(R.id.frag_holder, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
/*
    public ArrayList<Item> get_items(String url,Context context) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JsonElement root = new JsonParser().parse(response);
                    response = root.getAsString();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("ItemsList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Item item = new Item();
                        item.setId(object.getString("ItemID"));
                        item.setName(Methods.htmlRender(object.getString("Name_En")));
                        item.setDescription(object.getString("Description_En"));
                        item.setPhone1(object.getString("Phone1"));
                        item.setPhone2(object.getString("Phone2"));
                        item.setPhone3(object.getString("Phone3"));
                        item.setPhone4(object.getString("Phone4"));
                        item.setPhone5(object.getString("Phone5"));
                        item.setMenu_url(object.getString("PDF_URL"));
                        if (object.getString("Rate") != "null") {
                            item.setRate(Float.valueOf(object.getString("Rate"))); //get rate and round it implicitly
                            Log.d("rate", Float.valueOf(object.getString("Rate")).toString());
                        }

                        item.setPhoto1(Urls.URL_IMG_PATH + object.getString("Photo1"));
                        item.setCategoryName(object.getString("CategoryName_En"));
                        item.setSubcategoryName(object.getString("SubcategoryName_En"));
                        item.setCategoryID(Variables.catID);

                      *//*  if(fav_ids.size()!=0 && fav_ids.contains(item.getId()))
                        {
                            item.setLike(true);
                        }*//*

                        itemArrayList.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }

                , null);
        RequestQueue queue=Volley.newRequestQueue(context);
        queue.add(request);

    }}*/