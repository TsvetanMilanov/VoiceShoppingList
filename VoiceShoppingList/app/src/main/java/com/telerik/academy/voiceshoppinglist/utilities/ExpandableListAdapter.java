package com.telerik.academy.voiceshoppinglist.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telerik.academy.voiceshoppinglist.R;
import com.telerik.academy.voiceshoppinglist.data.VoiceShoppingListDbHelper;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;

import java.util.ArrayList;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private ArrayList<ShoppingList> shoppingLists;
    private Context context;
    private VoiceShoppingListDbHelper db;

    public ExpandableListAdapter(Context context, ArrayList<ShoppingList> shoppingLists) {
        this.context = context;
        this.shoppingLists = shoppingLists;
        this.db = new VoiceShoppingListDbHelper(context);
    }

    @Override
    public int getGroupCount() {
        return this.shoppingLists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        LinearLayout shoppingListLinearLayout =
                (LinearLayout) layoutInflater.inflate(R.layout.expandable_list_view_group_view, parent, false);

        TextView shoppingListNameTextView = (TextView) shoppingListLinearLayout.findViewById(R.id.tv_shopping_list_name);

        ShoppingList currentShoppingList = this.shoppingLists.get(groupPosition);

        shoppingListNameTextView.append(" " + currentShoppingList.getName());

        return shoppingListLinearLayout;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        LinearLayout shoppingListDetailsLinearLayout =
                (LinearLayout) layoutInflater.inflate(R.layout.expandable_list_view_child_view, parent, false);

        TextView createdOnTextView = (TextView) shoppingListDetailsLinearLayout.findViewById(R.id.tv_created_on);
        TextView productsCountTextView = (TextView) shoppingListDetailsLinearLayout.findViewById(R.id.tv_products_count);

        ShoppingList currentShoppingList = this.shoppingLists.get(groupPosition);

        createdOnTextView.append(" " + currentShoppingList.getCreatedOn());
        productsCountTextView.append(" " + this.db.getProductsByShoppingListId(currentShoppingList.get_ID()).size());

        return shoppingListDetailsLinearLayout;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
