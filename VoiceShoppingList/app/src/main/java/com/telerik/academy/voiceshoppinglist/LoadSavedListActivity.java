package com.telerik.academy.voiceshoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import com.telerik.academy.voiceshoppinglist.data.VoiceShoppingListDbHelper;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;
import com.telerik.academy.voiceshoppinglist.utilities.ExpandableListAdapter;
import com.telerik.academy.voiceshoppinglist.utilities.speech.SpeechRecognitionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadSavedListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load_shopping_list_activity);

        VoiceShoppingListDbHelper db = new VoiceShoppingListDbHelper(this);
        final ArrayList<ShoppingList> allItemLists = db.getAllShoppingLists();

        final ArrayList<String> list = new ArrayList<>();
        for (ShoppingList itemsList : allItemLists) {
            list.add(itemsList.getName());
        }

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.loadShoppingItemsExpandableListView);
        expandableListView.setAdapter(new ExpandableListAdapter(this, allItemLists));
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Activity activity = (Activity) v.getContext();
                Intent intent = new Intent(activity, ViewShoppingListActivity.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable(Constants.SHOPPING_LIST_BUNDLE_KEY, allItemLists.get(groupPosition));

                intent.putExtras(bundle);

                activity.startActivity(intent);
                activity.finish();
                return true;
            }
        });


//        final ListView listview = (ListView) findViewById(R.id.loadShoppingItemsListView);
//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, list);
//        listview.setAdapter(adapter);
//
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view,
//                                    int position, long id) {
//
//                Activity activity = (Activity) view.getContext();
//                Intent intent = new Intent(activity, ViewShoppingListActivity.class);
//
//                Bundle bundle = new Bundle();
//
//                bundle.putSerializable(Constants.SHOPPING_LIST_BUNDLE_KEY, allItemLists.get(position));
//
//                intent.putExtras(bundle);
//
//                activity.startActivity(intent);
//                activity.finish();
//            }
//
//        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_shopping_lists, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.option_delete_all:
                VoiceShoppingListDbHelper db = new VoiceShoppingListDbHelper(LoadSavedListActivity.this);
                db.deleteAllShoppingLists();
                Intent intent = new Intent(LoadSavedListActivity.this, MainActivity.class);
                LoadSavedListActivity.this.startActivity(intent);
                LoadSavedListActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoadSavedListActivity.this, MainActivity.class);
        LoadSavedListActivity.this.startActivity(intent);
        LoadSavedListActivity.this.finish();
        super.onBackPressed();
    }
}
