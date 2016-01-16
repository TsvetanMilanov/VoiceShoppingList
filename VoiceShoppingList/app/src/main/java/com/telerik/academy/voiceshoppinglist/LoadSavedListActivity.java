package com.telerik.academy.voiceshoppinglist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.telerik.academy.voiceshoppinglist.data.VoiceShoppingListDbHelper;
import com.telerik.academy.voiceshoppinglist.data.models.Product;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class LoadSavedListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load_shopping_list_activity);

        VoiceShoppingListDbHelper db = new VoiceShoppingListDbHelper(this);
        ArrayList<ShoppingList> allItemLists = db.getAllShoppingLists();

        final ArrayList<String> list = new ArrayList<String>();
        for (ShoppingList itemsList: allItemLists) {
            list.add(itemsList.getName());
        }


        final ListView listview = (ListView) findViewById(R.id.loadShoppingItemsListview);
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                list.remove(item);
                adapter.notifyDataSetChanged();
            }

        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
