package com.lowestprice.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.lowestprice.LazyAdapter;
import com.lowestprice.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CartListActivity extends ActionBarActivity {
    ListView list;
    LazyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carts);

        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

        String artist = "The Doors";
        String[] titles = new String[]{
            "Everyday shopping", "Beer", "Need to buy today",
            "Update whisky bar", "For kids", "2013/10/3",
            "Nice milk", "For party",
            "Rare stuff", "Unsorted"};
        String[] items = new String[]{"10", "3", "5", "12",
            "24", "2", "1", "16", "8", "6"};

        // looping through all song nodes <song>
        for (int i = 0; i < 10; i++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            // adding each child node to HashMap key => value
            map.put(LazyAdapter.KEY_ID, String.valueOf(i));
            map.put(LazyAdapter.KEY_TITLE, titles[i]);
            map.put(LazyAdapter.KEY_DESCRIPTION, null);
            map.put(LazyAdapter.KEY_NUMBER, items[i]);

            // adding HashList to ArrayList
            songsList.add(map);
        }

        list = (ListView) findViewById(R.id.cartListView);

        // Getting adapter by passing xml data ArrayList
        adapter = new LazyAdapter(this, songsList, R.layout.cart_row,
            R.id.cartId, R.id.cartTitle, R.id.cartDescription, R.id.numberOfProducts);
        list.setAdapter(adapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }
}