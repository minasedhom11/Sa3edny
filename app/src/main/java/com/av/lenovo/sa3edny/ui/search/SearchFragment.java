package com.av.lenovo.sa3edny.ui.search;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.av.lenovo.sa3edny.utils.Methods;
import com.av.lenovo.sa3edny.utils.Variables;
import com.av.lenovo.sa3edny.ui.MainActivity;
import com.av.lenovo.sa3edny.R;
import com.av.lenovo.sa3edny.ui.items.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SearchFragment extends Fragment {
    public SearchFragment() {
        // Required empty public constructor
    }

   ListView listView;

    ArrayList<Item> itemArrayList;
    MyItemSearchAdapter myItemSearchAdapter;
    EditText editText;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
try{
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_search, container, false);
         listView= (ListView) v.findViewById(R.id.items_name_list);
        editText= (EditText) v.findViewById(R.id.search_et);
        editText.setTypeface(MainActivity.font);
        Methods.FabToList(listView);
        progressBar= (ProgressBar) v.findViewById(R.id.progress_bar);
        itemArrayList =new ArrayList<>();
    
        if(Variables.searchList.size()!=0)
        {
            myItemSearchAdapter=new MyItemSearchAdapter(getContext(),android.R.layout.simple_list_item_1,Variables.searchList);
            listView.setAdapter(myItemSearchAdapter);
            progressBar.setVisibility(View.GONE);

        }
        else {Methods.toast("Please wait...",getContext());}


//getActivity().onBackPressed();
/**********************************************************************************************************************************************/
        // The filter's action is BROADCAST_ACTION
        IntentFilter statusIntentFilter = new IntentFilter("GETITEMS");
        /*        // Adds a data filter for the HTTP scheme
        statusIntentFilter.addDataScheme("http");*/
        // Instantiates a new DownloadStateReceiver

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

                @Override
            public void onReceive(Context context, Intent intent) {
                try{
                itemArrayList =(ArrayList<Item>) intent.getSerializableExtra("SEARCHITEMS");
                myItemSearchAdapter=new MyItemSearchAdapter(getContext(),android.R.layout.simple_list_item_1,Variables.searchList);
                listView.setAdapter(myItemSearchAdapter);
                progressBar.setVisibility(View.GONE);}
                catch (Exception e){
                 e.printStackTrace();
                }
            }
        };
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                broadcastReceiver,
                statusIntentFilter);
/**********************************************************************************************************************************************/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Item item=  myItemSearchAdapter.getItem(i);
                Variables.SINGLE_ITEM_ID = String.valueOf(item.getId());
                Fragment fragment = new SingleItemFragment();
                getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).commit();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myItemSearchAdapter.getFilter().filter(charSequence); }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    return v;   }
catch(Exception e){
    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
}
       return  null;
    }


    class MyItemSearchAdapter extends ArrayAdapter<Item> implements Filterable {
        Context context;
        ArrayList<Item> itemsList;
        ArrayList<Item> filterdList;

        public MyItemSearchAdapter(Context context, int resource, ArrayList<Item> itemsList) {
            super(context, resource,itemsList);
            this.context= context;
            Collections.sort(itemsList, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            this.itemsList=itemsList;
            this.filterdList=itemsList;
        }

        class ViewHolder
        {
            TextView name;
            Button call;
        }
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            try {

                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.itetm_search_list, parent, false);
                    holder.name = (TextView) convertView.findViewById(R.id.item_name_tv);
                    holder.call= (Button) convertView.findViewById(R.id.search_call_btn);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                final Item myItem = filterdList.get(position);
                holder.call.setTypeface(MainActivity.font);
                holder.name.setText(Html.fromHtml(myItem.getName()));
                if (myItem.getPhone1().matches("null"))holder.call.setVisibility(View.GONE);else holder.call.setVisibility(View.VISIBLE);
                holder.call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //   context.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + phones_adapter.getItem(position))));
                       getContext().startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + myItem.getPhone1())));
                       // Methods.toast("call me",getContext());
                    }
                });
                return  convertView;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        public int getCount() {return filterdList.size();}
        @Nullable
        @Override
        public Item getItem(int position) {return filterdList.get(position);}

        @NonNull
        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    // FilterResults filterResults = new FilterResults();
                    FilterResults results = new FilterResults();

                    //If there's nothing to filter on, return the original data for your list
                    if(charSequence == null || charSequence.length() == 0)
                    {
                        results.values = itemsList;
                        results.count = itemsList.size();
                    }

                    else
                    {
                        ArrayList<Item> filterResultsData = new ArrayList<Item>();
                        for(Item data : itemsList)
                        {
                            //In this loop, you'll filter through originalData and compare each item to charSequence.
                            //If you find a match, add it to your new ArrayList
                            //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                            if(data.getName().toLowerCase().contains(charSequence)||data.getDescription().toLowerCase().contains(charSequence))
                            {
                                filterResultsData.add(data);
                            }
                        }
                        results.values = filterResultsData;
                        results.count = filterResultsData.size();
                    }
                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence contraint, FilterResults results) {
                    filterdList = (ArrayList<Item>) results.values;
                    if (results.count > 0) {
                        notifyDataSetChanged();
                    } else
                    {
                        notifyDataSetInvalidated();
                    }
                }
            };
        }
    }
}
