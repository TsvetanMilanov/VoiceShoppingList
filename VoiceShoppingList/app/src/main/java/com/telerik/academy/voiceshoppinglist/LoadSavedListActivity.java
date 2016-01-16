package com.telerik.academy.voiceshoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.telerik.academy.voiceshoppinglist.data.VoiceShoppingListDbHelper;
import com.telerik.academy.voiceshoppinglist.data.models.Product;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;
import com.telerik.academy.voiceshoppinglist.utilities.commands.MainMenuCommands;
import com.telerik.academy.voiceshoppinglist.utilities.speech.SpeechRecognizerFactory;

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
//                list.remove(item);
//                adapter.notifyDataSetChanged();

                Activity activity = (Activity) view.getContext();
                Intent intent = new Intent(activity, AddNewShoppingListActivity.class);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "" + position);

                // Need to stop the current speech recognizer because it will try to destroy it in the next
                // activity where the intent class is not the same and the will cause service not registered exception.
                SpeechRecognizer currentSpeechRecognizer = SpeechRecognizerFactory.getCurrentSpeechRecognizer();

                if (currentSpeechRecognizer != null) {
                    currentSpeechRecognizer.destroy();
                }

                activity.startActivity(intent);
                activity.finish();
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
