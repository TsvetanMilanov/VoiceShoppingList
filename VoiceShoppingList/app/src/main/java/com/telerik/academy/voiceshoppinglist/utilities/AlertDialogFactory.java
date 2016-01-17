package com.telerik.academy.voiceshoppinglist.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.telerik.academy.voiceshoppinglist.R;

public final class AlertDialogFactory {
    public static AlertDialog createInformationAlertDialog(Context context, String message, String title, final OkCommand command) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title == null ? context.getResources().getString(R.string.app_name) : title);
        builder.setPositiveButton("OK", null);
        builder.setCancelable(true);

        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (command != null) {
                            command.execute();
                        }
                    }
                });

        return builder.create();
    }
}
