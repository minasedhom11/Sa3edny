package com.example.lenovo.citycenter.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.citycenter.Assets.Methods;
import com.example.lenovo.citycenter.Assets.Urls;
import com.example.lenovo.citycenter.Assets.Variables;
import com.example.lenovo.citycenter.MainActivity;
import com.example.lenovo.citycenter.classes.ExpandListAdpter;
import com.example.lenovo.citycenter.classes.GetDataRequest;
import com.example.lenovo.citycenter.classes.Category;
import com.example.lenovo.citycenter.R;
import com.example.lenovo.citycenter.classes.Subcategory;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class GrandCinema extends Fragment {
    private ExpandableListView customListView;
    private ArrayList<Category> categoryArrayList;
    private ExpandListAdpter myAdapter;

    FloatingActionButton fab;

    public GrandCinema() {
        // Required empty public constructor
    }

    JSONArray jsonArray;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_grandcinema, container, false);
        customListView = (ExpandableListView)view.findViewById(R.id.expandded_list_category);
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, customListView, false);
        customListView.addFooterView(footerView,null,false);
        categoryArrayList = new ArrayList<>();
        /*----------------------------------------------------------------------------------------------------------------------------------------------------*/
        GetDataRequest.setUrl(Urls.URL_GET_CATEGORIES_SERVICES);
        RequestQueue queue = Volley.newRequestQueue(getContext());

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JsonElement root=new JsonParser().parse(response);
                    response = root.getAsString();  //not .toString
                    jsonArray = new JSONArray(response) ;
                    for (int i = 0; i < jsonArray.length(); i++)

                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Category myCategory=new Category();
                        myCategory.set_id(object.getInt("CategoryID"));
                        myCategory.set_name(Methods.htmlRender(object.getString("Name_En"))); // X
                        myCategory.set_details(Methods.htmlRender(object.getString("Description_En"))); // X
                        myCategory.set_icon(Urls.URL_IMG_PATH + object.getString("Logo"));
                        myCategory.setHas_sub(object.getBoolean("AllowSubcategory"));//filter here
                        if(myCategory.isHas_sub())
                        {myCategory.setSub_array(getSubs(object.getInt("CategoryID")));}//filter here
                        categoryArrayList.add(myCategory);
                    }

                   myAdapter=new ExpandListAdpter((AppCompatActivity) getActivity(),categoryArrayList);
                   customListView.setAdapter(myAdapter);
               //     myAdapter.setNotifyOnChange(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetDataRequest fetchRequest = new GetDataRequest(responseListener);
        queue.add(fetchRequest);
        return view;
    }

/*-----------------------------------------------------------------------------------------------------------------------------------*/


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customListView.setGroupIndicator(null);

         fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // customListView.setSmoothScrollbarEnabled(true);
                customListView.smoothScrollToPositionFromTop(0,0);
            }}
        );
        customListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(i==0)
                    fab.hide();
                else fab.show();
            }
        });

        customListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Button explore = (Button) v.findViewById(R.id.explore_btn);
                explore.setTypeface(MainActivity.font);
                if(customListView.isGroupExpanded(groupPosition))
                {
                    explore.setText(getString(R.string.arrow_left));

                }
                else
                    explore.setText(getString(R.string.arrow_bottom));

                Category cat = (Category) myAdapter.getGroup(groupPosition);

                if(!cat.isHas_sub())
                {
                    Variables.catID = String.valueOf(cat.get_id());
                    Fragment fragment = new ItemsFragment();
                    getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
                    GetDataRequest.setUrl(Urls.URL_GET_SELECTED_CATEGORY_ITEMS+ Variables.catID ); //set the clicked cat id to fetch it's items
                }
                return false;
            }
        });

        customListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Category cat = (Category) myAdapter.getGroup(groupPosition);
                String subcatID=cat.getSub_array().get(childPosition).getSubcat_id();
                Fragment fragment = new ItemsFragment();
                getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
                String url = Urls.URL_GET_SELECTED_SUBCATEGORY_ITEM + subcatID;
                GetDataRequest.setUrl(url );
                return false;
            }
        });

    }


        public  ArrayList<Subcategory> getSubs(int catID) {
        final ArrayList<Subcategory> subCat_array = new ArrayList();
        RequestQueue queue=Volley.newRequestQueue(getContext());
        StringRequest subcatRequest= new StringRequest(Request.Method.GET, Urls.URL_GET_SELECTED_CATEGORY_SUBCATEGORIES + catID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JsonElement root = new JsonParser().parse(response);
                            response = root.getAsString();  //not .toString
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Subcategory mySub = new Subcategory();
                                mySub.setSubcat_id((object.getString("SubCategoryID")));
                                mySub.setSubCat_name(object.getString("Name_En")); // X
                                mySub.setSubCat_describtion(object.getString("Description_En")); // X
                                mySub.setSubCat_icon_url(Urls.URL_IMG_PATH + object.getString("Photo1"));
                                subCat_array.add(mySub);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, null) ;
        queue.add(subcatRequest);
        return subCat_array;
    }

}
