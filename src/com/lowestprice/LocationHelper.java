package com.lowestprice;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

public class LocationHelper {

    private Activity activity = null;
    private LocationManager locationManager;

    private LocationHelper() {}

    public LocationHelper(Activity activity) {
        this.activity = activity;
    }

    public Location getLocation() {
        Location location = null;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        boolean skipGps = false;
        if (locationManager != null) {
            while (location == null) {
                if (isProviderEnabled(LocationManager.GPS_PROVIDER) && !skipGps) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else if (isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } else {
                    Toast toast = Toast.makeText(activity.getApplicationContext(),
                        "Location provider isn't available!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                if (location == null) {
                    skipGps = true;
                }
            }
        } else {
            Toast toast = Toast.makeText(activity.getApplicationContext(),
                "Location Manager isn't available!", Toast.LENGTH_SHORT);
            toast.show();
        }
        return location;
    }

    private boolean isProviderEnabled(String provider) {
        return locationManager.isProviderEnabled(provider);
    }
}
