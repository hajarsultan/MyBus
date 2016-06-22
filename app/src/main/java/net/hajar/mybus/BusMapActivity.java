package net.hajar.mybus;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class BusMapActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "BusMapActivity";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Location savedLocation;
    private MarkerOptions usersCurrentLocation;
    private boolean mRequestingLocationUpdates = false;


    private TextView currentLocationTV, savedLocationTV, savedDistanceTV;
    private Button saveBTN,showBusesBTN,selectBusesBTN;




    private void initViews() {
        currentLocationTV = (TextView) findViewById(R.id.currntLoca);
        savedLocationTV = (TextView) findViewById(R.id.savedLoc);
        savedDistanceTV = (TextView) findViewById(R.id.savedDistance);

        saveBTN = (Button) findViewById(R.id.saveLoc);
        showBusesBTN = (Button) findViewById(R.id.showBuses);
        selectBusesBTN = (Button) findViewById(R.id.selectBuses);

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedLocation = mLastLocation;
                savedLocationTV.setText("Saved Location : "+getReadbleLatLng(savedLocation));
            }
        });

        showBusesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.createInputDialog(BusMapActivity.this, "أدخل رقم الأوتوبيس للبحث عنة", new DialogHelper.OnInputDialogOK() {
                    @Override
                    public void onOK(String s) {
                        if(s.isEmpty()){
                            //Err its empty string
                        }else{
                            //do your magic

                            String whereClause = "busnumber LIKE '%"+s+"%'";
                            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
                            dataQuery.setWhereClause( whereClause );
                            dataQuery.setPageSize(50);
                            Backendless.Persistence.of(Buses.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Buses>>() {
                                @Override
                                public void handleResponse(BackendlessCollection<Buses> busesBackendlessCollection) {
                                    Log.d(TAG, "handleResponse: "+busesBackendlessCollection.toString());
                                }

                                @Override
                                public void handleFault(BackendlessFault backendlessFault) {

                                }
                            });

                        }
                    }
                });
            }
        });
    }

    void showLocation(){
        currentLocationTV.setText("Current Location : "+getReadbleLatLng(mLastLocation) );
        if(savedLocation != null){
            savedDistanceTV.setText("Distance : "+ savedLocation.distanceTo(mLastLocation) + "meters");
        }
    }

    String getReadbleLatLng(Location loc){
        return "{ lat:" + loc.getLatitude() + " ,lng:"+loc.getLongitude()+" }";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViews();

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();

    }


    float distFrom(Location loc1,Location loc2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(loc2.getLatitude()-loc1.getLatitude());
        double dLng = Math.toRadians(loc2.getLongitude()-loc1.getLongitude());
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(loc1.getLatitude())) * Math.cos(Math.toRadians(loc2.getLatitude())) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                //final LocationSettingsStates = locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    BusMapActivity.this,
                                    565);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

//        if (mLastLocation != null) {
//            LatLng currentPos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            usersCurrentLocation = new MarkerOptions().position(currentPos).title("You are here !");
//            mMap.addMarker(usersCurrentLocation);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos));
//            showLocation();
//        }
    }


    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        LatLng currentPos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        usersCurrentLocation = new MarkerOptions().position(currentPos).title("You are here !");
        mMap.addMarker(usersCurrentLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos));
        showLocation();
    }
}
