package com.telerik.academy.voiceshoppinglist.utilities.commands;

import android.app.Activity;
import android.content.Intent;

import com.telerik.academy.voiceshoppinglist.AddNewShoppingListActivity;

public final class MainMenuCommands {
    public static void navigateToAddNewShoppingListActivity(Activity activity) {
        Intent intent = new Intent(activity, AddNewShoppingListActivity.class);

        activity.startActivity(intent);
        activity.finish();
    }

    public static void exitApplication(Activity activity) {
        activity.finish();
        System.exit(0);
    }
}
