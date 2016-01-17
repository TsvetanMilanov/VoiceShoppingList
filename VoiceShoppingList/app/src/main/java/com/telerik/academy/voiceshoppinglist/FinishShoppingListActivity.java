package com.telerik.academy.voiceshoppinglist;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.telerik.academy.voiceshoppinglist.async.SaveShoppingListAsyncTask;
import com.telerik.academy.voiceshoppinglist.async.SaveShoppingListCommand;
import com.telerik.academy.voiceshoppinglist.data.models.Product;
import com.telerik.academy.voiceshoppinglist.data.models.ShoppingList;
import com.telerik.academy.voiceshoppinglist.utilities.AlertDialogFactory;
import com.telerik.academy.voiceshoppinglist.utilities.Constants;

import java.util.ArrayList;
import java.util.Date;

public class FinishShoppingListActivity extends AppCompatActivity {

    private Context context;
    private ArrayList<Product> productsList;
    private EditText shoppingListNameEditText;
    private Button saveToLocalDbBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_shopping_list);

        productsList = (ArrayList<Product>) getIntent().getExtras().get(Constants.PRODUCTS_LIST_BUNDLE_KEY);

        context = this;

        shoppingListNameEditText = (EditText) findViewById(R.id.et_shopping_list_name);
        saveToLocalDbBtn = (Button) findViewById(R.id.btn_save_to_local_db);
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving shopping list to local database.");

        saveToLocalDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, false);

                boolean isFineLocationPermissionGranted = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                boolean isCoarseLocationPermissionGranted = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

                if (!isFineLocationPermissionGranted || !isCoarseLocationPermissionGranted) {
                    return;
                }

                CustomLocationListener locationListener = new CustomLocationListener();

                locationManager.requestLocationUpdates(provider, 0, 0, locationListener);

                Location location = locationManager.getLastKnownLocation(provider);
                locationManager.removeUpdates(locationListener);

                ShoppingList shoppingList;

                if (location != null) {
                    shoppingList = new ShoppingList(new Date(), location.getLatitude(), location.getLongitude(), shoppingListNameEditText.getText().toString());
                } else {
                    shoppingList = new ShoppingList(new Date(), 0d, 0d, shoppingListNameEditText.getText().toString());
                }

                SaveShoppingListAsyncTask saveShoppingListAsyncTask = new SaveShoppingListAsyncTask(context, shoppingList, productsList, new SaveShoppingListCommand() {
                    @Override
                    public void execute() {
                        progressDialog.dismiss();

                        AlertDialogFactory.createInformationAlertDialog(context, "Shopping list saved successfully.", null, null).show();
                    }
                });

                saveShoppingListAsyncTask.execute();
            }
        });
    }


    private class CustomLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.d("LOCATION", "onLocationChanged");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
